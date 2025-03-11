package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

public class BlockSeagrasss extends BlockBush implements IGrowable, IChecksWater
{
	protected static final AxisAlignedBB SEAGRASS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D);
	protected static final AxisAlignedBB TALL_SEAGRASS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	/** 0 = normal Seagrass, 1 = bottom half, 2 = top half*/
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);
	
	@SuppressWarnings("deprecation")
	public BlockSeagrasss()
	{
		super(Material.WATER);
		this.setSoundType(OESounds.WET_GRASS);
		this.setLightOpacity(Blocks.WATER.getLightOpacity(Blocks.WATER.getDefaultState()));
		setDefaultState(blockState.getBaseState().withProperty(TYPE, 0));
	}
	 
	@SuppressWarnings("deprecation")
	public Material getMaterial(IBlockState state)
	{ return Main.proxy.fluidlogged_enable ? Material.PLANTS : super.getMaterial(state); }
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return state.getValue(TYPE) == 0 ? SEAGRASS_AABB : TALL_SEAGRASS_AABB; }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && checkPlaceWater(worldIn, pos, true);
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        if (checkSurfaceWater(worldIn, pos, state)) return false;
        
        switch (state.getValue(TYPE))
        {
        	case 0:
        		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
        	case 1:
                return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && worldIn.getBlockState(pos.up()).getBlock() == this;
        	case 2:
        		if (!checkWater(worldIn, pos) && !Main.proxy.fluidlogged_enable) return false;
                return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        
        return false;
    }

	public void placeAt(World worldIn, BlockPos lowerPos, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(TYPE, 1), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(TYPE, 2), flags);
    }

	// Just used the placeAt, less typing
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    { if (stack.getMetadata() > 0) this.placeAt(worldIn, pos, 2); }
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(TYPE, Integer.valueOf(meta)); }

    @Override
    public int getMetaFromState(IBlockState state)
    { return state.getValue(TYPE); }
    
    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, TYPE, BlockLiquid.LEVEL); }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            boolean flag = state.getValue(TYPE) == 2;
            BlockPos blockpos = flag ? pos : pos.up();
            BlockPos blockpos1 = flag ? pos.down() : pos;
            Block block = (Block)(flag ? this : worldIn.getBlockState(blockpos).getBlock());
            Block block1 = (Block)(flag ? worldIn.getBlockState(blockpos1).getBlock() : this);
            
            if (block == this)
            {
                worldIn.setBlockState(blockpos, Blocks.WATER.getDefaultState(), 2);
            }

            if (block1 == this)
            {
                worldIn.setBlockState(blockpos1, Blocks.WATER.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Bonemeal Growing
     */
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return checkPlaceWater(worldIn, pos.up(), true) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && ConfigHandler.block.seagrass.enableTallSeagrass; }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return checkPlaceWater(worldIn, pos.up(), true) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP); }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { this.placeAt(worldIn, pos, 2); }
    
    /**
     * Shearing stuffs.
     */
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    { return world.getBlockState(pos).getValue(TYPE) != 2; }
	
    public java.util.List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    { return java.util.Arrays.asList(new ItemStack(this, 2)); }
    
    @Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() instanceof ItemShears && ConfigHandler.block.seagrass.enableSeagrass)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(this, state.getValue(TYPE) == 0 ? 1 : 2, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Items.AIR; }
}