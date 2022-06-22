package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoconutLeaves extends BlockLeaves
{
	public BlockCoconutLeaves() 
	{
		super();
		setLightOpacity(2);
	}
	
	@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(CHECK_DECAY, Boolean.valueOf(false)).withProperty(DECAYABLE, Boolean.valueOf(false));
    }
	
	protected ItemStack getSilkTouchDrop(IBlockState state)
    { return new ItemStack(Item.getItemFromBlock(this), 1, 0); }

    @Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    { return 60; }

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{ return 30; }

    public int damageDropped(IBlockState state)
    { return 0; }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(OEBlocks.COCONUT_SAPLING);
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this, 1));
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() instanceof ItemShears)
        { player.addStat(StatList.getBlockStats(this)); }
        else
        { super.harvestBlock(worldIn, player, pos, state, te, stack); }
    }
    
    @Override
    public EnumType getWoodType(int meta)
    { return null; }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.LEAVES.getBlockLayer();
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    { return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side); }
    
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0)); }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
        { i |= 4; }
        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        { i |= 8; }
        return i;
    }
    
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE}); }
}