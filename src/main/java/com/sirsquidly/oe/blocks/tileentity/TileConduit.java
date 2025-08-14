package com.sirsquidly.oe.blocks.tileentity;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.blocks.IChecksWater;
import com.sirsquidly.oe.init.OEPotions;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileConduit extends TileEntity implements ITickable
{
	public EntityLivingBase attackTarget;
	public boolean isActive;
	public boolean canHunt;
	public int bobTick;
	public float shellRotation;
    public float shellRotationPrev;
    public float shellRotateSpeed;
    public int windTick;
    /** The minimum blocks required for a Conduit to be able to Hunt/Defend. */
    public int minHuntrame = 42;
    /** The range at which a hunting Conduit can attack mobs.*/
    public int huntRange = 8;
    
	@Override
	public void update()
	{
		if(world == null) return;

		doAnimCounters();
		
		if(world.getTotalWorldTime() % 20L == 0L)
		{
			if(countFrame(world, pos) >= 12)
			{
				doAmbientSounds(world, pos);
				if (!world.isRemote) doEffect(world, pos);
				canHunt = countFrame(world, pos) >= this.minHuntrame;
				if (canHunt) doHunting(world, pos);

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
					canHunt = false;
				}
			}
		}
		
		if(world.isRemote && isActive)
		{
			spawnParticles(world, pos);
		}
	}
	
	/** Handles giving out the Conduit Power effect to nearby Players.*/
	public void doEffect(World worldIn, BlockPos pos)
	{
		int expandBy = 16 * (int) Math.floor(countFrame(world, pos) / 7);
		
		for(EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-expandBy, -expandBy, -expandBy), pos.add(expandBy + 1, expandBy + 1, expandBy + 1))))
		{
			int sphereCheck = (expandBy + 1) * (expandBy + 1);
			if (player.getDistanceSqToCenter(pos) < sphereCheck && player.isWet())
			{
				player.addPotionEffect(new PotionEffect(OEPotions.CONDUIT_POWER, 10 * 20 + 18, 0, true, true));
			}
		}
	}
	
	/** Handles the Hunting/Attacking behavior of the Conduit.*/
	public void doHunting(World worldIn, BlockPos pos)
	{
		List<EntityMob> nearbyMobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(pos.add(-huntRange, -huntRange, -huntRange), pos.add(huntRange + 1, huntRange + 1, huntRange + 1)));
		
		if (!nearbyMobs.contains(this.attackTarget))
		{ this.attackTarget = null; }
		
		for(EntityMob nearbyMob : nearbyMobs)
		{
			if (nearbyMob.isWet()) 
			{
				if (this.attackTarget == null || this.attackTarget.isDead)
				{
					this.attackTarget = nearbyMob;
				}
				else
				{
					if(world.isRemote) spawnParticles(world, this.attackTarget.getPosition()); 
					if(world.getTotalWorldTime() % 40L == 0L)
					{
						world.playSound(null, this.attackTarget.getPosition(), OESounds.BLOCK_CONDUIT_ATTACK, SoundCategory.BLOCKS, 1.0f, 1.0f);
						this.attackTarget.attackEntityFrom(DamageSource.MAGIC,4);
					}
				}
			}
		}
	
	}
	
	/** Handles the variables used for the Conduit's animations. A LOT copied from the Enchantment Table.*/
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
		this.shellRotateSpeed += 0.02F;
		
		while (this.shellRotation >= (float)Math.PI)
	    {
	        this.shellRotation -= ((float)Math.PI * 2F);
	    }
	
	    while (this.shellRotation < -(float)Math.PI)
	    {
	        this.shellRotation += ((float)Math.PI * 2F);
	    }
	    
	    while (this.shellRotateSpeed >= (float)Math.PI)
	    {
	        this.shellRotateSpeed -= ((float)Math.PI * 2F);
	    }
	
	    while (this.shellRotateSpeed < -(float)Math.PI)
	    {
	        this.shellRotateSpeed += ((float)Math.PI * 2F);
	    }
	
	    float f2;
	
	    for (f2 = this.shellRotateSpeed - this.shellRotation; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
	    {
	        ;
	    }
	    while (f2 < -(float)Math.PI)
	    {
	        f2 += ((float)Math.PI * 2F);
	    }
		this.shellRotation += f2 * 0.4F;
		
		
		if(world.getTotalWorldTime() % 2 == 0)
		{ 
			++this.windTick;
			
			if (this.windTick > 80) this.windTick = 1;
		}
		
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

                if (world.rand.nextInt(64) == 0 && ConfigHandler.block.conduit.conduitParticles != 0)
                {
                    for (int k = -1; k <= 2; ++k)
                    {
                    	int getTexture = world.rand.nextInt(12);
                    	if (ConfigHandler.block.conduit.conduitParticles == 2) getTexture = 12;
                    	if (ConfigHandler.block.conduit.conduitParticles == 3) getTexture = world.rand.nextInt(13);
                    	if (ConfigHandler.block.conduit.conduitParticles == 4) getTexture = 6;
                    	
                    	Main.proxy.spawnParticle(0, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.5D, (double)pos.getZ() + 0.5D, (double)((float)i + world.rand.nextFloat()) - 0.5D, (double)((float)k - world.rand.nextFloat() - 1.0F), (double)((float)j + world.rand.nextFloat()) - 0.5D, getTexture);
                    }
                }
            }
        }
	}
	
	/** Counts the acceptable frame blocks around the Conduit. Thank you past self for the Coral Bulb code! */
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
	    			IBlockState blockState = worldIn.getBlockState(tPos);
	    			/** If the area directly around the Conduit isn't water, just return 0.  */
					if (h1 != -2 && i1 != -2 && j1 != -2 && h1 != 2 && i1 != 2 && j1 != 2)
					{
						/** If Fluidlogged API is installed, we get to use their methods! */
						if (Main.proxy.fluidlogged_enable)
						{
							final FluidState fluidState = FluidloggedUtils.getFluidState(world, tPos);
			            	
							if(fluidState.isEmpty() || fluidState.getMaterial() != Material.WATER) return 0;
			            }
						else if (blockState.getMaterial() != Material.WATER && !(blockState.getBlock() instanceof IChecksWater)) return 0;
					}
					else if ( ArrayUtils.contains(ConfigHandler.block.conduit.conduitFrameBlocks, worldIn.getBlockState(tPos).getBlock().getRegistryName().toString()) )
					{ i++; }
		    	}	
	    	}
	    }
		return i;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("Active", this.isActive);
        compound.setBoolean("Can Hunt", this.canHunt);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.isActive = compound.getBoolean("Active");
        this.canHunt = compound.getBoolean("Can Hunt");
    }
}