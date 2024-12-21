package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.Main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler 
{
	public static final ResourceLocation ENTITIES_COD = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/cod"));
	public static final ResourceLocation ENTITIES_SALMON = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/salmon"));
	public static final ResourceLocation ENTITIES_PUFFERFISH = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/pufferfish"));
	public static final ResourceLocation ENTITIES_TROPICAL_FISH = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/tropical_fish"));
	public static final ResourceLocation ENTITIES_TURTLE = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/turtle"));
	public static final ResourceLocation ENTITIES_GLOW_SQUID = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/glow_squid"));
	public static final ResourceLocation ENTITIES_CRAB = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/crab"));
	public static final ResourceLocation ENTITIES_DOLPHIN = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/dolphin"));

	public static final ResourceLocation ENTITIES_DROWNED = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/drowned"));
	public static final ResourceLocation ENTITIES_DROWNED_CAPTAIN = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/drowned_captain"));
	public static final ResourceLocation ENTITIES_PICKLED = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/pickled"));
	public static final ResourceLocation ENTITIES_TROPICAL_SLIME = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/tropical_slime"));
	public static final ResourceLocation ENTITIES_LOBSTER = LootTableList.register(new ResourceLocation(Main.MOD_ID, "entities/lobster"));
	
	
	public static final ResourceLocation GAMEPLAY_CRAB_DIG_GRAVEL = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/gravel"));
	public static final ResourceLocation GAMEPLAY_CRAB_DIG_RED_SAND = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/red_sand"));
	public static final ResourceLocation GAMEPLAY_CRAB_DIG_SAND = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/sand"));
	public static final ResourceLocation GAMEPLAY_CRAB_DIG_SHELLY_SAND = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/shelly_sand"));
	public static final ResourceLocation GAMEPLAY_CRAB_DIG_SOUL_SAND = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/soul_sand"));

	public static final ResourceLocation GAMEPLAY_SHELL_COMB = LootTableList.register(new ResourceLocation(Main.MOD_ID, "gameplay/shell_sand"));
	
	public static final ResourceLocation SHIPWRECK_SUPPLY = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/shipwreck_supply"));
	public static final ResourceLocation SHIPWRECK_MAP = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/shipwreck_map"));
	public static final ResourceLocation SHIPWRECK_TREASURE = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/shipwreck_treasure"));
	
	//public static final ResourceLocation MONUMENT_TREASURE = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/monument_treasure"));
	public static final ResourceLocation MONUMENT_MYSTIC = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/monument_mystic"));
}