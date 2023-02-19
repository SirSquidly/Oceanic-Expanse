package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDulse extends BlockBush implements IGrowable, IChecksWater
{
	/** Note that Age 4 is exclusive for a top-half block. So, at age 3, a top block (at age 4 for rendering purposes) is also added.*/
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);
	public static final PropertyBool SHEARED = PropertyBool.create("sheared");
	
	protected static final AxisAlignedBB[] DULSE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.5625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.8125D, 0.8125D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};
	
	public BlockDulse() {
		super(Material.WATER);
		this.setSoundType(SoundHandler.WET_GRASS);
		this.setTickRandomly(true);

		setDefaultState(blockState.getBaseState().withProperty(SHEARED, false));
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return DULSE_AABB[((Integer)state.getValue(AGE)).intValue()];
    }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && checkWater(worldIn, pos);
    }
	
	/** Checks if the Double tall 4th age crop can be placed here.*/
	public boolean canPlaceFullAge(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && checkWater(worldIn, pos) && checkWater(worldIn, pos.up());
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (!checkWater(worldIn, pos)) return false;
        if (worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) return true;
        return false;
    }
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{
			if (state.getValue(AGE).intValue() != 4 || (state.getValue(AGE).intValue() == 4 && worldIn.getBlockState(pos.down()).getBlock() != this))
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
        if (worldIn.isRemote || (Boolean)state.getValue(SHEARED).booleanValue()) return;
        
        if (worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0))
        {
        	this.grow(worldIn, rand, pos, state);
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		((Integer)state.getValue(AGE)).intValue();
		
		if (!((Boolean)state.getValue(SHEARED)).booleanValue() && ConfigHandler.block.driedKelpShears)
        {
			if (item == Items.SHEARS)
	        {
				worldIn.setBlockState(pos, state.withProperty(SHEARED, true));
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
	            return true;
	        }
        }
		return false;
    }
	
	public void placeAt(World worldIn, BlockPos lowerPos, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(SHEARED, true).withProperty(AGE, 3), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(SHEARED, true).withProperty(AGE, Integer.valueOf(4)));
    }
	
	/**
     * Bonemeal Growing
     */
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { 
    	if (state.getValue(AGE).intValue() > 2) return false;
		return worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER; 
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { 
    	if (state.getValue(AGE).intValue() > 2) return false;
    	return worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	int i = ((Integer)state.getValue(AGE)).intValue();
    	
    	if(rand.nextInt(5) < 100 && i != 4 && !(Boolean)state.getValue(SHEARED).booleanValue())
		{ 
    		if (i < 2) worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)));
    		if (i == 2 && this.canPlaceFullAge(worldIn, pos))
    		{ this.placeAt(worldIn, pos, 2); }
		}
    }

    @Override public int quantityDropped(IBlockState state, int fortune, Random random){ return (Math.min(4, (Integer)state.getValue(AGE) + 1)); }
	
	public IBlockState getStateFromMeta(int meta)
    {
		int gogle = meta;
		
		if (meta > 4)
		{ gogle -= 8; }
		
		return this.getDefaultState().withProperty(AGE, Integer.valueOf((gogle))).withProperty(SHEARED, (meta & 8) != 0);
    }
 
	public int getMetaFromState(IBlockState state)
	{
	    int i = 0;
	    i = i | ((Integer)state.getValue(AGE)).intValue();
	    
	    if (((Boolean)state.getValue(SHEARED)).booleanValue())
	    { i |= 8; }
	    return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLiquid.LEVEL, AGE, SHEARED);
	}
}