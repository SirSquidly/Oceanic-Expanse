package com.sirsquidly.oe.init;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.*;
import com.sirsquidly.oe.entity.item.*;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import static com.sirsquidly.oe.util.handlers.ConfigArrayHandler.getSpawnBiomes;

public class OEEntities
{
	public static int id;

	public static void registerEntities()
	{
		registerEntity("glow_squid", EntityGlowSquid.class, ++id, 100, 2243405, 8454080);
		if (ConfigHandler.entity.cod.enableCod) registerEntity("cod", EntityCod.class, ++id, 64, 8564132, 11386587);
		if (ConfigHandler.entity.salmon.enableSalmon) registerEntity("salmon", EntitySalmon.class, ++id, 64, 12860482, 5268308);
		if (ConfigHandler.entity.tropicalFish.enableTropicalFish) registerEntity("tropical_fish", EntityTropicalFish.class, ++id, 64, 16019232, 15724527);
		if (ConfigHandler.entity.pufferfish.enablePufferfish) registerEntity("pufferfish", EntityPufferfish.class, ++id, 64, 15453241, 4365242);
		if (ConfigHandler.entity.turtle.enableTurtle) registerEntity("turtle", EntityTurtle.class, ++id, 64, 12763520, 4702026);
		if (ConfigHandler.entity.dolphin.enableDolphin) registerEntity("dolphin", EntityDolphin.class, ++id, 64, 5265784, 14213610);
		if (ConfigHandler.entity.crab.enableCrab) registerEntity("crab", EntityCrab.class, ++id, 64, 11765373, 14894652);
		if (ConfigHandler.entity.clam.enableClam) registerEntity("clam", EntityClam.class, ++id, 64, 14327661, 15701910);
		if (ConfigHandler.entity.lobster.enableLobster) registerEntity("lobster", EntityLobster.class, ++id, 64, 7425351, 14894652);
		
		if (ConfigHandler.entity.babySquid.enableBabySquid) registerEntity("baby_squid", EntityBabySquid.class, ++id, 100, 3696778, 10531777);
		if (ConfigHandler.entity.babyGlowSquid.enableBabyGlowSquid) registerEntity("baby_glow_squid", EntityBabyGlowSquid.class, ++id, 100, 3696778, 10944467);
		
		if (ConfigHandler.entity.drowned.enableDrowned) registerEntity("drowned", EntityDrowned.class, ++id, 80, 5609880, 12434265);
		if (ConfigHandler.entity.tropicalSlime.enableTropicalSlime) registerEntity("tropical_slime", EntityTropicalSlime.class, ++id, 64, 8498384, 7381193);
		
		registerEntity("palm_boat", EntityOEBoat.class, ++id, 100);
		registerEntity("glow_item_frame", EntityGlowItemFrame.class, ++id, 100);
		if (ConfigHandler.entity.pickled.enablePickled) registerEntity("pickled", EntityPickled.class, ++id, 80, 8223277, 14221270);
		registerEntity("drowned_summon", EntityDrownedSummon.class, ++id, 80);

		registerEntity("coconut", EntityFallingCoconut.class, ++id, 20);
		registerEntity("conduit_eye", EntityConduitEye.class, ++id, 80);
		registerEntity("trident", EntityTrident.class, ++id, 80);
		registerEntity("underwater_tnt", EntityUnderwaterTNTPrimed.class, ++id, 80);
	}

