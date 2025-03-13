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
	
	public static final Enchantment MOBSTOMP = new EnchantmentMobStomp(Enchantment.Rarity.UNCOMMON);
	public static final Enchantment REBOUND = new EnchantmentRebound(Enchantment.Rarity.UNCOMMON);
	
	@SubscribeEvent
	public static void onEnchantRegister(RegistryEvent.Register<Enchantment> event)
	{
		if (ConfigHandler.enchant.impaling.enableImpalingEnchant) event.getRegistry().register(OEEnchants.IMPALING);
		if (ConfigHandler.enchant.impaling.enableWaterJet == 1) event.getRegistry().register(OEEnchants.WATER_JET);
		
		if (ConfigHandler.enchant.loyalty.enableLoyaltyEnchant) event.getRegistry().register(LOYALTY);
		
		if (ConfigHandler.enchant.channeling.enableChannelingEnchant) event.getRegistry().register(CHANNELING);
		if (ConfigHandler.enchant.riptide.enableRiptideEnchant) event.getRegistry().register(RIPTIDE);
		
		event.getRegistry().register(OEEnchants.MOBSTOMP);
		event.getRegistry().register(OEEnchants.REBOUND);
	}
}