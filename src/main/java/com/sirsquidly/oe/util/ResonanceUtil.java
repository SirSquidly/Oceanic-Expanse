package com.sirsquidly.oe.util;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.enchantment.resonance.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.*;

/*
* This class handles the registry, and item checks of Resonances.
*
* The actual class used for functions is `Resonance`, which is extended for each new effect.
* */
public abstract class ResonanceUtil
{
    public static final Map<Integer, Resonance> REGISTRY = new HashMap<>();

    /** Resonances get registered to a list, so they can be checked. */
    public static void registerResonanceEffects()
    {
        ResonanceUtil.register(0, new ResonanceWaveWail(Main.MOD_ID + "." + "wave_wail", 8));
        ResonanceUtil.register(1, new ResonanceBrineBreath(Main.MOD_ID + "." + "brine_breath", 16));
        ResonanceUtil.register(2, new ResonanceCaptainCall(Main.MOD_ID + "." + "captain_call", 0, 4));
        ResonanceUtil.register(3, new ResonanceSeaSerenade(Main.MOD_ID + "." + "sea_serenade", 16));
    }

    public static void register(int id, Resonance effect)
    { REGISTRY.put(id, effect); }

    public static Resonance getEffectClass(int id)
    { return REGISTRY.get(id); }



    /** Returns the Resonance ID of a given itemstack. */
    @Nullable
    public static Integer getResonanceIDFromItem(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound tagCompound = stack.getTagCompound();

            if (tagCompound != null && tagCompound.hasKey("Resonance"))
            { return tagCompound.getInteger("Resonance"); }
        }
        return null;
    }

    /** Returns the Resonance of a given itemstack. */
    public static Resonance getResonance(ItemStack stack)
    {
        Integer resonance = ResonanceUtil.getResonanceIDFromItem(stack);
        if (resonance != null)
        { return ResonanceUtil.getEffectClass(resonance); }

        return null;
    }

    /** Assigns a random Resonance from the registry to the given ItemStack. */
    public static void addRandomResonance(ItemStack stack, Random rand)
    {
        if (REGISTRY.isEmpty()) return;
        List<Integer> keys = new ArrayList<>(REGISTRY.keySet());
        int randomId = keys.get(rand.nextInt(keys.size()));

        setResonance(stack, randomId);
    }

    /** Applies the given Resonance to the Itemstack. */
    public static void setResonance(ItemStack stack, int sound)
    {
        NBTTagCompound nbt;
        if (stack.hasTagCompound())
        { nbt = stack.getTagCompound(); }
        else
        { nbt = new NBTTagCompound(); }

        nbt.setInteger("Resonance", sound);
        stack.setTagCompound(nbt);
    }
}