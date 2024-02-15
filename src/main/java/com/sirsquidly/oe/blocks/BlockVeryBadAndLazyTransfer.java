package com.sirsquidly.oe.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 	A class that, when it loads at all (via getStateFromMeta), in transformed into whatever got passed in the IBlockState
 * 
 *  This... exists because Forge's `RegistryEvent.MissingMappings` event is ONLY for complete replacements, when I just wanted to combine a few existing classes!
 *  Yes, it is hacky. But by god it works, which makes it better than anything else.
 */
public class BlockVeryBadAndLazyTransfer extends Block
{
	private final IBlockState transferBlock;
	
	public BlockVeryBadAndLazyTransfer(IBlockState modelState)
	{
		super(Material.WATER);
		this.transferBlock = modelState;
		setDefaultState(blockState.getBaseState());
	}
	
	protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, BlockLiquid.LEVEL); }
	
	public IBlockState getStateFromMeta(int meta)
    { return transferBlock; }
	
	public int getMetaFromState(IBlockState state)
	{ return 0; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.BLUE + "This is a technical block from Oceanic Expanse. It will transform into " + transferBlock.toString() + " when placed. This is hidden from JEI. You should not have this.");
	}
}