package com.sirsquidly.oe.common.world.structure;

import com.sirsquidly.oe.common.world.structure.ocean_ruins.WorldGenOceanRuins;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ModGenStructures implements IWorldGenerator {

    public static final WorldGenOceanRuins ocean_ruins = new WorldGenOceanRuins();

    //I'll typically just have one master class that manages all structures using this style. You can remove it or shift it into another class
    //if you have something that does similar

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);

        if(world.provider.getDimension() == 0 && ConfigHandler.worldGen.oceanRuins.enableOceanRuins) {
            ocean_ruins.generate(world, random, pos);
        }
    }
}
