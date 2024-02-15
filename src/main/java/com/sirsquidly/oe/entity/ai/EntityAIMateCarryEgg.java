package com.sirsquidly.oe.entity.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import com.sirsquidly.oe.entity.EntityTurtle;
import com.sirsquidly.oe.entity.IEggCarrierMob;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

/**
 * AI for mating mobs that carry an egg, rather than instantly making a baby.
 * 
 * A few parts had to be copied from EntityAIMate, as they are private
 */
public class EntityAIMateCarryEgg extends EntityAIMate
{
	private final EntityAnimal animal;
	private final IEggCarrierMob carrier;
	private final Class <? extends EntityAnimal > mateClass;
	World world;
	private EntityAnimal targetMate;
	int spawnBabyDelay;
	double moveSpeed;
	/** The cooldown before the mob(s) can breed again */
	int breedCooldown;
	/** If only the one carrying an egg should have a breeding cooldown */
	boolean onlyOneCooldown;
    
	public EntityAIMateCarryEgg(EntityAnimal animalIn, double speedIn)
    {
        this(animalIn, speedIn, 6000);
    }
	
	public EntityAIMateCarryEgg(EntityAnimal animalIn, double speedIn, int breedCooldownIn)
    {
        this(animalIn, speedIn, breedCooldownIn, false);
    }
	
	public EntityAIMateCarryEgg(EntityAnimal animalIn, double speedIn, int breedCooldownIn, boolean onlyOneCooldownIn)
    {
        super(animalIn, speedIn);
        
        if (!(animalIn instanceof IEggCarrierMob))
        {
            throw new IllegalArgumentException("EntityAIMateCarryEgg requires Mob implements IEggCarrierMob");
        }
        else
        {
        	this.animal = animalIn;
        	this.carrier = (IEggCarrierMob)animalIn;
            this.world = animal.world;
            this.mateClass = animalIn.getClass();
            this.moveSpeed = speedIn;
            this.breedCooldown = breedCooldownIn;
            this.onlyOneCooldown = onlyOneCooldownIn;
            this.setMutexBits(3);
        }
    }
	
	public boolean shouldExecute()
    {
        if (this.carrier.isCarryingEgg() && !this.doLiveBirth() || !this.animal.isInLove())
        { return false; }

        this.targetMate = this.getNearbyMate();
        
        return this.targetMate != null;
    }

	public boolean shouldContinueExecuting()
	{ return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60; }
	
	private EntityAnimal getNearbyMate()
    {
        List<EntityAnimal> list = this.world.<EntityAnimal>getEntitiesWithinAABB(this.mateClass, this.animal.getEntityBoundingBox().grow(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;

        for (EntityAnimal entityanimal1 : list)
        {
            if (this.animal.canMateWith(entityanimal1) && this.animal.getDistanceSq(entityanimal1) < d0)
            {
                entityanimal = entityanimal1;
                d0 = this.animal.getDistanceSq(entityanimal1);
            }
        }

        return entityanimal;
    }
	
	public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }
	
	public void updateTask()
    {
		this.animal.getLookHelper().setLookPositionWithEntity(this.targetMate, (float)this.animal.getHorizontalFaceSpeed(), (float)this.animal.getVerticalFaceSpeed());
		this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0D)
        { doBabyStuffs(); }
    }

	public void doBabyStuffs()
	{
    	this.animal.setGrowingAge(this.breedCooldown);
    	if (!this.onlyOneCooldown) this.targetMate.setGrowingAge(this.breedCooldown);
        this.animal.resetInLove();
        this.targetMate.resetInLove();
        
        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(animal, targetMate, null);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        if (cancelled) { return; }
        
        EntityPlayerMP playerMP = this.animal.getLoveCause();

        if (playerMP == null && this.targetMate.getLoveCause() != null)
        { playerMP = this.targetMate.getLoveCause(); }

        if (playerMP != null)
        {
        	playerMP.addStat(StatList.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(playerMP, this.animal, this.targetMate, null);
        }
        
        if (this.doLiveBirth())
		{
        	EntityAgeable entityBaby = animal.createChild(this.targetMate);
        	entityBaby.setGrowingAge(-24000);
        	entityBaby.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            world.spawnEntity(entityBaby);
		}
        else
        { 
        	this.carrier.setCarryingEgg(true);
    		
    		if (this.animal instanceof EntityTurtle) ((EntityTurtle) this.animal).setGoingHome(true);
        }
        
        Random random = this.animal.getRNG();

        if (this.world.getGameRules().getBoolean("doMobLoot"))
        { this.world.spawnEntity(new EntityXPOrb(this.world, this.animal.posX, this.animal.posY, this.animal.posZ, random.nextInt(7) + 1)); }
    }
	
	/**
	 * If this entity should, instead of carrying an egg, just give birth like any normal mob.
	 * 
	 * Also what a horrible method name.
	 */
	public boolean doLiveBirth()
	{
		if (this.animal instanceof EntityTurtle)
		{
			return !ConfigHandler.block.turtleEgg.enableTurtleEgg;
		}
		return false;
	}
}