package com.sirsquidly.oe.entity;

import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityLobster extends EntityAnimal
{
	protected Block spawnableBlock = Blocks.SAND;
	
	/** 0 = Normal, 1 = Digging, 2 = Eating */
	private static final DataParameter<Integer> ANIM_STATE = EntityDataManager.createKey(EntityLobster.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ANGRY = EntityDataManager.<Boolean>createKey(EntityLobster.class, DataSerializers.BOOLEAN);
	private static final Set<Item>TRADE_ITEMS = Sets.newHashSet(Items.FISH);
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.FISH);
	/** Handles all the colors */
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityLobster.class, DataSerializers.VARINT);
	private int randomAngrySoundDelay;
	
	public EntityLobster(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.3F);
		this.setCanPickUpLoot(true);
		this.setAnimationState(0);
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void entityInit()
    { 
		super.entityInit(); 
		this.dataManager.register(ANIM_STATE, 0); 
		this.dataManager.register(ANGRY, Boolean.valueOf(false));
		this.dataManager.register(VARIANT, 0);
	}
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }
	
	public int getTalkInterval()
    { return 0; }
	
	public void onLivingUpdate()
    {
		ItemStack offered = this.getHeldItemOffhand();
		BlockPos blockpos = new BlockPos(this.posX, this.posY-1, this.posZ);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		
		if (this.isInWater())
			{ stepHeight = 1.0F; }
		else { stepHeight = 0.6F; }
		
		if (this.getAnimationState() == 1)
        {
        	if (this.world.isRemote && iblockstate.getBlock() != Blocks.AIR)
            {
        		for (int i = 0; i < 2; ++i)
                { this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Block.getStateId(iblockstate)); }	
            }
        	if (this.rand.nextInt(2) == 0) 
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
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (this.isBarterItem(itemstack) && this.getHeldItemOffhand().isEmpty() && !(this.isChild()))
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
	
	protected SoundEvent getAmbientSound()
	{ return OESounds.ENTITY_CRAB_AMBIENT; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_CRAB_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_CRAB_DEATH; }
    
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_LOBSTER; }
	
	protected float getWaterSlowDown()
    { return 0.98F; }
	
	public EnumCreatureAttribute getCreatureAttribute()
    { return EnumCreatureAttribute.ARTHROPOD; }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	public EntityLobster createChild(EntityAgeable ageable)
    { return new EntityLobster(this.world); }
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }
	
	public boolean isBarterItem(ItemStack stack)
    { return TRADE_ITEMS.contains(stack.getItem()); }
	
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();

        if (this.isBarterItem(itemstack) && this.getHeldItemOffhand().isEmpty() && !(this.getHeldItemMainhand().isEmpty()) && !(this.isChild()))
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemstack);
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
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.getRandomLobsterVariant();

        this.setLobsterVariant(i);

        return livingdata;
    }
	
	/** Creates a random number for a Lobster varient, within acceptable range. */
	private int getRandomLobsterVariant()
    {
		if (this.rand.nextFloat() <= (float)ConfigHandler.entity.lobster.lobsterBlueChance)
		{ return 1; }
		
		if (this.rand.nextFloat() <= (float)ConfigHandler.entity.lobster.lobsterBlueChance)
		{ return 2; }
		
		return 0;
    }
    
    /** This generates the specific name of the tropical fish variant. */
    public static String getSpecificName(int variantInt)
    {
    	String fullName;
    	int patternNum = variantInt >> 8 & 255;
        String patternName = I18n.format("description.oe.tropical_fish_a_pattern" + patternNum + ".name");

        /** This overrides the name generator if the name override config includes the variant. */
    	for(String line : ConfigHandler.entity.tropicalFish.tropicalFishNameOverrides)
    	{
    		String[] split = line.split("=");
    		int what = 0;
			
    		try
    		{ what = Integer.parseInt(split[0]); }
    		catch(NumberFormatException e)
    		{ what = 0; }
    		
			if (what == variantInt)
			{
				return split[1];
			}
    	}
        
    	if ((variantInt & 255) != 0)
    	{
    		patternName = I18n.format("description.oe.tropical_fish_b_pattern" + patternNum + ".name");
    	}
    	
    	String color1 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 16 & 255).getDyeColorName());
    	String color2 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 24 & 255).getDyeColorName());
    	
    	if (ConfigHandler.entity.tropicalFish.tropicalFishBedrockColors)
    	{
    		int[] bedrockSpecials = new int[]{3, 6, 8, 9, 10}; 
    		
    		if (ArrayUtils.contains(bedrockSpecials, EnumDyeColor.byMetadata(variantInt >> 16 & 255).getMetadata()))
        	{ color1 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 16 & 255).getMetadata()); }
        	if (ArrayUtils.contains(bedrockSpecials, EnumDyeColor.byMetadata(variantInt >> 24 & 255).getMetadata()))
        	{ color2 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 24 & 255).getMetadata()); }
    	}
    	
    	if (!color1.equals(color2))
    	{
    		fullName = color1 + "-" + color2 + " " + patternName;
    	}
    	else
    	{
    		fullName = color1 + " " + patternName;
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
	
	public boolean isAngry()
    { return ((Boolean)this.dataManager.get(ANGRY)).booleanValue(); }

    public void setAngry(boolean angry)
    { this.dataManager.set(ANGRY, Boolean.valueOf(angry)); }
	
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
	
	public int getAnimationState()
    { return this.dataManager.get(ANIM_STATE); }
	
	public void setAnimationState(int state)
    {
        if(state < 0) { state = 0; }
        else if(state > 2) { state = 2; }

        this.dataManager.set(ANIM_STATE, state);
    }
	
	public int getLobsterVariant()
    { return this.dataManager.get(VARIANT); }
	
	public void setLobsterVariant(int state)
    { this.dataManager.set(VARIANT, state); }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("AnimationState", this.getAnimationState());
        compound.setBoolean("Angry", this.isAngry());
        compound.setInteger("Variant", this.getLobsterVariant());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAngry(compound.getBoolean("Angry"));
        this.setAnimationState(compound.getInteger("AnimationState"));
        this.setLobsterVariant(compound.getInteger("Variant"));
    }
}