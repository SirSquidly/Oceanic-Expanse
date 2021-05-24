package com.sirsquidly.oe.proxy;

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
    	GameRegistry.registerWorldGenerator(new GeneratorCoconutTree(5, 11, Biomes.BEACH), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenKelp(48, Biomes.OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenKelp(80, Biomes.DEEP_OCEAN), 0);

    	
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(48, 0.4, Biomes.RIVER), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(48, 0.3, Biomes.OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(48, 0.8, Biomes.DEEP_OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(64, 0.6, BiomeDictionary.getBiomes(Type.SWAMP).toArray(new Biome[0])), 0);
	}
}
