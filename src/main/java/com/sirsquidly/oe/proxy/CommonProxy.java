package com.sirsquidly.oe.proxy;

import com.sirsquidly.oe.init.OEBiomes;
import com.sirsquidly.oe.init.OEEntities;
import com.sirsquidly.oe.util.handlers.RenderHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;
import com.sirsquidly.oe.world.feature.*;

import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void preInitRegisteries(FMLPreInitializationEvent event)
	{
		OEEntities.registerEntities();
		RenderHandler.registerEntityRenders();
		OEBiomes.registerBiomes();
		registerWorldGen();
	}
	
	public void initRegistries(FMLInitializationEvent event)
	{
		OEEntities.registerEntitySpawns();
		SoundHandler.registerSounds();
	}
	
	public void registerItemRenderer(Item item, int meta, String id){}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    
    public static void registerWorldGen()
	{
    	GameRegistry.registerWorldGenerator(new GeneratorCoconutTree(3, 8, Biomes.BEACH), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeaOats(2, 3, 25, Biomes.BEACH), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 30, 3, false, Biomes.BEACH), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 15, 6, true, Biomes.STONE_BEACH), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenKelpForest(2,40,4,48, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);
    	
    	//GameRegistry.registerWorldGenerator(new WorldGenKelp(10,10,48, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(2, 6, 32, 0.4, true, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(2, 2, 48, 0.4, false, Biomes.RIVER), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(6, 2, 48, 0.3, false, Biomes.OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(6, 2, 64, 0.8, false, Biomes.DEEP_OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(10, 2, 48, 0.3, false, OEBiomes.TIDE_POOL), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(2, 2, 48, 0.6, false, BiomeDictionary.getBiomes(Type.SWAMP).toArray(new Biome[0])), 0);
	}
}
