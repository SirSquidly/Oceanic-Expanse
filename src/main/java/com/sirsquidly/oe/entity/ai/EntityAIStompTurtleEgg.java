package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.blocks.BlockTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIStompTurtleEgg extends EntityAIMoveToBlock
{
	private boolean onEggs;
    private final EntityCreature creature;
    private final double movementSpeed;
	private int timeoutCounter;
	private int jumpCounter;

    public EntityAIStompTurtleEgg(EntityCreature creatureIn, double speedIn)
    {
        super(creatureIn, speedIn, 35);
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature) || !ConfigHandler.block.turtleEgg.enableTurtleEgg)
            { return false; }
        }
        return super.shouldExecute();
    }

    public void resetTask()
    { this.jumpCounter = 0; }
    
    public void updateTask()
    {
        super.updateTask();
        this.creature.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.creature.getVerticalFaceSpeed());

        if (this.creature.getDistanceSqToCenter(this.destinationBlock) > 1.0D)
        {
            this.onEggs = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0)
            {
                this.creature.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 0.5D), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        }
        else
        {
            this.onEggs = true;
            --this.timeoutCounter;
        }
        
        if (this.getIsOnEggs())
        {
            World world = this.creature.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (block == OEBlocks.SEA_TURTLE_EGG)
            { 
            	++this.jumpCounter;
            	if (this.creature.onGround)
            	{ 
            		this.creature.motionY = 0.2D; 
            		this.creature.velocityChanged = true;
            	}
            }
            if (this.jumpCounter > 60)
            { 
            	this.jumpCounter = 0; 
            	((BlockTurtleEgg) OEBlocks.SEA_TURTLE_EGG).onBroken(world, blockpos, true);
            }
            
            this.runDelay = 10;
        }
    }

    protected boolean getIsOnEggs()
    { return this.onEggs; }
    
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == OEBlocks.SEA_TURTLE_EGG)
        { 
        	pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (iblockstate.getMaterial() == Material.AIR && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR)
            {
            	return true; 
            }
        }
        return false;
    }
}