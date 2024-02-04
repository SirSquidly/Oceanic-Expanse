package com.sirsquidly.oe.blocks;

import java.util.List;

import com.sirsquidly.oe.entity.EntityTropicalSlime;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlueSlime extends BlockBreakable
{
	@SuppressWarnings("deprecation")
	public BlockBlueSlime()
	{
		super(Material.CLAY, false, MapColor.LAPIS);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.slipperiness = 0.8F;
	}

	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (entityIn.isSneaking())
        {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else
        {
            entityIn.fall(fallDistance, 0.0F);
        }
    }
    
    @Override
    public boolean isStickyBlock(IBlockState state)
    {
        return true;
    }
    
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.IGNORE;
    }
    
    
    public boolean isFullCube(IBlockState state)
    { return false; }
    
    public boolean causesSuffocation(IBlockState state)
    { return false; }
    
    @SuppressWarnings("deprecation")
	@Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState)
    {
        if(entity == null)
        {
            super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
        }
        else
        {
        	/** The minimum X-Z movement speed required to pass through. */
        	double minHorMove = 0.4F;
        	
        	if (entity instanceof EntityLivingBase)
        	{
        		EntityLivingBase livingBase = (EntityLivingBase) entity;
                
                if (livingBase.isElytraFlying())
                { minHorMove = 0.4F; }
        	}
        	
        	/** Riding has weird issues passing through blocks, so we ignore it.*/
        	if (entity.isBeingRidden() || entity.isRiding())
        	{ minHorMove = 999.0F; }

            if(!((entity.motionY > 0.8F || entity.motionY < -0.8F) || (entity.motionX > minHorMove || entity.motionX < -minHorMove) || (entity.motionZ > minHorMove || entity.motionZ < -minHorMove)) && !(entity instanceof EntityTropicalSlime))
            {
                super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
            }
        }
    }
}