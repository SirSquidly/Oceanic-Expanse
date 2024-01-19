package com.sirsquidly.oe.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityTropicalSlime extends EntitySlime
{
	public EntityTropicalSlime(World worldIn)
	{
		super(worldIn);
		
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	protected void initEntityAI()
    { 
        super.initEntityAI();
        
        this.targetTasks.removeTask(new EntityAIFindEntityNearestPlayer(this));
        
        //this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }
	
	/**
	 * Forcefully updates the Health of the Tropical Slime afer size changes
	 */
	public void updateHealth(boolean resetHealth)
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(this.getSlimeSize() * this.getSlimeSize() * 2));
		if (resetHealth)
        { this.setHealth(this.getMaxHealth()); }
	}
	
	@Override
	protected EnumParticleTypes getParticleType()
    { return this.inWater ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.WATER_SPLASH; }
	
	@Nullable
    protected ResourceLocation getLootTable()
    { return this.getSlimeSize() <= 2 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY; }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        if (this.rand.nextFloat() < 0.05F)
        { this.setLeftHanded(true); }
        else
        { this.setLeftHanded(false); }

        this.setSlimeSize(2, true);
        this.updateHealth(true);
        return livingdata;
    }
	
	public void setDead()
    {
        int i = this.getSlimeSize();

        if (!this.world.isRemote && i > 2 && this.getHealth() <= 0.0F)
        {
            int j = 2 + this.rand.nextInt(3);

            for (int k = 0; k < j; ++k)
            {
                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
                EntityTropicalSlime entityTropicalslime = new EntityTropicalSlime(this.world);

                if (this.hasCustomName())
                { entityTropicalslime.setCustomNameTag(this.getCustomNameTag()); }

                if (this.isNoDespawnRequired())
                { entityTropicalslime.enablePersistence(); }

                entityTropicalslime.setSlimeSize(i / 2, true);
                this.updateHealth(true);
                entityTropicalslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.spawnEntity(entityTropicalslime);
            }
        }

        this.isDead = true;
    }
	
	/**
	 * We Override this so the hopping is unaffected by Water, and it does not try to float
	 */
	@Override
	public boolean isInWater()
    {
        return false;
    }
}