package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockShellSand extends BlockFalling
{
	public BlockShellSand()
    {
        super(Material.SAND);
        this.setSoundType(SoundType.SAND);
    }
	
	@SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state)
    { return -2370656; }
}