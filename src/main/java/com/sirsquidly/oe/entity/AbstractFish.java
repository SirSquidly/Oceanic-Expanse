package com.sirsquidly.oe.entity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
/**
 * Common traits for all normal fish to inherit
 */
public class AbstractFish extends EntityAnimal
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet();
	protected EntityAIWander wander;
	
	public AbstractFish(World worldIn) {
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
    { return true; }
	
	public boolean isNotColliding()
    { return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }
	
	/**
	* Detects if the fish isn't in water. Used for flopping motion and animation.
	*/
	public boolean isFlopping() 
	{ return !isInWater() && world.isAirBlock(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY + 1), MathHelper.floor(posZ))) && world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY - 1), MathHelper.floor(posZ))).getBlock().isCollidable(); }

	@Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        float f = 0.0F;
        BlockPos blockpos = new BlockPos(this);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        
        if (iblockstate.getMaterial() == Material.WATER)
        {
            f = BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos);
        }
        if (this.world.isRemote) {} 
        else {
            if (f > 0.0F) 
            {
                setAir(150);
                motionY -= 0.0003D;
            }  
            else if (onGround && this.isFlopping()) 
            {
                motionY += 0.4D;
                motionX += (double) ((rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
                motionZ += (double) ((rand.nextFloat() * 2.0F - 1.0F) * 0.2F);
                rotationYaw = rand.nextFloat() * 360.0F;
                onGround = false;
                isAirBorne = true;
                if (world.getTotalWorldTime() % 5 == 0)
                    world.playSound((EntityPlayer) null, posX, posY, posZ, SoundEvents.ENTITY_GUARDIAN_FLOP, SoundCategory.HOSTILE, 1F, 1F);
            }
        }
    }
	
	// Warning, Guardian stuff ahead    
		
	protected boolean canTriggerWalking()
    { return false; }
	
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