	public static void registerEntitySpawns()
	{
		/* Spawn Weight needs to be high due to the height limitation of the spawning */
		buildSpawnsFromConfig(EntityGlowSquid.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.glowSquid.spawning.spawnWeight, 2, 4, ConfigHandler.entity.glowSquid.spawning.biomes, ConfigHandler.entity.glowSquid.spawning.biomesAsBlacklist);

		if (ConfigHandler.entity.cod.enableCod) buildSpawnsFromConfig(EntityCod.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.cod.spawning.spawnWeight, 3,7, ConfigHandler.entity.cod.spawning.biomes, ConfigHandler.entity.cod.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.dolphin.enableDolphin) buildSpawnsFromConfig(EntityDolphin.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.dolphin.spawning.spawnWeight, 3, 5, ConfigHandler.entity.dolphin.spawning.biomes, ConfigHandler.entity.dolphin.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.drowned.enableDrowned)
		{
			buildSpawnsFromConfig(EntityDrowned.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.drowned.spawning.spawningOcean.spawnWeight, 2, 4, ConfigHandler.entity.drowned.spawning.spawningOcean.biomes, ConfigHandler.entity.drowned.spawning.spawningOcean.biomesAsBlacklist);
			buildSpawnsFromConfig(EntityDrowned.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.drowned.spawning.spawningRiver.spawnWeight, 2, 4, ConfigHandler.entity.drowned.spawning.spawningRiver.biomes, ConfigHandler.entity.drowned.spawning.spawningRiver.biomesAsBlacklist);
		}
		if (ConfigHandler.entity.pufferfish.enablePufferfish) buildSpawnsFromConfig(EntityPufferfish.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.pufferfish.spawning.spawnWeight, 1, 5, ConfigHandler.entity.pufferfish.spawning.biomes, ConfigHandler.entity.pufferfish.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.salmon.enableSalmon) buildSpawnsFromConfig(EntitySalmon.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.salmon.spawning.spawningOcean.spawnWeight, 1, 5, ConfigHandler.entity.salmon.spawning.spawningOcean.biomes, ConfigHandler.entity.salmon.spawning.spawningOcean.biomesAsBlacklist);
		if (ConfigHandler.entity.salmon.enableSalmon) buildSpawnsFromConfig(EntitySalmon.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.salmon.spawning.spawningRiver.spawnWeight, 1, 5, ConfigHandler.entity.salmon.spawning.spawningRiver.biomes, ConfigHandler.entity.salmon.spawning.spawningRiver.biomesAsBlacklist);
		if (ConfigHandler.entity.tropicalFish.enableTropicalFish) buildSpawnsFromConfig(EntityTropicalFish.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.tropicalFish.spawning.spawnWeight, 8,8, ConfigHandler.entity.tropicalFish.spawning.biomes, ConfigHandler.entity.tropicalFish.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.turtle.enableTurtle) buildSpawnsFromConfig(EntityTurtle.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.turtle.spawning.spawnWeight, 2, 6, ConfigHandler.entity.turtle.spawning.biomes, ConfigHandler.entity.turtle.spawning.biomesAsBlacklist);


		if (ConfigHandler.entity.clam.enableClam) buildSpawnsFromConfig(EntityClam.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.clam.spawning.spawnWeight, 1, 1, ConfigHandler.entity.clam.spawning.biomes, ConfigHandler.entity.clam.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.crab.enableCrab) buildSpawnsFromConfig(EntityCrab.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.crab.spawning.spawnWeight, 1, 4, ConfigHandler.entity.crab.spawning.biomes, ConfigHandler.entity.crab.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.lobster.enableLobster) buildSpawnsFromConfig(EntityLobster.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.lobster.spawning.spawnWeight, 1, 4, ConfigHandler.entity.lobster.spawning.biomes, ConfigHandler.entity.lobster.spawning.biomesAsBlacklist);
		if (ConfigHandler.entity.tropicalSlime.enableTropicalSlime)
		{
			buildSpawnsFromConfig(EntityTropicalSlime.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.tropicalSlime.spawning.spawningOcean.spawnWeight, 2, 4, ConfigHandler.entity.tropicalSlime.spawning.spawningOcean.biomes, ConfigHandler.entity.tropicalSlime.spawning.spawningOcean.biomesAsBlacklist);
			buildSpawnsFromConfig(EntityTropicalSlime.class, EnumCreatureType.WATER_CREATURE, ConfigHandler.entity.tropicalSlime.spawning.spawningJungle.spawnWeight, 1, 1, ConfigHandler.entity.tropicalSlime.spawning.spawningJungle.biomes, ConfigHandler.entity.tropicalSlime.spawning.spawningJungle.biomesAsBlacklist);
		}

		setEntityPlacementTypes();
	}

	public static void buildSpawnsFromConfig(Class<? extends EntityLiving> entity, EnumCreatureType type, int weight, int min, int max, String[] biomes, boolean blacklist)
	{ EntityRegistry.addSpawn(entity, weight, min, max, type, getSpawnBiomes(biomes, blacklist)); }
	
	public static void setEntityPlacementTypes()
	{
		EntitySpawnPlacementRegistry.setPlacementType(EntityGlowSquid.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityCod.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntitySalmon.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityTropicalFish.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityPufferfish.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityDrowned.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityDolphin.class, SpawnPlacementType.IN_WATER);
		
		EntitySpawnPlacementRegistry.setPlacementType(EntityClam.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityLobster.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityTropicalSlime.class, SpawnPlacementType.IN_WATER);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{ EntityRegistry.registerModEntity(new ResourceLocation(Main.MOD_ID, name), entity, Main.MOD_ID + "." + name, id, Main.instance, range, 1, true, color1, color2); }
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
	{ EntityRegistry.registerModEntity(new ResourceLocation(Main.MOD_ID, name), entity, Main.MOD_ID + "." + name, id, Main.instance, range, 1, true); }
}