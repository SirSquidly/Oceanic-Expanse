package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
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

	/* How long this Clam has held its current item. */
	private int minutesItemHeld;
	/* When a Clam will need to convert the held item. */
	private int minutesItemSwap;
	
	public EntityClam(World worldIn)
	{
		super(worldIn);
		this.setSize(1.5F, 0.4F);
		this.isImmuneToFire = true;
	}

	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityClam.AIClamLaunch(this));
		this.tasks.addTask(2, new EntityClam.AIClamAmbient(this));
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
		this.dataManager.register(OPEN_TICK, (byte) 0);
		this.dataManager.register(SHAKING, Boolean.FALSE);
		this.dataManager.register(LAUNCH_OPEN, Boolean.FALSE);
	}
	
	public boolean getShaking()
    { return this.dataManager.get(SHAKING); }

    public void setShaking(boolean shake)
    { this.dataManager.set(SHAKING, shake); }
    
    public boolean getLaunching()
    { return this.dataManager.get(LAUNCH_OPEN); }

    public void setLaunching(boolean launch)
    { this.dataManager.set(LAUNCH_OPEN, launch); }
    
	public int getOpenTick()
    { return (Byte) this.dataManager.get(OPEN_TICK); }
	
	protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_CLAM_DEATH; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return this.getOpenTick() > 0 ? OESounds.ENTITY_CLAM_HURT : OESounds.ENTITY_CLAM_HURT_CLOSED; }

	protected SoundEvent getOpenSound()
	{ return this.isInWater() ? OESounds.ENTITY_CLAM_OPEN : OESounds.ENTITY_CLAM_OPEN_LAND; }

	protected SoundEvent getCloseSound()
	{ return OESounds.ENTITY_CLAM_CLOSE; }

	protected SoundEvent getShakeSound()
	{ return OESounds.ENTITY_CLAM_SHAKE; }
	
	public void onUpdate()
	{
		super.onUpdate();
		
		float f1 = (float)this.getOpenTick() * 0.01F;
        this.prevOpenAmount = this.openAmount;
		float clamSpeed = this.getLaunching() ? 0.05F : 0.005F;

		if (this.openAmount > f1)
		{ this.openAmount = MathHelper.clamp(this.openAmount - clamSpeed, f1, 1.0F); }
		else if (this.openAmount < f1)
		{ this.openAmount = MathHelper.clamp(this.openAmount + clamSpeed, 0.0F, f1); }

		if (!this.getShaking() && this.openAmount == 0) this.setLaunching(false);

		/** Every minute, update the `minutesItemHeld` data and swap out the item if required. */
		if (!this.world.isRemote && (this.ticksExisted + this.getEntityId()) % 1200 == 0)
		{
			if (++this.minutesItemHeld >= this.minutesItemSwap) swapHeldItem();
		}
    }

	private void swapHeldItem()
	{
		ItemStack currentItem = this.getHeldItemMainhand();

		for (int i = 0; i < ConfigArrayHandler.itemClamConvertFrom.size(); i++)
		{
			ItemStack listItem = ConfigArrayHandler.itemClamConvertFrom.get(i);
			if (ItemStack.areItemsEqual(listItem, currentItem))
			{
				ItemStack newItemstack = ConfigArrayHandler.itemClamConvertTo.get(i);

				this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newItemstack);
				this.setupConvertTimes(newItemstack);
				this.minutesItemHeld = 0;
				return;
			}
		}
	}

	private void setupConvertTimes(ItemStack item)
	{
		for (int i = 0; i < ConfigArrayHandler.itemClamConvertFrom.size(); i++)
		{
			ItemStack listItem = ConfigArrayHandler.itemClamConvertFrom.get(i);

			if (ItemStack.areItemsEqual(listItem, item))
			{
				this.minutesItemSwap = ConfigArrayHandler.itemClamConvertTime.get(i);
				return;
			}
		}
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
		ItemStack playerItem = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack heldItem = this.getHeldItemMainhand();
		
		Boolean doAddClamItem = heldItem.isEmpty() && !playerItem.isEmpty();
		Boolean doRemoveClamItem = !heldItem.isEmpty() && playerItem.isEmpty();
		
		if (this.world.isRemote || this.getOpenTick() == 0) return super.processInteract(player, hand);
		
		if (doAddClamItem || doRemoveClamItem)
		{
			SoundEvent playSound = doAddClamItem ? SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM : SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM;
			ItemStack clamHeld =  doAddClamItem ? playerItem.copy() : ItemStack.EMPTY;
			
			this.playSound(playSound, 1.0F, 1.0F);
			
			
			if (doAddClamItem)
			{ if (!player.capabilities.isCreativeMode) { playerItem.shrink(1); } }
			else
			{
	        	heldItem.setCount(1);
	        	this.entityDropItem(heldItem.copy(), 0.25F);
			}
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, clamHeld);
			if (!clamHeld.isEmpty()) this.setupConvertTimes(clamHeld);
			player.swingArm(EnumHand.MAIN_HAND);
			this.minutesItemHeld = 0;
			return true;
		}
		
        return super.processInteract(player, hand);
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        if (this.rand.nextFloat() < 0.05F && ConfigHandler.item.pearl.enablePearl)
        { this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(OEItems.PEARL)); }
        else if (this.rand.nextFloat() < 0.5F)
        {
        	boolean spawnGravel = this.rand.nextFloat() < 0.1F;
			ItemStack block = new ItemStack(spawnGravel ? Blocks.GRAVEL : Blocks.SAND);

        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, block);
			this.setupConvertTimes(block);
        }
        
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 100);
        this.setDropChance(EntityEquipmentSlot.OFFHAND, 100);
        
        return super.onInitialSpawn(difficulty, livingdata);
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
        
        List<Entity> checkSurroundingClams = this.world.getEntitiesWithinAABB(EntityClam.class, getEntityBoundingBox().grow(64, 64, 64));
		if ( checkSurroundingClams.size() > 3) return false;
		
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
			SoundEvent sound = getOpenSound();

        	iattributeinstance.removeModifier(CLOSED_ARMOR_BONUS);

            if (ticks == 0)
            {
            	iattributeinstance.applyModifier(CLOSED_ARMOR_BONUS);
				sound = getCloseSound();
            }

			this.playSound(sound, 1.0F, 1.0F);
        }

        this.dataManager.set(OPEN_TICK, (byte) ticks);
    }

	@SideOnly(Side.CLIENT)
    public float getClientOpenAmount(float p_184688_1_)
    { return this.prevOpenAmount + (this.openAmount - this.prevOpenAmount) * p_184688_1_; }

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("MinutesItemHeld", this.minutesItemHeld);
		compound.setInteger("MinutesItemSwap", this.minutesItemSwap);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.minutesItemHeld = compound.getInteger("MinutesItemHeld");
		this.minutesItemSwap = compound.getInteger("MinutesItemSwap");
	}

	/**
	* AI for Clams to randomly Open and Close
	* */
	class AIClamAmbient extends EntityAIBase
    {
		private final EntityClam clam;

        private AIClamAmbient(EntityClam clamIn)
        { this.clam = clamIn; }

        public boolean shouldExecute()
        { return !this.clam.getLaunching() && !this.clam.getShaking() && this.clam.isInWater() && this.clam.rand.nextInt(100) == 0; }

        public void updateTask()
        {
			if (this.clam.getOpenTick() != 0)
			{ this.clam.doClamOpening(0); }
			else
			{ this.clam.doClamOpening(20); }
        }
    }

	/**
	 * AI for Clams to detect and launch mobs atop them
	 * */
	class AIClamLaunch extends EntityAIBase
	{
		private final EntityClam clam;
		private List<Entity> entitiesAtopClam;
		/** Timer for how long the Clam has been shaking for. */
		private int launchWarnShaking;
		/** Timer for how long the Clam has been open since preforming a Launch. */
		private int launchOpenTimer;
		/** If the Clam preformed the launching of above entities. */
		private boolean didLaunch;

		private AIClamLaunch(EntityClam clamIn)
		{ this.clam = clamIn; }

		public boolean shouldExecute()
		{
			if (this.clam.getLaunching()) return true;
			this.entitiesAtopClam = getAboveEntities();
			return this.clam.getOpenTick() == 0 && !entitiesAtopClam.isEmpty();
		}

		public void resetTask()
		{
			this.clam.setShaking(false);
			this.launchWarnShaking = 0;
			this.launchOpenTimer = 0;
			this.didLaunch = false;
		}

		public void updateTask()
		{
			if (this.didLaunch)
			{
				if (this.clam.getOpenTick() > 0 && ++launchOpenTimer > 20) this.clam.doClamOpening(0);
				return;
			}

			if (entitiesAtopClam.isEmpty()) return;

			if (!this.clam.getLaunching())
			{
				if (!this.clam.getShaking())
				{
					this.clam.playSound(this.clam.getShakeSound(), 1.0F, 1.0F);
					this.clam.setShaking(true);
				}

				BlockPos blockpos = new BlockPos(this.clam.posX, this.clam.posY, this.clam.posZ);

				if ((++launchWarnShaking >= 20 || this.clam.world.getBlockState(blockpos.down()).getBlock() instanceof BlockMagma) && !this.clam.world.getBlockState(blockpos.up()).isSideSolid(world, blockpos.up(), EnumFacing.DOWN))
				{
					/* This does not work within AI Tasks, look into some better way to do launch particles? */
					//if (this.clam.isInWater())
					//{
					//	  for (int i = 0; i < 300; ++i)
					//	  { this.clam.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.clam.posX + ((double)this.clam.rand.nextFloat() - 0.5D), this.clam.posY, this.clam.posZ + ((double)this.clam.rand.nextFloat() - 0.5D), ((double)this.clam.rand.nextFloat() - 0.5D) * 0.5D, ((double)this.clam.rand.nextFloat() - 0.5D) * 8.0D, ((double)this.clam.rand.nextFloat() - 0.5D) * 0.5D); }
					//}

					this.clam.setShaking(false);
					this.clam.doClamOpening(20);
					launchWarnShaking = 0;
					this.clam.setLaunching(true);
				}
			}
			else
			{
				for (Entity e : entitiesAtopClam)
				{
					e.motionY = e.isInWater() ? 3.0F : 1.0F;
					e.velocityChanged = true;
				}

				this.clam.setShaking(false);
				this.didLaunch = true;
			}
		}

		public List<Entity> getAboveEntities()
		{
			List<Entity> checkAbove = this.clam.world.getEntitiesWithinAABBExcludingEntity(this.clam, getEntityBoundingBox().offset(0, 1, 0).grow(0, 0.5, 0));
			checkAbove.removeIf(thisEntity -> thisEntity.isDead || thisEntity instanceof EntityClam);
			return checkAbove;
		}
	}
}