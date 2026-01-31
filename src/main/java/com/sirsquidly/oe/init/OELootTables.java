package com.sirsquidly.oe.init;

import com.sirsquidly.oe.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class OELootTables
{
    public static final ResourceLocation ENTITIES_COD = new ResourceLocation(Main.MOD_ID, "entities/cod");
    public static final ResourceLocation ENTITIES_SALMON = new ResourceLocation(Main.MOD_ID, "entities/salmon");
    public static final ResourceLocation ENTITIES_PUFFERFISH = new ResourceLocation(Main.MOD_ID, "entities/pufferfish");
    public static final ResourceLocation ENTITIES_TROPICAL_FISH = new ResourceLocation(Main.MOD_ID, "entities/tropical_fish");
    public static final ResourceLocation ENTITIES_TURTLE = new ResourceLocation(Main.MOD_ID, "entities/turtle");
    public static final ResourceLocation ENTITIES_GLOW_SQUID = new ResourceLocation(Main.MOD_ID, "entities/glow_squid");
    public static final ResourceLocation ENTITIES_CRAB = new ResourceLocation(Main.MOD_ID, "entities/crab");
    public static final ResourceLocation ENTITIES_DOLPHIN = new ResourceLocation(Main.MOD_ID, "entities/dolphin");
    public static final ResourceLocation ENTITIES_LOBSTER = new ResourceLocation(Main.MOD_ID, "entities/lobster");

    public static final ResourceLocation ENTITIES_DROWNED = new ResourceLocation(Main.MOD_ID, "entities/drowned");
    public static final ResourceLocation ENTITIES_DROWNED_CAPTAIN = new ResourceLocation(Main.MOD_ID, "entities/drowned_captain");
    public static final ResourceLocation ENTITIES_PICKLED = new ResourceLocation(Main.MOD_ID, "entities/pickled");
    public static final ResourceLocation ENTITIES_TROPICAL_SLIME = new ResourceLocation(Main.MOD_ID, "entities/tropical_slime");

    public static final ResourceLocation GAMEPLAY_CRAB_DIG_GRAVEL = new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/gravel");
    public static final ResourceLocation GAMEPLAY_CRAB_DIG_RED_SAND = new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/red_sand");
    public static final ResourceLocation GAMEPLAY_CRAB_DIG_SAND = new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/sand");
    public static final ResourceLocation GAMEPLAY_CRAB_DIG_SHELLY_SAND = new ResourceLocation(Main.MOD_ID, "gameplay/crab_dig/shelly_sand");

    public static final ResourceLocation GAMEPLAY_LOBSTER_MOLT = new ResourceLocation(Main.MOD_ID, "gameplay/lobster_molt");
    public static final ResourceLocation GAMEPLAY_SHELL_COMB = new ResourceLocation(Main.MOD_ID, "gameplay/shell_sand");

    public static final ResourceLocation SHIPWRECK_SUPPLY = new ResourceLocation(Main.MOD_ID, "chests/shipwreck_supply");
    public static final ResourceLocation SHIPWRECK_MAP = new ResourceLocation(Main.MOD_ID, "chests/shipwreck_map");
    public static final ResourceLocation SHIPWRECK_TREASURE = new ResourceLocation(Main.MOD_ID, "chests/shipwreck_treasure");

    public static final ResourceLocation UNDERWATER_RUINS_SMALL = new ResourceLocation(Main.MOD_ID, "chests/ruins_small");

    //public static final ResourceLocation MONUMENT_TREASURE = LootTableList.register(new ResourceLocation(Main.MOD_ID, "chests/monument_treasure"));
    public static final ResourceLocation MONUMENT_MYSTIC = new ResourceLocation(Main.MOD_ID, "chests/monument_mystic");

    public static void registerLootTables()
    {
        LootTableList.register(ENTITIES_COD);
        LootTableList.register(ENTITIES_SALMON);
        LootTableList.register(ENTITIES_PUFFERFISH);
        LootTableList.register(ENTITIES_TROPICAL_FISH);
        LootTableList.register(ENTITIES_TURTLE);
        LootTableList.register(ENTITIES_GLOW_SQUID);
        LootTableList.register(ENTITIES_CRAB);
        LootTableList.register(ENTITIES_DOLPHIN);
        LootTableList.register(ENTITIES_LOBSTER);

        LootTableList.register(ENTITIES_DROWNED);
        LootTableList.register(ENTITIES_DROWNED_CAPTAIN);
        LootTableList.register(ENTITIES_PICKLED);
        LootTableList.register(ENTITIES_TROPICAL_SLIME);

        LootTableList.register(GAMEPLAY_CRAB_DIG_GRAVEL);
        LootTableList.register(GAMEPLAY_CRAB_DIG_RED_SAND);
        LootTableList.register(GAMEPLAY_CRAB_DIG_SAND);
        LootTableList.register(GAMEPLAY_CRAB_DIG_SHELLY_SAND);

        LootTableList.register(GAMEPLAY_LOBSTER_MOLT);
        LootTableList.register(GAMEPLAY_SHELL_COMB);

        LootTableList.register(SHIPWRECK_SUPPLY);
        LootTableList.register(SHIPWRECK_MAP);
        LootTableList.register(SHIPWRECK_TREASURE);

        LootTableList.register(UNDERWATER_RUINS_SMALL);
        LootTableList.register(MONUMENT_MYSTIC);
    }
}