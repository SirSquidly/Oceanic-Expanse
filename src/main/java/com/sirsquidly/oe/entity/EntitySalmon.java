package com.sirsquidly.oe.entity;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySalmon extends AbstractFish
{
	private static final DataParameter<Integer> SALMON_SIZE = EntityDataManager.<Integer>createKey(EntitySalmon.class, DataSerializers.VARINT);
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
	
	public EntitySalmon(World worldIn) {
		super(worldIn);
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIWanderUnderwater(this, 1.0D, 80));
		this.tasks.addTask(2, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));
    }
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SALMON_SIZE, Integer.valueOf(1));
    }
	
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_SALMON; }
	
	public void onEntityUpdate()
    {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater())
        {
            --i;
            this.setAir(i);

            if (this.getAir() == -20)
            {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 1.0F);
            }
        }
        else
        { this.setAir(150); }
    }
	
	public float getEyeHeight()
    { return this.height * 0.6F; }
	
	public EntitySalmon createChild(EntityAgeable ageable)
    { return new EntitySalmon(this.world); }
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }

	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
		if (ConfigHandler.entity.salmon.salmonSizeVarience)
		{
			int i = this.rand.nextInt(3);
			int j = this.rand.nextInt(99);

	        if (j <= 50)
	        { i = 2;}
	        if (j > 50 && j <= 65)
	        { i = 3;}
	        if (j > 65 && j <= 99)
	        { i = 1;}
	        
	        this.setSalmonSize(i, false);
		}
		else
		{ this.setSalmonSize(2, false); }
		
        return super.onInitialSpawn(difficulty, livingdata);
    }
	
	protected void setSalmonSize(int size, boolean resetHealth)
    {
        this.dataManager.set(SALMON_SIZE, Integer.valueOf(size));
        this.setSize(0.55F + (size*0.25F), 0.25F + (size*0.05F));
        this.setPosition(this.posX, this.posY, this.posZ);
        //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
        if (resetHealth)
        { this.setHealth(this.getMaxHealth()); }
    }
	
    public int getSalmonSize()
    {
        return ((Integer)this.dataManager.get(SALMON_SIZE)).intValue();
    }
    
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SALMON_SIZE.equals(key))
        {
            int i = this.getSalmonSize();
            this.setSize(0.55F + (i*0.25F), 0.25F + (i*0.05F));
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }
	
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Size", this.getSalmonSize());
    }
	
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        int i = compound.getInteger("Size");

        if (i < 0) { i = 0; }

        this.setSalmonSize(i, false);
    }
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (ConfigHandler.entity.salmon.salmonFeedingGrowth && BREEDING_ITEMS.contains(itemstack.getItem()) &&  !(this.isChild()))
        {
        	this.setSalmonSize(getSalmonSize() + 1, false);

        	if (!player.capabilities.isCreativeMode) { itemstack.shrink(1); }
        }
        return super.processInteract(player, hand);
    }
}