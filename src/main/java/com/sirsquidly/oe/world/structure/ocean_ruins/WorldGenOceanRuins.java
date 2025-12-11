package com.sirsquidly.oe.world.structure.ocean_ruins;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.util.OELogger;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

public class WorldGenOceanRuins extends WorldGenerator {
    private int spacing;
    private int separation;

    public WorldGenOceanRuins() {
        //this is just an extra non-configurable setting for extra spacing. Incase players make the base spacing 0 they still won't spawn every chunk
        this.separation = 8;
        this.spacing = ConfigHandler.worldGen.oceanRuins.ruins_spacing;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        if(canSpawnStructureAtPos(world, pos.getX() >> 4, pos.getZ() >> 4)) {
            //this creates a structure bounding box in which the structure cannot exceed this size. If it does, it will just cut it off
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 50, pos.getZ() - 50, pos.getX() + 50, pos.getZ() + 50));
            return true;
        }

        return false;
    }

    /**
     * This is where spacing, and specific needs for the biome are checked. It is basically saying if the spacing is met for the next point
     * in the structure to spawn and the biome types match. It will return true.
     * @param world
     * @param chunkX
     * @param chunkZ
     * @return
     */
    protected boolean canSpawnStructureAtPos(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing - 1;
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random =  world.setRandomSeed(k, l, 45099936);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            //passes it to checking biome types!
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
        Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
        if(BiomeDictionary.hasType(biomeCurrently, BiomeDictionary.Type.OCEAN)) {
            for(BiomeDictionary.Type types : getSpawnBiomeTypes()) {
                if(BiomeDictionary.hasType(biomeCurrently, types)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static List<BiomeDictionary.Type> oceanRuinsBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(oceanRuinsBiomeTypes == null) {
            oceanRuinsBiomeTypes = Lists.newArrayList();

            for(String str : ConfigHandler.worldGen.oceanRuins.biome_types_ocean_ruins) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) oceanRuinsBiomeTypes.add(type);
                    else OELogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    OELogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return oceanRuinsBiomeTypes;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenOceanRuins.Start(world, rand, chunkX, chunkZ);
    }


    public static class Start extends StructureStart {

        private boolean valid;

        public Start() {

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, rand, chunkX, chunkZ);
        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand = random.nextInt(Rotation.values().length);

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);

            int y = getSurfaceHeight(worldIn, posI, ConfigHandler.worldGen.oceanRuins.ruins_min_y, ConfigHandler.worldGen.oceanRuins.ruins_max_y);

            //ensures there's ground somewhere within a range
            if(y != 0) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();
                    BlockPos blockpos = posI.add(0, y, 0);
                    OceanRuins structure = new OceanRuins(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    structure.startBuilding(blockpos, rotation, "dulse_cookie");
                    this.updateBoundingBox();

                    this.valid = true;
                    if (this.isSizeableStructure()) {

                        break;
                    }


                }
            }

        }

        /**
         * Just a base getSurfaceHeight at designated location. I'll typically call this twice for bigger structures. Plotting a point at one end and another
         * on the opposite or close to center of the structure. The Night Lich's tower uses this to try and best orientate it's self in the ground.
         * @param world
         * @param pos
         * @param min
         * @param max
         * @return
         */
        private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
        {
            int currentY = max;

            while(currentY >= min)
            {
                if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock() && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES
                        && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES2 && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG2
                        && world.getBlockState(pos.add(0, currentY, 0)) != Blocks.WATER.getDefaultState()) {
                    return currentY;
                }

                currentY--;
            }

            return 0;
        }

        /**
            Method used for calling biome types initially after this structure has been called to generate, I use this for biome variants of structures
         */
        private boolean validBiomeType(BiomeDictionary.Type biomesAllowed, BlockPos pos, World world) {
            for(Biome biome : BiomeDictionary.getBiomes(biomesAllowed)) {
                if(biome != null) {
                    if (BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos), biomesAllowed)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() >= 1;
        }


    }
}
