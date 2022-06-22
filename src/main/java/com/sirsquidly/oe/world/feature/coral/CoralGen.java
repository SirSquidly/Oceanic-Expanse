package com.sirsquidly.oe.world.feature.coral;

import java.util.Random;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CoralGen extends WorldGenerator
{
	public int coralType;
	
	public boolean generate(World worldIn, Random rand, BlockPos position) 
	{
		return false;
	}

	
	public boolean placeCoralBlockAt(World worldIn, BlockPos pos, int type)
	{
		
		if(pos.getY() >= worldIn.getSeaLevel() - 1)
		{
			return false;
		}
		
		/** SWITCH ISN'T WORKING, IF STATEMENTS IT IS THEN! */
	    if (type == 1)
		{
			this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.BLUE_CORAL_BLOCK.getDefaultState());
		}
	    else if (type == 2)
		{
			this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.PINK_CORAL_BLOCK.getDefaultState());
		}
	    else if (type == 3)
		{
			this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.PURPLE_CORAL_BLOCK.getDefaultState());
		}
	    else if (type == 4)
		{
			this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.RED_CORAL_BLOCK.getDefaultState());
		}
	    else if (type == 5)
		{
			this.setBlockAndNotifyAdequately(worldIn, pos, OEBlocks.YELLOW_CORAL_BLOCK.getDefaultState());
		}
	    
	    return true;
	}
}