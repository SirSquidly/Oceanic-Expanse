package com.sirsquidly.oe.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleBase extends Particle
{
	protected TextureManager textureManager;
	public ResourceLocation texture;
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
	
	/** SIZE. Ya know, the PARTICLE SIZE. */
	public float size;
	/** The spot to on the cut texture sheet to use */
	public int texSpot;
	/** Determines how the particle sheet is cut, EX. Default 4 cuts the sheet 4x4*/
	public int texSheetSeg;
	
	public ParticleBase(TextureManager textureManager, World world, double x, double y, double z, double speedX, double ySpeed, double zSpeed, ResourceLocation resource, int texSpotIn)
	{
		this(textureManager, world, x, y, z, speedX, ySpeed, zSpeed, resource, texSpotIn, 4);
	}
	
	public ParticleBase(TextureManager textureManager, World world, double x, double y, double z, double speedX, double ySpeed, double zSpeed, ResourceLocation resource, int texSpotIn, int texSheetSeg)
	{
		super(world, x, y, z, speedX, ySpeed, zSpeed);
        this.textureManager = textureManager;
        
        this.size = 0.1F;
        this.texture = resource;
        this.texSpot = texSpotIn;
        this.texSheetSeg = texSheetSeg;
	}

	/** Why yes, this is weird. I can't be bothered to learn the damn TextureStich stuff, so here we are.  */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
        this.textureManager.bindTexture(texture);
        float f = (float) (texSpot % texSheetSeg) / texSheetSeg * 1.0F;
        float f1 = f + 0.24975F;
        float f2 = (float) (texSpot / texSheetSeg) / texSheetSeg * 1.0F;
        float f3 = f2 + 0.24975F;
        float f4 = this.size;
        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //GlStateManager.disableLighting();
        //RenderHelper.disableStandardItemLighting();
        buffer.begin(7, VERTEX_FORMAT);
        buffer.pos((f5 - rotationX * f4 - rotationXY * size), (f6 - rotationZ * size), (f7 - rotationYZ * size - rotationXZ * size)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((f5 - rotationX * f4 + rotationXY * size), (f6 + rotationZ * size), (f7 - rotationYZ * size + rotationXZ * size)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((f5 + rotationX * f4 + rotationXY * size), (f6 + rotationZ * size), (f7 + rotationYZ * size + rotationXZ * size)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((f5 + rotationX * f4 - rotationXY * size), (f6 - rotationZ * size), (f7 + rotationYZ * size - rotationXZ * size)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        Tessellator.getInstance().draw();
        //GlStateManager.enableLighting();
    }

    @Override
    public int getFXLayer()
    { return 3; }
}