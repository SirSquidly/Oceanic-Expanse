package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOEFence extends BlockFence
{
    public BlockOEFence(Material materialIn, MapColor mapColorIn, SoundType soundIn)
    {
        super(materialIn, mapColorIn);
        this.setSoundType(soundIn);
    }
}