package com.sirsquidly.oe.entity;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBabyGlowSquid extends EntityGlowSquid
{
	public int minBrightness = 983055 * ConfigHandler.entity.glowSquid.glowSquidBodyBright;
	
	public EntityBabyGlowSquid(World worldIn) {
		super(worldIn);
        this.setSize(0.4F, 0.4F);

        this.particleChancePerTick = 4;
        this.particlePerTick = 1;
	}
	
	public static void registerFixesSquid(DataFixer fixer)
    { EntityLiving.registerFixesMob(fixer, EntityBabyGlowSquid.class); }
	
	@Override
	public boolean isChild()
    { return true; }
        
	static class AISpeedAway extends EntityAIBase
    {
			protected double speedMult;
			protected int fleeTics;
			protected int fleeTimer;
			
			protected final EntityBabyGlowSquid glowSquid;

		    public AISpeedAway(EntityBabyGlowSquid entityGlowSquid, double speedIn, int fleeTime)
		    {
		        this.glowSquid = entityGlowSquid;
		        this.speedMult = speedIn;
		        this.fleeTics = fleeTime;
		        this.fleeTimer = this.fleeTics;
		        this.setMutexBits(1);
		    }

		    public boolean shouldExecute()
		    {
		        if (this.glowSquid.getRevengeTarget() == null) { return false; }
		        return true;
		    }
		    
		    public void resetTask()
		    { this.fleeTimer = this.fleeTics; }
		    
		    public void updateTask()
		    {
		    	float speedNORM = (float) this.glowSquid.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
		    	
		    	if (this.fleeTimer > 1)
		    	{
		    		glowSquid.setAIMoveSpeed((float)(speedNORM * speedMult));	
		    		--this.fleeTimer;
		    	}
		    	if (this.fleeTimer == 1)
		    	{ glowSquid.setAIMoveSpeed((float)(speedNORM)); }
		    }
    }
	
	static class AIMoveRandom extends EntityAIBase
    {
        private final EntityBabyGlowSquid babySquid;

        public AIMoveRandom(EntityBabyGlowSquid entity)
        { this.babySquid = entity; }

        public boolean shouldExecute()
        { return true; }

        public void updateTask()
        {
        	int i = this.babySquid.getIdleTime();
        	
            if (i > 100)
            { this.babySquid.setMovementVector(0.0F, 0.0F, 0.0F); }
            else if (this.babySquid.getRNG().nextInt(50) == 0 || !this.babySquid.inWater || !this.babySquid.hasMovementVector())
            {
                float f = this.babySquid.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.babySquid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.babySquid.setMovementVector(f1, f2, f3);
            }
        }
    }
}