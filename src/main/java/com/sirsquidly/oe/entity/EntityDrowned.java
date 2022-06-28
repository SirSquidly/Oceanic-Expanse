package com.sirsquidly.oe.entity;

import javax.annotation.Nullable;

import com.sirsquidly.oe.entity.ai.EntityAITridentThrowing;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDrowned extends EntityZombie implements IRangedAttackMob 
{
	private static final DataParameter<Boolean> IS_SWIMMING = EntityDataManager.<Boolean>createKey(EntityDrowned.class, DataSerializers.BOOLEAN);
	private final PathNavigateSwimmer waterNavigator;
	private final PathNavigateGround groundNavigator;
	
	public EntityDrowned(World worldIn) {
		super(worldIn);
		this.setPathPriority(PathNodeType.WALKABLE, 1.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
        
        this.waterNavigator = new PathNavigateSwimmer(this, worldIn);
        this.groundNavigator = new PathNavigateGround(this, worldIn);
	}
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(IS_SWIMMING, Boolean.valueOf(false));
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(0, new EntityDrowned.DrownedAIGettoWater(this, 1.0D, 35));
		this.tasks.addTask(1, new EntityDrowned.DrownedAISwimToTarget(this));
		this.tasks.addTask(2, new EntityAITridentThrowing<EntityDrowned>(this, 1.0D, 20, 20.0F, (float)ConfigHandler.entity.drowned.drownedTridentMeleeRange));
		this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.applyEntityAI();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void applyEntityAI()
    {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }
	
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
	public void onUpdate()
    {
		super.onUpdate();
		
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		EntityLivingBase attackTarget = this.getAttackTarget();
		
		if (!world.isRemote) 
        {
            if (isServerWorld() && (isInWater() && this.world.getBlockState(blockpos.up()).getMaterial() == Material.WATER)) navigator = waterNavigator; 
            else navigator = groundNavigator;
            
            if (isInWater()) stepHeight = 1.0F;
            else stepHeight = 0.6F;
            
            //** The material check is to make sure they don't act odd when the player is bobbing on the surface of water. */
            if (attackTarget != null && this.world.isDaytime() && (!attackTarget.isWet() && this.world.getBlockState(attackTarget.getPosition().down()).getMaterial() != Material.WATER))
            {
            	setAttackTarget(null);
            }
        }
    }
    
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        if (entitylivingbaseIn != null && !entitylivingbaseIn.isDead && !entitylivingbaseIn.isWet() && this.world.isDaytime())
        {}
        else
        { super.setAttackTarget(entitylivingbaseIn); }
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
    	if (ConfigHandler.entity.drowned.drownedArmorSpawning)
		{
    		super.setEquipmentBasedOnDifficulty(difficulty);
		}
    	
        if (this.rand.nextFloat() <= (float)ConfigHandler.entity.drowned.drownedTridentSpawnChance * 0.01F && ConfigHandler.item.trident.enableTrident)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(OEItems.TRIDENT_ORIG));
        }
        else if (this.rand.nextFloat() <=  (float)ConfigHandler.entity.drowned.drownedRodSpawnChance * 0.01F)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
        }
        else
        {
        	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        
        if (this.rand.nextFloat() <= (float)ConfigHandler.entity.drowned.drownedNautilusSpawnChance * 0.01F)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(OEItems.NAUTILUS_SHELL));
            this.setDropChance(EntityEquipmentSlot.OFFHAND, 100);
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
    
    public class DrownedAIGettoWater extends EntityAIMoveToBlock
    {
    	EntityCreature drowned;
    	private final double movementSpeed;
    	private int timeoutdrownedCounter;
    	
		public DrownedAIGettoWater(EntityCreature creature, double speedIn, int length)
		{
			super(creature, speedIn, length);
			this.drowned = creature;
			this.movementSpeed = speedIn;
		}
		
		public boolean shouldExecute()
	    {
			EntityLivingBase target = this.drowned.getAttackTarget();
			this.runDelay -= 50;
			
	        if (!this.drowned.isWet() && target == null)
	        {
	        	return super.shouldExecute();
	        }
	        return false;
	    }
		
		public boolean shouldContinueExecuting()
	    {
	        return !this.drowned.isWet();
	    }
		
		public void updateTask()
	    {
	        if (this.drowned.getDistanceSqToCenter(this.destinationBlock) > 0.1D)
	        {
	            ++this.timeoutdrownedCounter;

	            if (this.timeoutdrownedCounter % 40 == 0)
	            {
	                this.drowned.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 0.5D), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
	            }
	        }
	        else
	        { --this.timeoutdrownedCounter; }
	    }
		
		@Override
		protected boolean shouldMoveTo(World worldIn, BlockPos pos)
		{
			return worldIn.getBlockState(pos).getMaterial() == Material.WATER;	
		}
	}
    
    
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
    		{ this.drowned.motionY += 0.05D; drowned.velocityChanged = true;}
        }
    }

    @SideOnly(Side.CLIENT)
    public void setSwingingArms(boolean swingingArms)
    { }
    
    class GroupData implements IEntityLivingData
    {
        public boolean isChild;

        private GroupData(boolean p_i47328_2_)
        {
            this.isChild = p_i47328_2_;
        }
    }
}