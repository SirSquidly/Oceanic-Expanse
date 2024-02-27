package com.sirsquidly.oe.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 	A class that, when it loads at all (via getStateFromMeta), in transformed into whatever got passed in the IBlockState
 * 
 *  This... exists because Forge's `RegistryEvent.MissingMappings` event is ONLY for complete replacements, when I just wanted to combine a few existing classes!
 *  Yes, it is hacky. But the alternative is to do a `onWorldLoad` event, and check EVERY SINGLE BLOCK. So, we just make the block itself do the work here!
 */
public class BlockVeryBadAndLazyTransfer extends Block
{
	/** This is meant to be a copy from the old 'blockDoubleUnderwater', as it is required for proper loading of the metadata under getMetaFromState */
	public static final PropertyEnum<BlockVeryBadAndLazyTransfer.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockVeryBadAndLazyTransfer.EnumBlockHalf.class);
	
	private final IBlockState transferBlock;
	public BlockVeryBadAndLazyTransfer(IBlockState modelState)
	{
		super(Material.WATER);
		this.transferBlock = modelState;
		setDefaultState(blockState.getBaseState());
	}
	
	public IBlockState getStateFromMeta(int meta)
    { 
		return complexReplacer() ? transferBlock.withProperty(BlockSeagrasss.TYPE, meta == 0 ? 2 : 1) : transferBlock;
    }
	
	/** If this is a block that requires specialized logic to replace. This logic is under `onBlockAdded` */
	public boolean complexReplacer()
    { return transferBlock == OEBlocks.SEAGRASS.getDefaultState(); }
	
	public int getMetaFromState(IBlockState state)
	{ return complexReplacer() && state.getValue(HALF) == BlockVeryBadAndLazyTransfer.EnumBlockHalf.LOWER ? 1 : 0; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.BLUE + "This is a technical block from Oceanic Expanse. It will transform into " + transferBlock.toString() + " when placed. This is hidden from JEI. You should not have this.");
	}
	
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, HALF, BlockLiquid.LEVEL); }
	
	public static enum EnumBlockHalf implements IStringSerializable
    {
        UPPER, LOWER;

        public String toString()
        { return this.getName();  }

        public String getName()
        { return this == UPPER ? "upper" : "lower"; }
    }
}