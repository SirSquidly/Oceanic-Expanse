package com.sirsquidly.oe.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.Loader;

public class PotionDolphinsGrace extends PotionBase
{
    double amount = 1.0D;

    public PotionDolphinsGrace(String name, boolean isBadEffectIn, int liquidColorIn, int icon)
    {
        super(name, isBadEffectIn, liquidColorIn, icon);

        this.registerPotionAttributeModifier(EntityLivingBase.SWIM_SPEED, "020E0DFB-87AE-4653-9556-831010E291A0", amount, 0);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        if(entity.isInWater() || entity.isInLava())
        {
            if (entity.motionY > 0.0 || (Loader.isModLoaded("aquaacrobatics") && entity.motionY < 0.0))
            {
                entity.motionY *= 0.75;
            }
        }
    }
}