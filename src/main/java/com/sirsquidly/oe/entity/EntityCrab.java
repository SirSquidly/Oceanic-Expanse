package com.sirsquidly.oe.entity;

import java.util.Set;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAICrabBarter;
import com.sirsquidly.oe.entity.ai.EntityAICrabDig;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCrab extends EntityAnimal
{
	protected Block spawnableBlock = Blocks.SAND;
	
	/** 0 = Normal, 1 = Digging, 2 = Eating */
	private static final DataParameter<Integer> ANIM_STATE = EntityDataManager.createKey(EntityCrab.class, DataSerializers.VARINT);
	private static final Set<Item>TRADE_ITEMS = Sets.newHashSet(Items.FISH);
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.FISH);
	private boolean retaliated;
	
	public EntityCrab(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.setCanPickUpLoot(true);
		this.setAnimationState(0);
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void entityInit()
    { super.entityInit(); this.dataManager.register(ANIM_STATE, 0); }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
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
        	if (this.world.isRemote)
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
    { return new EntityCrab(this.world); }
	
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
        	this.retaliated = true;
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
	
	private void setRetaliated(boolean retaliated)
    { this.retaliated = retaliated; }
	
	static class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget(EntityCrab crab)
        {
            super(crab, false);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            if (this.taskOwner instanceof EntityCrab)
            {
            	EntityCrab entitycrab = (EntityCrab)this.taskOwner;

                if (entitycrab.retaliated)
                {
                	entitycrab.setRetaliated(false);
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
    }
}