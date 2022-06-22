package com.sirsquidly.oe.proxy;

import java.util.ArrayList;
import java.util.List;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEEntities;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;
import com.sirsquidly.oe.world.*;
import com.sirsquidly.oe.world.feature.*;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
	public static final List<Biome> allOceans = new ArrayList<Biome>();
	
	public void preInitRegisteries(FMLPreInitializationEvent event)
	{
		allOceans.addAll(BiomeDictionary.getBiomes(Type.OCEAN));
		allOceans.addAll(BiomeDictionary.getBiomes(Type.BEACH));
		
		OEEntities.registerEntities();
		GameRegistry.registerWorldGenerator(new GeneratorWarmOcean(allOceans.toArray(new Biome[0])), 0);
		GameRegistry.registerWorldGenerator(new GeneratorFrozenOcean(allOceans.toArray(new Biome[0])), 0);
		registerWorldGen();
		
		if (ConfigHandler.vanillaTweak.waterTweak)
		{
			Blocks.WATER.setLightOpacity(1);
			Blocks.FLOWING_WATER.setLightOpacity(1);
		}
	}
	
	public void initRegistries(FMLInitializationEvent event)
	{
		OEEntities.registerEntitySpawns();
		SoundHandler.registerSounds();
	}
	
	@SideOnly(Side.CLIENT)
	public void registerItemRenderer(Item item, int meta, String id){}
	@SideOnly(Side.CLIENT)
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    
    public static void registerWorldGen()
	{
    	//GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 5, 3, false, Biomes.BEACH), 0);
    	//GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 15, 6, true, Biomes.STONE_BEACH), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenTidePools(2, 30, Biomes.BEACH), 0);
    	
    	GameRegistry.registerWorldGenerator(new GeneratorCoconutTree(3, 8, Biomes.BEACH), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeaOats(2, 3, 25, Biomes.BEACH), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenKelpForest(BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);

    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(OEBlocks.SEAGRASS, 2, 6, 32, 0.4, true, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(OEBlocks.SEAGRASS, 2, 2, 48, 0.4, false, Biomes.RIVER), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(OEBlocks.SEAGRASS, 6, 2, 48, 0.3, false, Biomes.OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(OEBlocks.SEAGRASS, 6, 2, 64, 0.8, false, Biomes.DEEP_OCEAN), 0);
    	GameRegistry.registerWorldGenerator(new WorldGenSeagrass(OEBlocks.SEAGRASS, 2, 2, 48, 0.6, false, BiomeDictionary.getBiomes(Type.SWAMP).toArray(new Biome[0])), 0);
	}
}
