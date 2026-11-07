package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;

public class BlockOEFenceGate extends BlockFenceGate
{
    public BlockOEFenceGate(BlockPlanks.EnumType p_i46394_1_, SoundType soundIn)
    {
        super(p_i46394_1_);
        this.setSoundType(soundIn);
    }
}