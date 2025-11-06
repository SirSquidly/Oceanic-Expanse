package com.sirsquidly.oe.event;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityNautilus;
import com.sirsquidly.oe.util.CapabilityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientEvents
{
    private static final ResourceLocation TEXTURE_NAUTILUS_CHARGE_BAR = new ResourceLocation(Main.MOD_ID, "textures/gui/nautilus_charge_bar.png");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (mc.player != null && mc.player.isRiding())
            {
                if (mc.player.getRidingEntity() instanceof EntityNautilus)
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderOverlayPost(RenderGameOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (mc.player != null && mc.player.isRiding() && mc.player.getRidingEntity() instanceof EntityNautilus)
            { renderChargeBar(mc, (EntityNautilus)mc.player.getRidingEntity()); }
        }
    }

    private static void renderChargeBar(Minecraft mc, EntityNautilus ridden)
    {
        ScaledResolution res = new ScaledResolution(mc);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        int barX = width / 2 - 91;
        int barY = height - 29;

        float progress = CapabilityUtil.getChargeStrength(mc.player);
        //float progress = 0.2F;

        GlStateManager.enableTexture2D();
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(TEXTURE_NAUTILUS_CHARGE_BAR);

        mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 0, 182, 5);

        int filled = (int)(progress * 182);
        if (filled > 0)
        {
            mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 5, filled, 5);
        }

        if (ridden.getDashCooldown() > 0)
        { mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 10, 182, 5); }
    }
}