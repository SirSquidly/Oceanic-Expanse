package com.sirsquidly.oe.entity;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.blocks.BlockTurtleEgg;
import com.sirsquidly.oe.entity.ai.EntityAIMateCarryEgg;
import com.sirsquidly.oe.entity.ai.EntityAIMateDepositEgg;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTurtle extends AbstractFish implements IEggCarrierMob
{
	protected Block spawnableBlock = Blocks.SAND;

	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Item.getItemFromBlock(OEBlocks.SEAGRASS));
	private static final UUID SWIMMING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier SWIM_SPEED_BOOST = (new AttributeModifier(SWIMMING_SPEED_BOOST_ID, "Swimming speed boost", 0.15000000596046448D, 0)).setSaved(false);
	protected static final DataParameter<BlockPos> HOME_BLOCK_POS = EntityDataManager.<BlockPos>createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Boolean> CARRYING_EGG = EntityDataManager.<Boolean>createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> GOIN_HOME = EntityDataManager.<Boolean>createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DIGGING = EntityDataManager.<Boolean>createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
	private final PathNavigateSwimmer waterNavigator;
	private final PathNavigateGround groundNavigator;
		    
	public EntityTurtle(World worldIn) 
	{
		super(worldIn);
		this.setSize(1.4F, 0.55F);
        stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WALKABLE, 1.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
		
        this.waterNavigator = new PathNavigateSwimmer(this, worldIn);
        this.groundNavigator = new PathNavigateGround(this, worldIn);
        this.setHomePos(new BlockPos(this));
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HOME_BLOCK_POS, BlockPos.ORIGIN);
		this.dataManager.register(CARRYING_EGG, Boolean.valueOf(false));
		this.dataManager.register(GOIN_HOME, Boolean.valueOf(false));
		this.dataManager.register(DIGGING, Boolean.valueOf(false));
	}
	
	@Override
	public boolean canBeLeashedTo(EntityPlayer player)
    { 
		
		return true;
	}
	
	@Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
		this.setHomePos(new BlockPos(this));
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        super.onInitialSpawn(difficulty, livingdata);
        return livingdata;
    }
    
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.08D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }
	
	protected void initEntityAI()
    {	
		this.tasks.addTask(1, new EntityAIMateCarryEgg(this, 1.0D, 20 * ConfigHandler.entity.turtle.turtleBreedCooldown, true));
		this.tasks.addTask(2, new EntityAIMateDepositEgg(this, 1.0D));
		this.tasks.addTask(2, new EntityTurtle.TurtleAIGOHOME(this, 1.0D));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.1D));
        this.tasks.addTask(3, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(3, new EntityTurtle.TurtleAITempt(this, 1.1D, BREEDING_ITEMS));
        this.tasks.addTask(5, new EntityTurtle.EntityAIWanderLand(this, 1.0D, 40));
		this.tasks.addTask(5, new EntityAIWanderUnderwater(this, 1.0D, 80, true));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
    }
	
	protected SoundEvent getAmbientSound()
    { return this.isInWater() ? OESounds.ENTITY_TURTLE_SWIM : OESounds.ENTITY_TURTLE_AMBIENT; }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return this.isChild() ? OESounds.ENTITY_TURTLE_BABY_HURT : OESounds.ENTITY_TURTLE_HURT; }

    protected SoundEvent getDeathSound()
    { return this.isChild() ? OESounds.ENTITY_TURTLE_BABY_DEATH : OESounds.ENTITY_TURTLE_DEATH; }
   
    protected SoundEvent getStepSound()
    { return this.isChild() ? OESounds.ENTITY_TURTLE_BABY_STEP : OESounds.ENTITY_TURTLE_STEP; }

	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_TURTLE; }
	
	public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.world.isRemote)
        {
        	
        	ItemStack itemstack = new ItemStack(Items.BOWL, 1);

            if (this.hasCustomName())
            {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }

            this.entityDropItem(itemstack, 0.0F);
        	this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 9999.0F);
        }
    }
	
	public EntityTurtle createChild(EntityAgeable ageable)
    { 
		EntityTurtle entityturtle = new EntityTurtle(this.world);
        return entityturtle;
    }
	
	/* This drops the Scute when reaching max age. Wow, how easy! **/
	protected void onGrowingAdult()
    {
		ItemStack itemstack = new ItemStack(OEItems.SCUTE, 1);
		this.entityDropItem(itemstack, 0.0F);
    }
	
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        
        BlockPos check1 = new BlockPos(this.getEntityBoundingBox().minX, j, this.getEntityBoundingBox().minZ);
        BlockPos check2 = new BlockPos(this.getEntityBoundingBox().maxX, j, this.getEntityBoundingBox().maxZ);
        if (!this.world.getBlockState(check1).getBlock().isPassable(this.getEntityWorld(), check1) || !this.world.getBlockState(check2).getBlock().isPassable(this.getEntityWorld(), check2)) return false;
        
        return this.posY > 59.0D && this.posY < 68.0D && this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.world.getLight(blockpos) > 7 && isNotColliding();
    }
	
	/** As we inherit from the AbstractFish class, we want to re-disable this. Turtles are rare enough for players. */
	protected boolean canDespawn()
    { return false; }
	
	@Override
	public boolean isNotColliding()
    {
        return !this.world.containsAnyLiquid(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }
	
	@Override
	public void setScaleForAge(boolean child)
    { this.setScale(child ? 0.25F : 1.0F); }

	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }
	
	public float getEyeHeight()
    { return this.height * 0.5F; }
	
	public boolean canFlop() { return false; }
	
	public void onLivingUpdate()
    {
		BlockPos blockpos = new BlockPos(this.posX, this.posY-1, this.posZ);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		
		if (this.isDigging())
		{
			if (this.world.isRemote && iblockstate.getBlock() != Blocks.AIR)
            {
        		for (int i = 0; i < 16; ++i)
                { 
        			double d0 = this.posX + (this.rand.nextDouble() * 1.5 - 0.7);
        			double d1 = this.posY + (this.rand.nextDouble() - 0.5);
                    double d2 = this.posZ + (this.rand.nextDouble() * 1.5 - 0.7);
        			this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, d0, d1, d2, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Block.getStateId(iblockstate));
        		}	
            }
        	if (this.rand.nextInt(2) == 0) 
			{ this.playSound(SoundEvents.BLOCK_SAND_BREAK, 1.0F, 1.0F); }
		}
		
		if (this.getLeashed())
		{ this.setGoingHome(false); }
		
		super.onLivingUpdate();
    }
	
	@Override
	public void onUpdate()
    {
		super.onUpdate();
		
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (this.getHomePos() == BlockPos.ORIGIN && this.getPosition() != BlockPos.ORIGIN)
        { this.setHomePos(new BlockPos(this)); }
		
		if (!world.isRemote) 
        {
            if (isServerWorld() && isInWater()) 
            { 
            	navigator = waterNavigator; 
            	if (!iattributeinstance.hasModifier(SWIM_SPEED_BOOST))
                { iattributeinstance.applyModifier(SWIM_SPEED_BOOST); }
            }
            else 
            { 
            	navigator = groundNavigator;
            	if (iattributeinstance.hasModifier(SWIM_SPEED_BOOST))
            	{ iattributeinstance.removeModifier(SWIM_SPEED_BOOST); }
            }
        }
    }
	
	public void setHomePos(BlockPos pos)
    { this.dataManager.set(HOME_BLOCK_POS, pos); }
	
	public BlockPos getHomePos()
    { return (BlockPos)this.dataManager.get(HOME_BLOCK_POS); }
	
	public boolean isGoingHome()
    { return ((Boolean)this.dataManager.get(GOIN_HOME)).booleanValue(); }

    public void setGoingHome(boolean bool)
    { this.dataManager.set(GOIN_HOME, Boolean.valueOf(bool)); }
    
    public boolean isCarryingEgg()
    { return ((Boolean)this.dataManager.get(CARRYING_EGG)).booleanValue(); }

    public void setCarryingEgg(boolean bool)
    { this.dataManager.set(CARRYING_EGG, Boolean.valueOf(bool)); }
    
    public boolean isDigging()
    { return ((Boolean)this.dataManager.get(DIGGING)).booleanValue(); }

    public void setDigging(boolean bool)
    { this.dataManager.set(DIGGING, Boolean.valueOf(bool)); }
    
	@SuppressWarnings("unused")
	public class TurtleAITempt extends EntityAIBase
	{
		private final EntityCreature turtle;
	    private final double speed;
		private double targetX;
		private double targetY;
		private double targetZ;
	    private EntityPlayer temptPlay;
	    private int delay;
	    private final Set<Item> temptItem;
	    
		public TurtleAITempt(EntityCreature temptedEntityIn, double speedIn, Item temptItemIn)
	    { this(temptedEntityIn, speedIn, Sets.newHashSet(temptItemIn)); }

	    public TurtleAITempt(EntityCreature temptedEntityIn, double speedIn, Set<Item> temptItemIn)
	    {
	        this.turtle = temptedEntityIn;
	        this.speed = speedIn;
	        this.temptItem = temptItemIn;
	        this.setMutexBits(3);
	    }

	    public boolean shouldExecute()
	    {
	        if (this.delay > 0) { --this.delay; return false; }
	        else
	        {
	            this.temptPlay = this.turtle.world.getClosestPlayerToEntity(this.turtle, 10.0D);

	            if (this.temptPlay == null) { return false; }
	            else
	            { return this.isTempting(this.temptPlay.getHeldItemMainhand()) || this.isTempting(this.temptPlay.getHeldItemOffhand()); }
	        }
	    }

	    protected boolean isTempting(ItemStack stack)
	    { return this.temptItem.contains(stack.getItem()); }

	    public void startExecuting()
	    {
	        this.targetX = this.temptPlay.posX;
	        this.targetY = this.temptPlay.posY;
	        this.targetZ = this.temptPlay.posZ;
	    }

	    public void resetTask()
	    {
	        this.temptPlay = null;
	        this.turtle.getNavigator().clearPath();
	        this.delay = 100;
	    }

	    public void updateTask()
	    {
	        this.turtle.getLookHelper().setLookPositionWithEntity(this.temptPlay, (float)(this.turtle.getHorizontalFaceSpeed() + 20), (float)this.turtle.getVerticalFaceSpeed());

	        if (this.turtle.getDistanceSq(this.temptPlay) < 6.25D)
	        { this.turtle.getNavigator().clearPath(); }
	        else
	        { this.turtle.getNavigator().tryMoveToEntityLiving(this.temptPlay, this.speed); }
	    }
		
	}
	
	public class EntityAIWanderLand extends EntityAIWander
	{
		public EntityAIWanderLand(EntityCreature creatureIn, double speedIn, int chance) 
		{
			super(creatureIn, 1.0, chance);
		}
		
		@Override
		public boolean shouldExecute()
	    {
	        if (this.entity.isInWater() || ((EntityTurtle) this.entity).isGoingHome())
	        { return false; }

	        return super.shouldExecute();
	    }
	}
	
	public class TurtleAIGOHOME extends EntityAIBase
	{
		private final EntityTurtle turtle;
	    private final double speed;
		private double moveX;
		private double moveY;
		private double moveZ;
	    private int giveUp;

	    public TurtleAIGOHOME(EntityTurtle turtleIn, double speedIn)
	    {
	        this.turtle = turtleIn;
	        this.speed = speedIn;
	        this.setMutexBits(1);
	    }

	    public boolean shouldExecute()
	    {
	    	if (this.turtle.getRNG().nextInt(1000) != 0 || this.turtle.isChild() || this.turtle.getLeashed())
	    	{ return false; }

	    	return this.turtle.getDistanceSqToCenter(this.turtle.getHomePos()) > 5.0D && this.turtle.isGoingHome();
	    }
	    
	    public void startExecuting()
	    { this.turtle.setGoingHome(true); this.giveUp = 100; }

	    public void resetTask()
	    {
	        this.giveUp = 0;
	    }

	    public boolean shouldContinueExecuting()
	    { return this.giveUp > 0;}
	    
	    public void updateTask()
	    {
	    	if (this.turtle.getDistanceSqToCenter(this.turtle.getHomePos()) < 16.0D)
	    	{ --this.giveUp; }
	    	if (this.turtle.getDistanceSqToCenter(this.turtle.getHomePos()) < 3.0D)
	    	{ this.giveUp = this.giveUp - 10;}
	    	
	    	BlockPos turtlePos = new BlockPos(this.turtle.posX, this.turtle.posY, this.turtle.posZ);
	    	
	    	BlockPos blockpos = this.turtle.getHomePos();
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 16, 7, new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
            
            if (this.turtle.world.getBlockState(turtlePos.up()).getMaterial() == Material.AIR && this.turtle.world.getBlockState(turtlePos).getMaterial() == Material.WATER && this.turtle.world.getBlockState(turtlePos.down()).getMaterial() != Material.WATER)
            { this.turtle.motionY -= 0.1D; }
            
            if (this.turtle.getNavigator().noPath() && vec3d != null)
        	{
            	if (this.turtle.getDistanceSqToCenter(this.turtle.getHomePos()) > 16.0D)
            	{
            		this.moveX = vec3d.x;
            		this.moveY = vec3d.y;
            		this.moveZ = vec3d.z;
            	}
            	else
    	    	{ this.turtle.getNavigator().tryMoveToXYZ(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.speed);; }
        	}	
	        this.turtle.getNavigator().tryMoveToXYZ(this.moveX, this.moveY, this.moveZ, this.speed);
	        
	        if (this.giveUp < 50)
	        { 
	        	this.turtle.setGoingHome(false); 
	        }
	    }
	}
	
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        
        compound.setBoolean("Going_Home", this.isGoingHome());
        compound.setBoolean("Has_Egg", this.isCarryingEgg());
        BlockPos blockpos = this.getHomePos();
        compound.setInteger("HomePosX", blockpos.getX());
        compound.setInteger("HomePosY", blockpos.getY());
        compound.setInteger("HomePosZ", blockpos.getZ());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        
        this.setGoingHome(compound.getBoolean("Going_Home"));
        this.setCarryingEgg(compound.getBoolean("Has_Egg"));
        int x = compound.getInteger("HomePosX");
        int y = compound.getInteger("HomePosY");
        int z = compound.getInteger("HomePosZ");
        this.dataManager.set(HOME_BLOCK_POS, new BlockPos(x, y, z));
    }
    
    static class GroupData implements IEntityLivingData
    {
        public BlockPos homePos;

        private GroupData(BlockPos homeIn)
        {
            this.homePos = homeIn;
        }
    }

	public boolean canLayEgg(World world, BlockPos pos)
	{
		IBlockState iblockstate = world.getBlockState(pos);

        return iblockstate.getMaterial() == Material.SAND && world.getBlockState(pos.up()).getBlock() == Blocks.AIR; 
	}

	@Override
	public void placeEgg(World world, BlockPos pos)
	{
		this.world.setBlockState(pos.up(), OEBlocks.SEA_TURTLE_EGG.getDefaultState().withProperty(BlockTurtleEgg.AMOUNT, Integer.valueOf(world.rand.nextInt(3) + 1)), 3);
		this.setCarryingEgg(false);
	}
}