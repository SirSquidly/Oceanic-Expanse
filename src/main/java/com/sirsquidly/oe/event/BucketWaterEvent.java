package com.sirsquidly.oe.event;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BucketWaterEvent
{
	/**
	 * This checks where a bucket is placing liquid, and rejects it if the position is Material Water, but doesn't extend BlockLiquid
	 */
	@SubscribeEvent
	public static void RightClickItem(PlayerInteractEvent.RightClickItem event)
    {
		if (!ConfigHandler.vanillaTweak.waterCancelReplace) return;
		
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		RayTraceResult rtresult = Minecraft.getMinecraft().objectMouseOver;

		if (rtresult != null && rtresult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
			if (stack.getItem() instanceof ItemBucket) 
			{
				boolean flag1 = event.getWorld().getBlockState(rtresult.getBlockPos()).getMaterial() == Material.WATER;
	            BlockPos blockpos1 = flag1 ? rtresult.getBlockPos() : rtresult.getBlockPos().offset(rtresult.sideHit);
	                
	            IBlockState iblockstate = event.getWorld().getBlockState(blockpos1);
					
				if (iblockstate.getMaterial() == Material.WATER && !(iblockstate.getBlock() instanceof BlockLiquid))
				{
					event.setCanceled(true);
				}
			}
        }
    }
}