package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.entity.EntityTurtle;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderUnderwater extends EntityAIWander
{
	public EntityAIWanderUnderwater(EntityCreature creatureIn, double speedIn, int chance) 
	{
		super(creatureIn, 1.0, chance);
	}
	
	@Override
	public boolean shouldExecute()
    {
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