package com.sirsquidly.oe.common.world.structure.ocean_ruins;

import com.sirsquidly.oe.common.entity.EntityDrowned;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import com.sirsquidly.oe.common.world.structure.base.ModStructureTemplate;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OceanRuinsTemplate extends ModStructureTemplate
{

    private final List<BlockPos> chestCandidates = new ArrayList<>();


    public OceanRuinsTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public OceanRuinsTemplate() {

    }



    // This handles data blocks that you put down. You can do some pretty awesome stuff with this such as generating smaller structures or
    // decoration pieces inside of a structure. Handle loot tables and whatnot. This is the bread and butter of adding to the structure
    //
    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb)
    {
        if (function.equals("chest")) placeRuinsChest(world, pos, rand);

        if (function.equals("chest_p1"))
        {
            chestCandidates.add(pos.toImmutable());
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }

        if (function.equals("stone_brick"))
        {
            world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState(), 2);

            if (rand.nextFloat() < 0.5f)
            {
                BlockStoneBrick.EnumType variant = rand.nextBoolean() ? BlockStoneBrick.EnumType.MOSSY : BlockStoneBrick.EnumType.CRACKED;
                world.setBlockState( pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, variant), 2);
            }
            else if (rand.nextFloat() < 0.2f)
            {
                world.setBlockState( pos, Blocks.AIR.getDefaultState(), 2);
            }
        }

        if (function.equals("chiseled_stone_brick"))
        {
            world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 2);

            if (rand.nextFloat() < 0.2f)
            {
                world.setBlockState( pos, Blocks.AIR.getDefaultState(), 2);
            }
        }




        if (function.startsWith("drowned") && ConfigHandler.entity.drowned.enableDrowned)
        {
            int min = 1;
            int max = 1;

            if (function.contains(";"))
            {
                String[] parts = function.split(";", 2);

                if (parts.length == 2)
                {
                    String[] range = parts[1].split("-", 2);

                    if (range.length == 2)
                    {
                        min = Integer.parseInt(range[0]);
                        max = Integer.parseInt(range[1]);
                    }
                    else
                    { min = max = Integer.parseInt(range[0]); }
                }
            }

            int count = (max > min) ? min + rand.nextInt(max - min + 1) : min;

            if (max < min || max < 0) return;
            for (int i = 0; i < count; i++)
            {
                pos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));

                if (world.getBlockState(pos).getMaterial() == Material.WATER && world.getBlockState(pos.up()).getMaterial() == Material.WATER)
                {
                    EntityDrowned entitydrowned = new EntityDrowned(world);
                    entitydrowned.enablePersistence();
                    entitydrowned.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);

                    world.spawnEntity(entitydrowned);
                }
                else
                { i -= 1; }
            }
        }
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);

        if (!chestCandidates.isEmpty())
        {
            BlockPos chosen = chestCandidates.get(randomIn.nextInt(chestCandidates.size()));

            placeRuinsChest(worldIn, chosen, randomIn);

            for (BlockPos pos : chestCandidates)
            {
                if (!pos.equals(chosen)) worldIn.setBlockState(pos, Blocks.GRAVEL.getDefaultState(), 2);
            }
        }

        return true;

    }

    public void placeRuinsChest(World world, BlockPos pos, Random rand)
    {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 3);
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityChest) ((TileEntityChest)tileentity).setLootTable(LootTableHandler.UNDERWATER_RUINS_SMALL, rand.nextLong());
    }


    //The folder name in assets/oe/structures/
    @Override
    public String templateLocation() {
        return "ocean_ruins";
    }
}
