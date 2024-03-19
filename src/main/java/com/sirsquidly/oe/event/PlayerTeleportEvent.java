package com.sirsquidly.oe.event;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This converts normal Pearls to Ender Pearls.
 */
@Mod.EventBusSubscriber
public class PlayerTeleportEvent
{
	/**
	 * We have no way to easily check if the player Teleported, so we just check after they use an item.
	 */
	@SubscribeEvent
	public static void didPlayerTeleport(LivingEntityUseItemEvent.Finish event)
	{
		Entity user = event.getEntity();
		ItemStack item = event.getItem();

		if (user instanceof EntityPlayer && item.getItem() == Items.CHORUS_FRUIT)
		{
			EntityPlayer player = (EntityPlayer) user;
			ItemStack pearlHand = ((EntityLivingBase)user).getHeldItem(EnumHand.MAIN_HAND).getItem() == OEItems.PEARL ? ((EntityLivingBase)user).getHeldItem(EnumHand.MAIN_HAND) : ((EntityLivingBase)user).getHeldItem(EnumHand.OFF_HAND).getItem() == OEItems.PEARL ? ((EntityLivingBase)user).getHeldItem(EnumHand.OFF_HAND) : ItemStack.EMPTY;
			
			if (!pearlHand.isEmpty())
			{
				ItemStack newStack = new ItemStack(Items.ENDER_PEARL);
				pearlHand.shrink(1);
				
				if (pearlHand.isEmpty()) 
		        {
					player.setHeldItem(EnumHand.OFF_HAND, newStack);
					// Prevent the player from instantly throwing the new Ender Pearl they are holding
					player.getCooldownTracker().setCooldown(newStack.getItem(), 10);
		        } 
		        else if (!player.addItemStackToInventory(newStack)) 
		        {
		        	player.dropItem(newStack, false);
		        }
			
			}
		}
	}
}