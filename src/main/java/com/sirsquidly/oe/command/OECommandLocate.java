package com.sirsquidly.oe.command;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sirsquidly.oe.util.OELogger;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class OECommandLocate implements ICommand {
    private final List<String> aliases;

    public OECommandLocate() {
        aliases = new ArrayList<>();
        aliases.add("locateOE");
    }

    @Override
    public String getName() {
        return "locateOE";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "locateOE <mod location>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("locateOE <mod location>");
        } else {
            String s = args[0];
            if(s.equals("OceanRuins")) {
                BlockPos blockpos = findNearestPos(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            }
        }
    }

    public static BlockPos findNearestPos(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //Searches each chunk to see if the attributes are met for one spawning there
        for (int i = -ConfigHandler.worldGen.oceanRuins.ruins_search_distance; i < ConfigHandler.worldGen.oceanRuins.ruins_search_distance + 1; i++) {
            for (int j = -ConfigHandler.worldGen.oceanRuins.ruins_search_distance; j < ConfigHandler.worldGen.oceanRuins.ruins_search_distance + 1; j++) {
                boolean c = IsOceanRuinsAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 60, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsOceanRuinsAtPos(World world, int chunkX, int chunkZ) {
        //use the same config option for this as in WorldGenOceanRuins
        int spacing = ConfigHandler.worldGen.oceanRuins.ruins_spacing;
        int separation = 8;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        //make sure to match the seed of this command with WorldGenOceanRuins
        Random random = world.setRandomSeed(k, l, 45099936);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        if(dimensionIn == 0) {
            return true;
        }

        return false;
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

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "OceanRuins") : Collections.emptyList();
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        List<String> list = Lists.newArrayList();

        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            if (list.isEmpty()) {
                for (Object object : possibleCompletions) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getPath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }

        return list;
    }

    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
