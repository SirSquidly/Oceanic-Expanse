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
public class ParticleConchWave extends ParticleBase
{
    private static final ResourceLocation[] CONCH_TEXTURES = new ResourceLocation[] {new ResourceLocation(Main.MOD_ID, "textures/particles/conch_wail.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/conch_breath.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/conch_call.png"), new ResourceLocation(Main.MOD_ID, "textures/particles/conch_serenade.png")};

    public ParticleConchWave(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int waveType)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, CONCH_TEXTURES[waveType], 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 20;
        this.texSheetSeg = 2;
        this.canCollide = false;
        this.particleScale = 10.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();

        float f = ((float)this.particleAge ) / ((float)this.particleMaxAge / 2);
        this.particleScale += f/2;
        this.texSpot = Math.min(this.particleAge * 5 / (this.particleMaxAge), 3);


        //this.particleAlpha = (1.0F - (float)particleAge / (float)this.particleMaxAge);

        if (this.particleAge > this.particleMaxAge / 2) this.setAlphaF(1.2F - (float)particleAge / (float)this.particleMaxAge - ((float)(this.particleMaxAge / 2)));
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return super.getBrightnessForRender(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleConchWave(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]); }
    }
}