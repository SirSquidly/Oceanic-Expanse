package com.sirsquidly.oe.enchantment.resonance;

import com.sirsquidly.oe.Main;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ResonanceWaveWail extends Resonance
{
    public ResonanceWaveWail(String nameIn, int resonanceRangeIn)
    {
        super(nameIn, resonanceRangeIn);
    }

    @Override
    public void onUse(EntityLivingBase user, ItemStack item)
    {
        if (user.world.isRemote) return;
        super.onUse(user, item);
        Main.proxy.spawnParticle(3, user.world, user.posX, user.posY + user.getEyeHeight(), user.posZ, 0, 0.01, 0, 0);
        spawnResonanceManyParticles(user, user.width, 80);
    }

    /** Throws nearby mobs away from the conch user. */
    @Override
    public void onEntitiesInRange(EntityLivingBase user, EntityLivingBase target, int range)
    {
        double knockbackResist = 1.0D - ((EntityLivingBase)target).getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();

        double d12 = target.getDistance(target.posX, target.posY, target.posZ) / (double) range;
        if (d12 <= 1.0)
        {
            double dx = target.posX - user.posX;
            double dy = target.posY + (target.height / 2) - user.posY + (user.height / 2);
            double dz = target.posZ - user.posZ;

            double reCheckDistance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
            if (reCheckDistance != 0.0)
            {
                dx /= reCheckDistance;
                dy /= reCheckDistance;
                dz /= reCheckDistance;
                double kmult = (1.1 - d12);

                target.motionX += dx * kmult * knockbackResist;
                target.motionY += dy * kmult * knockbackResist;
                target.motionZ += dz * kmult * knockbackResist;
                target.velocityChanged = true;
            }
        }
    }

    @Override
    public void spawnResonanceParticle(EntityLivingBase user, float distance)
    {
        double posX = (double)user.posX + ((user.getRNG().nextFloat() * 2 - 1) * distance);
        double posY = (double)user.posY + user.height/2 + ((user.getRNG().nextFloat() * 2 - 1) * user.height);
        double posZ = (double)user.posZ + ((user.getRNG().nextFloat() * 2 - 1) * distance);

        Main.proxy.spawnParticle(4, user.world, posX, posY, posZ, (user.getRNG().nextFloat() * 2 - 1) * 0.4F, 0.04F, (user.getRNG().nextFloat() * 2 - 1) * 0.4F, 0);
    }
}
