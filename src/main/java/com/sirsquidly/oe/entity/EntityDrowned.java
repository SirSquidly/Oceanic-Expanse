package com.sirsquidly.oe.entity;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.ai.EntityAITridentThrowing;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.entity.item.EntityTrident;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
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
	// IF this Drowned is a Captain. This changes the texture, along with giving it unique equipment on spawn and the summoning AI
	private static final DataParameter<Boolean> IS_CAPTAIN = EntityDataManager.<Boolean>createKey(EntityDrowned.class, DataSerializers.BOOLEAN);
	/** The time since the conch was last used */
    private int conchUseTime;
    
    private float swimTime;
    private float prevSwimTime;
    
	private final PathNavigateSwimmer waterNavigator;
	private final PathNavigateGround groundNavigator;
	
	public EntityDrowned(World worldIn) {
		super(worldIn);
		
		this.experienceValue = 10;
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
        this.dataManager.register(IS_CAPTAIN, Boolean.valueOf(false));
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(0, new EntityDrowned.DrownedAIGettoWater(this, 1.0D, 35));
		this.tasks.addTask(1, new EntityDrowned.DrownedAISwimToTarget(this));
		this.tasks.addTask(2, new EntityAITridentThrowing<EntityDrowned>(this, 1.0D, 40, 20.0F, (float)ConfigHandler.entity.drowned.drownedTridentMeleeRange));
		this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderUnderwater(this, 1.0D, 80, false));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.applyEntityAI();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void applyEntityAI()
    {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }
	
	public boolean isNotColliding()
    { return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	protected SoundEvent getAmbientSound()
    { return OESounds.ENTITY_DROWNED_AMBIENT; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_DROWNED_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_DROWNED_DEATH; }
    
    protected SoundEvent getStepSound()
    { return OESounds.ENTITY_DROWNED_STEP; }
    
    @Override
    protected ResourceLocation getLootTable()
    { return isCaptain() ? LootTableHandler.ENTITIES_DROWNED_CAPTAIN : LootTableHandler.ENTITIES_DROWNED; }
    
	public boolean canBreatheUnderwater()
    { return true; }
	
	protected float getWaterSlowDown()
    { return this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ).up()).getMaterial() == Material.WATER ? 0.98F : super.getWaterSlowDown(); }
	
	public boolean isSwimming()
    { return ((Boolean)this.dataManager.get(IS_SWIMMING)).booleanValue(); }

    public void setSwimming(boolean swimming)
    { this.dataManager.set(IS_SWIMMING, Boolean.valueOf(swimming)); }
    
    public boolean isCaptain()
    { return ((Boolean)this.dataManager.get(IS_CAPTAIN)).booleanValue(); }

    public void setIsCaptain(boolean captain)
    { this.dataManager.set(IS_CAPTAIN, Boolean.valueOf(captain)); }
    
    protected int getExperiencePoints(EntityPlayer player)
    {
        return isCaptain() ? this.experienceValue = (int)((float)this.experienceValue * 2.5F) : super.getExperiencePoints(player);
    }
    
    @Override
	public void onUpdate()
    {
		super.onUpdate();
        this.setupSwimTimeing();
        // TODO: Move to a timed check
		this.removeInjectedAI();
		
		if (!this.isEntityAlive()) return;
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		EntityLivingBase attackTarget = this.getAttackTarget();
		
		if (attackTarget != null && this.isCaptain() && this.getDistanceSq(attackTarget.getPosition()) <= 10.0F)
        {
            this.conchUseTime += 1;

            if (this.conchUseTime >= ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainSummonCooldown * 20)
            {
            	List<Entity> checkNearbyDrowned = this.world.getEntitiesWithinAABB(EntityDrowned.class, getEntityBoundingBox().grow(15, 15, 15));
        		if ( checkNearbyDrowned.size() < ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainMaxNearbyForSummon)
        		{
        			ItemStack offHand = this.getHeldItemOffhand();
                	
                	if (offHand.getItem() == OEItems.CONCH)
                	{
                		this.playSound(OESounds.ITEM_CONCH_BLOW1, ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainConchSoundDistance * 0.0625F, 0.8F);   
                		
                		for (int i = 0; i < 4; i++)
                        {
                        	Main.proxy.spawnParticle(2, this.world, this.posX + (rand.nextFloat() - rand.nextFloat()), this.posY + 1.5, this.posZ + (rand.nextFloat() - rand.nextFloat()), 0, 0, 0, 4, 128, 255, 192);
                        	Main.proxy.spawnParticle(1, this.world, this.posX + (rand.nextFloat() - rand.nextFloat()), this.posY + 1.5, this.posZ + (rand.nextFloat() - rand.nextFloat()), 0.0D, 0.0D, 0.0D);
                        }
                    }
                	
                	summonReinforcements();
        		}
            	this.conchUseTime = 0;
            }
        }

		if (!world.isRemote) 
        {
			this.setSwimming(attackTarget != null && this.getItemInUseMaxCount() == 0 && (attackTarget.posY - 1.9 > this.posY || attackTarget.posY + 1.9 < this.posY));
			
            navigator = isInWater() && this.world.getBlockState(blockpos.up()).getMaterial() == Material.WATER ? waterNavigator : groundNavigator;
            stepHeight = isInWater() || ConfigHandler.entity.drowned.enableDrownedStepup ? 1.0F : 0.6F;

            //** The material check is to make sure they don't act odd when the player is bobbing on the surface of water. */
            if (attackTarget != null && this.world.isDaytime() && (!attackTarget.isWet() && this.world.getBlockState(attackTarget.getPosition().down()).getMaterial() != Material.WATER))
            { setAttackTarget(null); }
        }
    }
    
    //** Increases the swim timers when is swimming. */
    public void setupSwimTimeing()
    {
    	prevSwimTime = swimTime;
    	
    	if (this.isInWater() && this.isSwimming())
    	{
    		this.swimTime = Math.min(1.0F, this.swimTime + 0.09F);
    	}
    	else
    	{
    		this.swimTime = Math.max(0.0F, this.swimTime - 0.09F);
    	}
    }
    
    //** Sets up the swim timer for proper animation usage.  */
    @SideOnly(Side.CLIENT)
    public float getClientSwimTime(float partialTick)
    {
        return this.prevSwimTime + (this.swimTime - this.prevSwimTime) * partialTick;
    }
    
    private void summonReinforcements()
    {
        EntityDrowned entityDrowned = new EntityDrowned(this.world);
        
        for (int l = 0; l < 50; ++l)
        {
            int i1 = (int)this.posX + MathHelper.getInt(this.rand, 3, 10) * MathHelper.getInt(this.rand, -1, 1);
            int j1 = (int)this.posY + MathHelper.getInt(this.rand, 3, 10) * MathHelper.getInt(this.rand, -1, 1);
            int k1 = (int)this.posZ + MathHelper.getInt(this.rand, 3, 10) * MathHelper.getInt(this.rand, -1, 1);

            if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).getMaterial() == Material.WATER)
            {
            	entityDrowned.setPosition((double)i1, (double)j1, (double)k1);

                if (!this.world.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 2.0D) && this.world.checkNoEntityCollision(entityDrowned.getEntityBoundingBox(), entityDrowned) && this.world.getCollisionBoxes(entityDrowned, entityDrowned.getEntityBoundingBox()).isEmpty())
                {
                    this.world.spawnEntity(entityDrowned);
                    
                    for (int i = 0; i < 80; i++)
                    {
                    	Main.proxy.spawnParticle(2, this.world, entityDrowned.posX + (rand.nextFloat() - rand.nextFloat()), entityDrowned.posY + 1 + (rand.nextFloat() - rand.nextFloat()), entityDrowned.posZ + (rand.nextFloat() - rand.nextFloat()), 0, 0, 0, 4, 128, 255, 192);
                    }
                    
                    entityDrowned.setAttackTarget(this.getAttackTarget());
                    if (ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainEquippedSpawns) entityDrowned.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityDrowned)), (IEntityLivingData)null);
                    break;
                }
            }
        }
    }
    
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        if (entitylivingbaseIn != null && !entitylivingbaseIn.isDead && !entitylivingbaseIn.isWet() && this.world.isDaytime())
        {}
        if (entitylivingbaseIn instanceof EntityDrowned && entitylivingbaseIn.getHeldItemMainhand().getItem() == OEItems.TRIDENT_ORIG)
        {}
        else
        { super.setAttackTarget(entitylivingbaseIn); }
    }
    
    @Override
   	public boolean getCanSpawnHere()
    {
        /* Lazy fix for Drowned spawning where they shouldn't **/
    	if (this.world.provider.getDimension() != 0) return false;

        List<Entity> checkSurroundingDrowned = this.world.getEntitiesWithinAABB(EntityDrowned.class, getEntityBoundingBox().grow(64, 64, 64));
		if ( checkSurroundingDrowned.size() > 2) return false;

        return super.getCanSpawnHere();
	}
    
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
    	this.removeInjectedAI();
    	
    	this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));

    	this.setLeftHanded(this.rand.nextFloat() < 0.05F);

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

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        
        if (this.rand.nextFloat() < f * (ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainSetChance * 0.01F) && ConfigHandler.entity.drowned.drownedCaptain.enableDrownedCaptain)
        {
        	this.setIsCaptain(true);	
        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Captain drowned bonus", ConfigHandler.entity.drowned.drownedCaptain.drownedCaptainHealthMultiplier, 2));
        	this.setHealth(this.getMaxHealth());
        	this.setCanPickUpLoot(true);
        }
        
        this.setNaturalEquipment(difficulty);
        //this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
        return livingdata;
    }
    
    protected void setNaturalEquipment(DifficultyInstance difficulty)
    {
    	if (ConfigHandler.entity.drowned.drownedArmorSpawning)
		{
    		super.setEquipmentBasedOnDifficulty(difficulty);
		}
    	
    	if (isCaptain())
    	{
    		if (ConfigHandler.item.trident.enableTrident)
    		{
    			//float f = difficulty.getClampedAdditionalDifficulty();
    			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(OEItems.TRIDENT_ORIG));
    			
    			//EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int)(5.0F + f * (float)this.rand.nextInt(18)), false);
    		}
    		if (ConfigHandler.item.conch.enableConch) this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(OEItems.CONCH));
    		
    		return;
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
        
        if (this.rand.nextFloat() <= (float)ConfigHandler.entity.drowned.drownedNautilusSpawnChance * 0.01F && ConfigHandler.item.enableNautilusShell)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(OEItems.NAUTILUS_SHELL));
            this.setDropChance(EntityEquipmentSlot.OFFHAND, 100);
        }

        this.setEnchantmentBasedOnDifficulty(difficulty);
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
        this.playSound(OESounds.ENTITY_DROWNED_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entitytrident);
    }
    
    //Lots of AI Below
    
  //** Checks and removes any AI Tasks the Drowned should NEVER have. */
	public void removeInjectedAI()
	{	
		Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.tasks.taskEntries.iterator();
		while (iterator.hasNext())
		{
			EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
			EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

			if (entityaibase instanceof EntityAIBreakDoor || entityaibase instanceof EntityAIMoveThroughVillage)
			{
                Main.logger.error(iterator + " was removed from a Drowned! This injected AI was incompatible and would cause crashes. Adjust the configs of mob AI mods (e.g., Special AI) to disable Door Breaking or Move Through Village for Drowned and Pickled mobs.");
				iterator.remove();
			}
		}
	}
  	
  	
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
			
	        if (!this.drowned.isWet() && this.drowned.world.isDaytime() && target == null)
	        {
	        	return super.shouldExecute();
	        }
	        return false;
	    }
		
		public boolean shouldContinueExecuting()
	    {
	        return !this.drowned.isWet() && this.drowned.world.isDaytime();
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
    		{ this.drowned.motionY += 0.001D; drowned.velocityChanged = true;}
        }
    }

    @SideOnly(Side.CLIENT)
    public void setSwingingArms(boolean swingingArms)
    { }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsCaptain", this.isCaptain());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setIsCaptain(compound.getBoolean("IsCaptain"));
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