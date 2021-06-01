package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sirsquidly.oe.Main;

public class BlockDoubleSeaOats extends BlockBush implements IGrowable, net.minecraftforge.common.IShearable
{
	protected static final AxisAlignedBB TALL_SEAGRASS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	public static final PropertyEnum<BlockDoubleSeaOats.EnumBlockHalf> HALF = PropertyEnum.<BlockDoubleSeaOats.EnumBlockHalf>create("half", BlockDoubleSeaOats.EnumBlockHalf.class);
	public static final PropertyBool SANDY = PropertyBool.create("sandy");
	
	public BlockDoubleSeaOats() {
		super(Material.GRASS);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(Main.OCEANEXPTAB);
		this.setTickRandomly(true);

		setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.LOWER).withProperty(SANDY, false));
	}
	
	public boolean checkTouching(World worldIn, BlockPos pos, boolean doNotTogether)
    {
		for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing != EnumFacing.DOWN && enumfacing != EnumFacing.UP)
            {
                BlockPos blockpos = pos.offset(enumfacing);

                if (doNotTogether && worldIn.getBlockState(blockpos).getBlock() == this)
                { return false; }
                
                if (worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, enumfacing))
                { return true; }
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
        	if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER && state.getValue(SANDY) && this.canPlaceBlockAt(worldIn, pos.down()))
        	{ this.placeAt(worldIn, pos.down(), 2); }	

        	else { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
        }
    }
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state);
        
        if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER && !(state.getValue(SANDY)))
        { return worldIn.getBlockState(pos.down()).getBlock() == this; }
        if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.LOWER && !(state.getValue(SANDY)))
        { return worldIn.getBlockState(pos.up()).getBlock() == this && this.canPlaceBlockAt(worldIn, pos);}
        else
        { return this.canPlaceBlockAt(worldIn, pos); }
    }
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return TALL_SEAGRASS_AABB; }
	
	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { 
		return worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSand && (worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock() ==  this); 
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
    	IBlockState blockBelow = worldIn.getBlockState(pos.down());

    	if (state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.LOWER && !(state.getValue(SANDY)) && this.checkTouching(worldIn, pos, false) && worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0))
        {
    		worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, BlockDoubleSeaOats.EnumBlockHalf.UPPER).withProperty(SANDY, true)); 
    		worldIn.setBlockState(pos, blockBelow);
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

        if (rand.nextInt(this.checkTouching(worldIn, pos, false) ? 10 : 100) == 0)
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

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{ return state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.UPPER ||  state.getValue(HALF) == BlockDoubleSeaOats.EnumBlockHalf.LOWER && worldIn.getBlockState(pos.up()).getBlock() == this; }

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{ return true; }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if (state.getValue(SANDY) && this.canPlaceBlockAt(worldIn, pos.down()))
		{ this.placeAt(worldIn, pos, 2); }
		
		else if (rand.nextInt(2) == 0)
		{ spawnAsEntity(worldIn, pos, new ItemStack(this, 1)); }
	}
}
