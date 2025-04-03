package com.sirsquidly.oe.client.render.entity.layer;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.capabilities.CapabilityRiptide;
import com.sirsquidly.oe.client.model.entity.ModelRiptideAnim;
import com.sirsquidly.oe.entity.EntityDrowned;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerRiptideAnim implements LayerRenderer<EntityLivingBase>
{
    private static final ResourceLocation TEXTURE_RIPTIDE = new ResourceLocation(Main.MOD_ID + ":textures/entities/riptide_anim.png");
    protected final RenderLivingBase<?> renderLiving;
    private final ModelRiptideAnim modelRiptideAnim = new ModelRiptideAnim();

    public LayerRiptideAnim(RenderLivingBase<?> renderLivingIn)
    {
        this.renderLiving = renderLivingIn;
    }

    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = false;

        if (entity.hasCapability(CapabilityRiptide.RIPTIDE_CAP, null))
        {
            flag = entity.getCapability(CapabilityRiptide.RIPTIDE_CAP, null).getRiptideAnimate();
        }
        if (entity instanceof EntityDrowned)
        {
            flag = ((EntityDrowned)entity).getRiptideUseTime() > 0;
        }


        if (flag)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.renderLiving.bindTexture(TEXTURE_RIPTIDE);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelRiptideAnim.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            this.modelRiptideAnim.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() { return false; }
}