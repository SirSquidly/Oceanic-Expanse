package com.sirsquidly.oe.event;

import java.util.List;

import net.minecraft.block.BlockJukebox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sirsquidly.oe.entity.EntityCrab;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@Mod.EventBusSubscriber
public class PlayerJukeboxEvent
{
	/**
	 * This checks where a bucket is placing liquid, and rejects it if the position is Material Water, but doesn't extend BlockLiquid
	 */
	@SubscribeEvent
	public static void RightClickItem(PlayerInteractEvent.RightClickBlock event)
    {
		if (!ConfigHandler.vanillaTweak.waterCancelReplace) return;
		
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		
		if (!(stack.getItem() instanceof ItemRecord)) return;
		
		BlockPos pos = event.getPos();
		
		if (player.world.getBlockState(pos).getBlock() instanceof BlockJukebox)
		{
			List<Entity> checkForCrabs = event.getWorld().getEntitiesWithinAABB(EntityCrab.class, new AxisAlignedBB(pos).grow(16.0F, 16.0F, 16.0F));
			for (Entity e : checkForCrabs)
			{
				((EntityLivingBase) e).setPartying(event.getPos(), true);
			}
		}
    }
}