package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSand;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
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

public class BlockDoubleSeaOats extends BlockBush implements IGrowable, net.minecraftforge.common.IShearable
{
	protected static final AxisAlignedBB LOWER_SEA_OATS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	protected static final AxisAlignedBB UPPER_SEA_OATS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
	protected static final AxisAlignedBB SHORT_SEA_OATS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.6875D, 0.875D);
	public static final PropertyEnum<BlockDoubleSeaOats.EnumBlockHalf> HALF = PropertyEnum.<BlockDoubleSeaOats.EnumBlockHalf>create("half", BlockDoubleSeaOats.EnumBlockHalf.class);
	public static final PropertyBool SANDY = PropertyBool.create("sandy");
	
	public BlockDoubleSeaOats() {
		super(Material.GRASS);
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);

		setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.LOWER).withProperty(SANDY, false));
	}
	
	public int checkTouching(World worldIn, BlockPos pos, boolean doNotTogether)
    {
		int i = 0;
		
		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
			BlockPos blockpos = pos.offset(enumfacing);

            if (doNotTogether && worldIn.getBlockState(blockpos).getBlock() == this)
            { continue; }
            
            if (worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, enumfacing))
            { i++; }
        }
		return i;
    }
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();
		boolean isUpper = state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER;
		
		if (item == Items.SHEARS && !state.getValue(SANDY) && ConfigHandler.block.seaOats.seaOatShears)
        {
			if (state.getBlock() == this)
			{
				worldIn.setBlockState(isUpper ? pos : pos.up(), Blocks.AIR.getDefaultState(), 3);
				worldIn.setBlockState(isUpper ? pos.down() : pos, this.getDefaultState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.UPPER).withProperty(SANDY, true), 3);
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
				itemstack.damageItem(1, playerIn);
				return true;
			}
        }
		else if (item instanceof ItemSpade)
        {
			if ((isUpper && worldIn.getBlockState(pos.down()).getBlock() == this && worldIn.getBlockState(pos.down()).getValue(SANDY)) || (!isUpper && state.getValue(SANDY) ))
			{ 
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				
				Random rand = worldIn.rand;
			    for (int i = 0; i < 50; ++i)
			    {
			    	double speedX = ((double)rand.nextFloat() - 0.5D) * 0.3D;
			     	double speedY = ((double)rand.nextFloat() - 0.5D) * 0.3D;
			     	double speedZ = ((double)rand.nextFloat() - 0.5D) * 0.3D;
			     	worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + rand.nextDouble(), pos.getY() - (isUpper ? 1 : 0) + rand.nextDouble(), pos.getZ() + rand.nextDouble(), speedX, speedY, speedZ, Block.getStateId(worldIn.getBlockState(pos.down(isUpper ? 2 : 1))));
			    }
			    itemstack.damageItem(1, playerIn);
			    ItemStack blockBelow = new ItemStack(worldIn.getBlockState(pos.down(isUpper ? 2 : 1)).getBlock(), 1, worldIn.getBlockState(pos.down(isUpper ? 2 : 1)).getBlock().getMetaFromState(worldIn.getBlockState(pos.down(isUpper ? 2 : 1))));
			    
				spawnAsEntity(worldIn, isUpper ? pos.down() : pos, blockBelow);
				worldIn.setBlockState(isUpper ? pos.down() : pos, this.getDefaultState().withProperty(SANDY, false), 3);
				return true;
			}
		
        }
		return false;
    }
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{ worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
        	worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state);
        boolean upper = state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER;
        
        if (state.getValue(SANDY) && upper)
        { return worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSand; }
        
        if (upper)
        { return worldIn.getBlockState(pos.down()).getBlock() == this; }
        else
        { return worldIn.getBlockState(pos.up()).getBlock() == this && worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSand; }
    }
	
	// Me when I REALLY didn't want to use an if check for literally no reason
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { 
		return state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER ? state.getValue(SANDY) ? SHORT_SEA_OATS_AABB : UPPER_SEA_OATS_AABB : LOWER_SEA_OATS_AABB;
    }
	
	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { 
		return worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSand && worldIn.isAirBlock(pos) && (worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock() == this); 
    }
	
	@Override
	protected boolean canSustainBush(IBlockState state) 
	{ 
		return state.getBlock() instanceof BlockSand || state.getBlock() == this;
	}

	public void placeAt(World worldIn, BlockPos lowerPos, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.LOWER), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.UPPER), flags);
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	//IBlockState blockBelow = worldIn.getBlockState(pos.down());

    	if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.LOWER && !(state.getValue(SANDY)) && this.checkTouching(worldIn, pos, false) >= 1 && worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0))
        {
    		worldIn.setBlockState(pos, this.getDefaultState().withProperty(SANDY, true));
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }
    
	// Just used the placeAt, less typing
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    { this.placeAt(worldIn, pos, 2); }
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(HALF, (meta & 1) == 0 ? EnumBlockHalf.UPPER : EnumBlockHalf.LOWER).withProperty(SANDY, (meta & 4) != 0); }

    @Override
    public int getMetaFromState(IBlockState state)
    { 
    	int i = 0;
    	if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.LOWER)
        { i |= 1; }
    	
    	if (((Boolean)state.getValue(SANDY)).booleanValue())
        { i |= 4; }
    	
    	return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] {HALF, SANDY}); }
    
    /**
     * Shearing stuffs.
     */
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(HALF) == EnumBlockHalf.LOWER;
    }
	
    public java.util.List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    { return java.util.Arrays.asList(new ItemStack(this, 2)); }
    
    @Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(this, 2, 0));
        }
        else
        { super.harvestBlock(worldIn, player, pos, state, te, stack); }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	IBlockState blockDown = worldIn.getBlockState(pos.down(stateIn.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER ? 2 : 1));
    	
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);

        if (rand.nextInt(this.checkTouching(worldIn, pos, false) > 0 ? 10 : 100) == 0)
        { worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, d0, d1, d2, 0, 0, 0, Block.getStateId(blockDown)); }
    }
    
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Items.AIR; }
	
	public static enum EnumBlockHalf implements IStringSerializable
    {
        UPPER,
        LOWER;

        public String toString()
        { return this.getName(); }

        public String getName()
        { return this == UPPER ? "upper" : "lower"; }
    }

	public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{ return true; }

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{ return true; }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if (state.getValue(SANDY) && worldIn.isAirBlock(pos.up()))
		{ this.placeAt(worldIn, pos, 2); }
		
		else if (rand.nextInt(2) == 0)
		{ spawnAsEntity(worldIn, pos, new ItemStack(this, 1)); }
	}
}
