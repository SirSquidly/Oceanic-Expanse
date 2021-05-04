package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSeaPickle extends BlockBush implements IGrowable
{
	public static final PropertyInteger AMOUNT = PropertyInteger.create("amount", 1, 4);
	public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
	protected static final AxisAlignedBB[] PICKLE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.4375D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.875D, 0.375D, 0.8125D), new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.4375D, 0.875D)};
	
	public BlockSeaPickle() {
		super(Material.GOURD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AMOUNT, Integer.valueOf(1)).withProperty(IN_WATER, false));
		
		this.setSoundType(SoundType.SLIME);
		this.setHardness(0.1F);
		this.setResistance(10.0F);
		this.setCreativeTab(Main.OCEANEXPTAB);
	}
	
	@Override
	@Deprecated
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(IN_WATER)) {
			return Material.WATER;
		}
		return super.getMaterial(state);
	}

	@SuppressWarnings("deprecation")
	@Override
    public int getLightValue(IBlockState state)
    {
		if (state.getValue(IN_WATER))
		{
	        switch (state.getValue(AMOUNT))
	        {
	            case 1: return 6;
	            case 2: return 9;
	            case 3: return 12;
	            case 4: return 15;
	        }
		}
        return super.getLightValue(state);
    }
	
	
	/** 
	 * All Placing and Staying 
	 * **/

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) || 
        		worldIn.getBlockState(pos.down()).getBlock() instanceof BlockFence || 
        		worldIn.getBlockState(pos.down()).getBlock() instanceof BlockWall;   
    }
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) || 
        		worldIn.getBlockState(pos.down()).getBlock() instanceof BlockFence || 
        		worldIn.getBlockState(pos.down()).getBlock() instanceof BlockWall) return true;
        return false;
    }

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }
	
	/**
	 *  Checking for the Pickels ot update if Submerged 
	 *  **/
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		this.checkWater(worldIn, pos, state);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkWater(worldIn, pos, state);
        super.onBlockAdded(worldIn, pos, state);
    }
    
    protected boolean checkWater(World worldIn, BlockPos pos, IBlockState state)
    {
    	boolean flag = false;
    	
    	/** If water around but not above, remove **/
		for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing != EnumFacing.DOWN && enumfacing != EnumFacing.UP)
            {
                BlockPos blockpos = pos.offset(enumfacing);

                if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
                {
                	if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR)
            		{ flag = true; break; }
    				if (worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN) || worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER) 
    				{ worldIn.setBlockState(pos, state.withProperty(IN_WATER, true)); flag = false; break;}
    				else { flag = false; break; }
                }
            }
        }
		/** Seperated from the above check, as to not multiply the item **/
		if (flag)
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			if (state.getValue(IN_WATER) && !(worldIn.provider.doesWaterVaporize())) 
			{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
			else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
		}
		return flag;
    }
    
    /**
	 *  Break for water if Submerged 
	 *  **/
    
    @Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			
			if (state.getValue(IN_WATER) && !(worldIn.provider.doesWaterVaporize()))
				{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
			else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(IN_WATER) && !(worldIn.provider.doesWaterVaporize()))
			{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
		else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return PICKLE_AABB[((Integer)state.getValue(AMOUNT)).intValue() - 1];
    }
	
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
		return PICKLE_AABB[((Integer)state.getValue(AMOUNT)).intValue() - 1];
    }
	
    public IBlockState getStateFromMeta(int meta)
    {
    	return this.getDefaultState().withProperty(AMOUNT, Integer.valueOf((meta & 3) + 1)).withProperty(IN_WATER, (meta & 4) != 0);
    }

    public int getMetaFromState(IBlockState state)
    {
    	int i = 0;
        i = i | (state.getValue(AMOUNT)).intValue() - 1;

        if (((Boolean)state.getValue(IN_WATER)).booleanValue())
        { i |= 4; }

        return i;
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override public int quantityDropped(IBlockState state, int fortune, Random random){ return ((Integer)state.getValue(AMOUNT)); }

    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, BlockLiquid.LEVEL, AMOUNT, IN_WATER);
    }
    
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }
    
    /** 
     * Bonemeal Growing 
     * **/
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return true;}

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return true; }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	int i = ((Integer)state.getValue(AMOUNT)).intValue();
    	
        if (i < 4)
        {
        	i = i + (rand.nextInt(3) + 1);
        	if (i > 4)
            { i = 4; }
        	
        	worldIn.setBlockState(pos, this.getDefaultState().withProperty(AMOUNT, Integer.valueOf(i)), 2);
        }
        
        if (this.isCoralBlock(worldIn, pos.down()))
        {
        	this.spreadOnCoral(worldIn, pos, state, rand);
        }
    }
    
    /** This is so bad, replace it later w/ something similar to Sponge w/ height check **/
    public void spreadOnCoral(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

        for (int k = 0; k < 3; ++k)
        {
            if (worldIn.getBlockState(blockpos1).getBlock() == Blocks.WATER && this.canBlockStay(worldIn, blockpos1, this.getDefaultState()) && this.isCoralBlock(worldIn, blockpos1.down()))
            {
                pos = blockpos1;
            }

            blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
        }

        if (worldIn.getBlockState(blockpos1).getBlock() == Blocks.WATER && this.canBlockStay(worldIn, blockpos1, this.getDefaultState()) && this.isCoralBlock(worldIn, blockpos1.down()))
        {
            worldIn.setBlockState(blockpos1, this.getDefaultState().withProperty(AMOUNT, Integer.valueOf(rand.nextInt(4) + 1)), 2);
        }
    }
    
    private boolean isCoralBlock(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock() instanceof BlockCoralFull;
    }
}