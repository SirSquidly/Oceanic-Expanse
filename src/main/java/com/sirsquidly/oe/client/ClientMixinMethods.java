package com.sirsquidly.oe.client;

import com.sirsquidly.oe.items.ItemTrident;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

public class ClientMixinMethods
{
    public static void preformTridentItemRotations3edPerson(EntityLivingBase entity, ItemStack stack)
    {
        if (ConfigHandler.item.trident.trident3PsnAnimStyle == 0) return;

        boolean mainIsRight = entity.getActiveHand() == EnumHand.MAIN_HAND ? entity.getPrimaryHand() == EnumHandSide.RIGHT : entity.getPrimaryHand() == EnumHandSide.LEFT;

        float anim = Math.min(((ItemTrident)entity.getActiveItemStack().getItem()).getAnimSpeed(entity) / 6.0F, 1.0F);
        if (ConfigHandler.item.trident.trident3PsnAnimStyle != 2) anim =  1;

        GlStateManager.rotate(anim * 180.0F, 0, 0, mainIsRight ? 1 : -1);
        GlStateManager.translate(0, anim * 0.25F, 0);
    }

    public static void preformTridentItemRotations1stPerson(EntityLivingBase entity, ItemStack stack, int offset)
    {
        float anim = ((ItemTrident)stack.getItem()).getAnimSpeed(entity);
        float lift = Math.min(anim / 4.0F, 1.0F);

        GlStateManager.translate(0.0F, -0.52F + (lift * 0.6F), 0.0F);
        GlStateManager.rotate(lift * -90.0F, 1, 0, 0);
        GlStateManager.translate(0.0F, (lift * 0.8F), 0.0F);
        GlStateManager.translate((float)offset * -0.56F, (Math.min(anim / 20.0F, 1.0F) * -0.5F), -0.72 + Math.min(anim / 10.0F, 0.72F));
    }
}
