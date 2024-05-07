package com.sirsquidly.oe.world.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

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
import net.minecraft.util.math.ChunkPos;
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
	ResourceLocation SHIP_SIDEFT1 = new ResourceLocation(Main.MOD_ID, "shipwreck_sidebk1");
	ResourceLocation SHIP_SIDEBK1 = new ResourceLocation(Main.MOD_ID, "shipwreck_sidebk1");
	ResourceLocation SHIP_DOWN1 = new ResourceLocation(Main.MOD_ID, "shipwreck_downbk1");
	ResourceLocation SHIP_DOWNFT1 = new ResourceLocation(Main.MOD_ID, "shipwreck_downbk1");
	ResourceLocation SHIP_DOWNBK1 = new ResourceLocation(Main.MOD_ID, "shipwreck_downbk1");
	
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
        ChunkPos chunkPos = world.getChunk(chunkX, chunkZ).getPos();
        Biome biome = world.getBiomeForCoordsBody(chunkPos.getBlock(0, 0, 0));

		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				for(int j = 0; j < attemptsPerChunk; j++)
				{
					if(rand.nextInt(chancePerAttempt) == 0)
					{
						spawnShipwreck(world, rand, chunkX * 16 + rand.nextInt(8) + 8, chunkZ * 16 + rand.nextInt(8) + 8);
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
		BlockPos pos = new BlockPos(x, 1, z);
		
		PlacementSettings placementsettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setRotation(arotation[rand.nextInt(arotation.length)]);
		Template template = manager.get(mcServer, getRandomStructure(rand.nextInt(10)));

		BlockPos size = template.getSize();
		//** Grabs the lowest Y block from around the middle of the structure, for snapping to the floor. Just need the middle, because no need to check each corner, it's cooler for overhangs and such.*/
		BlockPos placeCheck = pos.add(Template.transformedBlockPos(placementsettings, new BlockPos(0 + (size.getX()/2), 0, 0 + (size.getZ()/2))));
		
		//** Uses placeCheck to set the y to the Seafloor. */
		pos = new BlockPos(x, getSeaFloor(world, placeCheck.getX(), placeCheck.getZ()).getY(), z);
				
		/** This buries the ship down a bit. */
		pos = pos.down(rand.nextInt(6));

		
		template.addBlocksToWorld(world, pos, placementsettings);
	
		
		Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
        for (Entry<BlockPos, String> entry : map.entrySet())
        {
        	doDataBlockLoading(entry, pos, world, rand);
        }
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
	
	protected void doDataBlockLoading(Entry<BlockPos, String> entry, BlockPos pos, World worldIn, Random rand)
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
        
        //** Spawns 1-8 Drowned around the area of the Data Block. */
        if ("drowned".equals(entry.getValue()) && ConfigHandler.worldGen.shipwreck.enableShipwreckDrowned && ConfigHandler.entity.drowned.enableDrowned)
        {
        	for (int i = 0; i < rand.nextInt(8)+1; i++)
        	{
        		blockpos2 = blockpos2.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));
            	
                if (worldIn.getBlockState(blockpos2).getMaterial() == Material.WATER && worldIn.getBlockState(blockpos2.up()).getMaterial() == Material.WATER)
                {
                	EntityDrowned entitydrowned = new EntityDrowned(worldIn);
                    entitydrowned.enablePersistence();
                    entitydrowned.moveToBlockPosAndAngles(blockpos2, 0.0F, 0.0F);
                    
                    worldIn.spawnEntity(entitydrowned);
                }
                else
                { i -= 1; }
        	}
        } 
    }	
	
	public static BlockPos getSeaFloor(World world, int x, int z)
    {
    	int yPos = Math.max(world.getSeaLevel() - 2, 1);;
    	BlockPos pos = new BlockPos(x, yPos, z);
    	
        for (; pos.getY() > 0; pos = pos.down())
        {
        	IBlockState state = world.getBlockState(pos);
        	if (!(state.getBlock().isReplaceable(world, pos)) && state.getMaterial() != Material.LEAVES && state.getMaterial() != Material.ICE)
        	{ break;}
        }
        return pos;
    }
}