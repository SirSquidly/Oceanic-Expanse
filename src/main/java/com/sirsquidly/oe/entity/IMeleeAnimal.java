package com.sirsquidly.oe.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

/** 
 * Used for various neutral entities to be able to attack, some of which need enchantments and tools to function
 *
 * Mostly taken from `EntityMob` with minor alterations
 */
public interface IMeleeAnimal
{
	default boolean normalAttack(EntityLivingBase attacker, Entity target)
	{ return normalAttack(attacker, target, true); }

	default boolean normalAttack(EntityLivingBase attacker, Entity target, boolean applyItemAttributes)
	{
		float f = (float)attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int knockback = 0;

		if (target instanceof EntityLivingBase && applyItemAttributes)
		{
			f += EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), ((EntityLivingBase)target).getCreatureAttribute());
			knockback += EnchantmentHelper.getKnockbackModifier(attacker);
		}

		boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(attacker), f);

		if (flag && applyItemAttributes)
		{
			if (knockback > 0 && target instanceof EntityLivingBase)
			{
				((EntityLivingBase)target).knockBack(attacker, (float)knockback * 0.5F, MathHelper.sin(attacker.rotationYaw * 0.017453292F), -MathHelper.cos(attacker.rotationYaw * 0.017453292F));
				target.motionX *= 0.6D;
				target.motionZ *= 0.6D;
			}

			int fireAspect = EnchantmentHelper.getFireAspectModifier(attacker);

			if (fireAspect > 0)
			{ target.setFire(fireAspect * 4); }

			if (target instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)target;

				ItemStack attackerItem = attacker.getHeldItemMainhand();
				ItemStack playerUsingItem = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!attackerItem.isEmpty() && !playerUsingItem.isEmpty() && attackerItem.getItem().canDisableShield(attackerItem, playerUsingItem, entityplayer, attacker) && playerUsingItem.getItem().isShield(playerUsingItem, entityplayer))
				{
					float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(attacker) * 0.05F;

					if (attacker.world.rand.nextFloat() < f1)
					{
						entityplayer.getCooldownTracker().setCooldown(playerUsingItem.getItem(), 100);
						attacker.world.setEntityState(entityplayer, (byte)30);
					}
				}
			}

			if (target instanceof EntityLivingBase) EnchantmentHelper.applyThornEnchantments((EntityLivingBase)target, attacker);
			EnchantmentHelper.applyArthropodEnchantments(attacker, target);
		}
		return flag;
	}
}