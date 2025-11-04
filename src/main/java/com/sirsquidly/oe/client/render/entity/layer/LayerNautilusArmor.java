package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.RenderNautilus;
import com.sirsquidly.oe.entity.EntityNautilus;
import com.sirsquidly.oe.items.ItemNautilusArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerNautilusArmor implements LayerRenderer<EntityNautilus>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/nautilus_saddle.png");
    private final RenderNautilus nautilusRenderer;

    public LayerNautilusArmor(RenderNautilus nautilusRendererIn)
    {
        this.nautilusRenderer = nautilusRendererIn;
    }

    @Override
    public void doRenderLayer(EntityNautilus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entity.isTamed())
        {
            ItemStack armorStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ResourceLocation texture = null;

            if (!armorStack.isEmpty() && armorStack.getItem() instanceof ItemNautilusArmor)
            {
                ItemNautilusArmor armor = (ItemNautilusArmor) armorStack.getItem();
                texture = armor.getTexture();
            }
            float saddleScale = 1.0001F;

            if (texture == null) return;

            this.nautilusRenderer.bindTexture(texture);
            this.nautilusRenderer.getMainModel().setModelAttributes(this.nautilusRenderer.getMainModel());
            GlStateManager.pushMatrix();
            GlStateManager.scale(saddleScale, saddleScale, saddleScale);
            GlStateManager.translate(0, -0.0001, 0);
            this.nautilusRenderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() { return false; }
}