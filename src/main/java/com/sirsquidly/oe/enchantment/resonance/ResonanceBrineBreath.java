package com.sirsquidly.oe.enchantment.resonance;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class ResonanceBrineBreath extends Resonance
{
    public ResonanceBrineBreath(String nameIn, int resonanceRangeIn)
    {
        super(nameIn, resonanceRangeIn);
    }

    @Override
    public void onUse(EntityLivingBase user, ItemStack item)
    {
        if (user.world.isRemote) return;
        super.onUse(user, item);
        Main.proxy.spawnParticle(3, user.world, user.posX, user.posY + user.getEyeHeight(), user.posZ, 0, 0.01, 0, 1);
        spawnResonanceManyParticles(user, 5, 80);
    }

    /** Throws nearby mobs away from the conch user. */
    @Override
    public void onEntitiesInRange(EntityLivingBase user, EntityLivingBase target, int range)
    {
        if (target.getDistanceSq(user) > range * range) return;

        target.addPotionEffect(new PotionEffect(OEPotions.DESCENT, 210, 0));
        /* Gives Slowness 2 for Non-Players, 1 for normal Players */
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 210, target instanceof EntityPlayer ? 0 : 1));

        spawnResonanceManyParticles(target, user.width, 20);
    }

    @Override
    public void spawnResonanceParticle(EntityLivingBase user, float distance)
    {
        double posX = (double)user.posX + ((user.getRNG().nextFloat() * 2 - 1) * distance);
        double posY = (double)user.posY + ((user.getRNG().nextFloat() * 2 - 1) * distance);
        double posZ = (double)user.posZ + ((user.getRNG().nextFloat() * 2 - 1) * distance);
        if (distance == user.width) posY += user.height/2;

        Main.proxy.spawnParticle(4, user.world, posX, posY, posZ, 0, -0.05F, 0, 1);
    }
}
