package com.sirsquidly.oe.world.gen;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSeagrass extends WorldGenerator
{
	private final IBlockState tallGrassState;

    public WorldGenSeagrass(BlockTallGrass.EnumType p_i45629_1_)
    {
        this.tallGrassState = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, p_i45629_1_);
    }
    
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (IBlockState iblockstate = worldIn.getBlockState(position); (iblockstate.getBlock().isAir(iblockstate, worldIn, position) || iblockstate.getBlock().isLeaves(iblockstate, worldIn, position)) && position.getY() > 0; iblockstate = worldIn.getBlockState(position))
        {
            position = position.down();
        }

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && OEBlocks.SEAGRASS.canPlaceBlockAt(worldIn, blockpos))
            {
            	worldIn.setBlockState(blockpos, OEBlocks.SEAGRASS.getDefaultState());
            }
        }
        return true;
    }
}