package com.sirsquidly.oe.client.render.tileentity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.tileentity.ModelConduitOff;
import com.sirsquidly.oe.client.model.tileentity.ModelJustABlock;
import com.sirsquidly.oe.client.model.tileentity.ModelStasis;
import com.sirsquidly.oe.tileentity.TileStasis;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderStasis extends TileEntitySpecialRenderer<TileStasis>
{
	private static final ResourceLocation STASIS_OFF = new ResourceLocation(Main.MOD_ID + ":textures/entities/stasis/stagnant_off.png");
    private final ModelBase modelConduitOff = new ModelConduitOff();
    private static final ResourceLocation STASIS = new ResourceLocation(Main.MOD_ID + ":textures/entities/stasis/stagnant.png");
    private static final ResourceLocation STASIS_ELDER = new ResourceLocation(Main.MOD_ID + ":textures/entities/stasis/stagnant_elder.png");
    private final ModelBase modelStasis = new ModelStasis();
    private final ModelBase modelConduitWind = new ModelJustABlock();

    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/stasis/stagnant_beam.png");

    public void render(TileStasis te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);

        GlStateManager.disableCull();

        if (te.isActive)
        {
            /**Used for progressing the bobbing on the Eye and Shell, up and down.*/
            float bobbing = (float)te.bobTick + partialTicks;

            if (te.windTick <= 34)
            {
            	GlStateManager.pushMatrix();
            	this.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/conduit/wind/wind" + te.windTick + ".png"));
            	GlStateManager.translate(0.0F, -0.5F, 0.0F);

            	if(te.windTick > 0 && te.windTick <= 18)
            	{
	            	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
	            	GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	            	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            	}

            	if(te.windTick > 18)
            	{
            		this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                	GlStateManager.translate(0.0F, -1.0F, -1.0F);
                	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            	}
            	GlStateManager.popMatrix();
            }

        	GlStateManager.translate(0.0F, MathHelper.sin(bobbing * 0.1F) * 0.2F, 0.0F);
            renderShell(te, x, y, z, partialTicks);

            GlStateManager.enableCull();
            GlStateManager.popMatrix();


            renderLaser(te, x, y, z, partialTicks, bobbing);
        }
        else
        {
        	GlStateManager.pushMatrix();
        	GlStateManager.translate(0.0F, -0.5625F, 0.0F);
        	this.bindTexture(STASIS_OFF);
        	this.modelConduitOff.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
        	GlStateManager.popMatrix();

            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

    public void renderShell(TileStasis tile, double x, double y, double z, float partialTicks)
    {
        float shellRotation = tile.shellRotationPrev + (tile.shellRotation - tile.shellRotationPrev) * partialTicks;
        float headPitch = tile.eyePitchPrev + (tile.eyePitch - tile.eyePitchPrev) * partialTicks;
        float headYaw = tile.eyeRotationPrev + (tile.eyeRotation - tile.eyeRotationPrev) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -0.5F, 0.0F);
        /* Swaps the texture if the Stasis is an Elder. */
        this.bindTexture(tile.isElder ? STASIS_ELDER : STASIS);
        this.modelStasis.render((Entity)null, shellRotation, 3.0F, 0.2F, headYaw, headPitch, 0.0625F);
        GlStateManager.popMatrix();
    }

    public void renderLaser(TileStasis tile, double x, double y, double z, float partialTicks, float bobbingIn)
    {
        EntityLivingBase target = tile.getAttackTarget();


        if (target != null && tile.canSeeEntity(target))
        {
            float f = tile.getAttackAnimationScale(partialTicks);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(BEAM_TEXTURE);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            //GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            float f2 = (float)target.world.getTotalWorldTime() + partialTicks;
            float f3 = f2 * 0.5F % 1.0F;
            float f4 = 0.5F;
            GlStateManager.pushMatrix();

            GlStateManager.translate((float)x + 0.5, (float)y + 0.5 + MathHelper.sin(bobbingIn * 0.1F) * 0.2F, (float)z + 0.5);

            Vec3d beamEnd = new Vec3d(target.posX, target.posY + target.height * 0.5, target.posZ);
            Vec3d beamStart = new Vec3d(tile.getPos()).add(0.5, 0.5, 0.5);
            Vec3d vec3d2 = beamEnd.subtract(beamStart);
            double d0 = vec3d2.length();
            vec3d2 = vec3d2.normalize();
            float f5 = (float)Math.acos(vec3d2.y);
            float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
            GlStateManager.rotate((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            int i = 1;
            double d1 = (double)f2 * 0.05D * -1.5D;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f * f;
            int j = 64 + (int)(f7 * 191.0F);
            int k = 32 + (int)(f7 * 191.0F);
            int l = 128 - (int)(f7 * 64.0F);
            double d2 = 0.2D;
            double d3 = 0.282D;
            double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
            double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
            double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
            double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
            double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
            double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
            double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
            double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
            double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
            double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
            double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
            double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
            double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
            double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
            double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
            double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
            double d20 = 0.0D;
            double d21 = 0.4999D;
            double d22 = (double)(-1.0F + f3);
            double d23 = d0 * 2.5D + d22;
            bufferbuilder.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            double d24 = 0.0D;

            if (target.ticksExisted % 2 == 0) { d24 = 0.5D; }

            bufferbuilder.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableBlend();
            GlStateManager.enableCull();

            /* A hacky, manual reset of blend functions, so it does not bleed into the rendering of other Conduits. */
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }
    }
}