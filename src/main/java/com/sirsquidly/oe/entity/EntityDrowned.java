package com.sirsquidly.oe.entity;

import javax.annotation.Nullable;

import com.sirsquidly.oe.entity.ai.EntityAITridentThrowing;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDrowned extends EntityZombie implements IRangedAttackMob 
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
		this.tasks.addTask(2, new EntityAITridentThrowing<EntityDrowned>(this, 1.0D, 20, 20.0F));
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
    
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        if (entitylivingbaseIn != null && !entitylivingbaseIn.isDead && !entitylivingbaseIn.isWet() && this.world.isDaytime())
        {}
        else
        {
        	super.setAttackTarget(entitylivingbaseIn);
        }
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
    	this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));

        if (this.rand.nextFloat() < 0.05F)
        { this.setLeftHanded(true); }
        else
        { this.setLeftHanded(false); }

        float f = difficulty.getClampedAdditionalDifficulty();

        if (livingdata == null)
        { livingdata = new EntityDrowned.GroupData(this.world.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance); }

        if (livingdata instanceof EntityDrowned.GroupData)
        {
        	EntityDrowned.GroupData entityzombie$groupdata = (EntityDrowned.GroupData)livingdata;

            if (entityzombie$groupdata.isChild)
            {
                this.setChild(true);
            }
        }

        this.setNaturalEquipment(difficulty);
        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        
        return livingdata;
    }
    
    protected void setNaturalEquipment(DifficultyInstance difficulty)
    {
    	super.setEquipmentBasedOnDifficulty(difficulty);
    	
        if (this.rand.nextFloat() <= 0.0625F)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(OEItems.TRIDENT_ORIG));
        }
        else if (this.rand.nextFloat() <=  0.02F)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
        }
        else
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        
        if (this.rand.nextFloat() <= 0.03F)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(OEItems.NAUTILUS_SHELL));
        }
    }
    
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityTrident entitytrident = new EntityTrident(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytrident.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        entitytrident.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, 0.8F * 3.0F, 1.0F);
        entitytrident.setItem(this.getHeldItemMainhand());
        this.playSound(SoundHandler.ENTITY_DROWNED_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entitytrident);
    }
    
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

            if (this.drownedTarget != null && !this.drownedTarget.isDead && this.drownedTarget.posY > this.drowned.posY && this.drowned.inWater)
            { 
            	if ((!this.drowned.world.isDaytime() || this.drowned.world.isRaining()) && (this.drowned.world.getBlockState(blockpos.up()).getMaterial() == Material.WATER || this.drowned.world.getBlockState(blockpos.down()).getMaterial() != Material.WATER))
    			{ return true;  }
            	
            	if (this.drowned.world.getBlockState(blockpos.up(2)).getMaterial() == Material.WATER && this.drownedTarget.isInWater())
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

    @SideOnly(Side.CLIENT)
    public void setSwingingArms(boolean swingingArms)
    {
        ;
    }
    
    class GroupData implements IEntityLivingData
    {
        public boolean isChild;

        private GroupData(boolean p_i47328_2_)
        {
            this.isChild = p_i47328_2_;
        }
    }
}