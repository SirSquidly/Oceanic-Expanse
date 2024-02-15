package com.sirsquidly.oe.entity.ai;

import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.sirsquidly.oe.entity.EntityTurtle;
import com.sirsquidly.oe.entity.IEggCarrierMob;

/**
 * AI for mobs that need to go and drop off an egg.
 */
public class EntityAIMateDepositEgg extends EntityAIMoveToBlock
{
	private boolean onSand;
    private final EntityAnimal animal;
    private final IEggCarrierMob carrier;
    private final double movementSpeed;
	private int timeoutCounter;
	private int layCountdown;

    public EntityAIMateDepositEgg(EntityAnimal turtleIn, double speedIn)
    {
        super(turtleIn, speedIn, 35);
        
        if (!(turtleIn instanceof IEggCarrierMob))
        {
            throw new IllegalArgumentException("EntityAIMateDepositEgg requires Mob implements IEggCarrierMob");
        }
        else
        {
        	this.animal = turtleIn;
            this.carrier = (IEggCarrierMob)turtleIn;
            this.movementSpeed = speedIn;
            this.setMutexBits(3);
        }
    }

    public boolean shouldExecute()
    {
    	if (this.runDelay <= 0)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.animal.world, this.animal))
            { return false; }
            
            if (!this.carrier.isCarryingEgg())
            { return false; }
            
            if (this.animal instanceof EntityTurtle &&  ((EntityTurtle)this.animal).isGoingHome()) return false;
        }
        
        return super.shouldExecute();
    }

    public void startExecuting()
    {
    	this.layCountdown = 100;
    	super.startExecuting();
    }
    
    public void resetTask()
    {
    	if (this.animal instanceof EntityTurtle) ((EntityTurtle)this.animal).setDigging(false);
    	this.layCountdown = 0; 
    	super.resetTask();
    }
    
    public boolean shouldContinueExecuting()
    {
    	if (!this.carrier.isCarryingEgg())
    	{ return false; }
        return super.shouldContinueExecuting();
    }
    
    public void updateTask()
    {
        super.updateTask();
        this.animal.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.animal.getVerticalFaceSpeed());
        BlockPos blockpos = new BlockPos(this.animal.posX, this.animal.posY-1, this.animal.posZ);
        
        if (this.animal.getDistanceSqToCenter(this.destinationBlock) > 1.0D)
        {
            this.onSand = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0)
            {
                this.animal.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 0.5D), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        }
        else
        {
            this.onSand = true;
            --this.timeoutCounter;
        }
        
        if (this.onSand)
        {
        	if (this.layCountdown != 1)
        	{ --this.layCountdown; }
        	
        	if (this.layCountdown == 1)
            { 
        		 if (this.animal instanceof EntityTurtle) ((EntityTurtle)this.animal).setDigging(false);
        		this.carrier.placeEgg(animal.world, blockpos);
            }
        	else
        	{ 
        		if (this.animal instanceof EntityTurtle) ((EntityTurtle)this.animal).setDigging(true);
        	}
        	
        	this.runDelay = 10;
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    { return this.carrier.canLayEgg(worldIn, pos); }
}