package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.blocks.BlockCoral;
import com.sirsquidly.oe.blocks.BlockCoralFan;
import com.sirsquidly.oe.blocks.BlockCoralFull;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
/**
 * Common traits for all normal fish to inherit
 */
public class AbstractFish extends EntityAnimal
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet();
	protected EntityAIWander wander;
	/** If the fish just spawned. Used to help fish wth specific spawning coditions, like Tropical Fish. */
	public boolean justSpawned;
	
	public AbstractFish(World worldIn)
	{
		super(worldIn);
		this.moveHelper = new AbstractFish.FishMoveHelper(this);
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
    }
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	public AbstractFish createChild(EntityAgeable ageable)
    { return null; }
	
	public boolean canBeLeashedTo(EntityPlayer player)
    { return false; }
		
	@Override
	public boolean getCanSpawnHere()
    { 
		return !checkNearbyEntites(16, 10, null) && checkHeight((int)this.posY, this.world);
    }
	
	/** Used to check nearby entities.
	 * 
	 *  int 'areaCheck' is the area to check, in a cube
	 *  int 'minNear' is the minimum entities required for this to pass
	 *  class 'checkClass' is the class of entity to check for. If left null, checks for any AbstractFish
	 *  */
	public boolean checkHeight(int posY, World world)
    { 
		return posY <= world.getSeaLevel()+1 && posY >= world.getSeaLevel()-12; 
    }
	
	/** Used to check nearby entities.
	 * 
	 *  int 'areaCheck' is the area to check, in a cube
	 *  int 'minNear' is the minimum entities required for this to pass
	 *  class 'checkClass' is the class of entity to check for. If left null, checks for any AbstractFish
	 *  */
	@SuppressWarnings("unchecked")
	public boolean checkNearbyEntites(int areaCheck, int minNear, @SuppressWarnings("rawtypes")@Nullable Class checkClass)
    { 
		List<Entity> checkAbove = this.world.getEntitiesWithinAABB(AbstractFish.class, getEntityBoundingBox().grow(areaCheck, areaCheck, areaCheck));
		
		if (checkClass != null)
		{
			checkAbove = this.world.getEntitiesWithinAABB(checkClass, getEntityBoundingBox().grow(areaCheck, areaCheck, areaCheck));
		}
		return checkAbove.size() > minNear; 
    }
	
	/** Checks if the nearby entity can spawn here. This helps fish with particular spawn conditions.
	 * 
	 *  int 'areaCheck' is the area to check, in a cube.
	 *  class 'checkClass' is the class of entity to check for.
	 *  */
	@SuppressWarnings("unchecked")
	public boolean checkNeighborSpawn(int areaCheck, @SuppressWarnings("rawtypes") Class checkClass)
    { 
		List<Entity> checkNeighboring = this.world.getEntitiesWithinAABB(checkClass, getEntityBoundingBox().grow(areaCheck, areaCheck, areaCheck));
		for (Entity e : checkNeighboring) 
    	{
			if (((AbstractFish) e).justSpawned)
			{
				return true;
			}
    	}
		return false; 
    }
	
	/** Used to check the first solid block below this position.
	 * 
	 *  int 'length' is how far down it will attempt to find a solid block, before giving up.
	 *  'blockIn' is the block we are checking for.
	 *  */
	public boolean checkBlockDown(int x, int y, int z, int length, Block blockIn)
    { 
        BlockPos blockpos = new BlockPos(x, y, z);
        
        for (int l = 0; l <= length; l++)
        { if (this.world.getBlockState(blockpos.down()).getMaterial() == Material.WATER) blockpos = blockpos.down(); }
        
        // If the given block is an instance of Coral Full, we let it pass for any coral.
        if (blockIn instanceof BlockCoralFull)
        { return this.world.getBlockState(blockpos.down()).getBlock() instanceof BlockCoralFull || this.world.getBlockState(blockpos.down()).getBlock() instanceof BlockCoral || this.world.getBlockState(blockpos.down()).getBlock() instanceof BlockCoralFan; 
        }
		return this.world.getBlockState(blockpos.down()).getBlock() == blockIn;
    }
	
	/** Used to check the first solid block above this position.
	 * 
	 *  int 'length' is how far down it will attempt to find a solid block, before giving up.
	 *  'blockIn' is the block we are checking for.
	 *  */
	public boolean checkBlockUp(int x, int y, int z, int length, Block blockIn)
    { 
        BlockPos blockpos = new BlockPos(x, y, z);
        
        for (int l = 0; l <= length; l++)
        { if (this.world.getBlockState(blockpos.up()).getMaterial() == Material.WATER) blockpos = blockpos.up(); }
        
		return this.world.getBlockState(blockpos.up()).getBlock() == blockIn;
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        justSpawned = true;
        return livingdata;
    }
	
	public boolean isNotColliding()
    { return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }
	
	/**
	* Detects if the fish isn't in water. Used for flopping motion and animation.
	*/
	public boolean isFlopping() 
	{ 
		return !this.isInsideOfMaterial(Material.WATER);
	}
	
	protected SoundEvent getAmbientSound()
    { return this.isInWater() ? SoundHandler.ENTITY_FISH_SWIM : null; }
	
	/**
	* The sound a fish uses when Flopping
	*/
	public SoundEvent getFlopSound()
    { return SoundHandler.ENTITY_FISH_FLOP; }
	
	@Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        justSpawned = false;
        
        float f = 0.0F;
        BlockPos blockpos = new BlockPos(this);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        
        if (iblockstate.getMaterial() == Material.WATER)
        {
            f = BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos);
        }
        if (this.world.isRemote) {} 
        else {
            if (onGround && isFlopping()) 
            {
                motionY += 0.4D;
                motionX += (double) ((rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
                motionZ += (double) ((rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
                rotationYaw = rand.nextFloat() * 360.0F;
                onGround = false;
                isAirBorne = true;
                if (world.getTotalWorldTime() % 1 == 0)
                	world.playSound((EntityPlayer) null, posX, posY, posZ, getFlopSound(), SoundCategory.NEUTRAL, 0.3F, 0.8F / (this.rand.nextFloat() * 0.4F + 0.8F));
            }
            else
            {
            	motionY -= 0.001D;
            	if (f > 0.0F) setAir(150);
            }
        }
    }
	
	// Warning, Guardian stuff ahead    
		
	protected boolean canTriggerWalking()
    { return false; }
	
	public int getTalkInterval()
    { return 120; }
	
	@Override
	protected PathNavigate createNavigator(World worldIn)
    { return new PathNavigateSwimmer(this, worldIn); }
	
	@Override
	public void travel(float strafe, float vertical, float forward)
    {
        if (this.isServerWorld() && this.isInWater())
        {
            this.moveRelative(strafe, vertical, forward, 0.1F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8;
            this.motionY *= 0.9;
            this.motionZ *= 0.8;
        }
        else
        { super.travel(strafe, vertical, forward); }
    }
	
	static class FishMoveHelper extends EntityMoveHelper
    {
        private final AbstractFish Fish;

        public FishMoveHelper(AbstractFish Fish)
        {
            super(Fish);
            this.Fish = Fish;
        }
     
    @Override
	public void onUpdateMoveHelper()
    {
        if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.Fish.getNavigator().noPath() && this.Fish.isInWater())
        {    
        	if (this.Fish.isInsideOfMaterial(Material.WATER)) 
        	{ this.Fish.motionY += 0.005; }

            double d0 = this.posX - this.Fish.posX;
            double d1 = this.posY - this.Fish.posY;
            double d2 = this.posZ - this.Fish.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 = d1 / d3;
            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.Fish.rotationYaw = this.limitAngle(this.Fish.rotationYaw, f, 90.0F);
            this.Fish.renderYawOffset = this.Fish.rotationYaw;
            
            float f1 = (float)(this.speed * this.Fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            this.Fish.setAIMoveSpeed(this.Fish.getAIMoveSpeed() + (f1 - this.Fish.getAIMoveSpeed()) * 0.125F);
            
            this.Fish.motionY += (double)this.Fish.getAIMoveSpeed() * d1 * 0.1D;
        	} 
        
        	if (!(this.Fish.isInWater()) && this.Fish.isFlopping()) 
        	{ this.Fish.setAIMoveSpeed(0.0F); }
        	
        	else if ((!(this.Fish.isInWater()) && !(this.Fish.isFlopping())))
        	{ super.onUpdateMoveHelper();}
    	}
    }
}