package com.sirsquidly.oe.event;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.init.OEBlocks;

@Mod.EventBusSubscriber
public class UnderwaterBonemealEvent 
{
	/** This feels hacky. Am I a hack? Probably. **/
	@SubscribeEvent
	public static void onBonemeal(BonemealEvent event) {
		IBlockState state = event.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();

		if(world.getBlockState(pos.up()).getMaterial() == Material.WATER && world.getBlockState(pos.up(2)).getMaterial() == Material.WATER && state.isFullCube())
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
                    if (world.getBlockState(blockpos1.down()).isNormalCube() && world.getBlockState(blockpos1.up()).getBlock() == Blocks.WATER && !world.isRemote)
                    {
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
	
}