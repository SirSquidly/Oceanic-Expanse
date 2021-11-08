package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;

import com.sirsquidly.oe.enchantment.*;

public class OEEnchants 
{
	public static final List<Enchantment> ENCHANTMENTS = new ArrayList<Enchantment>();
	
	public static final Enchantment IMPALING = new EnchantmentImpaling(Enchantment.Rarity.RARE, 0);
	public static final Enchantment WATER_JET = new EnchantmentImpaling(Enchantment.Rarity.RARE, 1);
	
	public static final Enchantment LOYALTY = new EnchantmentLoyalty(Enchantment.Rarity.UNCOMMON);
	public static final Enchantment CHANNELING = new EnchantmentChanneling(Enchantment.Rarity.UNCOMMON);
	public static final Enchantment RIPTIDE = new EnchantmentRiptide(Enchantment.Rarity.UNCOMMON);
}
