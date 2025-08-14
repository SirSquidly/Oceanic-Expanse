 package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.world.feature.GeneratorCoconutTree;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockPalmSapling extends BlockBush implements IGrowable
{
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.12D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);
    
	public BlockPalmSapling()
	{
		setSoundType(SoundType.PLANT);
		setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
		this.setCreativeTab(Main.OCEANEXPTAB);
	}

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) { return SAPLING_AABB; }

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			super.updateTick(worldIn, pos, state, rand);

			if (!worldIn.isAreaLoaded(pos, 1)) return;
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
			{ this.grow(worldIn, rand, pos, state); }
		}
	}

	protected boolean canSustainBush(IBlockState state) { return state.getMaterial() == Material.SAND; }

	public IBlockState getStateFromMeta(int meta)
	{ return this.getDefaultState().withProperty(STAGE, Integer.valueOf((meta & 8) >> 3)); }

	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(STAGE).intValue() << 3;
		return i;
	}

	protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, STAGE); }

	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if(state.getValue(STAGE).intValue() == 0)
		{ worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4); }
		else
		{ this.generateTree(worldIn, rand, pos, state); }
	}

	public void generateTree(World world, Random rand, BlockPos pos, IBlockState state)
	{
		if(!TerrainGen.saplingGrowTree(world, rand, pos)) 
		{ return; }
		
		WorldGenerator gen = new GeneratorCoconutTree();
		int i = 0, j = 0;

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if(!gen.generate(world, rand, pos.add(i, 0, j)))
		{
			world.setBlockState(pos, state, 4);
		}
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { return true; }

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) 
	{ return (double)worldIn.rand.nextFloat() < 1.45D; }
}