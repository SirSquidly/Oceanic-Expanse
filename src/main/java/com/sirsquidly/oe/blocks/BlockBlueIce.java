package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBlueIce extends Block
{
	@SuppressWarnings("deprecation")
	public BlockBlueIce()
    {
        super(Material.PACKED_ICE);
        this.setLightLevel((float)ConfigHandler.block.blueIce.blueIceLight * 0.0625F);
        this.setSoundType(SoundType.GLASS);
        this.setHarvestLevel("pickaxe", 0);
        this.slipperiness = Math.max((float)ConfigHandler.block.blueIce.blueIceSlipperiness, 0.6f);
    }
	
	public int quantityDropped(Random random)
    { return 0; }
}