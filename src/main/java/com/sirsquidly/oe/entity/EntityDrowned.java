package com.sirsquidly.oe.entity;

import com.sirsquidly.oe.util.handlers.LootTableHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDrowned extends EntityZombie 
{
	private static final DataParameter<Boolean> IS_SWIMMING = EntityDataManager.<Boolean>createKey(EntityDrowned.class, DataSerializers.BOOLEAN);
	   
	public EntityDrowned(World worldIn) {
		super(worldIn);
		this.setPathPriority(PathNodeType.WALKABLE, 1.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(IS_SWIMMING, Boolean.valueOf(false));
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityDrowned.DrownedAISwimToTarget(this));
		this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.applyEntityAI();
    }
	
	protected PathNavigate createNavigator(World worldIn)
    { return new EntityDrowned.PathNavigateDrowned(this, worldIn); }
	
	protected SoundEvent getAmbientSound()
    { return SoundHandler.ENTITY_DROWNED_AMBIENT; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return SoundHandler.ENTITY_DROWNED_HURT; }

    protected SoundEvent getDeathSound()
    { return SoundHandler.ENTITY_DROWNED_DEATH; }
    
    @Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_DROWNED; }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	protected float getWaterSlowDown()
    { return 0.98F; }
	
	public boolean isSwimming()
    { return ((Boolean)this.dataManager.get(IS_SWIMMING)).booleanValue(); }

    public void setSwimming(boolean swimming)
    { this.dataManager.set(IS_SWIMMING, Boolean.valueOf(swimming)); }
    
    /**protected void updateAITasks()
    {
        if (this.world.isDaytime())
        {
            float f = this.getBrightness();

            if (f > 0.5F)
            	if (!(this.getAttackTarget().isWet()))
            		{ this.setAttackTarget((EntityLivingBase)null); }
        }

        super.updateAITasks();
    }**/
    
    //Lots of AI Below
    
    public class DrownedAISwimToTarget extends EntityAISwimming
    {
    	EntityDrowned drowned;
    	EntityLivingBase drownedTarget;
    	
    	public DrownedAISwimToTarget(EntityDrowned entityIn) 
    	{
    		super(entityIn);
    		this.drowned = entityIn;
    	}
    	
    	public boolean shouldExecute()
        {
    		this.drownedTarget = this.drowned.getAttackTarget();
    		BlockPos blockpos = new BlockPos(this.drowned.posX, this.drowned.posY, this.drowned.posZ);

            if (this.drownedTarget != null && this.drownedTarget.posY > this.drowned.posY && this.drowned.inWater)
            { 
            	if (!(this.drownedTarget.isInWater()) && !(this.drowned.world.isDaytime()))
    			{ return true;  }
            	
            	else if (this.drowned.world.getBlockState(blockpos.up()).getMaterial() == Material.WATER || this.drowned.world.getBlockState(blockpos.down()).getMaterial() != Material.WATER && !(this.drowned.world.isDaytime()))
            	{ return true;  }
            }
            return false;
        }
    	
    	@Override
    	public void updateTask()
        {
    		super.updateTask();
    		this.drownedTarget = this.drowned.getAttackTarget();
    				
    		if (this.drownedTarget != null && this.drownedTarget.posY > this.drowned.posY && this.drowned.isInWater()) 
    		{ this.drowned.motionY += 0.1D; drowned.velocityChanged = true;}
        }
    }
    	
    	
    public class PathNavigateDrowned extends PathNavigateGround
    {

    	public PathNavigateDrowned(EntityDrowned drowned, World worldIn) 
    	{
    		super(drowned, worldIn);
    	}
    	
    	@Override
    	protected void pathFollow()
        {
    		if (!(this.isInLiquid())) { super.pathFollow(); }
    		
    		Vec3d vec3d = this.getEntityPosition();
    		float f = this.entity.width * this.entity.width;

    		if (this.isInLiquid())
    		{
    			if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < (double)f)
    	        {
    	            this.currentPath.incrementPathIndex();
    	        }

    	        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j)
    	        {
    	            Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, j);

    	            if (vec3d1.squareDistanceTo(vec3d) <= 36.0D && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0))
    	            {
    	                this.currentPath.setCurrentPathIndex(j);
    	                break;
    	            }
    	        }
    		}
        }
    	
    	protected boolean isLiquidPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
    	{
    		if (this.isInLiquid())
    		{
    		RayTraceResult raytraceresult = this.world.rayTraceBlocks(posVec31, new Vec3d(posVec32.x, posVec32.y + (double)this.entity.height * 0.5D, posVec32.z), false, true, false);
    		return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
    		}
    		return false;
    	}
    }
}
