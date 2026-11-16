package com.sirsquidly.oe.world.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import java.util.Map.Entry;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GeneratorShipwreck implements IWorldGenerator
{		
	/** This the untouched Ship. */
	ResourceLocation SHIP_FULL = new ResourceLocation(Main.MOD_ID, "shipwreck_full");
	ResourceLocation SHIP_UP1 = new ResourceLocation(Main.MOD_ID, "shipwreck_up1");
	ResourceLocation SHIP_UP2 = new ResourceLocation(Main.MOD_ID, "shipwreck_up2");
	ResourceLocation SHIP_UPFT1 = new ResourceLocation(Main.MOD_ID, "shipwreck_upft1");
	ResourceLocation SHIP_UPBK1 = new ResourceLocation(Main.MOD_ID, "shipwreck_upbk1");
	ResourceLocation SHIP_SIDE1 = new ResourceLocation(Main.MOD_ID, "shipwreck_side1");
	ResourceLocation SHIP_SIDEFT1 = new ResourceLocation(Main.MOD_ID, "shipwreck_sideft1");
	ResourceLocation SHIP_SIDEBK1 = new ResourceLocation(Main.MOD_ID, "shipwreck_sidebk1");
	ResourceLocation SHIP_DOWN1 = new ResourceLocation(Main.MOD_ID, "shipwreck_down1");
	ResourceLocation SHIP_DOWNFT1 = new ResourceLocation(Main.MOD_ID, "shipwreck_downft1");
	ResourceLocation SHIP_DOWNBK1 = new ResourceLocation(Main.MOD_ID, "shipwreck_downbk1");
	
	// Populating a chunk only guarantees that it and its +X/+Z neighbors exist; touching any other chunk triggers cascading generation.
	private static final int GENERATION_AREA_SIZE = 32;
	private static final int DROWNED_SPAWN_ATTEMPTS = 10;
	private static final int CAPTAIN_MIN_DROWNED_GROUPS = 3;
	
	private Biome[] biomes;
	private int attemptsPerChunk;
	private int chancePerAttempt;

	public GeneratorShipwreck(int perChunk,int perAttempt ,Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.biomes = biomes; 
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider)
	{
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;
        BlockPos pos = new BlockPos(x, 0, z);
        Biome biome = world.getBiomeForCoordsBody(pos);
		
        Block topBlock = world.getBlockState(world.getTopSolidOrLiquidBlock(pos)).getBlock();
        
		if (topBlock == Blocks.LAVA || topBlock == Blocks.AIR) return;
		
		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				for(int j = 0; j < attemptsPerChunk; j++)
				{
					if(rand.nextInt(chancePerAttempt) == 0)
					{
						spawnShipwreck(world, rand, pos.getX(), pos.getZ());
					}
				}
				break;
			}
		}
	}
	
	public void spawnShipwreck(World world, Random rand, int x, int z)
	{
		MinecraftServer mcServer = world.getMinecraftServer();
		TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
		Rotation[] arotation = Rotation.values();
		
		PlacementSettings placementsettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setRotation(arotation[rand.nextInt(arotation.length)]);
		Template template = manager.get(mcServer, getRandomStructure(rand.nextInt(11)));
		BlockPos size = template.getSize();

		int areaMinX = (x >> 4) << 4;
		int areaMinZ = (z >> 4) << 4;
		BlockPos cornerA = Template.transformedBlockPos(placementsettings, BlockPos.ORIGIN);
		BlockPos cornerB = Template.transformedBlockPos(placementsettings, size.add(-1, -1, -1));
		int minX = areaMinX - Math.min(cornerA.getX(), cornerB.getX());
		int maxX = areaMinX + GENERATION_AREA_SIZE - 1 - Math.max(cornerA.getX(), cornerB.getX());
		int minZ = areaMinZ - Math.min(cornerA.getZ(), cornerB.getZ());
		int maxZ = areaMinZ + GENERATION_AREA_SIZE - 1 - Math.max(cornerA.getZ(), cornerB.getZ());
		if (minX > maxX || minZ > maxZ) return;

		x = MathHelper.clamp(x, minX, maxX);
		z = MathHelper.clamp(z, minZ, maxZ);

		// The seafloor is sampled at the structure's center only, so overhangs are kept.
		BlockPos placeCheck = new BlockPos(x, 1, z).add(Template.transformedBlockPos(placementsettings, new BlockPos(size.getX() / 2, 0, size.getZ() / 2)));
		BlockPos pos = new BlockPos(x, getSeaFloor(world, placeCheck.getX(), placeCheck.getZ()).getY(), z).down(rand.nextInt(6));

		template.addBlocksToWorld(world, pos, placementsettings);

		Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
		BlockPos captainMarker = pickCaptainMarker(map, rand);
		for (Entry<BlockPos, String> entry : map.entrySet())
		{ doDataBlockLoading(entry, pos, world, rand, areaMinX, areaMinZ, entry.getKey().equals(captainMarker)); }
	}
	
	@Nullable
	private static BlockPos pickCaptainMarker(Map<BlockPos, String> dataBlocks, Random rand)
	{
		if (!ConfigHandler.worldGen.shipwreck.shipwreckGuaranteedCaptain || !ConfigHandler.worldGen.shipwreck.enableShipwreckDrowned) return null;
		if (!ConfigHandler.entity.drowned.enableDrowned || !ConfigHandler.entity.drowned.drownedCaptain.enableDrownedCaptain) return null;

		List<BlockPos> markers = new ArrayList<BlockPos>();
		for (Entry<BlockPos, String> entry : dataBlocks.entrySet())
		{ if ("drowned".equals(entry.getValue())) markers.add(entry.getKey()); }

		if (markers.size() < CAPTAIN_MIN_DROWNED_GROUPS) return null;

		Collections.sort(markers);
		return markers.get(rand.nextInt(markers.size()));
	}
	
	/** Picks which Shipwreck to generate. */
	protected ResourceLocation getRandomStructure(int grab)
	{
		switch (grab)
		{
			case 0:
				return SHIP_UP1;
			case 1:
				return SHIP_UP2;
			case 2:
				return SHIP_UPFT1;
			case 3:
				return SHIP_UPBK1;
			case 4:
				return SHIP_SIDE1;
			case 5:
				return SHIP_SIDEFT1;
			case 6:
				return SHIP_SIDEBK1;
			case 7:
				return SHIP_DOWN1;
			case 8:
				return SHIP_DOWNFT1;
			case 9:
				return SHIP_DOWNBK1;
		}
		return SHIP_FULL;
	}
	
	protected void doDataBlockLoading(Entry<BlockPos, String> entry, BlockPos pos, World worldIn, Random rand, int areaMinX, int areaMinZ, boolean spawnCaptain)
    {
		BlockPos blockpos2 = entry.getKey();
		
        if ("map_chest".equals(entry.getValue()))
        {
            worldIn.setBlockState(blockpos2, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 3);
            TileEntity tileentity = worldIn.getTileEntity(blockpos2);

            if (tileentity instanceof TileEntityChest)
            {
                ((TileEntityChest)tileentity).setLootTable(LootTableHandler.SHIPWRECK_MAP, rand.nextLong());
            }
        }
        if ("supply_chest".equals(entry.getValue()))
        {
            worldIn.setBlockState(blockpos2, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 3);
            TileEntity tileentity = worldIn.getTileEntity(blockpos2);

            if (tileentity instanceof TileEntityChest)
            {
                ((TileEntityChest)tileentity).setLootTable(LootTableHandler.SHIPWRECK_SUPPLY, rand.nextLong());
            }
        }
        if ("tresure_chest".equals(entry.getValue()))
        {
            worldIn.setBlockState(blockpos2, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 3);
            TileEntity tileentity = worldIn.getTileEntity(blockpos2);

            if (tileentity instanceof TileEntityChest)
            {
                ((TileEntityChest)tileentity).setLootTable(LootTableHandler.SHIPWRECK_TREASURE, rand.nextLong());
            }
        }
        
		if ("drowned".equals(entry.getValue()) && ConfigHandler.worldGen.shipwreck.enableShipwreckDrowned && ConfigHandler.entity.drowned.enableDrowned)
		{
			int drownedCount = rand.nextInt(8) + 1;
			boolean captainPending = spawnCaptain;
			
			for (int i = 0; i < drownedCount; i++)
			{
				for (int attempt = 0; attempt < DROWNED_SPAWN_ATTEMPTS; attempt++)
				{
					BlockPos spawnPos = blockpos2.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));
					
					if (!isInsideGenerationArea(spawnPos, areaMinX, areaMinZ)) continue;
					
					if (worldIn.getBlockState(spawnPos).getMaterial() == Material.WATER && worldIn.getBlockState(spawnPos.up()).getMaterial() == Material.WATER)
					{
						EntityDrowned entitydrowned = new EntityDrowned(worldIn);
						DifficultyInstance difficulty = worldIn.getDifficultyForLocation(pos);
						entitydrowned.onInitialSpawn(difficulty, null);
						
						if (captainPending)
						{
							entitydrowned.makeCaptain(difficulty);
							captainPending = false;
						}
						entitydrowned.enablePersistence();
						entitydrowned.moveToBlockPosAndAngles(spawnPos, 0.0F, 0.0F);
						
						worldIn.spawnEntity(entitydrowned);
						break;
					}
				}
			}
		}
	}
	
	private static boolean isInsideGenerationArea(BlockPos pos, int areaMinX, int areaMinZ)
	{
		return pos.getX() >= areaMinX && pos.getX() < areaMinX + GENERATION_AREA_SIZE && pos.getZ() >= areaMinZ && pos.getZ() < areaMinZ + GENERATION_AREA_SIZE;
	}
	
	public static BlockPos getSeaFloor(World world, int x, int z)
    {
    	int yPos = Math.max(world.getSeaLevel() - 2, 1);;
    	BlockPos pos = new BlockPos(x, yPos, z);
    	
        for (; pos.getY() > 0; pos = pos.down())
        {
        	IBlockState state = world.getBlockState(pos);
        	if (!(state.getBlock().isReplaceable(world, pos)) && state.getMaterial() != Material.LEAVES && state.getMaterial() != Material.ICE && state.getMaterial() != Material.WATER)
        	{ break;}
        }
        return pos;
    }
}