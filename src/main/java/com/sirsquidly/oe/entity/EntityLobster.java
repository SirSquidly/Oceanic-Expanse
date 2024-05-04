package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIMateCarryEgg;
import com.sirsquidly.oe.entity.ai.EntityAIMateDepositEgg;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityLobster extends EntityAnimal implements IEggCarrierMob
{
	private static final DataParameter<Boolean> ANGRY = EntityDataManager.<Boolean>createKey(EntityLobster.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> LOBSTER_SIZE = EntityDataManager.<Integer>createKey(EntityLobster.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SADDLED = EntityDataManager.<Boolean>createKey(EntityLobster.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.<Boolean>createKey(EntityLobster.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> FOOD = EntityDataManager.<Integer>createKey(EntityLobster.class, DataSerializers.VARINT);
	/** Time between being able to molt. */
    public int moltCooldown;
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.FISH);
	private static final Set<Item>EDIBLE_ITEMS = Sets.newHashSet(Items.FISH, Item.getItemFromBlock(OEBlocks.KELP), OEItems.DRIED_KELP, OEItems.CRAB_UNCOOKED, OEItems.CRAB_COOKED, OEItems.LOBSTER_COOKED, OEItems.LOBSTER_UNCOOKED, OEItems.CRUSTACEAN_SHELL);
	/** Handles all the colors */
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityLobster.class, DataSerializers.VARINT);
	private int randomAngrySoundDelay;
	
	public EntityLobster(World worldIn)
	{
		super(worldIn);
		//this.setSize(0.8F, 0.3F);
		this.setCanPickUpLoot(true);
		this.setPathPriority(PathNodeType.WALKABLE, 1.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
		this.moltCooldown = ConfigHandler.entity.lobster.lobsterMoltCooldown;
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void entityInit()
    { 
		super.entityInit();
		this.dataManager.register(ANGRY, Boolean.valueOf(false));
		this.dataManager.register(LOBSTER_SIZE, Integer.valueOf(1));
		this.dataManager.register(VARIANT, 0);
		this.dataManager.register(SADDLED, Boolean.valueOf(false));
		this.dataManager.register(HAS_EGG, Boolean.valueOf(false));
		this.dataManager.register(FOOD, 0);
	}
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIMateCarryEgg(this, 1.0D));
		this.tasks.addTask(1, new EntityAIMateDepositEgg(this, 1.0D));
		this.tasks.addTask(3, new EntityAIStompTurtleEgg(this, 1.0D));
		this.tasks.addTask(4, new EntityAITempt(this, 1.0D, Items.FISH, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityLobster.AIHurtByTarget(this));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(EntityLivingBase.SWIM_SPEED).setBaseValue(1.0D);
    }
	
	public int getTalkInterval()
    { return 0; }
	
	public void onLivingUpdate()
    {
		if (this.isInWater())
			{ stepHeight = 1.0F; }
		else { stepHeight = 0.6F; }
		
		if (world.getTotalWorldTime() % 20L == 0L)
		{
			if (!this.getHeldItemMainhand().isEmpty()) doEatingStuff();
			
			if (!this.world.isRemote && !this.isChild() &&(this.getFood() * 0.75 ) / getSalmonSize() > 1 && --this.moltCooldown <= 0)
			{
				this.moltCooldown = ConfigHandler.entity.lobster.lobsterMoltCooldown;
				this.playSound(OESounds.ENTITY_LOBSTER_MOLT, 1.0F, 0.5F);
				this.setSize(this.getSalmonSize() + 1, true);
				this.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 300));
				this.dropItem(OEItems.CRUSTACEAN_SHELL, 1);
				this.setFood(0);
			}
		}
		
		if (this.isAngry())
        {
			if (this.randomAngrySoundDelay <= 0)
			{
				this.playSound(OESounds.ENTITY_LOBSTER_ANGRY, 1.0F, 1.0F);
				this.randomAngrySoundDelay = this.rand.nextInt(40);
			}
			this.randomAngrySoundDelay -= 1;
        }
		
		if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry())
        {
            this.setAngry(false);
        }
		
        super.onLivingUpdate();
    }
	
	public void doEatingStuff()
	{
		ItemStack food = this.getHeldItemMainhand();
		
		if (isEdibleItem(food))
		{
			if (this.world.isRemote)
            {
        		for (int i = 0; i < 20; ++i)
                {
        			this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY + this.height, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Item.getIdFromItem(food.getItem()), food.getMetadata());
                }	
            }
        	
        	this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
        	
        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
        	
        	this.setFood(this.getFood() + 1);
		}
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (this.isEdibleItem(itemstack) && this.getHeldItemMainhand().isEmpty())
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
        	if (!player.capabilities.isCreativeMode) { itemstack.shrink(1); }
        }

        if (this.getSalmonSize() > 12)
        {
        	if (itemstack.getItem() == Items.SADDLE && !this.getSaddled() && !player.isSneaking())
            {
            	this.setSaddled(true);
                this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                itemstack.shrink(1);
                return true;
            }
        	else if (this.getSaddled())
        	{
        		if (itemstack.getItem() instanceof ItemShears && this.getSaddled() && player.isSneaking())
        		{
        			this.setSaddled(false);
            		this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            		if (!this.world.isRemote) this.dropItem(Items.SADDLE, 1);
            		return true;
        		}
        		else if (!this.isBeingRidden() && !player.isSneaking())
        		{
        			if (!this.world.isRemote) player.startRiding(this);
                    return true;
        		}
        	}
        }
        
        return super.processInteract(player, hand);
    }
	
	/** Literally loops the dropLoot method depending on the Lobster's Size. A little hacky. */
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        int loopAmount = Math.max(this.getSalmonSize() / 3, 1);
     
        for (int l = 0; l < lootingModifier; l++)
        { if (this.rand.nextBoolean() == true) ++loopAmount; }
        
        for (int i = 0; i < loopAmount; i++)
        { super.dropLoot(wasRecentlyHit, lootingModifier, source); }
        
        if (this.getSaddled())
        { this.dropItem(Items.SADDLE, 1); }
    }
	
	public boolean isNotColliding()
    { return this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	@Override
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        
        List<Entity> checkSurroundingClams = this.world.getEntitiesWithinAABB(EntityLobster.class, getEntityBoundingBox().grow(64, 64, 64));
		if ( checkSurroundingClams.size() > 3) return false;
		
        for (int l = 0; l < 3; l++)
        {
        	if (this.world.getBlockState(blockpos.down()).getMaterial() == Material.WATER) blockpos = blockpos.add(0, -l, 0);
        }

		return this.world.getBlockState(blockpos.down()).isSideSolid(world, new BlockPos(this).down(), EnumFacing.UP);
    
		
        //return this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this) && this.world.getBlockState((new BlockPos(this)).down()).isSideSolid(world, new BlockPos(this).down(), EnumFacing.UP);
    }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_LOBSTER_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_LOBSTER_DEATH; }
    
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_LOBSTER; }
	
	protected float getWaterSlowDown()
    { return this.isBeingRidden() ? 0.4F : 0.8F; }
	
	public EnumCreatureAttribute getCreatureAttribute()
    { return EnumCreatureAttribute.ARTHROPOD; }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	@Override
    public boolean shouldDismountInWater(Entity rider)
    { return false; }
	
	public EntityLobster createChild(EntityAgeable ageable)
    { 
		EntityLobster entity = new EntityLobster(world);
		int i = entity.getRandomLobsterVariant(0, false);
		if (new Random().nextDouble() <= 0.005) i = entity.getRandomLobsterVariant(i, true);
     	
		entity.setLobsterVariant(i);
		entity.setSize(1 + this.rand.nextInt(4), true); 
		return entity;
	}
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }
	
	public boolean isEdibleItem(ItemStack stack)
    { return EDIBLE_ITEMS.contains(stack.getItem()); }

	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();

        if (this.isEdibleItem(itemstack) && this.getHeldItemMainhand().isEmpty() && itemEntity.getAge() >= 5 * 20)
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
        	int itemnumber = itemstack.getCount();

            if (itemnumber - 1 == 0)
            { itemEntity.setDead(); }
            else
            { itemstack.setCount(itemnumber - 1); }
        }
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        int i = this.getRandomLobsterVariant(0, false);

        if (new Random().nextDouble() <= 0.005) i = this.getRandomLobsterVariant(i, true);
        	
        this.setLobsterVariant(i);

        this.setSize(1 + this.rand.nextInt(4), true);
        
        return super.onInitialSpawn(difficulty, livingdata);
    }
	
	/** Creates a random number for a Lobster varient, within acceptable range. */
	private int getRandomLobsterVariant(int firstColor, boolean doSecondaryColoring)
    {
		Random rand = new Random();
		double seed = 0;
		int color = firstColor;
		int shift = doSecondaryColoring ? 8 : 0;

		int[] colorInt = {rand.nextInt(2) + 7, rand.nextInt(4) + 3, 2};
	    double[] probabilities = {0.001, 0.005, 0.01};
	    
	    
	    for (int i = 0; i < probabilities.length; i++)
	    {
	    	seed = rand.nextDouble() / (doSecondaryColoring ? 2 : 1);
	    	
	        if (seed <= probabilities[i])
	        {
	            color |= (colorInt[i] << shift);
	            return color;
	        }
	    }
	    if (color == 0)
	    {
	    	color |= (rand.nextInt(2) << shift);
	    }

		return color;
    }
    
    /** This generates the specific name of the tropical fish variant. */
    public static String getSpecificName(int variantInt)
    {
    	String fullName;
    	String color1 = I18n.format("description.oe.lobster_color" + (variantInt & 255) + ".name");
    	String color2 = I18n.format("description.oe.lobster_color" + (variantInt >> 8 & 255) + ".name");
    	
    	if (!color1.equals(color2) && (variantInt >> 8 & 255) != 0)
    	{ fullName = color1 + "-" + color2 + " " + I18n.format("entity.oe.lobster.name"); }
    	else
    	{ fullName = color1 + " " + I18n.format("entity.oe.lobster.name"); }

    	if ((variantInt & 255) == 1 && (variantInt >> 8 & 255) == 5 || (variantInt & 255) == 1 && (variantInt >> 8 & 255) == 5)
    	{
    		fullName = I18n.format("description.oe.lobster_half" + (variantInt & 255) + (variantInt >> 8 & 255) + ".name") + " " + I18n.format("entity.oe.lobster.name");
    	}
    	
    	return fullName;
    }
    
	public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
        	this.setAngry(false);
        	
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            { entityIn.setFire(j * 4); }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
	
	static class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget(EntityLobster crab)
        {
            super(crab, false);
        }

        public void startExecuting()
        {
        	EntityLobster entitycrab = (EntityLobster)this.taskOwner;
        	
        	entitycrab.setAngry(true);
            super.startExecuting();
        }
        
        public boolean shouldContinueExecuting()
        {
            if (this.taskOwner instanceof EntityLobster)
            {
            	EntityLobster entitycrab = (EntityLobster)this.taskOwner;

                if (!entitycrab.isAngry())
                {
                    return false;
                }
            }

            return super.shouldContinueExecuting();
        }
    }
	
	//** Chunk of code dedicated to Riding Behavior*/
	@Nullable
	public Entity getControllingPassenger()
	{ return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0); }

	public boolean canBeSteered()
	{
		Entity entity = this.getControllingPassenger();
		if (!(entity instanceof EntityLivingBase) || entity == null) return false;
		return (this.isEdibleItem(((EntityLivingBase) entity).getHeldItemMainhand())); 
	}
		
	public void travel(float strafe, float vertical, float forward)
	{
		Entity entity = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);

		if (this.isBeingRidden() && this.canBeSteered())
		{
			this.rotationYaw = entity.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
            
            if (this.canPassengerSteer())
	         {
	                float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225F;

	                this.setAIMoveSpeed(f);
	                super.travel(0.0F, 0.0F, 1.0F);
	         }
	         else
	         {
	                this.motionX = 0.0D;
	                this.motionY = 0.0D;
	                this.motionZ = 0.0D;
	         }

	         this.prevLimbSwingAmount = this.limbSwingAmount;
	         double d1 = this.posX - this.prevPosX;
	         double d0 = this.posZ - this.prevPosZ;
	         float f1 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 0.5F;

	         if (f1 > 1.0F)
	         { f1 = 1.0F; }

	         this.limbSwingAmount += (f1 - this.limbSwingAmount) * 0.4F;
	         this.limbSwing += this.limbSwingAmount;
	     }
	     else
	     { super.travel(strafe, vertical, forward); }
	 }
	    
	public int getLobsterVariant()
    { return this.dataManager.get(VARIANT); }
	
	public void setLobsterVariant(int state)
    { this.dataManager.set(VARIANT, state); }
	
	protected void setSize(int size, boolean resetHealth)
    {
        this.dataManager.set(LOBSTER_SIZE, Integer.valueOf(size));
        
        this.setSize(0.55F + (size * 0.1F - 0.1F), 0.25F + (size * 0.025F));
        this.setPosition(this.posX, this.posY, this.posZ);
        
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(10.0D + size/2));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.1D + (size * 0.001D)));
        this.getEntityAttribute(EntityLivingBase.SWIM_SPEED).setBaseValue((double)(10.0D + (size * 0.001D)));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue((double)(1.0D + size/4));
        
        if (resetHealth)
        { this.setHealth(this.getMaxHealth()); }
    }
	
	//** Forces the game to reload and properly  set the size */
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (LOBSTER_SIZE.equals(key))
        {
            int i = this.getSalmonSize();
            this.setSize(0.55F + (i * 0.1F - 0.1F), 0.25F + (i * 0.025F));
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }
	
    public int getSalmonSize()
    { return ((Integer)this.dataManager.get(LOBSTER_SIZE)).intValue(); }
    
    public boolean getSaddled()
    { return ((Boolean)this.dataManager.get(SADDLED)).booleanValue(); }

    public void setSaddled(boolean angry)
    { this.dataManager.set(SADDLED, Boolean.valueOf(angry)); }
    
    public boolean isAngry()
    { return ((Boolean)this.dataManager.get(ANGRY)).booleanValue(); }

    public void setAngry(boolean angry)
    { this.dataManager.set(ANGRY, Boolean.valueOf(angry)); }
    
    public int getFood()
    { return this.dataManager.get(FOOD); }
	
	public void setFood(int state)
    { this.dataManager.set(FOOD, state); }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Angry", this.isAngry());
        compound.setInteger("Size", this.getSalmonSize());
        compound.setInteger("Variant", this.getLobsterVariant());
        compound.setBoolean("Saddle", this.getSaddled());
        compound.setInteger("MoltCooldown", this.moltCooldown);
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAngry(compound.getBoolean("Angry"));
        int i = compound.getInteger("Size");

        if (i < 0) { i = 0; }

        this.setSize(i, false);
        this.setLobsterVariant(compound.getInteger("Variant"));
        this.setSaddled(compound.getBoolean("Saddle"));
        
        if (compound.hasKey("EggLayTime"))
        { this.moltCooldown = compound.getInteger("MoltCooldown"); }
    }

	@Override
	public boolean isCarryingEgg()
	{ return ((Boolean)this.dataManager.get(HAS_EGG)).booleanValue(); }

	@Override
	public void setCarryingEgg(boolean bool)
	{ this.dataManager.set(HAS_EGG, Boolean.valueOf(bool)); }

	@Override
	public boolean canLayEgg(World world, BlockPos pos)
	{ return world.getBlockState(pos).getMaterial() == Material.WATER; }

	@Override
	public void placeEgg(World world, BlockPos pos)
	{
		EntityLobster entityBaby = this.createChild(this);
     	entityBaby.setGrowingAge(-24000);
     	entityBaby.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
     	
        world.spawnEntity(entityBaby);
        
		this.setCarryingEgg(false);
	}
}