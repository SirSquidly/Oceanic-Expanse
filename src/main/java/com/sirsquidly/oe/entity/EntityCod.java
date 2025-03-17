package com.sirsquidly.oe.entity;

import java.util.Set;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCod extends AbstractFish
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
	
	public EntityCod(World worldIn) {
		super(worldIn);
        this.setSize(0.6F, 0.3F);
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
    }
	
	protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 8.0F, 1.6D, 1.4D));
		this.tasks.addTask(2, new EntityAIWanderUnderwater(this, 1.0D, 20, true));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));
    }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_COD_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_COD_DEATH; }
   
    public SoundEvent getFlopSound()
    { return OESounds.ENTITY_COD_FLOP; }
    
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
        EntityCod fish = new EntityCod(this.world);
        if (ageable.isNoDespawnRequired()) fish.enablePersistence();
        return fish;
    }
	
	public boolean isBreedingItem(ItemStack stack)
    {
        return BREEDING_ITEMS.contains(stack.getItem());
    }
}
