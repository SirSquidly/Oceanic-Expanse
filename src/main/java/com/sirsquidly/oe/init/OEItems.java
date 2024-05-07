package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.items.*;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@EventBusSubscriber(modid = Main.MOD_ID)
public class OEItems 
{
	public static final List<Item> itemList = new ArrayList<Item>();
	
	public static final ArmorMaterial TURTLE_ARMOR = EnumHelper.addArmorMaterial("turtle_armor", Main.MOD_ID + ":turtle_armor", 25, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	public static final ArmorMaterial HEAVY_ARMOR = EnumHelper.addArmorMaterial("heavy_armor", Main.MOD_ID + ":heavy_armor", 25, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	
	public static Item TURTLE_HELMET = new ItemTurtleArmor(TURTLE_ARMOR, 0, EntityEquipmentSlot.HEAD);
	public static Item HEAVY_BOOTS = new ItemHeavyBoots(HEAVY_ARMOR, 0, EntityEquipmentSlot.FEET);
	
	public static Item TRIDENT_ORIG = new ItemTrident();
	
	public static Item SCUTE = new Item();
	public static Item CHARM = new ItemCharm();
	
	public static Item SHELLS = new Item();
	public static Item NAUTILUS_SHELL = new Item();
	public static Item HEART_OF_THE_SEA = new Item();
	public static Item PEARL = new Item();
	public static Item CONCH = new ItemConch();
	public static Item GLOW_INK = new Item();
	public static Item GLOW_ITEM_FRAME = new ItemGlowItemFrame();
	public static Item BLEAK = new ItemBleak();
	public static Item SPONGE_CHUNK = new ItemSpongeChunk();
	public static Item SPONGE_CHUNK_WET = new ItemSpongeChunk();

	public static Item BLUE_SLIME_BALL = new ItemFoodBase(0, 0, false, 16).setAlwaysEdible();
	
	//public static Item SQUID_UNCOOKED = new ItemFoodBase(0, 0, 0, false);
	//public static Item SQUID_COOKED = new ItemFoodBase(0, 0, 0, false);
	
	public static Item CRAB_UNCOOKED = new ItemFoodBase(2, 0.4F, true);
	public static Item CRAB_COOKED = new ItemFoodBase(6, 0.6F, true);
	public static Item BISQUE_CRAB = new ItemFoodBase(10, 0.8F).setIsSoup();
	
	public static Item LOBSTER_UNCOOKED = new ItemFoodBase(3, 0.15F, true);
	public static Item LOBSTER_COOKED = new ItemFoodBase(8, 0.8F, true);
	public static Item BISQUE_LOBSTER = new ItemFoodBase(14, 0.8F).setIsSoup();
	
	public static Item CRUSTACEAN_SHELL = new Item();
	
	public static Item COCONUT_OPEN = new ItemFoodBase(3, 0.6F);
	
	public static Item DRIED_KELP = new ItemFoodBase(1, 0.6F, false, 16);
	public static Item DRIED_DULSE = new ItemFoodBase(3, 0.6F);
	
	public static Item PALM_BOAT = new ItemOEBoat();
	
	public static Item SPAWN_BUCKET = new ItemSpawnBucket();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		if (ConfigHandler.item.turtleShell.enableTurtleShell) itemReadyForRegister(TURTLE_HELMET, "turtle_helmet");
		if (ConfigHandler.item.trident.enableTrident) itemReadyForRegister(TRIDENT_ORIG, "trident");
		if (ConfigHandler.item.enableTurtleScute) itemReadyForRegister(SCUTE, "turtle_scute");
		itemReadyForRegister(SHELLS, "barnacle_shells");
		if (ConfigHandler.item.enableNautilusShell) itemReadyForRegister(NAUTILUS_SHELL, "nautilus_shell");
		itemReadyForRegister(HEART_OF_THE_SEA, "heart_of_the_sea");
		if (ConfigHandler.item.pearl.enablePearl) itemReadyForRegister(PEARL, "pearl");
		if (ConfigHandler.item.conch.enableConch) itemReadyForRegister(CONCH, "conch");
		if (ConfigHandler.item.conduitCharm.enableConduitCharm) itemReadyForRegister(CHARM, "charm");
		itemReadyForRegister(GLOW_INK, "glow_ink_sac");
		if (ConfigHandler.item.glowItemFrame.enableGlowItemFrame) itemReadyForRegister(GLOW_ITEM_FRAME, "glow_item_frame");
		itemReadyForRegister(BLEAK, "bleak");
		if (ConfigHandler.item.spongeChunk.enableSpongeChunk) itemReadyForRegister(SPONGE_CHUNK, "sponge_chunk");
		if (ConfigHandler.item.spongeChunk.enableSpongeChunk) itemReadyForRegister(SPONGE_CHUNK_WET, "sponge_chunk_wet");
		
		itemReadyForRegister(CRAB_UNCOOKED, "crab");
		itemReadyForRegister(CRAB_COOKED, "cooked_crab");
		if (ConfigHandler.item.bisque.enableCrabBisque) itemReadyForRegister(BISQUE_CRAB, "crab_bisque");
		itemReadyForRegister(CRUSTACEAN_SHELL, "crustacean_shell");
		
		itemReadyForRegister(LOBSTER_UNCOOKED, "lobster");
		itemReadyForRegister(LOBSTER_COOKED, "cooked_lobster");
		if (ConfigHandler.item.bisque.enableLobsterBisque) itemReadyForRegister(BISQUE_LOBSTER, "lobster_bisque");
		
		itemReadyForRegister(BLUE_SLIME_BALL, "blue_slime_ball");
		if (ConfigHandler.block.coconut.enableCoconut) itemReadyForRegister(COCONUT_OPEN, "coconut_open");
		if (ConfigHandler.block.enableKelp) itemReadyForRegister(DRIED_KELP, "dried_kelp");
		if (ConfigHandler.block.dulse.enableDulse) itemReadyForRegister(DRIED_DULSE, "dried_dulse");
		itemReadyForRegister(PALM_BOAT, "palm_boat");
		itemReadyForRegister(SPAWN_BUCKET, "spawn_bucket");
		if (ConfigHandler.item.heavyBoots.enableHeavyBoots) itemReadyForRegister(HEAVY_BOOTS, "heavy_boots");
		
		TURTLE_ARMOR.repairMaterial = new ItemStack(OEItems.SCUTE);
		HEAVY_ARMOR.repairMaterial = new ItemStack(OEItems.SHELLS);
		
		for (Item items : itemList) event.getRegistry().register(items);
	}
	
	public static Item itemReadyForRegister(Item item, String name)
	{
		if (name != null)
		{
			item.setTranslationKey(Main.MOD_ID + "." + name);
			item.setRegistryName(name);
		}
		item.setCreativeTab(Main.OCEANEXPTAB);
		
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