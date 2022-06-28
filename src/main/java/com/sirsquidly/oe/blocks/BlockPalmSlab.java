package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockPalmSlab extends BlockSlab
{
	public BlockPalmSlab()
    {
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        setHardness(2.0F);
		setResistance(5.0F);
		setLightOpacity(255);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        if (!this.isDouble())
        {
            return this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]);
        }

        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
    	if(this.isDouble())
    		return 0;
    	
    	return ((EnumBlockHalf)state.getValue(HALF)).ordinal() + 1;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
    	//this.isDouble() ? new BlockStateContainer(this, new IProperty[] {}) : 
        return new BlockStateContainer(this, new IProperty[] {HALF});
    }

	@Override
	public String getUnlocalizedName(int meta)
	{
		return getUnlocalizedName();
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}
	 
	@Override
	public IProperty<?> getVariantProperty()
	{
		return HALF;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) 
	{
		return null;
	}
}