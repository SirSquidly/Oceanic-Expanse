package com.sirsquidly.oe.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityTrident;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEEntities;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.network.OEPacketHandler;
import com.sirsquidly.oe.network.OEPacketSpawnParticles;
import com.sirsquidly.oe.tileentity.TileConduit;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.RenderHandler;
import com.sirsquidly.oe.world.*;
import com.sirsquidly.oe.world.feature.*;
import com.sirsquidly.oe.world.structure.GeneratorCoquinaOutcrop;
import com.sirsquidly.oe.world.structure.GeneratorShipwreck;

import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
	public static final List<Biome> allOceans = new ArrayList<Biome>();
	
	public static final DamageSource COCONUT = new DamageSource(Main.MOD_ID + "." + "coconut");
	
	public static DamageSource causeTridentDamage(EntityTrident trident, @Nullable Entity indirectEntityIn)
    { return (new EntityDamageSourceIndirect(Main.MOD_ID + "." + "trident", trident, indirectEntityIn)).setProjectile(); }

	/** This is used for checking if Fluidlogged API is installed. */
	public boolean fluidlogged_enable = false;
	
	public void preInitRegisteries(FMLPreInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileConduit.class, new ResourceLocation(Main.MOD_ID, "conduit"));
		
		allOceans.addAll(BiomeDictionary.getBiomes(Type.OCEAN));
		allOceans.addAll(BiomeDictionary.getBiomes(Type.BEACH));
		
		OEEntities.registerEntities();
		GameRegistry.registerWorldGenerator(new GeneratorWarmOcean(allOceans.toArray(new Biome[0])), 0);
		GameRegistry.registerWorldGenerator(new GeneratorFrozenOcean(allOceans.toArray(new Biome[0])), 0);
		registerWorldGen();
		
		if (ConfigHandler.vanillaTweak.waterTweak != 3)
		{
			Blocks.WATER.setLightOpacity(ConfigHandler.vanillaTweak.waterTweak);
			Blocks.FLOWING_WATER.setLightOpacity(ConfigHandler.vanillaTweak.waterTweak);
		}
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{ RenderHandler.registerEntityRenders(); }
	}
	
	public void initRegistries(FMLInitializationEvent event)
	{
		OEEntities.registerEntitySpawns();
		OESounds.registerSounds();
		
		OEPacketHandler.registerMessages();
		
		if (Loader.isModLoaded("fluidlogged_api"))
		{
			fluidlogged_enable = true;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerItemRenderer(Item item, int meta, String id){}
	@SideOnly(Side.CLIENT)
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    
    public static void registerWorldGen()
	{
    	//configWorldGen config = ConfigHandler.worldGen;
    	//GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 5, 3, false, Biomes.BEACH), 0);
    	//GameRegistry.registerWorldGenerator(new WorldGenShoreRock(1, 15, 6, true, Biomes.STONE_BEACH), 0);
    	//GameRegistry.registerWorldGenerator(new WorldGenTidePools(2, 30, Biomes.BEACH), 0);
    	
    	if (ConfigHandler.worldGen.palmTree.enablePalmTrees) GameRegistry.registerWorldGenerator(new WorldGenCoconutTree(ConfigHandler.worldGen.palmTree.palmTreeTriesPerChunk, ConfigHandler.worldGen.palmTree.palmTreeChancePerChunk, Biomes.BEACH), 0);
    	if (ConfigHandler.worldGen.seaOatsPatch.enableSeaOatsPatch) GameRegistry.registerWorldGenerator(new WorldGenSeaOats(ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchChancePerChunk, ConfigHandler.worldGen.seaOatsPatch.seaOatsPatchTriesPerChunk, 48, Biomes.BEACH), 0);
    	
    	GameRegistry.registerWorldGenerator(new WorldGenKelpForest(BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);

    	if (ConfigHandler.worldGen.coquinaOutcrop.enableCoquinaOutcrops) GameRegistry.registerWorldGenerator(new GeneratorCoquinaOutcrop(ConfigHandler.worldGen.coquinaOutcrop.coquinaOutcropTriesPerChunk, ConfigHandler.worldGen.coquinaOutcrop.coquinaOutcropChancePerChunk, Biomes.BEACH), 0);
    	
    	if (ConfigHandler.worldGen.shipwreck.enableShipwrecks) GameRegistry.registerWorldGenerator(new GeneratorShipwreck(1, ConfigHandler.worldGen.shipwreck.shipwreckChancePerChunk, BiomeDictionary.getBiomes(Type.OCEAN).toArray(new Biome[0])), 0);
    	
    	if (ConfigHandler.worldGen.shellPatch.enableShellPatch) GameRegistry.registerWorldGenerator(new WorldGenShellSand(ConfigHandler.worldGen.shellPatch.shellPatchChancePerChunk, ConfigHandler.worldGen.shellPatch.shellPatchTriesPerChunk, 25, Biomes.BEACH), 0);
    	
    	if (ConfigHandler.worldGen.enableSeagrassPatches)
    	{
    		GameRegistry.registerWorldGenerator(new WorldGenOceanPatch(OEBlocks.SEAGRASS, 2, 2, 48, 8, 4, 0.4, false, Biomes.RIVER), 0);
        	GameRegistry.registerWorldGenerator(new WorldGenOceanPatch(OEBlocks.SEAGRASS, 6, 2, 48, 8, 4, 0.3, false, Biomes.OCEAN), 0);
        	GameRegistry.registerWorldGenerator(new WorldGenOceanPatch(OEBlocks.SEAGRASS, 6, 2, 64, 8, 4, 0.8, false, Biomes.DEEP_OCEAN), 0);
        	GameRegistry.registerWorldGenerator(new WorldGenOceanPatch(OEBlocks.SEAGRASS, 2, 2, 48, 8, 4, 0.6, false, BiomeDictionary.getBiomes(Type.SWAMP).toArray(new Biome[0])), 0);	
    	}
	}

    /** 
     *  Specialized particle method that sends particles on servers
     * */
    public void spawnParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
	{
		if (world.isRemote)
    	{ spawnParticle(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters); }
		else
    	{ OEPacketHandler.CHANNEL.sendToAllTracking( new OEPacketSpawnParticles(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 0.0D)); }
	}
    
	public void spawnParticle(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
	{}
}