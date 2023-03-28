package com.sirsquidly.oe.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ParticleTypes
{
    CONDUIT,
    GLOW,
    INK;

    public int getId()
    {
        return this.ordinal();
    }

    @SideOnly(Side.CLIENT)
    public static IParticleFactory getFactory(int particleId)
    {
        switch(particleId)
        {
            default:
            case 0:
                return new ParticleConduit.Factory();
            case 1:
                return new ParticleGlow.Factory();
            case 2:
                return new ParticleInk.Factory();
        }
    }
}