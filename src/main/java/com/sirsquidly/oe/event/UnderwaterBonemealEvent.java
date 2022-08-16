package com.sirsquidly.oe.event;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import com.sirsquidly.oe.blocks.BlockCoral;
import com.sirsquidly.oe.blocks.BlockCoralFan;
import com.sirsquidly.oe.blocks.BlockCoralFull;
import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@Mod.EventBusSubscriber
public class UnderwaterBonemealEvent 
{
	/** This feels hacky. Am I a hack? Probably. **/
	@SubscribeEvent
	public static void onBonemeal(BonemealEvent event) {
		IBlockState state = event.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();

		if(ConfigHandler.vanillaTweak.waterBonemeal.enableWaterBonemeal && world.getBlockState(pos.up()).getBlock() == Blocks.WATER && world.getBlockState(pos.up(2)).getMaterial() == Material.WATER && state.isFullCube())
		{
			if (!world.isRemote)
            { world.playEvent(2005, pos, 0); }
			
			growSeaGrass(event);
		}
	}
	
	public static void growSeaGrass(BonemealEvent event)
    {
		World world = event.getWorld();
		Random rand = world.rand;
        BlockPos blockpos = event.getPos().up();

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true)
            {
                if (j >= i / 16)
                {
                    if (world.getBlockState(blockpos1.down()).isNormalCube() && world.getBlockState(blockpos1).getBlock() == Blocks.WATER && !world.isRemote)
                    {
                    	if (ConfigHandler.vanillaTweak.waterBonemeal.waterBonemealCoralGen != 0 && rand.nextInt(ConfigHandler.vanillaTweak.waterBonemeal.waterBonemealCoralChance) == 0 && checkCoral(world, blockpos1))
                    	{
                    		placeCoral(world, blockpos1, rand);
                    		break;
                    	}
                    	if (OEBlocks.SEAGRASS.canPlaceBlockAt(world, blockpos1))
                    	{ world.setBlockState(blockpos1, OEBlocks.SEAGRASS.getDefaultState()); }
                    	
                    	if (world.getBlockState(blockpos1).getBlock() == OEBlocks.SEAGRASS && rand.nextInt(10) == 0)
                    	{ ((BlockDoubleUnderwater) OEBlocks.TALL_SEAGRASS).placeAt(world, blockpos1, 2); }
                    }
                	break;
                }
                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (!(world.getBlockState(blockpos1.down()).isNormalCube()) || world.getBlockState(blockpos1).isNormalCube())
                { break; }
                
                ++j;
            }
        }
    event.setResult(Result.ALLOW);
    }
	
	
	public static void placeCoral(World worldIn, BlockPos pos, Random rand)
	{
		switch (rand.nextInt(10))
		{
			case 0: 
				((BlockCoralFan) OEBlocks.BLUE_CORAL_FAN).placeAt(worldIn, pos, rand, OEBlocks.BLUE_CORAL_FAN);
				break;
			case 1:
				((BlockCoralFan) OEBlocks.PINK_CORAL_FAN).placeAt(worldIn, pos, rand, OEBlocks.PINK_CORAL_FAN);
				break;
			case 2:
				((BlockCoralFan) OEBlocks.PURPLE_CORAL_FAN).placeAt(worldIn, pos, rand, OEBlocks.PURPLE_CORAL_FAN);
				break;
			case 3:
				((BlockCoralFan) OEBlocks.RED_CORAL_FAN).placeAt(worldIn, pos, rand, OEBlocks.RED_CORAL_FAN);
				break;
			case 4:
				((BlockCoralFan) OEBlocks.YELLOW_CORAL_FAN).placeAt(worldIn, pos, rand, OEBlocks.YELLOW_CORAL_FAN);
				break;
			case 5: 
				worldIn.setBlockState(pos, OEBlocks.BLUE_CORAL.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
				break;
			case 6:
				worldIn.setBlockState(pos, OEBlocks.PINK_CORAL.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
				break;
			case 7:
				worldIn.setBlockState(pos, OEBlocks.PURPLE_CORAL.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
				break;
			case 8:
				worldIn.setBlockState(pos, OEBlocks.RED_CORAL.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
				break;
			case 9:
				worldIn.setBlockState(pos, OEBlocks.YELLOW_CORAL.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
				break;
		}
	}
	
	
	public static boolean checkCoral(World worldIn, BlockPos pos)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
        {
			if (enumfacing == EnumFacing.DOWN || ConfigHandler.vanillaTweak.waterBonemeal.waterBonemealCoralGen == 2)
			{
				BlockPos blockpos = pos.offset(enumfacing);
	        	if (worldIn.getBlockState(blockpos).getBlock() instanceof BlockCoralFull) return true;
			}
        }
		return false;
	}
}