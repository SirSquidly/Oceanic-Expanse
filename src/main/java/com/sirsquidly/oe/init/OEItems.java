package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

import com.sirsquidly.oe.items.*;

public class OEItems 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item TRIDENT_ORIG = new ItemTrident("trident");
	
	public static final Item SCUTE = new ItemBase("turtle_scute");
	
	public static final Item SHELLS = new ItemBase("shells");
	public static final Item NAUTILUS_SHELL = new ItemBase("nautilus_shell");
	public static final Item CONCH = new ItemConch("conch");
	public static final Item GLOW_INK = new ItemBase("glow_ink_sac");
	public static final Item CHLORINE = new ItemChlorine("chlorine");

	public static final Item SQUID_UNCOOKED = new ItemFoodBase("calamari_uncooked", 0, 0, 0, false);
	public static final Item SQUID_COOKED = new ItemFoodBase("calamari_cooked", 0, 0, 0, false);
	
	public static final Item COCONUT_OPEN = new ItemFoodBase("coconut_open", 3, 4.0F, 32, false);
	
	public static final Item DRIED_KELP = new ItemFoodBase("dried_kelp", 1, 0.6F, 16, false);
	
	public static final Item SPAWN_BUCKET = new ItemSpawnBucket("spawn_bucket");
}