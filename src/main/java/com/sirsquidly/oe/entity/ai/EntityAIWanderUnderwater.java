package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.entity.EntityTurtle;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderUnderwater extends EntityAIWander
{
	/** Forces the mob to wander if touching the ocean floor. */
	boolean forceOffFloor;
	
	public EntityAIWanderUnderwater(EntityCreature creatureIn, double speedIn, int chance, boolean forceOffFloorIn) 
	{
		super(creatureIn, 1.0, chance);
		this.forceOffFloor = forceOffFloorIn;
	}
	
	@Override
	public boolean shouldExecute()
    {
		BlockPos entityPos = new BlockPos(this.entity.posX, this.entity.posY, this.entity.posZ);
		
		if (this.forceOffFloor && this.entity.isInWater() && this.entity.world.getBlockState(entityPos.down()).getMaterial() != Material.WATER && this.entity.world.getBlockState(entityPos.up()).getMaterial() != Material.AIR)
        { this.makeUpdate(); }
		
		if (!this.entity.isInWater())
        { return false; }

        if (this.entity instanceof EntityTurtle && ((EntityTurtle) this.entity).isGoingHome())
        { return false; }
        
        return super.shouldExecute();
    }
	
	@Override
	protected Vec3d getPosition()
    {
		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

		/** Forces it to retry if the Target is not in water **/
		
		for (int i = 0; i <= 10 && !(this.entity.isInWater()); i++) {
			vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
		}

        return vec3d;
    }
}