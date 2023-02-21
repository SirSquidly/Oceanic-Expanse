package com.sirsquidly.oe.world.structure;

import java.util.Random;
import java.util.Map.Entry;

import com.sirsquidly.oe.util.Reference;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GeneratorCoquinaOutcrop implements IWorldGenerator
{		
	/** This the untouched Ship. */
	ResourceLocation OUTCROP1 = new ResourceLocation(Reference.MOD_ID, "coquina_outcrop1");
	ResourceLocation OUTCROP2 = new ResourceLocation(Reference.MOD_ID, "coquina_outcrop2");
	ResourceLocation OUTCROP3 = new ResourceLocation(Reference.MOD_ID, "coquina_outcrop3");
	ResourceLocation OUTCROP4 = new ResourceLocation(Reference.MOD_ID, "coquina_outcrop4");
	
	private Biome[] biomes;
	private int attemptsPerChunk;
	private int chancePerAttempt;

	public GeneratorCoquinaOutcrop(int perChunk,int perAttempt ,Biome... biomes)
	{
		this.attemptsPerChunk = perChunk; 
		this.chancePerAttempt = perAttempt; 
		this.biomes = biomes; 
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider)
	{	
		boolean isValidBiome = false;
		int x = chunkX * 16 + 8 + rand.nextInt(10);
		int z = chunkZ * 16 + 8 + rand.nextInt(10);
		int y = world.getHeight();

		BlockPos xzPos = new BlockPos(x, 1, z);
		Biome biome = world.getBiomeForCoordsBody(xzPos);
		
		for(int i = 0; i < biomes.length; i++)
		{
			if(biome == biomes[i])
			{
				isValidBiome = true;
				break;
			}
		}
		
		BlockPos pos = new BlockPos(x, y, z);

		if(isValidBiome)
		{
			for(int i = 0; i < attemptsPerChunk; i++)
			{
				if(rand.nextInt(chancePerAttempt) == 0)
				{
					MinecraftServer mcServer = world.getMinecraftServer();
					TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
					Rotation[] arotation = Rotation.values();
					
					PlacementSettings placementsettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setRotation(arotation[rand.nextInt(arotation.length)]);
					//PlacementSettings placementsettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID);
					ResourceLocation location = getRandomStructure(rand.nextInt(10));
					Template template = manager.get(mcServer, location);
					
					BlockPos size = template.getSize();

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
				return OUTCROP1;
			case 1:
				return OUTCROP2;
			case 2:
				return OUTCROP3;
			case 3:
				return OUTCROP4;
		}
		return OUTCROP1;
	}
	
	protected void doDataBlockLoading(Entry<BlockPos, String> entry, BlockPos pos, World worldIn, Random rand)
    {}	
}