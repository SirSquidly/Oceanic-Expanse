package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOEBookshelf extends BlockBookshelf
{
	public BlockOEBookshelf(SoundType soundIn)
    {
        super();
        this.setSoundType(soundIn);
    }
	
	public float getEnchantPowerBonus(World world, BlockPos pos) { return 1;}
}