package com.sirsquidly.oe.client.particle;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleInk extends ParticleSmokeNormal
{
	protected ParticleInk(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn)
	{ this(worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, 2.5F); }
	
	protected ParticleInk(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn, float scale)
	{ this(worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, scale, 0, 0, 0); }
	
	protected ParticleInk(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn, float scale, float red, float green, float blue)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, scale);
		
		this.particleMaxAge = ConfigHandler.vanillaTweak.squidInking.inkParticleAge + this.rand.nextInt(12);
		this.particleGravity = 0.4F;
		this.canCollide = false;
		
		this.particleRed = red * 0.00392156862F;
        this.particleGreen = green * 0.00392156862F;
        this.particleBlue = blue * 0.00392156862F;
	}
	 
	public void onUpdate()
    {
        super.onUpdate();
        
        if (this.particleAge > this.particleMaxAge / 2) this.setAlphaF(1.0F - ((float)this.particleAge - (float)(this.particleMaxAge / 2) / (float)this.particleMaxAge));
        
        this.motionY -= 0.002D + 0.02D * (double)this.particleGravity;
    }
	
	@SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
        	switch (parameters.length)
        	{
        		case 0:
        			return new ParticleInk(world, posX, posY, posZ, speedX, speedY, speedZ);
        		case 1:
        			return new ParticleInk(world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
        		case 4:
        			return new ParticleInk(world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1], parameters[2], parameters[3]);
        	}
			return null;
        }
    }
}
