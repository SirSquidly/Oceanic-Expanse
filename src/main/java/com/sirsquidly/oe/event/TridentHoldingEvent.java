package com.sirsquidly.oe.event;

import javax.annotation.Nonnull;

import com.sirsquidly.oe.client.ClientMixinMethods;

import com.sirsquidly.oe.items.ItemTrident;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod.EventBusSubscriber
public class TridentHoldingEvent 
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	static void renderFirstPersonTrident(@Nonnull RenderSpecificHandEvent event)
	{
		 final ItemStack stack = event.getItemStack();
		 final EntityPlayer player = Minecraft.getMinecraft().player;
         final ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();

	     if(stack.getItem() instanceof ItemTrident)
	     {
	    	 final EnumHandSide arm = event.getHand() == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
	    	 final boolean isRightArm = arm == EnumHandSide.RIGHT;
	    	 int offset = isRightArm ? -1 : 1;
	    	 
	         GlStateManager.pushMatrix();

	         if(player.isHandActive() && player.getItemInUseCount() != 0 && player.getActiveHand() == event.getHand())
	         {
				 ClientMixinMethods.preformTridentItemRotations1stPerson(player, stack, offset);
	        	 event.setCanceled(true);
	         }
             
	         renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
	         GlStateManager.popMatrix();
	     }
	}
}