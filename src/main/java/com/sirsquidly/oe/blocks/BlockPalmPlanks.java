package com.sirsquidly.oe.blocks;

import com.sirsquidly.oe.Main;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPalmPlanks extends Block
{
	public BlockPalmPlanks()
    {
		super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        setHardness(2.0F);
		setResistance(5.0F);
        
        this.setCreativeTab(Main.OCEANEXPTAB);
    }
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return 20; }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return 5; }
}