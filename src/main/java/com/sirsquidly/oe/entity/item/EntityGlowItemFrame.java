package com.sirsquidly.oe.entity.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Predicate;
import com.sirsquidly.oe.init.OEItems;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGlowItemFrame extends EntityItemFrame implements IEntityAdditionalSpawnData 
{
	/** Copied from EntityItemFrame, as it is not public. */
	private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>()
		    {
		        public boolean apply(@Nullable Entity p_apply_1_)
		        { return p_apply_1_ instanceof EntityHanging; }
		    };
	
	/** Copied from EntityHanging, it's an override of the normal facingDirection. */
	@Nullable
	public EnumFacing extFacingDirection;
    
	private float itemDropChance = 1.0F;

	public EntityGlowItemFrame(World worldIn)
	{ super(worldIn); }

	public EntityGlowItemFrame(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos, facing);
        this.updateFacingWithBoundingBox(facing);
    }

	@SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
    	float minBright = 0.5F;
    	minBright = MathHelper.clamp(minBright, 0.0F, 1.0F);
        int normBright = super.getBrightnessForRender();
        int j = normBright & 255;
        int k = normBright >> 16 & 255;
        j = j + (int)(minBright * 15.0F * 16.0F);

        if (j > 240) j = 240;

        if (this.getDisplayedItem().getItem() instanceof ItemMap) return 15728880;
        
        return j | k << 16;
    }

    public float getBrightness()
    {  
    	if (this.getDisplayedItem().getItem() instanceof ItemMap) return 1.0F;
    	return 0.5F;
    }

    @Override
    public void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_)
    {
        if (!this.world.getGameRules().getBoolean("doEntityDrops")) return;

        float yOffset = this.extFacingDirection == EnumFacing.DOWN ? -0.5F : 0.0F;
        
        ItemStack itemstack = this.getDisplayedItem();

        if (entityIn instanceof EntityPlayer)
        {
        	if (((EntityPlayer)entityIn).capabilities.isCreativeMode)
        	{
        		this.removeFrameFromMap(itemstack);
        		return;
        	}
        }

        if (p_146065_2_) this.entityDropItem(new ItemStack(OEItems.GLOW_ITEM_FRAME), yOffset);
            
        if (!itemstack.isEmpty() && this.rand.nextFloat() < this.itemDropChance)
        {
        	itemstack = itemstack.copy();
        	this.removeFrameFromMap(itemstack);
        	this.entityDropItem(itemstack, yOffset);
        }  
    }

    /**
	 * Copied from EntityItemFrame, as it is not public.
	 */
    private void removeFrameFromMap(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            if (stack.getItem() instanceof ItemMap)
            {
                MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.world);
                mapdata.mapDecorations.remove("frame-" + this.getEntityId());
            }

            stack.setItemFrame((EntityItemFrame)null);
            this.setDisplayedItem(ItemStack.EMPTY);
        }
    }

	/**
     * Copied from EntityHanging, tweaked to support Y Axis
     */
    @Override
	public boolean onValidSurface()
    {
    	if(!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) { return false; }
    	
		if(this.extFacingDirection.getAxis() == EnumFacing.Axis.Y)
		{
			BlockPos blockpos = this.hangingPosition.offset(this.extFacingDirection.getOpposite());
			IBlockState iblockstate = this.world.getBlockState(blockpos);
			
			if(!iblockstate.isSideSolid(this.world, blockpos, this.extFacingDirection))
			{
				if(!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate)) return false;
			}	

			
			return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
		}
		else
		{ return super.onValidSurface(); }
	}

    @Override
	protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
		Validate.notNull(facingDirectionIn);
		this.extFacingDirection = facingDirectionIn;
		this.facingDirection = extFacingDirection.getAxis() == EnumFacing.Axis.Y ? EnumFacing.SOUTH : extFacingDirection;
		this.rotationYaw = extFacingDirection.getAxis() == EnumFacing.Axis.Y ? 0 : (this.extFacingDirection.getHorizontalIndex() * 90);
		this.rotationPitch = extFacingDirection.getAxis() == EnumFacing.Axis.Y ? (extFacingDirection == EnumFacing.UP ? -90.0F : 90.0F) : 0F;
		this.prevRotationYaw = this.rotationYaw;
		this.updateBoundingBox();
	}

	@Override
	protected void updateBoundingBox()
	{
		if(this.extFacingDirection == null) return;
		
		if(this.extFacingDirection.getAxis() == EnumFacing.Axis.Y)
		{
			double d0 = (double)this.hangingPosition.getX() + 0.5D;
            double d1 = (double)this.hangingPosition.getY() + 0.5D;
            double d2 = (double)this.hangingPosition.getZ() + 0.5D;
			d1 = d1 - (double)this.extFacingDirection.getFrontOffsetY() * 0.46875D;

			double d6 = this.getHeightPixels();
			double d7 = -this.extFacingDirection.getFrontOffsetY();
			double d8 = this.getHeightPixels();

			this.posX = d0;
			this.posY = d1 - (d7 / 32.0D);
			this.posZ = d2;
			this.height = 0.0625F;
			
			d6 = d6 / 32.0D;
			d7 = d7 / 32.0D;
			d8 = d8 / 32.0D;
			
			this.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
		}
		else
		{ super.updateBoundingBox(); }
	}

	/**
     * Copied from the base Entity class mostly
     */
	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
        ItemStack held = this.getDisplayedItem();
        if (held.isEmpty())
        { return new ItemStack(OEItems.GLOW_ITEM_FRAME); }
        else
        { return held.copy(); }
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setByte("RealFacing", (byte)this.extFacingDirection.getIndex());
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.updateFacingWithBoundingBox(EnumFacing.getFront(compound.getByte("RealFacing")));
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{ buffer.writeShort(extFacingDirection.getIndex()); }

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{ updateFacingWithBoundingBox(EnumFacing.getFront(additionalData.readShort())); }
}