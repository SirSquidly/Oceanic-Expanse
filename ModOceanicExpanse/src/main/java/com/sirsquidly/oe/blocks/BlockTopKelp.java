package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTopKelp extends BlockBush implements IGrowable
{
	protected static final AxisAlignedBB KELP_TOP_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.5625D, 0.875D);
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	
	public BlockTopKelp() {
		super(Material.WATER);
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
		this.setCreativeTab(Main.OCEANEXPTAB);

		setDefaultState(blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		Random rand = worldIn.rand;
        return this.getDefaultState().withProperty(BlockTopKelp.AGE, Integer.valueOf(rand.nextInt(10)));
    }
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return KELP_TOP_AABB;
    }
    
    @Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	if (!(worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER) || worldIn.getBlockState(pos.up()).getBlock() == OEBlocks.KELP || worldIn.getBlockState(pos.up()).getBlock() == this) 
		{
			worldIn.setBlockState(pos, OEBlocks.KELP.getDefaultState());
		}
		this.checkAndDropBlock(worldIn, pos, state);
	}
    
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || 
        		worldIn.getBlockState(pos.down()).getBlock() == this && worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || 
        				worldIn.getBlockState(pos.down()).getBlock() == OEBlocks.KELP && worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER;   
    }

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if ((worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) || 
        		(worldIn.getBlockState(pos.down()).getBlock() == this) ||  
        		(worldIn.getBlockState(pos.down()).getBlock() == OEBlocks.KELP)) return true;
        return false;
    }

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
	}

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }
	
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(AGE)).intValue();
	}

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockLiquid.LEVEL, AGE);
    }
    
    /** Natural Growing **/
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isAreaLoaded(pos, 1)) return;
        BlockPos blockpos = pos.up();
        int i = ((Integer)state.getValue(AGE)).intValue();
        
        if (i < 15 &&  net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, blockpos, state, rand.nextInt(1) == 0))
        {
        	this.grow(worldIn, rand, pos, state);
        }
    }
    
    /** Bonemeal Growing **/
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return ((Integer)state.getValue(AGE)).intValue() != 15; }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return true; }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	int i = ((Integer)state.getValue(AGE)).intValue();
    	
        if (this.canPlaceBlockAt(worldIn, pos.up()) && worldIn.getBlockState(pos.up(2)).getBlock() == Blocks.WATER)
        {
        	worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(AGE, Integer.valueOf(i + 1)), 2);
    		worldIn.setBlockState(pos, OEBlocks.KELP.getDefaultState(), 2);
        }
        else
        {
        	worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(i + 1)), 2);
        }
    }
    
}
