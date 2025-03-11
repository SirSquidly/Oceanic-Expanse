package com.sirsquidly.oe.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleBase extends Particle
{
	protected TextureManager textureManager;
	public ResourceLocation texture;
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    /** Adds this offset to the rendering of the Particle. Best to use for Particles that rely on collisions. */
    public float renderYOffset;
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
        this.renderYOffset = 0;
        this.size = 1.0F;
        this.texture = resource;
        this.texSpot = texSpotIn;
        this.texSheetSeg = texSheetSeg;
	}

	/** Why yes, this is weird. I can't be bothered to learn the damn TextureStich stuff, so here we are.  */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
        this.textureManager.bindTexture(texture);
        float f = (float) (texSpot % texSheetSeg) / texSheetSeg;
        float f1 = f + (1.0F / texSheetSeg);
        float f2 = (float) (texSpot / texSheetSeg) / texSheetSeg;
        float f3 = f2 + (1.0F / texSheetSeg);
        float particleSize = this.particleScale * 0.1F;
        /* The default particles use `interpPos*` for axis, which use `entity.lastTickPos*` for calculations, which causes pretty visible movement delay when combined with the FXLayer we use. */
        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - (entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks));
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - (entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks) + renderYOffset);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks));
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        /* Cram it in a Ver3d, makes it easier to store and read, vanilla was onto something good here. */
        Vec3d[] avec3d = new Vec3d[]
                {
                        new Vec3d((double)(-rotationX * particleSize - rotationXY * particleSize), (double)(-rotationZ * particleSize), (double)(-rotationYZ * particleSize - rotationXZ * particleSize)),
                        new Vec3d((double)(-rotationX * particleSize + rotationXY * particleSize), (double)(rotationZ * particleSize), (double)(-rotationYZ * particleSize + rotationXZ * particleSize)),
                        new Vec3d((double)(rotationX * particleSize + rotationXY * particleSize), (double)(rotationZ * particleSize), (double)(rotationYZ * particleSize + rotationXZ * particleSize)),
                        new Vec3d((double)(rotationX * particleSize - rotationXY * particleSize), (double)(-rotationZ * particleSize), (double)(rotationYZ * particleSize - rotationXZ * particleSize))
                };
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        buffer.begin(7, VERTEX_FORMAT);
        buffer.pos((double)f5 + avec3d[0].x, (double)f6 + avec3d[0].y, (double)f7 + avec3d[0].z).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((double)f5 + avec3d[1].x, (double)f6 + avec3d[1].y, (double)f7 + avec3d[1].z).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((double)f5 + avec3d[2].x, (double)f6 + avec3d[2].y, (double)f7 + avec3d[2].z).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((double)f5 + avec3d[3].x, (double)f6 + avec3d[3].y, (double)f7 + avec3d[3].z).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).normal(0.0F, 1.0F, 0.0F).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
    }

    /** The logic for brightness transitioning from Combined Light to Full Bright. Made a separate method due to how often it is used. */
    public int brightnessIncreaseToFull(float partialTicks)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTicks);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public int getFXLayer()
    { return 3; }
}