package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OESounds;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This code handles Prismarine Stalks. They grow up to 3 block tall.
 */
public class BlockPrismarineStalks extends BlockBush implements IGrowable, IChecksWater
{
	protected static final AxisAlignedBB[] DULSE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.5625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.8125D, 0.8125D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};
	/** 0 = Half, 1 = Full, 2 = Full Connected.*/
	public static final PropertyInteger HEIGHT = PropertyInteger.create("height", 0, 2);
	public static final PropertyBool CRYSTALLIZED = PropertyBool.create("sheared");
	
	public BlockPrismarineStalks()
	{
		super(Material.WATER);
		this.setSoundType(OESounds.WET_GRASS);
		this.setTickRandomly(true);

		//setDefaultState(blockState.getBaseState().withProperty(SHEARED, false));
	}
	
	@SuppressWarnings("deprecation")
	public Material getMaterial(IBlockState state)
	{
		if(Main.proxy.fluidlogged_enable) { return Material.PLANTS; }
		return super.getMaterial(state);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return DULSE_AABB[((Integer)state.getValue(HEIGHT)).intValue()];
    }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && checkPlaceWater(worldIn, pos, false);
    }
	
	/** Checks if the Double tall 4th age crop can be placed here.*/
	public boolean canPlaceFullAge(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && checkPlaceWater(worldIn, pos, false) && checkPlaceWater(worldIn, pos.up(), true);
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (checkSurfaceWater(worldIn, pos, state)) return false;
        if (worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) || worldIn.getBlockState(pos.down()).getBlock() == this) return true;
        return false;
    }
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			if (state.getValue(HEIGHT).intValue() != 4 || (state.getValue(HEIGHT).intValue() == 4 && worldIn.getBlockState(pos.down()).getBlock() != this))
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
        if (worldIn.isRemote) return;
        
        if (worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0) && canGrow(worldIn, pos, state, false))
        {
        	this.grow(worldIn, rand, pos, state);
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { 
    	if (((Integer)state.getValue(HEIGHT)).intValue() > 0 && worldIn.getBlockState(pos.down()).getBlock() == this)  return false;
		return (worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || Main.proxy.fluidlogged_enable); 
    }

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{ return false; }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		int i = ((Integer)state.getValue(HEIGHT)).intValue();
    	
    	if(rand.nextInt(5) < 100 && i != 2)
		{ 
    		worldIn.playSound(null, pos, OESounds.BLOCK_PRISMARINE_STALKS_GROW, SoundCategory.BLOCKS, 0.5F, -2.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()));
    		if (i == 0)
    		{
    			worldIn.setBlockState(pos, state.withProperty(HEIGHT, Integer.valueOf(1)));
    		}
    		else if (i > 0)
    		{
    			if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER && (worldIn.getBlockState(pos.up(2)).getBlock() == Blocks.WATER  || Main.proxy.fluidlogged_enable) && worldIn.getBlockState(pos.down()).getBlock() != this)
    			{
    				worldIn.setBlockState(pos, state.withProperty(HEIGHT, Integer.valueOf(2)));
    				worldIn.setBlockState(pos.up(), state.withProperty(HEIGHT, Integer.valueOf(0)));
    			}
    		}
		}
		
	}

	public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(HEIGHT, Integer.valueOf((meta & 3))).withProperty(CRYSTALLIZED, (meta & 4) != 0); }
 
	public int getMetaFromState(IBlockState state)
	{
	    int i = 0;
	    i = i | ((Integer)state.getValue(HEIGHT)).intValue();
	    
	    if (((Boolean)state.getValue(CRYSTALLIZED)).booleanValue())
	    { i |= 4; }
	    return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLiquid.LEVEL, HEIGHT, CRYSTALLIZED);
	}
}