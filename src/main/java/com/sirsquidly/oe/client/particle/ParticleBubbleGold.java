package com.sirsquidly.oe.client.particle;

import com.sirsquidly.oe.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleBubbleGold extends ParticleBase
{
    private static final ResourceLocation[] CONCH_TEXTURES = new ResourceLocation[] {new ResourceLocation(Main.MOD_ID, "textures/particles/sea_foam.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/oil_drip.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/spirit_summon.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/bubble_gold.png")};

    public ParticleBubbleGold(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int waveType)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, CONCH_TEXTURES[waveType], 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 5 + this.rand.nextInt(21);
        this.texSheetSeg = 2;
        this.canCollide = waveType == 0 ? true : false;
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.8F;

        this.particleGravity = waveType == 0 ? 0.02F : 0.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 3 / (this.particleMaxAge);

        this.motionY -= this.particleGravity;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return super.getBrightnessForRender(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleBubbleGold(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]); }
    }
}