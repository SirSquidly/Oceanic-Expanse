package com.sirsquidly.oe.event;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
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
import com.sirsquidly.oe.blocks.BlockSeagrasss;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@Mod.EventBusSubscriber
public class UnderwaterBonemealEvent 
{
	/** These lists are used when selecting which Coral to generate. */
	static List<Block> allCoralFans = Lists.newArrayList( OEBlocks.BLUE_CORAL_FAN, OEBlocks.PINK_CORAL_FAN, OEBlocks.PURPLE_CORAL_FAN, OEBlocks.RED_CORAL_FAN, OEBlocks.YELLOW_CORAL_FAN );
	static List<Block> allCorals = Lists.newArrayList( OEBlocks.BLUE_CORAL, OEBlocks.PINK_CORAL, OEBlocks.PURPLE_CORAL, OEBlocks.RED_CORAL, OEBlocks.YELLOW_CORAL );

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
                    	if (OEBlocks.SEAGRASS.canPlaceBlockAt(world, blockpos1) && ConfigHandler.block.seagrass.enableSeagrass)
                    	{ world.setBlockState(blockpos1, OEBlocks.SEAGRASS.getDefaultState()); }
                    	
                    	if ((world.getBlockState(blockpos1).getBlock() == OEBlocks.SEAGRASS || !ConfigHandler.block.seagrass.enableSeagrass) && rand.nextInt(10) == 0 && ConfigHandler.block.seagrass.enableTallSeagrass)
                    	{ ((BlockSeagrasss) OEBlocks.SEAGRASS).placeAt(world, blockpos1, 16 | 2); }
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
		if (!ConfigHandler.block.coralBlocks.enableCoralFan && !ConfigHandler.block.coralBlocks.enableCoral) return;
		
		int randCoral = ConfigHandler.block.coralBlocks.enableCoralFan ? ConfigHandler.block.coralBlocks.enableCoral ? rand.nextInt(10) : rand.nextInt(5) : rand.nextInt(5) + 5;

		boolean doFanGen = ConfigHandler.block.coralBlocks.enableCoralFan && rand.nextBoolean();

		if (doFanGen)
		{
			Block block = allCoralFans.get(rand.nextInt(allCoralFans.size()));
			((BlockCoralFan) block).placeGeneration(worldIn, pos, rand, block.getDefaultState());
		}
		else
		{
			Block block = allCorals.get(rand.nextInt(allCorals.size()));
			worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockCoral.IN_WATER, true), 16 | 2);
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