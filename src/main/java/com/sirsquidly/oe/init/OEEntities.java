package com.sirsquidly.oe.init;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.render.entity.*;
import com.sirsquidly.oe.client.render.tileentity.RenderConduit;
import com.sirsquidly.oe.client.render.tileentity.RenderNautilusShellBlock;
import com.sirsquidly.oe.client.render.tileentity.RenderPickledSkull;
import com.sirsquidly.oe.client.render.tileentity.RenderStasis;
import com.sirsquidly.oe.common.entity.*;
import com.sirsquidly.oe.common.entity.item.*;
import com.sirsquidly.oe.common.tileentity.*;
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
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OEEntities
{
	public static int id;

	public static void registerEntities()
	{
		registerEntity("glow_squid", EntityGlowSquid.class, ++id, 100, 2243405, 8454080);
		if (ConfigHandler.entity.cod.enableCod) registerEntity("cod", EntityCod.class, ++id, 64, 8564132, 11386587);
		if (ConfigHandler.entity.salmon.enableSalmon) registerEntity("salmon", EntitySalmon.class, ++id, 64, 12860482, 5268308);
		if (ConfigHandler.entity.tropicalFish.enableTropicalFish) registerEntity("tropical_fish", EntityTropicalFish.class, ++id, 64, 16019232, 15724527);
		registerEntity("nautilus", EntityNautilus.class, ++id, 64, 15453241, 4365242);
		registerEntity("zombie_nautilus", EntityZombieNautilus.class, ++id, 64, 15453241, 4365242);
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

	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileConduit.class, new ResourceLocation(Main.MOD_ID, "conduit"));
		GameRegistry.registerTileEntity(TilePickledSkull.class, new ResourceLocation(Main.MOD_ID, "pickled_skull"));
		GameRegistry.registerTileEntity(TileNautilusShellBlock.class, new ResourceLocation(Main.MOD_ID, "nautilus_shell_block"));
		GameRegistry.registerTileEntity(TilePrismarinePot.class, new ResourceLocation(Main.MOD_ID, "prismarine_pot"));
		GameRegistry.registerTileEntity(TileStasis.class, new ResourceLocation(Main.MOD_ID, "stagnant"));
	}

	@SideOnly(Side.CLIENT)
	public static void RegisterRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCod.class, RenderCod::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySalmon.class, RenderSalmon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNautilus.class, RenderNautilus::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieNautilus.class, RenderZombieNautilus::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPufferfish.class, RenderPufferfish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTropicalFish.class, RenderTropicalFish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, RenderTurtle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDolphin.class, RenderDolphin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDrowned.class, RenderDrowned::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityGlowSquid.class, RenderGlowSquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySquid.class, RenderBabySquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyGlowSquid.class, RenderBabyGlowSquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCrab.class, RenderCrab::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityClam.class, RenderClam::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDrownedSummon.class, RenderDrownedSummon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLobster.class, RenderLobster::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOEBoat.class, RenderOEBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGlowItemFrame.class, RenderGlowItemFrame::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPickled.class, RenderPickled::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTropicalSlime.class, RenderTropicalSlime::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityConduitEye.class, RenderConduitEye::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderwaterTNTPrimed.class, RenderUnderwaterTNT::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileConduit.class, new RenderConduit());

		ClientRegistry.bindTileEntitySpecialRenderer(TileStasis.class, new RenderStasis());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePickledSkull.class, new RenderPickledSkull());
		ClientRegistry.bindTileEntitySpecialRenderer(TileNautilusShellBlock.class, new RenderNautilusShellBlock());
	}


	public static void registerEntitySpawns()
	{
		/* Spawn Weight needs to be high due to the height limitation of the spawning */
		EntityRegistry.addSpawn(EntityGlowSquid.class, 8, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));

		if (ConfigHandler.entity.pufferfish.enablePufferfish) EntityRegistry.addSpawn(EntityPufferfish.class, 5, 1, 5, EnumCreatureType.WATER_CREATURE, Biomes.DEEP_OCEAN);
		
		if (ConfigHandler.entity.cod.enableCod) EntityRegistry.addSpawn(EntityCod.class, 15, 3, 7, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.salmon.enableSalmon) EntityRegistry.addSpawn(EntitySalmon.class, 15, 1, 5, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.RIVER).toArray(new Biome[0]));
		if (ConfigHandler.entity.salmon.enableSalmon) EntityRegistry.addSpawn(EntitySalmon.class, 15, 1, 5, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		EntityRegistry.addSpawn(EntityDolphin.class, 10, 3, 5, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.tropicalFish.enableTropicalFish) EntityRegistry.addSpawn(EntityTropicalFish.class, 30, 8, 8, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.clam.enableClam) EntityRegistry.addSpawn(EntityClam.class, 5, 1, 1, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.lobster.enableLobster) EntityRegistry.addSpawn(EntityLobster.class, 3, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		
		if (ConfigHandler.entity.turtle.enableTurtle) EntityRegistry.addSpawn(EntityTurtle.class, 100, 2, 6, EnumCreatureType.CREATURE, Biomes.BEACH);
		if (ConfigHandler.entity.crab.enableCrab) EntityRegistry.addSpawn(EntityCrab.class, 10, 1, 4, EnumCreatureType.CREATURE, BiomeDictionary.getBiomes(Type.BEACH).toArray(new Biome[0]));

		if (ConfigHandler.entity.tropicalSlime.enableTropicalSlime) EntityRegistry.addSpawn(EntityTropicalSlime.class, 6, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.tropicalSlime.enableTropicalSlime && ConfigHandler.entity.tropicalSlime.tropicalSlimeJungleSpawning) EntityRegistry.addSpawn(EntityTropicalSlime.class, 4, 1, 1, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.JUNGLE).toArray(new Biome[0]));
		
		if (ConfigHandler.entity.drowned.enableDrowned) EntityRegistry.addSpawn(EntityDrowned.class, 3, 2, 4, EnumCreatureType.WATER_CREATURE, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0]));
		if (ConfigHandler.entity.drowned.enableDrowned) EntityRegistry.addSpawn(EntityDrowned.class, 4, 2, 4, EnumCreatureType.WATER_CREATURE, Biomes.RIVER);
		
		setEntityPlacementTypes();
	}
	
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