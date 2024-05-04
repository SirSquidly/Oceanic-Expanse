package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAICrabBarter;
import com.sirsquidly.oe.entity.ai.EntityAICrabDig;
import com.sirsquidly.oe.entity.ai.EntityAIMateCarryEgg;
import com.sirsquidly.oe.entity.ai.EntityAIMateDepositEgg;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCrab extends EntityAnimal implements IEggCarrierMob
{
	protected Block spawnableBlock = Blocks.SAND;
	
	/** 0 = Normal, 1 = Digging, 2 = Eating */
	private static final DataParameter<Integer> ANIM_STATE = EntityDataManager.createKey(EntityCrab.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ANGRY = EntityDataManager.<Boolean>createKey(EntityCrab.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CAN_BARTER = EntityDataManager.<Boolean>createKey(EntityCrab.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CAN_DIG = EntityDataManager.<Boolean>createKey(EntityCrab.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.<Boolean>createKey(EntityCrab.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityCrab.class, DataSerializers.VARINT);
	private static final Set<Item>TRADE_ITEMS = Sets.newHashSet(Items.FISH);
	private int randomAngrySoundDelay;
	private boolean crabRave;
    private BlockPos jukeboxPosition;
	
	public EntityCrab(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.setCanPickUpLoot(true);
		this.setAnimationState(0);
		this.setPathPriority(PathNodeType.WALKABLE, 0.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void entityInit()
    { 
		super.entityInit(); 
		this.dataManager.register(ANIM_STATE, 0); 
		this.dataManager.register(ANGRY, Boolean.valueOf(false));
		this.dataManager.register(CAN_BARTER, Boolean.valueOf(true));
		this.dataManager.register(CAN_DIG, Boolean.valueOf(true));
		this.dataManager.register(HAS_EGG, Boolean.valueOf(false));
		this.dataManager.register(VARIANT, 0);
	}
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIMateCarryEgg(this, 1.0D));
		this.tasks.addTask(1, new EntityAIMateDepositEgg(this, 1.0D));
		this.tasks.addTask(3, new EntityAIStompTurtleEgg(this, 1.0D));
		this.tasks.addTask(2, new EntityAICrabBarter(this));
		this.tasks.addTask(3, new EntityAICrabDig(this));
		this.tasks.addTask(4, new EntityAITempt(this, 1.0D, Items.FISH, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityCrab.AIHurtByTarget(this));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }
	
	public int getTalkInterval()
    { return ConfigHandler.entity.crab.crabTalkInterval; }


    @SideOnly(Side.CLIENT)
    public void setPartying(BlockPos pos, boolean raving)
    {
        this.jukeboxPosition = pos;
        this.crabRave = raving;
    }

    @SideOnly(Side.CLIENT)
    public boolean isPartying()
    {
        return this.crabRave;
    }
    
    
	public void onLivingUpdate()
    {
		ItemStack offered = this.getHeldItemOffhand();
		BlockPos blockpos = new BlockPos(this.posX, this.posY - 0.25, this.posZ);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		this.updateArmSwingProgress();
		
		if (this.isInWater())
		{ 
			if (this.rand.nextInt(80) == 0)
			{
	            double yawX = Math.cos((this.renderYawOffset) * Math.PI / 180.0D);
				double yawZ = Math.sin((this.renderYawOffset) * Math.PI / 180.0D);
				
				for (int i = 0; i < 6; ++i)
                { this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - yawX * 0.2D, this.posY + 0.4D, this.posZ - yawZ * 0.2D, 0, 0.15D, 0); }	
			}
			stepHeight = 1.0F;
		}
		else { stepHeight = 0.6F; }
		
		if (this.jukeboxPosition == null || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 20.0D || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX)
        {
            this.crabRave = false;
            this.jukeboxPosition = null;
        }
		
		if (this.getAnimationState() == 1 || this.isPartying())
        {
        	if (this.world.isRemote && iblockstate.getBlock() != Blocks.AIR)
            {
        		for (int i = 0; i < 2; ++i)
                { this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * (this.isPartying() ? 0.01D : 0.3D), ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Block.getStateId(iblockstate)); }	
            }

        	if (!this.isPartying() && this.rand.nextInt(2) == 0) 
			{ this.playSound(SoundEvents.BLOCK_SAND_BREAK, 1.0F, 1.0F); }
        }
		
		if (this.getAnimationState() == 2 && this.isBarterItem(offered))
        {
        	if (this.world.isRemote)
            {
        		for (int i = 0; i < 2; ++i)
                { this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY + 0.3D, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Item.getIdFromItem(offered.getItem()), offered.getMetadata()); }	
            }
        	if (this.rand.nextInt(2) == 0) 
			{ this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F); }
        }

		if (this.isAngry())
        {
			if (this.randomAngrySoundDelay <= 0)
			{
				this.playSound(OESounds.ENTITY_CRAB_ANGRY, 1.0F, 1.0F);
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
	
	/**
     * Detects if a player nearby has a Trading Item.
     */
	public boolean checkNearbyTrade()
    {
		List<Entity> findPlayer = this.world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().grow(10));
		
		if (findPlayer.isEmpty()) return false;
		
		for (Entity e : findPlayer) 
    	{
			if (this.isBarterItem(((EntityPlayer) e).getHeldItemMainhand()) || this.isBarterItem(((EntityPlayer) e).getHeldItemOffhand()))
			{
				return true;
			}
		}

		return false;
    }

	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (this.canBarter() && this.isBarterItem(itemstack) && this.getHeldItemOffhand().isEmpty() && !(this.isChild()))
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemstack);

        	if (!player.capabilities.isCreativeMode) { itemstack.shrink(1); }
        }
        return super.processInteract(player, hand);
    }
	
	@Override
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.world.getLight(blockpos) > 7;
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setVariant(this.getRandomBiomeVariant());
        return super.onInitialSpawn(difficulty, livingdata);
    }
	
	/** Creates a random number for a Lobster varient, within acceptable range. */
	private int getRandomBiomeVariant()
    {
        Biome biome = this.world.getBiome(new BlockPos(this));
        
        int i = this.rand.nextInt(2);
        
        if (biome == Biomes.BEACH)
        {
            return i;
        }
        else if (biome == Biomes.STONE_BEACH)
        {
            return i + 2;
        }
        else if (biome == Biomes.COLD_BEACH)
        {
            return i + 4;
        }
        else
        {
            return this.rand.nextInt(51) == 0 ? 0 : this.rand.nextInt(6);
        }
    }

	/** This generates the specific name of the tropical fish variant. */
    public static String getSpecificName(int variantInt)
    { return I18n.format("description.oe.crab_color" + variantInt + ".name") + " " + I18n.format("entity.oe.crab.name"); }
    
	protected SoundEvent getAmbientSound()
	{ return ConfigHandler.entity.crab.crabTalkInterval > 0 ? OESounds.ENTITY_CRAB_AMBIENT : null; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_CRAB_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_CRAB_DEATH; }
    
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_CRAB; }
	
	protected float getWaterSlowDown()
    { return 0.98F; }
	
	public EnumCreatureAttribute getCreatureAttribute()
    { return EnumCreatureAttribute.ARTHROPOD; }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	public EntityCrab createChild(EntityAgeable ageable)
    {
		EntityCrab entityChild = new EntityCrab(this.world);
        entityChild.setVariant(this.getRandomBiomeVariant());
        
        return entityChild;
    }
	
	public boolean isBreedingItem(ItemStack stack)
    { return stack.getItem() == Items.FISH && stack.getMetadata() == 2; }
	
	public boolean isBarterItem(ItemStack stack)
    { return TRADE_ITEMS.contains(stack.getItem()); }
	
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();

        if (this.canBarter() && this.isBarterItem(itemstack) && this.getHeldItemOffhand().isEmpty() && !(this.getHeldItemMainhand().isEmpty()) && !(this.isChild()))
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemstack);
        	int itemnumber = itemstack.getCount();

            if (itemnumber - 1 == 0)
            { itemEntity.setDead(); }
            else
            { itemstack.setCount(itemnumber - 1); }
        }
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
	
	public boolean isAngry()
    { return ((Boolean)this.dataManager.get(ANGRY)).booleanValue(); }

    public void setAngry(boolean angry)
    { this.dataManager.set(ANGRY, Boolean.valueOf(angry)); }
	
    public boolean canBarter()
    { return ((Boolean)this.dataManager.get(CAN_BARTER)).booleanValue(); }

    public void setCanBarter(boolean barter)
    { this.dataManager.set(CAN_BARTER, Boolean.valueOf(barter)); }
    
    public boolean canDig()
    { return (!checkNearbyTrade() || this.getHeldItemMainhand().isEmpty()) && ((Boolean)this.dataManager.get(CAN_DIG)).booleanValue(); }

    public void setCanDig(boolean dig)
    { this.dataManager.set(CAN_DIG, Boolean.valueOf(dig)); }
    
	static class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget(EntityCrab crab)
        {
            super(crab, false);
        }

        public void startExecuting()
        {
        	EntityCrab entitycrab = (EntityCrab)this.taskOwner;
        	
        	entitycrab.setAngry(true);
            super.startExecuting();
        }
        
        public boolean shouldContinueExecuting()
        {
            if (this.taskOwner instanceof EntityCrab)
            {
            	EntityCrab entitycrab = (EntityCrab)this.taskOwner;

                if (!entitycrab.isAngry())
                {
                    return false;
                }
            }

            return super.shouldContinueExecuting();
        }
    }
	
	public int getAnimationState()
    { return this.dataManager.get(ANIM_STATE); }
	
	public void setAnimationState(int state)
    {
        if(state < 0) { state = 0; }
        else if(state > 2) { state = 2; }

        this.dataManager.set(ANIM_STATE, state);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("AnimationState", this.getAnimationState());
        compound.setBoolean("Angry", this.isAngry());
        compound.setBoolean("CanBarter", this.canBarter());
        compound.setBoolean("CanDig", this.canDig());
        compound.setBoolean("HasEgg", this.isCarryingEgg());
        compound.setInteger("Variant", this.getVariant());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAnimationState(0);
        this.setAngry(compound.getBoolean("Angry"));
        this.setCanBarter(compound.getBoolean("CanBarter"));
        this.setCanDig(compound.getBoolean("CanDig"));
        this.setCarryingEgg(compound.getBoolean("HasEgg"));
        this.setVariant(compound.getInteger("Variant"));
    }

	public int getVariant()
    { return this.dataManager.get(VARIANT); }
	
	public void setVariant(int state)
    { this.dataManager.set(VARIANT, state); }
	
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
		EntityCrab entityBaby = new EntityCrab(this.world);
    	entityBaby.setGrowingAge(-24000);
    	entityBaby.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
        world.spawnEntity(entityBaby);
        
		this.setCarryingEgg(false);
	}
}