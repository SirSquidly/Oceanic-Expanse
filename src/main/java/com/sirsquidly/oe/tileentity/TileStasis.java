package com.sirsquidly.oe.tileentity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileStasis extends TileEntity implements ITickable
{
	public EntityLivingBase attackTarget;
	@Nullable
	private UUID attackTargetUUID;

	public boolean isActive;
	public boolean isElder;

    /** The minimum blocks required for the Stasis to be an Elder. */
    public int minElderFrame = 64;

	/** How many blocks are in the frame of the Stasis. */
	public int frameSize;

	/** How long this Stasis has been Attacking for. */
	public int attackTick;
	/** Seperate from `attackTick`m as it needs to be updated more frequently. */
	public int clientAttackTick;

	public int bobTick;
	public float shellRotation;
	public float shellRotationPrev;
	public float shellRotateSpeed;
	public float eyeRotation;
	public float eyeRotationPrev;
	public float eyePitch;
	public float eyePitchPrev;
	public int windTick;
    
	@Override
	public void update()
	{
		if(world == null) return;

		doAnimCounters();
		
		if(world.getTotalWorldTime() % 20L == 0L)
		{
			if(countFrame(world, pos) >= 12)
			{
				/* Records the size of the frame. */
				frameSize = countFrame(world, pos);
				isElder = frameSize >= minElderFrame;

				doAmbientSounds(world, pos);
				doAttacking(pos);
				if (this.attackTarget == null) findNewAttackTarget(pos);
				if (isElder && !world.isRemote) doEffect(world, pos);

				if(!isActive)
				{
					world.playSound(null, pos, OESounds.BLOCK_CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
					isActive = true;
				}
			}
			else
			{ 
				if(isActive)
				{
					world.playSound(null, pos, OESounds.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
					isActive = false;
					isElder = false;
				}
			}
		}
		
		if(world.isRemote && isActive)
		{
			spawnParticles(world, pos);
		}
	}

	/** Sets a new `attackTarget` for the Stasis.*/
	public void findNewAttackTarget(BlockPos pos)
	{
		int expandBy = 8 * (int) Math.floor(frameSize / 7);

		List<EntityLivingBase> nearbyMobs = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-expandBy, -expandBy, -expandBy), pos.add(expandBy + 1, expandBy + 1, expandBy + 1)));

		/* Filters out any entities in range that do not match the conditions. Intellij automatically condensed this, it's pretty damn cool. */
		nearbyMobs.removeIf(thisEntity -> !this.canSeeEntity(thisEntity) || thisEntity.getDistanceSqToCenter(pos) > (expandBy + 1) * (expandBy + 1) || !isAcceptableTarget(thisEntity));

		if (!nearbyMobs.isEmpty())
		{
			this.attackTarget = nearbyMobs.get(world.rand.nextInt(nearbyMobs.size()));
			this.attackTick = 0;
			this.clientAttackTick = 0;

			System.out.println("New attack target: " + this.attackTarget.getName());
		}
	}

	/** Preforms the attacking of the Stasis. */
	public void doAttacking(BlockPos pos)
	{
		boolean resetAttackFlag = false;
		int expandBy = 8 * (int) Math.floor(frameSize / 7);

		if (attackTarget == null || attackTarget.isDead) resetAttackFlag = true;

		if (!resetAttackFlag && this.canSeeEntity(this.attackTarget) && this.attackTarget.getDistanceSqToCenter(pos) <= (expandBy + 1) * (expandBy + 1))
		{
			if (world.isRemote) spawnParticles(world, attackTarget.getPosition());
			if (++attackTick >= getAttackDuration() / 20)
			{
				world.playSound(null, attackTarget.getPosition(), OESounds.BLOCK_CONDUIT_ATTACK, SoundCategory.BLOCKS, 1.0f, 1.0f);
				attackTarget.attackEntityFrom(DamageSource.MAGIC, isElder ? 10 : 6);
				this.attackTick = 0;
				this.clientAttackTick = 0;
			}
		}
		else
		{
			/* If the target entity is alive but outside of range or visibility, remember it for a bit. */
			if (--attackTick <= 0) resetAttackFlag = true;
			/* Lowers by 20, since `clientAttackTick` updates every tick, while `doAttacking` updates every 20. */
			clientAttackTick -= 20;
		}

		if (resetAttackFlag)
		{
			this.attackTarget = null;
			this.attackTick = 0;
			this.clientAttackTick = 0;
		}
	}

	/** Handles giving out the Conduit Power effect to nearby Players.*/
	public void doEffect(World worldIn, BlockPos pos)
	{
		/* Extends 1 block per each 8 within the frame. Since `isElder` is required before this, the minimum should be 64 blocks (defined by `minElderFrame`), thus 8 blocks minimum when active. */
		int expandBy = (int) Math.floor(frameSize / 8);

		for(EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-expandBy, -expandBy, -expandBy), pos.add(expandBy + 1, expandBy + 1, expandBy + 1))))
		{
			int sphereCheck = (expandBy + 1) * (expandBy + 1);
			if (isAcceptableTarget(player) && player.getDistanceSqToCenter(pos) < sphereCheck)
			{ player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 10 * 20 + 18, 1, true, true)); }
		}
	}

	/** If Line of Sight exists between this and the given entity. */
	public boolean canSeeEntity(EntityLivingBase entity)
	{
		if (entity == null) return false;

		Vec3d vecStasisPos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		Vec3d vecTargetPos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		//entity.getPositionVector().add(0,entity.getEyeHeight(),0);

		return this.world.rayTraceBlocks(vecStasisPos, vecTargetPos, false, true, false) == null;
	}

	public int getAttackDuration()
	{ return 80; }

	public float getAttackAnimationScale(float partialTicks)
	{ return ((float)this.clientAttackTick + partialTicks) / (float)this.getAttackDuration(); }

	/** Simply returns the targetted entity. */
	public EntityLivingBase getAttackTarget()
	{ return this.attackTarget; }
	
	/** Handles the variables used for the Stasis's animations. A LOT copied from the Enchantment Table.*/
	public void doAnimCounters()
	{
		//** This is used to set an random bobbing offset for new conduits.*/
		if (this.bobTick < 10)
		{
			this.bobTick = world.rand.nextInt(100000);
			this.windTick = world.rand.nextInt(50);
		}
		++this.bobTick;

		this.shellRotationPrev = this.shellRotation;
		this.eyeRotationPrev = this.eyeRotation;
		this.eyePitchPrev = this.eyePitch;
		this.shellRotateSpeed = 0.05F;

		this.shellRotation = getSmoothened(this.shellRotation + this.shellRotateSpeed, this.shellRotation);

		/* Rotate the Eye to FACE the target! */
		if (this.getAttackTarget() != null)
		{
			double dx = this.getAttackTarget().posX - (this.pos.getX() + 0.5);
			double dz = this.getAttackTarget().posZ - (this.pos.getZ() + 0.5);
			this.eyeRotation = getSmoothened((float) MathHelper.atan2(dz, dx), this.eyeRotation);

			double dy = (this.getAttackTarget().posY + this.getAttackTarget().getEyeHeight()) - (this.pos.getY() + 0.5);
			double distance = Math.sqrt(dx * dx + dz * dz);
			this.eyePitch = -getSmoothened((float) MathHelper.atan2(dy, distance), this.eyePitch);

			++this.clientAttackTick;
		}

		if(world.getTotalWorldTime() % 2 == 0)
		{ 
			++this.windTick;
			if (this.windTick > 80) this.windTick = 1;
		}
	}

	/** Seemlessly loops a 360 operation. */
	public float getSmoothened(float target, float applyTo)
	{
		float whyGod = target - applyTo;
		while (whyGod >= Math.PI) whyGod -= (float) Math.PI * 2F;
		while (whyGod < -Math.PI) whyGod += (float) Math.PI * 2F;
		return applyTo += whyGod;
	}
	
	/** Handles the ambient sounds of the Conduit.*/
	public void doAmbientSounds(World worldIn, BlockPos pos)
	{	
		if(world.getTotalWorldTime() % 80L == 0L)
		{
			world.playSound(null, pos, OESounds.BLOCK_CONDUIT_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
		}
		
		long i = 60L + (long)this.world.rand.nextInt(40);
		
		if(world.getTotalWorldTime() % i == 0L)
		{
			world.playSound(null, pos, OESounds.BLOCK_CONDUIT_BEAT, SoundCategory.BLOCKS, 1.0f, 1.0f);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnParticles(World worldIn, BlockPos pos)
	{
		for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                if (i > -1 && i < 1 && j == -1)
                {
                    j = 1;
                }

                if (world.rand.nextInt(64) == 0 && ConfigHandler.block.stagnant.stagnantParticles != 0)
                {
                    for (int k = -1; k <= 2; ++k)
                    {
                    	int getTexture = world.rand.nextInt(12);
                    	if (ConfigHandler.block.stagnant.stagnantParticles == 2) getTexture = 12;
                    	if (ConfigHandler.block.stagnant.stagnantParticles == 3) getTexture = world.rand.nextInt(13);
                    	if (ConfigHandler.block.stagnant.stagnantParticles == 4) getTexture = 6;

                    	Main.proxy.spawnParticle(0, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.5D, (double)pos.getZ() + 0.5D, (double)((float)i + world.rand.nextFloat()) - 0.5D, (double)((float)k - world.rand.nextFloat() - 1.0F), (double)((float)j + world.rand.nextFloat()) - 0.5D, getTexture);
                    }
                }
            }
        }
	}
	
	/** Counts the acceptable frame blocks around the Stasis. Unlike a Conduit, blocks directly around the Stasis do NOT stop it! */
	public int countFrame(World worldIn, BlockPos pos)
	{
		int i = 0;
		for (int h1 = -2; h1 <= 2; h1++)
	    {
	    	for (int i1 = -2; i1 <= 2; i1++)
	    	{
	    		for (int j1 = -2; j1 <= 2; j1++)
		    	{
	    			BlockPos tPos = pos.add(h1, i1, j1);
					if ( ArrayUtils.contains(ConfigHandler.block.stagnant.stagnantFrameBlocks, worldIn.getBlockState(tPos).getBlock().getRegistryName().toString()) )
					{ i++; }
		    	}	
	    	}
	    }
		return i;
	}

	/** If this entity is an acceptable attack target. Requires it to NOT be within the `stagnantIgored` list, and if it is a player, to be capable of being damaged.*/
	public boolean isAcceptableTarget(EntityLivingBase entity)
	{
		if (entity == null || entity.isDead || ConfigArrayHandler.stagnantIgnored.contains(EntityList.getKey(entity))) return false;

		if (entity instanceof EntityPlayer)
		{ return !((EntityPlayer)entity).capabilities.disableDamage; }

		return true;
	}

	public static final net.minecraft.util.math.AxisAlignedBB INFINITE_EXTENT_AABB = new net.minecraft.util.math.AxisAlignedBB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	/** Forces the Stasis to render at extreme distances, so the Laser is always visible. */
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{ return INFINITE_EXTENT_AABB; }

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("Active", this.isActive);
        compound.setBoolean("Elder", this.isElder);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.isActive = compound.getBoolean("Active");
        this.isElder = compound.getBoolean("Elder");
    }
}