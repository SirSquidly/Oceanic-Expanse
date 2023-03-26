package com.sirsquidly.oe.util.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import com.sirsquidly.oe.util.Reference;

public class LootTableHandler 
{
	public static final ResourceLocation ENTITIES_COD = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/cod"));
	public static final ResourceLocation ENTITIES_SALMON = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/salmon"));
	public static final ResourceLocation ENTITIES_PUFFERFISH = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/pufferfish"));
	public static final ResourceLocation ENTITIES_TROPICAL_FISH = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/tropical_fish"));
	public static final ResourceLocation ENTITIES_TURTLE = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/turtle"));
	public static final ResourceLocation ENTITIES_GLOW_SQUID = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/glow_squid"));
	public static final ResourceLocation ENTITIES_CRAB = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/crab"));
	
	public static final ResourceLocation ENTITIES_DROWNED = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/drowned"));
	public static final ResourceLocation ENTITIES_PICKLED = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/pickled"));
	
	public static final ResourceLocation GAMEPLAY_CRAB_DIG = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "gameplay/crab_dig"));
	public static final ResourceLocation GAMEPLAY_SHELL_COMB = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "gameplay/shell_sand"));
	
	public static final ResourceLocation SHIPWRECK_SUPPLY = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "chests/shipwreck_supply"));
	public static final ResourceLocation SHIPWRECK_MAP = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "chests/shipwreck_map"));
	public static final ResourceLocation SHIPWRECK_TREASURE = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "chests/shipwreck_treasure"));
}