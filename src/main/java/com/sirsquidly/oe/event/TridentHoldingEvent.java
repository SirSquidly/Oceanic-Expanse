package com.sirsquidly.oe.event;

import javax.annotation.Nonnull;

import com.sirsquidly.oe.init.OEItems;

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

	     if(stack.getItem() == OEItems.TRIDENT_ORIG) 
	     {
	    	 //System.out.println("HAND is RUNNING");
	    	 final EnumHandSide arm = event.getHand() == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
	    	 final boolean isRightArm = arm == EnumHandSide.RIGHT;
	    	 int offset = isRightArm ? -1 : 1;
	    	 
	         GlStateManager.pushMatrix();

	         if(player.isHandActive() && player.getItemInUseCount() != 0 && player.getActiveHand() == event.getHand())
	         {
	        	 float smoothUseTime = (float)stack.getMaxItemUseDuration() - ((float)player.getItemInUseCount() - event.getPartialTicks() + 1.0F);
	        	 
	        	 GlStateManager.rotate(280, 1, 0, 0);
	        	 GlStateManager.translate((float)offset * -0.5F, 0.7F + (Math.min(smoothUseTime / 20.0F, 1.0F) * -0.5F), -0.4 + Math.min(smoothUseTime / 10.0F, 0.4F));
	        	 
	        	 event.setCanceled(true);
	         }
             
	         renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
	         GlStateManager.popMatrix();
	     }
	}
}