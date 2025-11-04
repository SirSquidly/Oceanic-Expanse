package com.sirsquidly.oe.util;

import com.sirsquidly.oe.capabilities.CapabilityNautilusCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class CapabilityUtil
{
    public static void alterChargeStrength(EntityPlayer playerIn, float shiftByIn)
    { alterChargeStrength(playerIn, shiftByIn, false, true); }

    public static void alterChargeStrength(EntityPlayer playerIn, float shiftByIn, boolean replaceWholeProgressIn, boolean sendPacket)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return;

        float newProgress = replaceWholeProgressIn ? shiftByIn : MathHelper.clamp(capCharge.getChargeStrength() + shiftByIn, 0.0F, 1.0F);

        if (newProgress != capCharge.getChargeStrength())
        {
            capCharge.setChargeStrength(newProgress);
        }
    }

    /** Sets if Space is being held. */
    public static void setHoldingSpace(EntityPlayer playerIn, boolean holdingSpaceIn)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return;

        capCharge.setHoldingSpace(holdingSpaceIn);
    }

    public static void setChargeCooldown(EntityPlayer playerIn, int cooldownIn)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return;

        capCharge.setChargeCooldownTimer(cooldownIn);
    }

    /** Gets the current Nautilus Charge Strength of the Player. */
    public static float getChargeStrength(EntityPlayer playerIn)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return 0;
        return capCharge.getChargeStrength();
    }

    /** Gets the current Nautilus Charge Cooldown of the Player. */
    public static int getChargeCooldown(EntityPlayer playerIn)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return 0;
        return capCharge.getChargeCooldownTimer();
    }

    /** If the Player happens to be Holding Space. */
    public static boolean getHoldingSpace(EntityPlayer playerIn)
    {
        CapabilityNautilusCharge.ICapabilityNautilusCharge capCharge = getNautilusChargeCapability(playerIn);
        if (capCharge == null) return false;
        return capCharge.getHoldingSpace();
    }


    public static CapabilityNautilusCharge.ICapabilityNautilusCharge getNautilusChargeCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityNautilusCharge.NAUTILUS_CHARGE_CAP, null))
        { return playerIn.getCapability(CapabilityNautilusCharge.NAUTILUS_CHARGE_CAP, null); }

        return null;
    }
}