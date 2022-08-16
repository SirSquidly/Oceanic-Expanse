package com.sirsquidly.oe.client.particle;

import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleConduit extends ParticleBase
{
	private static final ResourceLocation CONDUIT_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/particles/conduit.png");
	private final double coordX;
    private final double coordY;
    private final double coordZ;
    /* A default variable for the texture selector. If this is ever accidentally triggered, then I have to ask WHY I NEED THE 12090544 SPOT OF A TEXTURE.**/
    private static int specificTexture = 12090544;
    
    protected ParticleConduit(TextureManager textureManager, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double ySpeed, double zSpeed)
    {
        this(textureManager, worldIn, xCoordIn, yCoordIn, zCoordIn, speedX, ySpeed, zSpeed, specificTexture);
    }
    
	public ParticleConduit(TextureManager textureManager, World world, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double ySpeed, double zSpeed, int specificTextureIn)
	{
		super(textureManager, world, xCoordIn, yCoordIn, zCoordIn, speedX, ySpeed, zSpeed, CONDUIT_TEXTURE, 1);
        this.textureManager = textureManager;
        
        this.motionX = speedX;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        this.prevPosX = xCoordIn + speedX;
        this.prevPosY = yCoordIn + ySpeed;
        this.prevPosZ = zCoordIn + zSpeed;
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.posZ = this.prevPosZ;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleScale = this.rand.nextFloat() * 0.1F + 0.05F;
        this.particleRed = 0.9F * f;
        this.particleGreen = 0.9F * f;
        this.particleBlue = f;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;

        if (specificTextureIn == specificTexture) this.texSpot = this.rand.nextInt(12);
        else this.texSpot = specificTextureIn;
        
        this.size = this.rand.nextFloat() * 0.1F + 0.05F;
	}

	public void move(double x, double y, double z)
    {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 = f1 * f1;
        f1 = f1 * f1;
        this.posX = this.coordX + this.motionX * (double)f;
        this.posY = this.coordY + this.motionY * (double)f - (double)(f1 * 1.2F);
        this.posZ = this.coordZ + this.motionZ * (double)f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }
	
    @Override
    public int getBrightnessForRender(float p_189214_1_)
    {
        int i = super.getBrightnessForRender(p_189214_1_);
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int)(f * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }
    
	@SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
        	if (parameters.length == 0) return new ParticleConduit(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
        	
            return new ParticleConduit(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
        }
    }
}