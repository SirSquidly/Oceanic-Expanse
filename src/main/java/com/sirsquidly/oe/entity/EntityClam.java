package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityClam extends EntityAnimal
{
	protected Block spawnableBlock = Blocks.SAND;
	protected static final DataParameter<Byte> OPEN_TICK = EntityDataManager.<Byte>createKey(EntityClam.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> SHAKING = EntityDataManager.<Boolean>createKey(EntityClam.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> LAUNCH_OPEN = EntityDataManager.<Boolean>createKey(EntityClam.class, DataSerializers.BOOLEAN);
	private static final UUID CLOSED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier CLOSED_ARMOR_BONUS = (new AttributeModifier(CLOSED_ARMOR_BONUS_ID, "Closed armor bonus", 20.0D, 0)).setSaved(false);
    private float prevOpenAmount;
    private float openAmount;
    private float launchOpenTimer;
    private int launchWarnShaking;
	
	public EntityClam(World worldIn)
	{
		super(worldIn);
		this.setSize(1.5F, 0.4F);
		this.isImmuneToFire = true;
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityClam.AIClamAmbient(this));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }
	
	protected void entityInit()
    { 
		super.entityInit(); 
		this.dataManager.register(OPEN_TICK, Byte.valueOf((byte)0));
		this.dataManager.register(SHAKING, Boolean.valueOf(false));
		this.dataManager.register(LAUNCH_OPEN, Boolean.valueOf(false));
	}
	
	public boolean getShaking()
    { return ((Boolean)this.dataManager.get(SHAKING)).booleanValue(); }

    public void setShaking(boolean shake)
    { this.dataManager.set(SHAKING, Boolean.valueOf(shake)); }
    
    public boolean getLaunching()
    { return ((Boolean)this.dataManager.get(LAUNCH_OPEN)).booleanValue(); }

    public void setLaunching(boolean launch)
    { this.dataManager.set(LAUNCH_OPEN, Boolean.valueOf(launch)); }
    
	public int getOpenTick()
    { return ((Byte)this.dataManager.get(OPEN_TICK)).byteValue(); }
	
	protected SoundEvent getDeathSound()
    { return SoundHandler.ENTITY_CLAM_DEATH; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.getOpenTick() > 0 ? SoundHandler.ENTITY_CLAM_HURT : SoundHandler.ENTITY_CLAM_HURT_CLOSED;
    }
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.getOpenTick() == 0 && !this.isDead)
		{
			List<Entity> checkAbove = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().offset(0, 1, 0).grow(0, 0.5, 0));
			
			
			for (Entity e : checkAbove) 
	    	{
				if (e instanceof EntityClam) continue;
				
				if (!this.getShaking()) this.playSound(SoundHandler.ENTITY_CLAM_SHAKE, 1.0F, 1.0F);
				
				this.setShaking(true);
				launchWarnShaking += 1;
				
				BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
				
				if (launchWarnShaking > 20 || this.world.getBlockState(blockpos.down()).getBlock() instanceof BlockMagma)
				{
					this.setShaking(false);
					this.doClamOpening(20);
					this.setLaunching(true);
					launchWarnShaking = 0;
					
					if (this.isInWater())
					{
						for (int i = 0; i < 300; ++i)
	                    { this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + ((double)this.rand.nextFloat() - 0.5D), this.posY, this.posZ + ((double)this.rand.nextFloat() - 0.5D), ((double)this.rand.nextFloat() - 0.5D) * 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 8.0D, ((double)this.rand.nextFloat() - 0.5D) * 0.5D); }	
					}
					
					if (e.isInWater())
					{ e.setVelocity(e.motionX, 3.0F, e.motionZ); }
					else
					{ e.setVelocity(e.motionX, 1.0F, e.motionZ); }
				}
			}
			if (checkAbove.isEmpty())
			{
				this.setShaking(false);
				launchWarnShaking = 0;
			}
		}
		
		if (this.getOpenTick() > 0 && this.getLaunching())
		{
			launchOpenTimer += 1;
			
			
			if (launchOpenTimer > 20)
        	{
        		this.doClamOpening(0);
        	}
		}
		
		
		float f1 = (float)this.getOpenTick() * 0.01F;
        this.prevOpenAmount = this.openAmount;

        
        if (this.getLaunching())
    	{
        	if (this.openAmount > f1)
            {
                this.openAmount = MathHelper.clamp(this.openAmount - 0.05F, f1, 1.0F);
            }
            else if (this.openAmount < f1)
            {
                this.openAmount = MathHelper.clamp(this.openAmount + 0.05F, 0.0F, f1);
            }
        	
        	if (this.openAmount == f1 && launchOpenTimer > 20)
        	{ 
        		launchOpenTimer = 0;
        		this.setLaunching(false); 
        	}
    	}
        else
        {
        	if (this.openAmount > f1)
            {
                this.openAmount = MathHelper.clamp(this.openAmount - 0.005F, f1, 1.0F);
            }
            else if (this.openAmount < f1)
            {
                this.openAmount = MathHelper.clamp(this.openAmount + 0.005F, 0.0F, f1);
            }
        }
    }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox()
    { return this.getEntityBoundingBox(); }

    public boolean canBePushed()
    { return false; }
    
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{ return null; }
	
	public boolean canBeLeashedTo(EntityPlayer player)
    { return false; }
		
	public boolean isNotColliding()
    { return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	@Override
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        
        for (int l = 0; l < 3; l++)
        {
        	if (this.world.getBlockState(blockpos.down()).getMaterial() == Material.WATER) blockpos = blockpos.add(0, -l, 0);
        }

		return this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock;
    }
	
	//** Used for when the Clam opens and closes. */
	public void doClamOpening(int ticks)
    {
        if (!this.world.isRemote)
        {
        	IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        	
        	iattributeinstance.removeModifier(CLOSED_ARMOR_BONUS);

            if (ticks == 0)
            {
            	iattributeinstance.applyModifier(CLOSED_ARMOR_BONUS);
                this.playSound(SoundHandler.ENTITY_CLAM_CLOSE, 1.0F, 1.0F);
            }
            else
            {
            	if (this.isInWater())
            	{
            		this.playSound(SoundHandler.ENTITY_CLAM_OPEN, 1.0F, 1.0F);
            	}
            	else
            	{
            		this.playSound(SoundHandler.ENTITY_CLAM_OPEN_LAND, 1.0F, 1.0F);
            	}
            }
        }

        this.dataManager.set(OPEN_TICK, Byte.valueOf((byte)ticks));
    }
	
	@SideOnly(Side.CLIENT)
    public float getClientOpenAmount(float p_184688_1_)
    {
        return this.prevOpenAmount + (this.openAmount - this.prevOpenAmount) * p_184688_1_;
    }
	
	class AIClamAmbient extends EntityAIBase
    {
		private final EntityClam clam;

        private AIClamAmbient(EntityClam clamIn)
        {
        	this.clam = clamIn;
        }

        public boolean shouldExecute()
        {
        	List<Entity> checkAbove = this.clam.world.getEntitiesWithinAABBExcludingEntity(this.clam, getEntityBoundingBox().grow(2.0, 2.0, 2.0));
        	
            return !this.clam.getLaunching() && !this.clam.getShaking() && (this.clam.isInWater() && this.clam.rand.nextInt(40) == 0 || !checkAbove.isEmpty() && this.clam.getOpenTick() != 0);
        }

        public void updateTask()
        {
        	List<Entity> checkAbove = this.clam.world.getEntitiesWithinAABBExcludingEntity(this.clam, getEntityBoundingBox().grow(2.0, 2.0, 2.0));
        	
        	if (checkAbove.isEmpty())
        	{
        		if (this.clam.getOpenTick() != 0)
            	{ this.clam.doClamOpening(0); }
            	else
            	{ this.clam.doClamOpening(20); }
        	}
        	else for (Entity e : checkAbove)
        	{
        		if (!e.isSneaking())
        		{
        			if (this.clam.getOpenTick() != 0) this.clam.doClamOpening(0);
        			
        		}
        	}
        }
    }
}