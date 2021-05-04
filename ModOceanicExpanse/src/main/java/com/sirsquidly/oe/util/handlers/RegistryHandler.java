package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.init.OEEntities;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(OEItems.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onEnchantRegister(RegistryEvent.Register<Enchantment> event)
	{
		event.getRegistry().registerAll(OEEnchants.ENCHANTMENTS.toArray(new Enchantment[0]));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : OEItems.ITEMS)
		{
			Main.proxy.registerItemRenderer(item, 0, "inventory");
		}
	}
	
	@SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
        }
    }
	
	public static void preInitRegisteries(FMLPreInitializationEvent event)
	{
		OEEntities.registerEntities();
		RenderHandler.registerEntityRenders();
	}
	
}
