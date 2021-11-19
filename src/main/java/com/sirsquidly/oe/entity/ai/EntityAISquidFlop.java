package com.sirsquidly.oe.entity.ai;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

public class EntityAISquidFlop extends EntityAIBase
{
	/** The delay between jumps */
    int flopDelay;
    /** Counts down then resets to flopDelay */
    int flopTimer;
	private final EntityLiving entity;
	
	public EntityAISquidFlop(EntityLiving liveIn, int pause)
    { this.entity = liveIn; this.flopDelay = pause;}

    public boolean shouldExecute()
    { return !this.entity.isInWater() && this.entity.onGround; }
  
    public void startExecuting()
    { this.flopTimer = this.flopDelay; }
    
    public boolean shouldContinueExecuting()
    { return !this.entity.isInWater(); }
    
    public void updateTask()
    {    	
        World world = this.entity.world;
        Random rand = world.rand;
        
    	--this.flopTimer;
    	
    	if (entity.getAir() <= 0)
    	{ 
    		--this.flopTimer;
    	}
    	
    	if (this.flopTimer <= 0)
        {
    		this.entity.motionY += 0.25D;
    		this.entity.motionX += (double) rand.nextFloat() * 2.0F - 1.0F;
    		this.entity.motionZ += (double) rand.nextFloat() * 2.0F - 1.0F;
    		this.entity.rotationYaw = rand.nextFloat() * 360.0F;
    		this.entity.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, 0.6F, 1.0F);
    		this.entity.velocityChanged = true;
    		
    		this.flopTimer = this.flopDelay;
        }
    }
}