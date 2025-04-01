package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.SoundType;
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
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTubeSponge extends BlockBush implements IChecksWater
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
	public static final PropertyBool SHEARED = PropertyBool.create("sheared");
	
	protected static final AxisAlignedBB[] TUBE_SPONGE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.28125D, 0.0D, 0.28125D, 0.71875D, 0.5D, 0.71875D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.75D, 0.75D), new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.9375D, 0.875D)};
	
	@SuppressWarnings("deprecation")
	public BlockTubeSponge() {
		super(Material.WATER);
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
		this.setLightOpacity(Blocks.WATER.getLightOpacity(Blocks.WATER.getDefaultState()));
		setDefaultState(blockState.getBaseState().withProperty(SHEARED, false));
	}

	@SuppressWarnings("deprecation")
	public Material getMaterial(IBlockState state)
	{
		if(Main.proxy.fluidlogged_enable) { return Material.PLANTS; }
		return super.getMaterial(state);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return TUBE_SPONGE_AABB[(Integer) state.getValue(AGE)]; }
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && isPositionUnderwater(worldIn, pos); }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		if (!isPositionUnderwater(worldIn, pos)) return false;
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
	}
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) 
		{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }
	}
	
	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) 
	{ worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
        if (worldIn.isRemote) return;
        
        if (canGrow(worldIn, pos, state) && worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow(worldIn, pos, state)))
        {
        	int i = (Integer) state.getValue(AGE);
        	
        	if(i < 2 && (this.checkSponge(worldIn, pos) ? rand.nextInt(10) : rand.nextInt(40)) == 0)
    		{ worldIn.setBlockState(pos, state.withProperty(AGE, i + 1)); }
        	
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if (!(Boolean) state.getValue(SHEARED) && ConfigHandler.block.tubeSponge.tubeSpongeShears)
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
	
	public boolean checkSponge(World worldIn, BlockPos pos)
    {
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSponge) return true;
		return false;
    }
	
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state)
    { return (worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || Main.proxy.fluidlogged_enable) && state.getValue(AGE) < 2; }
    
	/**
     * Shearing stuffs.
     */
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() instanceof ItemShears)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(this, (Integer)state.getValue(AGE) == 2 ? 2 : 1, 0));
        }
        else
        { super.harvestBlock(worldIn, player, pos, state, te, stack); }
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(AGE, Integer.valueOf((meta & 3))).withProperty(SHEARED, (meta & 4) != 0); }
 
	public int getMetaFromState(IBlockState state)
	{
	    int i = 0;
	    i = i | (Integer) state.getValue(AGE);
	    
	    if ((Boolean) state.getValue(SHEARED))
	    { i |= 4; }
	    return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{ return new BlockStateContainer(this, BlockLiquid.LEVEL, AGE, SHEARED); }
}