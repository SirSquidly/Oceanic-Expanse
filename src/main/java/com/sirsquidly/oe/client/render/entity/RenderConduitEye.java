package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityConduitEye;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderConduitEye extends Render<EntityConduitEye>
{
	private static final ResourceLocation CONDUIT_EYE_TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/conduit/conduit_eye.png");

    public RenderConduitEye(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.0F;
        this.shadowOpaque = 0.0F;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityConduitEye entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            this.bindEntityTexture(entity);
            int tex;
            float pluse = (float)entity.ticksExisted + partialTicks;
        	float a = MathHelper.sin(pluse * 0.2F) * 0.2F;
        	
            if (entity.getHunting())
            {
            	tex = 8;
            	if (a >= 0.15) tex = 9;
            	if (a >= 0.17) tex = 10;
            	if (a >= 0.19) tex = 11;
            }
            else 
            {
            	tex = 4;	
            	if (a >= 0.15) tex = 5;
            	if (a >= 0.17) tex = 6;
            	if (a >= 0.19) tex = 7;
            }
            float f = (float)(tex % 4 * 16 + 0) / 64.0F;
            float f1 = (float)(tex % 4 * 16 + 16) / 64.0F;
            float f2 = (float)(tex / 4 * 16 + 0) / 64.0F;
            float f3 = (float)(tex / 4 * 16 + 16) / 64.0F;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)225, (float)225);
            GlStateManager.translate(0.0F, 0.1F, 0.0F);
            GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 5.0F, 0.0F);
            GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 5.0F, 0.0F, 0.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            double size = 0.6;
            bufferbuilder.pos(-size/2, -size/4, 0.0D).tex((double)f, (double)f3).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(size/2, -size/4, 0.0D).tex((double)f1, (double)f3).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(size/2, (size/4) * 3, 0.0D).tex((double)f1, (double)f2).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(-size/2, (size/4) * 3, 0.0D).tex((double)f, (double)f2).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityConduitEye entity)
    {
        return CONDUIT_EYE_TEXTURES;
    }
}