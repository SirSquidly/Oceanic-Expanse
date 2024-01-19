package com.sirsquidly.oe.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.tileentity.TilePrismarinePot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPrismarinePot extends BlockContainer implements IChecksWater
{
	protected static final AxisAlignedBB PRISMARINE_POT = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
	
	public static final PropertyEnum<BlockPrismarinePot.EnumAxis> FACING = PropertyEnum.<BlockPrismarinePot.EnumAxis>create("axis", BlockPrismarinePot.EnumAxis.class);
	//public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
	public static final PropertyBool SEALED = PropertyBool.create("sealed");
	
	public BlockPrismarinePot()
	{
		super(Material.WATER);
		this.hasTileEntity = true;
		this.setSoundType(SoundType.STONE);
		this.setHardness(2.5f);
		this.setResistance(2.5f);
		this.setHarvestLevel("pickaxe", 0);
		setDefaultState(blockState.getBaseState().withProperty(FACING, BlockPrismarinePot.EnumAxis.X).withProperty(SEALED, false));
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
		if (blockState.getValue(SEALED))
			return 10.0F;
        return super.getBlockHardness(blockState, worldIn, pos);
    }
	
	@Override
	@Deprecated
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(IN_WATER) && !Main.proxy.fluidlogged_enable) return super.getMaterial(state);
		return Material.GROUND;
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if (!state.getValue(SEALED))
		{
			if (worldIn.isRemote)
	        { return true; }
	        else
	        {
	    		if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
	    		{
	    			if (!playerIn.capabilities.isCreativeMode) itemstack.shrink(1);
	    			
	    			worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.25F);
	    			
	    			worldIn.setBlockState(pos, state.withProperty(SEALED, true), 3);
	    			return true;
	    		}
	    		
	            TileEntity tileentity = worldIn.getTileEntity(pos);

	            if (tileentity instanceof TilePrismarinePot)
	            {
	                playerIn.openGui(Main.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
	            }

	            return true;
	        }
		}
		else
		{
			if (item instanceof ItemAxe)
    		{
    			worldIn.setBlockState(pos, state.withProperty(SEALED, false), 3);
    			worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.25F);
    			
    			Random rand = worldIn.rand;
    			
    			/** This code handles an extra particle puff when unplugging a pot. We first skip if a solid block above would block particles anyway */
    			if (!worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.UP))
    			{
    				for (int i = 0; i < 5; ++i)
    			    {
    					/** Swap the particle to bubbles if the space above is Water. Otherwise, Cloud. */
        				EnumParticleTypes part = worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.CLOUD;
    			    	double speedX = ((double)rand.nextFloat() - 0.5D) * 0.1D;
    			     	double speedY = ((double)rand.nextFloat() - 0.5D) * 0.1D;
    			     	double speedZ = ((double)rand.nextFloat() - 0.5D) * 0.1D;
    			     	worldIn.spawnParticle(part, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, speedX, -speedY, speedZ);
    			    }
    			}
    			return true;
    		}
		}
		return false;
    }
	
	protected boolean canSilkHarvest() { return true; }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Items.AIR; }
	
	@Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        super.getDrops(drops, world, pos, state, 0);

        for (int i = 0; i < Math.min(3 + fortune, 7); ++i)
        {
        	drops.add(new ItemStack(Items.PRISMARINE_SHARD, 1, 0));
        }
    
    }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    { 
		if (!Main.proxy.fluidlogged_enable) swapWaterProperty(worldIn, pos, state);
		
		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePrismarinePot)
        {
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }
	
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	BlockPrismarinePot.EnumAxis convertFacing = placer.getHorizontalFacing().getOpposite() != EnumFacing.NORTH ? placer.getHorizontalFacing().getOpposite() != EnumFacing.SOUTH ? BlockPrismarinePot.EnumAxis.X : BlockPrismarinePot.EnumAxis.Z : BlockPrismarinePot.EnumAxis.Z;
    	
    	if (!checkWater(worldIn, pos)) return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(false)).withProperty(FACING, convertFacing);
    	return this.getDefaultState().withProperty(IN_WATER, Boolean.valueOf(true)).withProperty(FACING, convertFacing);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePrismarinePot)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TilePrismarinePot)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        worldIn.removeTileEntity(pos);
        super.breakBlock(worldIn, pos, state);
	
    }

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		this.dropBlockAsItem(worldIn, pos, state, 0);
		if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
	}
	
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    { return false; }
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ //if (state.getValue(IN_WATER)) worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); 
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{ return true; }
	
	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
	{
		return new TilePrismarinePot();
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    }
	
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.75D, 0.75D));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3125D, 0.75D, 0.3125D, 0.6875D, 1.0D, 0.6875D));
        if (state.getValue(SEALED)) addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.375D, 1.0D, 0.375D, 0.625D, 1.125D, 0.625D));
    }
	
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    public boolean isFullCube(IBlockState state)
    { return false; }

	public EnumBlockRenderType getRenderType(IBlockState state)
    { return EnumBlockRenderType.MODEL; }
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    { return BlockRenderLayer.CUTOUT; }
	
	/**
     * Convert the BlockState into the correct metadata value
     */
	public IBlockState getStateFromMeta(int meta)
    {
		BlockPrismarinePot.EnumAxis enumfacing$axis = BlockPrismarinePot.EnumAxis.X;

        if ((meta & 1) != 0)
        { enumfacing$axis = BlockPrismarinePot.EnumAxis.Z; }

        return this.getDefaultState().withProperty(FACING, enumfacing$axis).withProperty(SEALED, (meta & 4) != 0).withProperty(IN_WATER, (meta & 8) != 0);
    }
    
    public int getMetaFromState(IBlockState state)
    {
    	int i = 0;
    	
        if ((BlockPrismarinePot.EnumAxis)state.getValue(FACING) == BlockPrismarinePot.EnumAxis.Z)
        { i = 1; }
        
    	if (((Boolean)state.getValue(SEALED)).booleanValue())
        { i |= 4; }

	    if (((Boolean)state.getValue(IN_WATER)).booleanValue())
        { i |= 8; }
	    
	    return i;
    }

    
    
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLiquid.LEVEL, FACING, SEALED, IN_WATER);
	}
	
	public static enum EnumAxis implements IStringSerializable
    {
        X("x"),
        Z("z");
        private final String name;

        private EnumAxis(String name)
        { this.name = name; }

        public String toString()
        { return this.name; }

        public static BlockPrismarinePot.EnumAxis fromFacingAxis(EnumFacing.Axis axis)
        {
            switch (axis)
            {
            	default:
                case X:
                    return X;
                case Z:
                    return Z;
            }
        }

        public String getName()
        { return this.name; }
    }
}