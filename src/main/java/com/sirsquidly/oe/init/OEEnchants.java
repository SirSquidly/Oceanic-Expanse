package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sirsquidly.oe.enchantment.*;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@EventBusSubscriber
public class OEEnchants 
{
	public static final List<Enchantment> ENCHANTMENTS = new ArrayList<Enchantment>();
	
	public static final Enchantment IMPALING = new EnchantmentImpaling(Enchantment.Rarity.RARE, 0);
	public static final Enchantment WATER_JET = new EnchantmentImpaling(Enchantment.Rarity.RARE, 1);
	
	public static final Enchantment LOYALTY = new EnchantmentLoyalty(Enchantment.Rarity.UNCOMMON);
	public static final Enchantment CHANNELING = new EnchantmentChanneling(Enchantment.Rarity.UNCOMMON);
	public static final Enchantment RIPTIDE = new EnchantmentRiptide(Enchantment.Rarity.UNCOMMON);
	
	@SubscribeEvent
	public static void onEnchantRegister(RegistryEvent.Register<Enchantment> event)
	{
		if (ConfigHandler.enchant.impaling.enableImpalingEnchant) event.getRegistry().registerAll(OEEnchants.IMPALING);
		if (ConfigHandler.enchant.impaling.enableWaterJet == 1) event.getRegistry().registerAll(OEEnchants.WATER_JET);
		
		if (ConfigHandler.enchant.channeling.enableChannelingEnchant) event.getRegistry().registerAll(OEEnchants.ENCHANTMENTS.toArray(new Enchantment[0]));
	}
}