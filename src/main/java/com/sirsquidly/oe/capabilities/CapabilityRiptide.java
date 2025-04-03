package com.sirsquidly.oe.capabilities;

import com.sirsquidly.oe.Main;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityRiptide
{
    @CapabilityInject(ICapabilityRiptide.class)
    public static Capability<ICapabilityRiptide> RIPTIDE_CAP;
    public static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "riptideTime");

    /** Used for Riptide Animations. */
    private static final String BOOLEAN_TAG = "riptideAnimate";
    /** The max tick that the entity is expected to fall by. */
    private static final String TIMER_TAG = "riptideTimer";

    public interface ICapabilityRiptide
    {
        boolean getRiptideAnimate();
        void setRiptideAnimate(boolean value);

        int getRiptideTimer();
        void setRiptideTimer(int value);
    }

    public static class RiptideMethods implements ICapabilityRiptide
    {
        private boolean getRiptideAnimate = false;
        private int getRiptideTime = 0;

        @Override
        public boolean getRiptideAnimate()
        { return getRiptideAnimate; }

        @Override
        public void setRiptideAnimate(boolean value)
        { getRiptideAnimate = value; }

        @Override
        public int getRiptideTimer()
        { return getRiptideTime; }

        @Override
        public void setRiptideTimer(int value)
        { getRiptideTime = value; }
    }

    public static class Storage implements Capability.IStorage<ICapabilityRiptide>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityRiptide> capability, ICapabilityRiptide instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(BOOLEAN_TAG, instance.getRiptideAnimate());
            compound.setInteger(TIMER_TAG, instance.getRiptideTimer());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityRiptide> capability, ICapabilityRiptide instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setRiptideAnimate(compound.getBoolean(BOOLEAN_TAG));
            instance.setRiptideTimer(compound.getInteger(TIMER_TAG));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityRiptide> capability;
        final EnumFacing facing;
        final ICapabilityRiptide instance;

        public Provider(final ICapabilityRiptide instance, final Capability<ICapabilityRiptide> capability, @Nullable final EnumFacing facing)
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

        final Capability<ICapabilityRiptide> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityRiptide getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}