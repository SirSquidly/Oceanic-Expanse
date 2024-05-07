package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnderwaterTorch extends BlockTorch implements IChecksWater
{
	public BlockUnderwaterTorch()
    {
        this.setHardness(0.0F);
        this.setLightLevel((float)ConfigHandler.block.waterTorch.waterTorchLight * 0.0625F);
        this.setSoundType(SoundType.METAL);
    }
	
	@Override
	@Deprecated
	public Material getMaterial(IBlockState state)
	{
		if(!state.getValue(IN_WATER) || Main.proxy.fluidlogged_enable) return Material.CIRCUITS;
		return Material.WATER;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
		if (rand.nextInt(5) != 0) return;
		
		double partCheck = ConfigHandler.block.waterTorch.waterTorchParticles;
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.6D;
        double d2 = (double)pos.getZ() + 0.5D;

        for (int i = 0; i < 5; i++)
        {
        	double xRandSpead = (rand.nextInt(10) - rand.nextInt(10)) * 0.005D;
            double zRandSpead = (rand.nextInt(10) - rand.nextInt(10)) * 0.005D;
        	if (enumfacing.getAxis().isHorizontal())
            {
                EnumFacing enumfacing1 = enumfacing.getOpposite();
                
                if (partCheck == 1 || partCheck == 3) Main.proxy.spawnParticle(1, d0 + 0.27D * (double)enumfacing1.getXOffset(), d1 + 0.22D, d2 + 0.27D * (double)enumfacing1.getZOffset(), xRandSpead * 0.2D, 0, zRandSpead * 0.2D);
                if (partCheck >= 2) worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d0 + 0.27D * (double)enumfacing1.getXOffset(), d1 + 0.22D, d2 + 0.27D * (double)enumfacing1.getZOffset(), xRandSpead, 0.05D, zRandSpead);
            }
            else
            {
            	if (partCheck == 1 || partCheck == 3) Main.proxy.spawnParticle(1, d0, d1, d2, xRandSpead * 0.2D, 0, zRandSpead * 0.2D);
            	if (partCheck >= 2) worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d0, d1, d2, xRandSpead, 0.05D, zRandSpead);
            }
        }
    }
	
	@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		boolean isWet = checkPlaceWater(worldIn, pos, false);
		BlockPos blockpos = pos.offset(facing.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		//BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);
        
    	if (!isExceptBlockForAttachWithPiston(iblockstate.getBlock()) && iblockstate.getBlockFaceShape(worldIn, blockpos, facing) == BlockFaceShape.SOLID)
        {
            return this.getDefaultState().withProperty(FACING, facing).withProperty(IN_WATER, isWet);
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (super.canPlaceBlockAt(worldIn, pos))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(IN_WATER, isWet);
                }
            }
            return this.getDefaultState().withProperty(IN_WATER, isWet);
        }
    }
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		if (this.canPlaceBlockAt(worldIn, pos)) swapWaterProperty(worldIn, pos, state);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
	
	@Override
	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
		if (checkSurfaceWater(worldIn, pos, state)) return false;
        return super.checkForDrop(worldIn, pos, state);
    }
	
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
	
	public IBlockState getStateFromMeta(int meta)
    {
		EnumFacing enumfacing;
        
        switch (meta & 7)
        {
            case 1:
            	enumfacing = EnumFacing.EAST;
                break;
            case 2:
            	enumfacing = EnumFacing.WEST;
                break;
            case 3:
            	enumfacing = EnumFacing.SOUTH;
                break;
            case 4:
            	enumfacing =EnumFacing.NORTH;
                break;
            case 5:
            default:
            	enumfacing = EnumFacing.UP;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(IN_WATER, Boolean.valueOf((meta & 8) > 0));
    }
	
	public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch ((EnumFacing)state.getValue(FACING))
        {
            case EAST:
                i = 1;
                break;
            case WEST:
                i =  2;
                break;
            case SOUTH:
                i = 3;
                break;
            case NORTH:
                i = 4;
                break;
            case DOWN:
            case UP:
            default:
                i = 5;
        }
        if (state.getValue(IN_WATER).booleanValue()) i |= 8;
        
        return i;
    }
	
	protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] {FACING, BlockLiquid.LEVEL, IN_WATER}); }
}