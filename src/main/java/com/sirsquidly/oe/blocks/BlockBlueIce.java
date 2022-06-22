package com.sirsquidly.oe.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBlueIce extends Block
{
	@SuppressWarnings("deprecation")
	public BlockBlueIce()
    {
        super(Material.PACKED_ICE);
        this.setSoundType(SoundType.GLASS);
        this.slipperiness = 0.989F;
    }
	
	public int quantityDropped(Random random)
    { return 0; }
}