package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.items.*;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class OEItems 
{
	public static final List<Item> itemList = new ArrayList<Item>();
	
	public static final ArmorMaterial TURTLE_ARMOR = EnumHelper.addArmorMaterial("turtle_armor", Reference.MOD_ID + ":turtle_armor", 25, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	public static final ArmorMaterial HEAVY_ARMOR = EnumHelper.addArmorMaterial("heavy_armor", Reference.MOD_ID + ":heavy_armor", 25, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	
	public static Item TURTLE_HELMET = new ItemTurtleArmor(TURTLE_ARMOR, 0, EntityEquipmentSlot.HEAD);
	public static Item HEAVY_BOOTS = new ItemHeavyBoots(HEAVY_ARMOR, 0, EntityEquipmentSlot.FEET);
	
	public static Item TRIDENT_ORIG = new ItemTrident();
	
	public static Item SCUTE = new ItemBase();
	public static Item CHARM = new ItemCharm();
	
	public static Item SHELLS = new ItemBase();
	public static Item NAUTILUS_SHELL = new ItemBase();
	public static Item HEART_OF_THE_SEA = new ItemBase();
	public static Item CONCH = new ItemConch();
	public static Item GLOW_INK = new ItemBase();
	public static Item CHLORINE = new ItemChlorine();

	public static Item SQUID_UNCOOKED = new ItemFoodBase(0, 0, 0, false);
	public static Item SQUID_COOKED = new ItemFoodBase(0, 0, 0, false);
	
	public static Item COCONUT_OPEN = new ItemFoodBase(3, 4.0F, 32, false);
	
	public static Item DRIED_KELP = new ItemFoodBase(1, 0.6F, 16, false);
	public static Item DRIED_DULSE = new ItemFoodBase(3, 3.6F, 32, false);
	
	public static Item SPAWN_BUCKET = new ItemSpawnBucket();
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Item> event)
	{
		if (ConfigHandler.item.turtleShell.enableTurtleShell) itemReadyForRegister(TURTLE_HELMET, "turtle_helmet");
		if (ConfigHandler.item.trident.enableTrident) itemReadyForRegister(TRIDENT_ORIG, "trident");
		itemReadyForRegister(SCUTE, "turtle_scute");
		itemReadyForRegister(SHELLS, "barnacle_shells");
		itemReadyForRegister(NAUTILUS_SHELL, "nautilus_shell");
		itemReadyForRegister(HEART_OF_THE_SEA, "heart_of_the_sea");
		itemReadyForRegister(CONCH, "conch");
		itemReadyForRegister(CHARM, "charm");
		itemReadyForRegister(GLOW_INK, "glow_ink_sac");
		itemReadyForRegister(CHLORINE, "chlorine");
		itemReadyForRegister(SQUID_UNCOOKED, "calamari_uncooked");
		itemReadyForRegister(SQUID_COOKED, "calamari_cooked");
		itemReadyForRegister(COCONUT_OPEN, "coconut_open");
		itemReadyForRegister(DRIED_KELP, "dried_kelp");
		itemReadyForRegister(DRIED_DULSE, "dried_dulse");
		itemReadyForRegister(SPAWN_BUCKET, "spawn_bucket");
		itemReadyForRegister(HEAVY_BOOTS, "heavy_boots");
		
		for (Item items : itemList) event.getRegistry().register(items);
	}
	
	public static Item itemReadyForRegister(Item item, String name)
	{
		if (name != null)
		{
			item.setUnlocalizedName(name);
			item.setRegistryName(name);
		}
		
		itemList.add(item);

		return item;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for (Item items : itemList) Main.proxy.registerItemRenderer(items, 0, "inventory");
	}
}