package com.sirsquidly.oe.entity;

import java.util.Set;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCod extends AbstractFish
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
	
	public EntityCod(World worldIn) {
		super(worldIn);
        this.setSize(0.6F, 0.3F);
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
	
	@Override
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.ENTITIES_COD;
    }
	
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
        {
            this.setAir(150);
        }
    }
	
	public float getEyeHeight()
    {
        return this.height * 0.6F;
    }
	
	public EntityCod createChild(EntityAgeable ageable)
    {
        return new EntityCod(this.world);
    }
	
	public boolean isBreedingItem(ItemStack stack)
    {
        return BREEDING_ITEMS.contains(stack.getItem());
    }
	
	// Bucketing the Cod
		@Override
		public boolean processInteract(EntityPlayer player, EnumHand hand)
		    {
		        ItemStack itemstack = player.getHeldItem(hand);

		        if (itemstack.getItem() == Items.BUCKET && !player.capabilities.isCreativeMode && !this.isChild())
		        {
		            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
		            itemstack.shrink(1);
		            this.setDead();

		            if (itemstack.isEmpty())
		            {
		                player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
		            }
		            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET)))
		            {
		                player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
		            }

		            return true;
		        }
		        else
		        {
		            return super.processInteract(player, hand);
		        }
		    }
}
