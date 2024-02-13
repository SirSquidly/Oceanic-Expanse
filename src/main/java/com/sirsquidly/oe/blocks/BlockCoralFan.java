package com.sirsquidly.oe.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoralFan extends Block implements IChecksWater
{
	protected static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.125D, 1.0D, 0.125D, 0.875D, 0.75D, 0.875D);
    protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0625D, 0.3125D, 0.5D, 0.9375D, 0.6875D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0625D, 0.3125D, 0.0D, 0.9375D, 0.6875D, 0.5D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.5D, 0.3125D, 0.0625D, 1.0D, 0.6875D, 0.9375D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.3125D, 0.0625D, 0.5D, 0.6875D, 0.9375D);
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
	
	@SuppressWarnings("deprecation")
	public BlockCoralFan(MapColor blockMapColor, SoundType soundIn) 
	{
		super(Material.ROCK, blockMapColor);
		this.setSoundType(soundIn);
		this.setTickRandomly(ConfigHandler.block.coralBlocks.coralFanDryTicks == 0 ? false : true);
		this.setLightOpacity(Blocks.WATER.getLightOpacity(Blocks.WATER.getDefaultState()));
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IN_WATER, false));
	}
	
	//** This just helps register dead coral faster */
	public BlockCoralFan()
	{ this(MapColor.GRAY, SoundType.STONE); }
		
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    { return canPlaceBlock(worldIn, pos, side); }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (canPlaceBlock(worldIn, pos, enumfacing))
            { return true; }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
	protected static boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, direction) == BlockFaceShape.SOLID;
        Block block = iblockstate.getBlock();

        if (direction == EnumFacing.UP)
        {
            return iblockstate.isTopSolid() || !isExceptionBlockForAttaching(block) && flag;
        }
        else
        {
            return !isExceptBlockForAttachWithPiston(block) && flag;
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	boolean isWet = checkWater(worldIn, pos);
        return canPlaceBlock(worldIn, pos, facing) ? this.getDefaultState().withProperty(FACING, facing).withProperty(IN_WATER, isWet) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(IN_WATER, isWet);
    }


    public void placeAt(World worldIn, BlockPos pos, Random rand, Block block, boolean onlyOnCoral)
    {
    	List<EnumFacing> list = Lists.newArrayList(EnumFacing.values());
        Collections.shuffle(list, rand);
        
		for (EnumFacing enumfacing : list)
        {
			BlockPos checkPos = pos.offset(enumfacing);
			if (worldIn.getBlockState(checkPos).isSideSolid(worldIn, checkPos, enumfacing) && ( !onlyOnCoral || checkCoralBlock(worldIn.getBlockState(checkPos))))
            {  worldIn.setBlockState(pos, block.getDefaultState().withProperty(FACING, enumfacing.getOpposite()).withProperty(IN_WATER, true), 16 | 2); }
        }
    }
    
    public boolean checkCoralBlock(IBlockState state)
    {
        if (state.getBlock() == OEBlocks.BLUE_CORAL_BLOCK || state.getBlock() == OEBlocks.PINK_CORAL_BLOCK || state.getBlock() == OEBlocks.PURPLE_CORAL_BLOCK || state.getBlock() == OEBlocks.RED_CORAL_BLOCK || state.getBlock() == OEBlocks.YELLOW_CORAL_BLOCK)
    	{ return true; }
        
        if (state.getBlock() == OEBlocks.BLUE_CORAL_BLOCK_DEAD || state.getBlock() == OEBlocks.PINK_CORAL_BLOCK_DEAD || state.getBlock() == OEBlocks.PURPLE_CORAL_BLOCK_DEAD || state.getBlock() == OEBlocks.RED_CORAL_BLOCK_DEAD || state.getBlock() == OEBlocks.YELLOW_CORAL_BLOCK_DEAD)
    	{ return true; }
        
        return false;
    }
    
	/** 
     * Basic Block stuff
     * **/
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if (state.getBlock() == OEBlocks.BLUE_CORAL_FAN)
    	{ return Item.getItemFromBlock(OEBlocks.BLUE_CORAL_FAN_DEAD); }	
    	if (state.getBlock() == OEBlocks.PINK_CORAL_FAN)
    	{ return Item.getItemFromBlock(OEBlocks.PINK_CORAL_FAN_DEAD); }
    	if (state.getBlock() == OEBlocks.PURPLE_CORAL_FAN)
    	{ return Item.getItemFromBlock(OEBlocks.PURPLE_CORAL_FAN_DEAD); }
    	if (state.getBlock() == OEBlocks.RED_CORAL_FAN)
    	{ return Item.getItemFromBlock(OEBlocks.RED_CORAL_FAN_DEAD); }
    	if (state.getBlock() == OEBlocks.YELLOW_CORAL_FAN)
    	{ return Item.getItemFromBlock(OEBlocks.YELLOW_CORAL_FAN_DEAD); }
    	
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
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    /** Bounding Box and Collision Swapping **/
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        switch (enumfacing)
        {
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
            default:
                return AABB_NORTH;
            case UP:
                return AABB_UP;
            case DOWN:
                return AABB_DOWN;
        }
    }
	
    /** 
     * Metadata and Blockstate conversions. It's just big.
     * **/
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing;
        switch (meta & 7)
        {
            case 0:
                enumfacing = EnumFacing.DOWN;
                break;
            case 1:
                enumfacing = EnumFacing.EAST;
                break;
            case 2:
                enumfacing = EnumFacing.WEST;
                break;
            case 3:
                enumfacing = EnumFacing.SOUTH;
                break;
            case 4:
                enumfacing = EnumFacing.NORTH;
                break;
            case 5:
            default:
                enumfacing = EnumFacing.UP;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(IN_WATER, Boolean.valueOf((meta & 8) > 0));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i;
        switch ((EnumFacing)state.getValue(FACING))
        {
            case EAST:
                i = 1;
                break;
            case WEST:
                i = 2;
                break;
            case SOUTH:
                i = 3;
                break;
            case NORTH:
                i = 4;
                break;
            case UP:
            default:
                i = 5;
                break;
            case DOWN:
                i = 0;
        }
        if (((Boolean)state.getValue(IN_WATER)).booleanValue())
        { i |= 8; }

        return i;
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    { return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING))); }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    { return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING))); }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL, FACING, IN_WATER}); }
    
    /**
     * Handles the Coral Death and Submerging
     */

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (this.canBlockStay(worldIn, pos, state) && !Main.proxy.fluidlogged_enable) swapWaterProperty(worldIn, pos, state);
    	this.checkForDrop(worldIn, pos, state);
        if (!checkWater(worldIn, pos)) worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
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
    
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (this.canPlaceBlockAt(worldIn, pos) && canPlaceBlock(worldIn, pos, (EnumFacing)state.getValue(FACING))) return true;
        return false;
    }
    
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
    
    /** Should be ~6 Seconds **/
    public int tickRate(World worldIn)
    {
        return ConfigHandler.block.coralBlocks.coralFanDryTicks;
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean flag = ConfigHandler.block.coralBlocks.coralFanDryTicks == 0 ? true : checkWater(worldIn, pos);
        
        if (!flag)
        {
        	this.makeDead(worldIn, pos, state, rand);
        }
    }
    
    public void makeDead(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean w = (Boolean)state.getValue(IN_WATER);
        EnumFacing f = (EnumFacing)state.getValue(FACING);
        
        if (state.getBlock() == OEBlocks.BLUE_CORAL_FAN)
    	{ worldIn.setBlockState(pos, OEBlocks.BLUE_CORAL_FAN_DEAD.getDefaultState().withProperty(FACING, f).withProperty(IN_WATER, w), 2); }
    	if (state.getBlock() == OEBlocks.PINK_CORAL_FAN)
    	{ worldIn.setBlockState(pos, OEBlocks.PINK_CORAL_FAN_DEAD.getDefaultState().withProperty(FACING, f).withProperty(IN_WATER, w), 2); }
    	if (state.getBlock() == OEBlocks.PURPLE_CORAL_FAN)
    	{ worldIn.setBlockState(pos, OEBlocks.PURPLE_CORAL_FAN_DEAD.getDefaultState().withProperty(FACING, f).withProperty(IN_WATER, w), 2); }
    	if (state.getBlock() == OEBlocks.RED_CORAL_FAN)
    	{ worldIn.setBlockState(pos, OEBlocks.RED_CORAL_FAN_DEAD.getDefaultState().withProperty(FACING, f).withProperty(IN_WATER, w), 2); }
    	if (state.getBlock() == OEBlocks.YELLOW_CORAL_FAN)
    	{ worldIn.setBlockState(pos, OEBlocks.YELLOW_CORAL_FAN_DEAD.getDefaultState().withProperty(FACING, f).withProperty(IN_WATER, w), 2); }
    }
}