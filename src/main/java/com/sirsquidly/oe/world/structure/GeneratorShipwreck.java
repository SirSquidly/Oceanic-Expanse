package com.sirsquidly.oe.world.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.util.Reference;
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
	ResourceLocation SHIP_FULL = new ResourceLocation(Reference.MOD_ID, "shipwreck_full");
	ResourceLocation SHIP_UP1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_up1");
	ResourceLocation SHIP_UP2 = new ResourceLocation(Reference.MOD_ID, "shipwreck_up2");
	ResourceLocation SHIP_UPFT1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_upft1");
	ResourceLocation SHIP_UPBK1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_upbk1");
	ResourceLocation SHIP_SIDE1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_side1");
	ResourceLocation SHIP_SIDEFT1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_sidebk1");
	ResourceLocation SHIP_SIDEBK1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_sidebk1");
	ResourceLocation SHIP_DOWN1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_downbk1");
	ResourceLocation SHIP_DOWNFT1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_downbk1");
	ResourceLocation SHIP_DOWNBK1 = new ResourceLocation(Reference.MOD_ID, "shipwreck_downbk1");
	
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
        boolean isValidBiome = false;
        ChunkPos chunkPos = world.getChunkFromChunkCoords(chunkX, chunkZ).getPos();
        Biome biome = world.getBiomeForCoordsBody(chunkPos.getBlock(0, 0, 0));

		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}

		if(isValidBiome)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				int xPos = rand.nextInt(16) + 8;
				int zPos = rand.nextInt(16) + 8;
				int yPos = world.getHeight();
				
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					MinecraftServer mcServer = world.getMinecraftServer();
					TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
					Rotation[] arotation = Rotation.values();
					
					PlacementSettings placementsettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setRotation(arotation[rand.nextInt(arotation.length)]);
					ResourceLocation location = getRandomStructure(rand.nextInt(10));
					Template template = manager.get(mcServer, location);
					
					BlockPos size = template.getSize();
					
					//** The Front-Left corner of the structure, used for the X and Z Pos. */
					BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);

					//** Grabs the lowest Y block from around the middle of the structure, for snapping to the floor. Just need the middle, because no need to check each corner, it's cooler for overhangs and such.*/
					BlockPos placeCheck = pos.add(Template.transformedBlockPos(placementsettings, new BlockPos(0 + (size.getX()/2), 0, 0 + (size.getZ()/2))));
						
					for ( IBlockState state = world.getBlockState(placeCheck); ((state.getBlock().isReplaceable(world, placeCheck) || state.getMaterial() == Material.WATER || state.getMaterial() == Material.LEAVES) && placeCheck.getY() > 0); state = world.getBlockState(placeCheck) )
			        { placeCheck = placeCheck.down(); }	
					pos = new BlockPos(pos.getX(), placeCheck.getY(), pos.getZ());
					
					/** This buries the ship down a bit. */
					pos = pos.down(rand.nextInt(6));
					
					IBlockState state = world.getBlockState(pos);
					world.notifyBlockUpdate(pos, state, state, 3);
					template.addBlocksToWorldChunk(world, pos, placementsettings);
					
					
					Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

                    for (Entry<BlockPos, String> entry : map.entrySet())
                    {
                    	doDataBlockLoading(entry, pos, world, rand);
                    }
				}
			}
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
        if ("drowned".equals(entry.getValue()) && ConfigHandler.worldGen.shipwreck.enableShipwreckDrowned)
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
}