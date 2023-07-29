package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoral extends Block implements IChecksWater
{
	protected static final AxisAlignedBB CORAL_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.9375D, 0.875D);
    public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
    
	public BlockCoral(MapColor blockMapColor, SoundType soundIn) {
		super(Material.ROCK, blockMapColor);
		this.setSoundType(soundIn);
		this.setTickRandomly(ConfigHandler.block.coralBlocks.coralDryTicks == 0 ? false : true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(IN_WATER, false));
	}

	//** This just helps register dead coral faster */
	public BlockCoral()
	{ this(MapColor.GRAY, SoundType.STONE); }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP); }
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) return true;
        return false;
    }

	/** 
     * Basic Block stuff
     * **/
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if (state.getBlock() == OEBlocks.BLUE_CORAL)
    	{ return Item.getItemFromBlock(OEBlocks.BLUE_CORAL_DEAD); }	
    	if (state.getBlock() == OEBlocks.PINK_CORAL)
    	{ return Item.getItemFromBlock(OEBlocks.PINK_CORAL_DEAD); }
    	if (state.getBlock() == OEBlocks.PURPLE_CORAL)
    	{ return Item.getItemFromBlock(OEBlocks.PURPLE_CORAL_DEAD); }
    	if (state.getBlock() == OEBlocks.RED_CORAL)
    	{ return Item.getItemFromBlock(OEBlocks.RED_CORAL_DEAD); }
    	if (state.getBlock() == OEBlocks.YELLOW_CORAL)
    	{ return Item.getItemFromBlock(OEBlocks.YELLOW_CORAL_DEAD); }
    	
    	return super.getItemDropped(state, rand, fortune);
    }
    
    protected boolean canSilkHarvest() { return true; }
    
    @SuppressWarnings("deprecation")
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(IN_WATER) && !Main.proxy.fluidlogged_enable) {
			return Material.WATER;
		}
		return super.getMaterial(state);
	}
    
    @Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(IN_WATER) && !(worldIn.provider.doesWaterVaporize())) 
		{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
		else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
	}
    
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    public boolean isFullCube(IBlockState state)
    { return false; }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED;  }
    
    /** Bounding Box and Collision Swapping **/
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    { return NULL_AABB; }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return CORAL_AABB; }
	
    /** 
     * Metadata and Blockstate conversions. It's just big.
     * **/
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf((meta & 1) == 1)); }

    public int getMetaFromState(IBlockState state)
    { return ((Boolean)state.getValue(IN_WATER)).booleanValue() ? 1 : 0; }
    
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, BlockLiquid.LEVEL, IN_WATER); }
    
    /**
     * Handles the Coral Death and Submerging
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (this.canBlockStay(worldIn, pos, state) && !Main.proxy.fluidlogged_enable) swapWaterProperty(worldIn, pos, state);
    	this.checkForDrop(worldIn, pos, state);
        if (!checkWater(worldIn, pos)) worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	return this.getDefaultState().withProperty(IN_WATER, checkPlaceWater(worldIn, pos, false));
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (!Main.proxy.fluidlogged_enable) swapWaterProperty(worldIn, pos, state);
    	if (!checkWater(worldIn, pos)) worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
		if (this.canBlockStay(worldIn, pos, state)){return true;}
		else {
			if (state.getValue(IN_WATER) && !(worldIn.provider.doesWaterVaporize())) 
			{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
			else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
			return false;
        }
	}
    
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
    
    /** Should be ~6 Seconds **/
    public int tickRate(World worldIn)
    { return ConfigHandler.block.coralBlocks.coralDryTicks; }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean flag = ConfigHandler.block.coralBlocks.coralDryTicks == 0 ? true : checkWater(worldIn, pos);
        boolean w = (Boolean)state.getValue(IN_WATER);
        
        if (!flag)
        {
        	if (state.getBlock() == OEBlocks.BLUE_CORAL)
        	{ worldIn.setBlockState(pos, OEBlocks.BLUE_CORAL_DEAD.getDefaultState().withProperty(IN_WATER, w), 2); }
        	if (state.getBlock() == OEBlocks.PINK_CORAL)
        	{ worldIn.setBlockState(pos, OEBlocks.PINK_CORAL_DEAD.getDefaultState().withProperty(IN_WATER, w), 2); }
        	if (state.getBlock() == OEBlocks.PURPLE_CORAL)
        	{ worldIn.setBlockState(pos, OEBlocks.PURPLE_CORAL_DEAD.getDefaultState().withProperty(IN_WATER, w), 2); }
        	if (state.getBlock() == OEBlocks.RED_CORAL)
        	{ worldIn.setBlockState(pos, OEBlocks.RED_CORAL_DEAD.getDefaultState().withProperty(IN_WATER, w), 2); }
        	if (state.getBlock() == OEBlocks.YELLOW_CORAL)
        	{ worldIn.setBlockState(pos, OEBlocks.YELLOW_CORAL_DEAD.getDefaultState().withProperty(IN_WATER, w), 2); }
        }
    }
}