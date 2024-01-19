package com.sirsquidly.oe.event;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OESounds;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Used for overriding the Noteblock sound
 */
@Mod.EventBusSubscriber
public class NoteBlockPlay
{
	@SubscribeEvent
	public static void noteBlockPlayed(NoteBlockEvent.Play event)
	{
		BlockPos pos = event.getPos();
		if(event.getWorld().getBlockState(pos).getBlock() != Blocks.NOTEBLOCK)
			return;

		if (checkAroundForSkull(event.getWorld(), event.getPos()) == OEBlocks.PICKLED_HEAD)
		{
			event.setCanceled(true);
			
			float pitch = (float) Math.pow(2.0, (event.getVanillaNoteId() - 12) / 12.0);
			
			event.getWorld().playSound(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, OESounds.ENTITY_DROWNED_AMBIENT, SoundCategory.BLOCKS, 1F, pitch);
		}
	}
	
	public static Block checkAroundForSkull(World worldIn, BlockPos pos)
	{
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
	    {
			if(worldIn.getBlockState(pos.offset(facing)).getBlock() != null)
			{ 
				if (worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.SKULL || worldIn.getBlockState(pos.offset(facing)).getBlock() == OEBlocks.PICKLED_HEAD)
				{
					return worldIn.getBlockState(pos.offset(facing)).getBlock();
				}
			}
	    }
		return null;
	}
}
