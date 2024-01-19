package com.sirsquidly.oe.client.render.tileentity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.tileentity.ModelPickledHead;
import com.sirsquidly.oe.tileentity.TilePickledSkull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPickledSkull extends TileEntitySpecialRenderer<TilePickledSkull>
{
	public static final RenderPickledSkull INSTANCE = new RenderPickledSkull();
	private ModelPickledHead model = new ModelPickledHead();
	public static final ResourceLocation PICKLED_ZOMBIE_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/pickled.png");
	public static final ResourceLocation PICKLED_ZOMBIE_DRY_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/pickled_dry.png");
	public static final ResourceLocation PICKLED_SECRET_TEXTURE1 = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/pickled_spec1.png");
	public static final ResourceLocation PICKLED_ZOMBIE_BRIGHT_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/pickled_bright.png");

	public void render(TilePickledSkull te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing enumfacing = EnumFacing.getFront(te.getBlockMetadata() & 7);
        boolean intest = te.getIntestinesOut();
        float f = te.getAnimationProgress(partialTicks);
        this.renderSkull((float)x, (float)y, (float)z, enumfacing, (float)(te.getSkullRotation() * 360) / 16.0F, destroyStage, intest, f);
        
        if (intest) this.renderSkullGlow((float)x, (float)y, (float)z, enumfacing, (float)(te.getSkullRotation() * 360) / 16.0F, destroyStage, intest, f);
    }

	public void renderSkull(float x, float y, float z, EnumFacing facing, float rotationIn, int destroyStage, boolean intestinesOut, float animateTicks)
	{
		ModelPickledHead modelbase = this.model;
		
		if(destroyStage >= 0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
		}
		else
			this.bindTexture(PICKLED_ZOMBIE_TEXTURE);

		GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (facing == EnumFacing.UP)
        {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        }
        else
        {
            switch (facing)
            {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                    rotationIn = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                    rotationIn = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                    rotationIn = 90.0F;
            }
        }
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        
        modelbase.render((Entity)null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F);
        
        if (intestinesOut)
        { modelbase.renderIntestines((Entity)null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F); }
        
        
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}
	
	
	public void renderSkullGlow(float x, float y, float z, EnumFacing facing, float rotationIn, int destroyStage, boolean intestinesOut, float animateTicks)
	{
		ModelPickledHead modelbase = this.model;
		
		if(destroyStage >= 0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
		}
		else
			this.bindTexture(PICKLED_ZOMBIE_BRIGHT_TEXTURE);

		GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (facing == EnumFacing.UP)
        {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        }
        else
        {
            switch (facing)
            {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                    rotationIn = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                    rotationIn = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                    rotationIn = 90.0F;
            }
        }
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        
        modelbase.render((Entity)null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F);
        modelbase.renderIntestines((Entity)null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F);
        
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}
}