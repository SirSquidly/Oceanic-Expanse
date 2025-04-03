package com.sirsquidly.oe.entity;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityPickled extends EntityDrowned
{
	private static final DataParameter<Boolean> IS_DRY = EntityDataManager.<Boolean>createKey(EntityPickled.class, DataSerializers.BOOLEAN);

	public EntityPickled(World worldIn) {
		super(worldIn);
		this.setPathPriority(PathNodeType.WALKABLE, 0.0F);
		this.setPathPriority(PathNodeType.WATER, 10.0F);
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{ return LootTableHandler.ENTITIES_PICKLED; }

	protected void entityInit()
	{
		super.entityInit();
        this.dataManager.register(IS_DRY, Boolean.valueOf(false));
	}
	
	public boolean isDry()
	{ return ((Boolean)this.dataManager.get(IS_DRY)).booleanValue(); }

	public void setDry(boolean dry)
	{ this.dataManager.set(IS_DRY, Boolean.valueOf(dry)); }
    
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.DYE && itemstack.getItemDamage() == 15 && !this.isChild())
        {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            player.swingArm(hand);
            itemstack.shrink(1);

        	ItemStack dropPickles = new ItemStack(OEBlocks.SEA_PICKLE, 1);
        	this.heal(1);
        	this.setAttackTarget(null);
        	
        	for (int j = rand.nextInt(4) + 1; j > 0; j--)
        	{
        		double p1 = this.rand.nextDouble() * (double)this.width * 2.0 - (double)this.width;
            	double p2 = this.rand.nextDouble() * (double)this.height;
            	double p3 = this.rand.nextDouble() * (double)this.width * 2.0 - (double)this.width;
            	double p4 = this.rand.nextFloat() * 0.02;
            	double p5 = this.rand.nextFloat() * 0.02;
            	double p6 = this.rand.nextFloat() * 0.02;
            	this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + p1, this.posY + p2, this.posZ + p3, p4, p5, p6);
            	
            	if (this.world.rand.nextFloat() <= 0.069F)
            	{
            		dropPickles = new ItemStack(OEBlocks.PICKLED_HEAD, 1);
            		j = 0;
            	}
            	
            	if (!world.isRemote)
            	{
            		if (this.hasCustomName())
                    {
                    	dropPickles.setStackDisplayName(this.getCustomNameTag() + " Jr");
                    }
                    this.entityDropItem(dropPickles, 1.0F);
            	}
        	}
            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }
    
	protected ItemStack getSkullDrop()
	{ return new ItemStack(OEBlocks.PICKLED_HEAD, 1); }
    
    @Override
    public void onLivingUpdate()
    {
        if (this.isEntityAlive())
        {
        	for (int i = 0; i < 2; ++i)
        	{
        		if (!(this.isWet()) && !(this.isDry()))
        		{
        		this.setDry(true);
        		}
        		else if (this.isWet() && this.isDry())
        		{
        			this.setDry(false);
        		}
        	}
        }
        
        super.onLivingUpdate();
    }
}