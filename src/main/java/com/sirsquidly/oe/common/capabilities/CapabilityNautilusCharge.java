package com.sirsquidly.oe.common.capabilities;

import com.sirsquidly.oe.Main;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityNautilusCharge
{
    @CapabilityInject(ICapabilityNautilusCharge.class)
    public static Capability<ICapabilityNautilusCharge> NAUTILUS_CHARGE_CAP;
    public static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "nautilusCharge");

    /** A float representing the strength of the Charge. */
    private static final String STRENGTH_TAG = "chargeStrength";
    /** The max tick that the entity is expected to fall by. */
    private static final String COOLDOWN_TAG = "chargeCooldown";
    /** If the Player is holding Space. */
    private static final String SPACE_TAG = "holdingSpace";

    public interface ICapabilityNautilusCharge
    {
        int getChargeCooldownTimer();
        void setChargeCooldownTimer(int value);

        float getChargeStrength();
        void setChargeStrength(float value);

        boolean getHoldingSpace();
        void setHoldingSpace(boolean value);
    }

    public static class NautilusChargeMethods implements ICapabilityNautilusCharge
    {
        private int chargeCooldownTimer = 0;
        private float chargeStrength = 0;
        private boolean holdingSpace = false;

        @Override
        public int getChargeCooldownTimer()
        { return chargeCooldownTimer; }

        @Override
        public void setChargeCooldownTimer(int value)
        { chargeCooldownTimer = value; }

        @Override
        public float getChargeStrength()
        { return chargeStrength; }

        @Override
        public void setChargeStrength(float value)
        { chargeStrength = value; }

        @Override
        public boolean getHoldingSpace()
        { return holdingSpace; }

        @Override
        public void setHoldingSpace(boolean value)
        { holdingSpace = value; }
    }

    public static class Storage implements Capability.IStorage<ICapabilityNautilusCharge>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityNautilusCharge> capability, ICapabilityNautilusCharge instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(COOLDOWN_TAG, instance.getChargeCooldownTimer());
            compound.setFloat(STRENGTH_TAG, instance.getChargeStrength());
            compound.setBoolean(SPACE_TAG, instance.getHoldingSpace());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityNautilusCharge> capability, ICapabilityNautilusCharge instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setChargeCooldownTimer(compound.getInteger(COOLDOWN_TAG));
            instance.setChargeStrength(compound.getFloat(STRENGTH_TAG));
            instance.setHoldingSpace(compound.getBoolean(SPACE_TAG));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityNautilusCharge> capability;
        final EnumFacing facing;
        final ICapabilityNautilusCharge instance;

        public Provider(final ICapabilityNautilusCharge instance, final Capability<ICapabilityNautilusCharge> capability, @Nullable final EnumFacing facing)
        {
            this.instance = instance;
            this.capability = capability;
            this.facing = facing;
        }

        @Override
        public boolean hasCapability(@Nullable final Capability<?> capability, final EnumFacing facing)
        { return capability == getCapability(); }

        @Override
        public <T> T getCapability(@Nullable Capability<T> capability, EnumFacing facing)
        { return capability == getCapability() ? getCapability().cast(this.instance) : null; }

        final Capability<ICapabilityNautilusCharge> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityNautilusCharge getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}