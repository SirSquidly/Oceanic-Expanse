package com.sirsquidly.oe.event;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
		World world = event.getWorld();

		Vec3d eyePosition = player.getPositionEyes(1.0F);
        Vec3d traceEnd = eyePosition.add(player.getLook(1.0F).scale(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
		RayTraceResult rtresult = player.getEntityWorld().rayTraceBlocks(eyePosition, traceEnd, false);;

		if (rtresult != null && rtresult.typeOfHit == RayTraceResult.Type.BLOCK && stack.getItem() instanceof ItemBucket || stack.getItem() == OEItems.SPAWN_BUCKET)
        {
			BlockPos bucketPos = world.getBlockState(rtresult.getBlockPos()).getMaterial() == Material.WATER ? rtresult.getBlockPos() : rtresult.getBlockPos().offset(rtresult.sideHit);

			if (OEBlocks.blockList.contains(world.getBlockState(bucketPos).getBlock()))
			{ event.setCanceled(true); }
        }
    }
}