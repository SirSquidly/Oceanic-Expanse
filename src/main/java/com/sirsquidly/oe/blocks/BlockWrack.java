package com.sirsquidly.oe.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWrack extends Block
{
	protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
		
	public BlockWrack()
	    {
	        super(Material.PLANTS);
	        this.setSoundType(SoundType.PLANT);
	    }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return FLAT_AABB; }
	
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    { return FLAT_AABB; }
	
	public boolean isOpaqueCube(IBlockState state)
    { return false; }

    public boolean isFullCube(IBlockState state)
    { return false; }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        else
        { return true; }
    }
    
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
    { return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT; }
    
    private boolean canBlockStay(World worldIn, BlockPos pos)
    { return !worldIn.isAirBlock(pos.down()); }
}