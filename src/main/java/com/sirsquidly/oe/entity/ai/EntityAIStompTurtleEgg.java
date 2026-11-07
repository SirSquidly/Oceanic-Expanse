package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.blocks.BlockTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

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
	private int jumpingTicksCounter;

    public EntityAIStompTurtleEgg(EntityCreature creatureIn, double speedIn)
    {
        super(creatureIn, speedIn, 0);
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay > 0) { --this.runDelay; return false; }
        this.runDelay = 200 + creature.getRNG().nextInt(200);
        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature) || !ConfigHandler.block.turtleEgg.enableTurtleEgg)
        { return false; }
        return findNearestEgg();
    }

    /** The original block finder kinda sucks. */
    private boolean findNearestEgg()
    {
        BlockPos origin = new BlockPos(creature);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dist = 0; dist <= 24; dist++)
            for (int x = -dist; x <= dist; x++)
                for (int z = -dist; z <= dist; z++)
                {
                    /* ONLY check each LAYER! */
                    if (Math.max(Math.abs(x), Math.abs(z)) != dist) continue;

                    for (int y = -3; y <= 3; y++)
                    {
                        pos.setPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);

                        if (creature.isWithinHomeDistanceFromPosition(pos) && creature.world.isBlockLoaded(pos) && shouldMoveTo(creature.world, pos))
                        {
                            destinationBlock = pos.toImmutable();
                            return true;
                        }
                    }
                }
        return false;
    }

    public void resetTask()
    {
        this.jumpingTicksCounter = 0;
        this.timeoutCounter = 0;
        this.onEggs = false;
    }

    public void updateTask()
    {
        super.updateTask();
        BlockPos pos = destinationBlock;
        World world = this.creature.world;
        this.creature.getLookHelper().setLookPosition(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, 10.0F, (float)this.creature.getVerticalFaceSpeed());

        if (this.creature.getDistanceSqToCenter(pos) > 1.1D)
        {
            this.onEggs = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 == 0)
            {
                this.creature.getNavigator().tryMoveToXYZ(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, this.movementSpeed);
            }
        }
        else
        {
            this.onEggs = true;
            --this.timeoutCounter;
        }

        if (!this.getIsOnEggs()) return;

        if (world.getBlockState(pos).getBlock() == OEBlocks.SEA_TURTLE_EGG)
        {
            if (this.creature.onGround)
            {
                this.creature.motionY = 0.3D;
                this.creature.velocityChanged = true;
            }

            if (++jumpingTicksCounter > 60)
            {
                jumpingTicksCounter = 0;
                ((BlockTurtleEgg)OEBlocks.SEA_TURTLE_EGG).onBroken(world, pos, true);
            }
        }

        this.runDelay = 10;
    }

    protected boolean getIsOnEggs()
    { return this.onEggs; }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock() == OEBlocks.SEA_TURTLE_EGG && worldIn.isAirBlock(pos.up())
                && worldIn.isAirBlock(pos.up(2));
    }
}