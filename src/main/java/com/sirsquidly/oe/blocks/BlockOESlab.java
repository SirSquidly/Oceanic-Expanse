package com.sirsquidly.oe.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOESlab extends BlockSlab
{
	private int flamability;
	private int fireSpread;
	
	public BlockOESlab(Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn)
    {
        this(materialIn, soundIn, hardnessIn, resistenceIn, 0, 0);
    }
	
	public BlockOESlab(Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn, int flamabilityIn, int fireSpreadIn)
    {
        super(materialIn);
        this.setSoundType(soundIn);
        this.useNeighborBrightness = true;
        this.setHardness(hardnessIn);
        this.setResistance(resistenceIn);
		this.setLightOpacity(255);
		this.flamability = flamabilityIn;
		this.fireSpread = fireSpreadIn;
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
	public String getTranslationKey(int meta)
	{
		return getTranslationKey();
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
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return this.flamability; }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return this.fireSpread; }
	
	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) 
	{
		return null;
	}
}