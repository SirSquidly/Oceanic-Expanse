package com.sirsquidly.oe.init;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.*;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class OEEntities
{
	public static int id;

	public static void registerEntities()
	{
		registerEntity("glow_squid", EntityGlowSquid.class, ++id, 100, 3119001, 12976094);
		registerEntity("cod", EntityCod.class, ++id, 64, 8564132, 11386587);
		if (ConfigHandler.entity.salmon.enableSalmon) registerEntity("salmon", EntitySalmon.class, ++id, 64, 12860482, 5268308);
		if (ConfigHandler.entity.pufferfish.enablePufferfish) registerEntity("pufferfish", EntityPufferfish.class, ++id, 64, 15453241, 4365242);
		registerEntity("turtle", EntityTurtle.class, ++id, 64, 12763520, 4702026);
		registerEntity("crab", EntityCrab.class, ++id, 64, 12860482, 15453241);
		registerEntity("clam", EntityClam.class, ++id, 64, 14327661, 15701910);
		
		if (ConfigHandler.entity.drowned.enableDrowned) registerEntity("drowned", EntityDrowned.class, ++id, 80, 5609880, 12434265);
		
		registerEntity("pickled", EntityPickled.class, ++id, 80, 8223277, 14221270);
		
		registerEntity("coconut", EntityFallingCoconut.class, ++id, 20);
		registerEntity("conduit_eye", EntityConduitEye.class, ++id, 80);
		registerEntity("trident", EntityTrident.class, ++id, 80);
	}

	public static void registerEntitySpawns()
	{
		EntityRegistry.addSpawn(EntityGlowSquid.class, 5, 2, 4, EnumCreatureType.WATER_CREATURE, Biomes.DEEP_OCEAN);
		if (ConfigHandler.entity.pufferfish.enablePufferfish) EntityRegistry.addSpawn(EntityPufferfish.class, 5, 1, 5, EnumCreatureType.WATER_CREATURE, Biomes.DEEP_OCEAN);
		
		EntityRegistry.addSpawn(EntityCod.class, 15, 3, 7, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		EntityRegistry.addSpawn(EntitySalmon.class, 15, 1, 5, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.RIVER).toArray(new Biome[0]));
		EntityRegistry.addSpawn(EntityClam.class, 5, 1, 1, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		
		EntityRegistry.addSpawn(EntityTurtle.class, 5, 2, 6, EnumCreatureType.CREATURE, Biomes.BEACH);
		EntityRegistry.addSpawn(EntityCrab.class, 10, 1, 4, EnumCreatureType.CREATURE, Biomes.BEACH);
		
		EntitySpawnPlacementRegistry.setPlacementType(EntityGlowSquid.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityCod.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntitySalmon.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityPufferfish.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityClam.class, SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityDrowned.class, SpawnPlacementType.IN_WATER);
		
		if (ConfigHandler.entity.drowned.enableDrowned) EntityRegistry.addSpawn(EntityDrowned.class, 5, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.drowned.enableDrowned) EntityRegistry.addSpawn(EntityDrowned.class, 5, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.RIVER).toArray(new Biome[0]));
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{ EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ':' + name), entity, name, id, Main.instance, range, 1, true, color1, color2); }
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
	{ EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ':' + name), entity, name, id, Main.instance, range, 1, true); }
}