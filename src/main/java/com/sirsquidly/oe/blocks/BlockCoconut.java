package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.entity.EntityFallingCoconut;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoconut extends Block implements IGrowable
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
	public static final PropertyBool HANGING = PropertyBool.create("hanging");
	
	protected static final AxisAlignedBB[] C_FLOOR_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D), new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D)};
	protected static final AxisAlignedBB[] C_HANG_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.75D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.3125D, 0.625D, 0.3125D, 0.6875D, 1.0D, 0.6875D), new AxisAlignedBB(0.25D, 0.5D, 0.25D, 0.75D, 1.0D, 0.75D)};
    public static boolean fallInstantly;

    public BlockCoconut()
    {
    	super(Material.WOOD);
    	this.setSoundType(SoundType.WOOD);
    	this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(2)).withProperty(HANGING, false));
    	this.setTickRandomly(true);
    	
        setHardness(0.5f);
		setResistance(2.5f);
    }
    
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	if (state.getValue(HANGING))
    	{ return C_HANG_AABB[((Integer)state.getValue(AGE)).intValue()]; }
        return C_FLOOR_AABB[((Integer)state.getValue(AGE)).intValue()];
    }
    
	@SuppressWarnings("deprecation")
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(AGE, Integer.valueOf(2)); }
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
    	if (!worldIn.isRemote && state.getValue(AGE) == 2)
    	{
    		if ((stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword))
            {
                player.addStat(StatList.getBlockStats(this));
                spawnAsEntity(worldIn, pos, new ItemStack(OEItems.COCONUT_OPEN, 2, 0));
            }
            else
            { this.dropBlockAsItem(worldIn, pos, state, 0); }
    	}
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    { worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn)); }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    { worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn)); }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
    	int i = ((Integer)state.getValue(AGE)).intValue();
        if (worldIn.isRemote) return;
        this.checkFallable(worldIn, pos);
        
        if (i < 2 && worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0))
        {
        	this.grow(worldIn, rand, pos, state);
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
        if (i == 2 && worldIn.isAreaLoaded(pos, 1) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(2) == 0))
        {
        	this.grow(worldIn, rand, pos, state);
    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }

    private void checkFallable(World worldIn, BlockPos pos)
    {
    	IBlockState state = worldIn.getBlockState(pos);
    	BlockPos blockpos;
    	
    	if (state.getValue(HANGING))
    	{
    		if ((worldIn.isAirBlock(pos.up()) || canFallThrough(worldIn.getBlockState(pos.up()))) && pos.getY() >= 0)
            {
    			if (state.getValue(AGE) == 2)
                { worldIn.setBlockState(pos, state.withProperty(HANGING, false)); worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));}
    			else {worldIn.setBlockToAir(pos);}
            }
    	}
    	else
    	{
    		if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
            {
    			if (state.getValue(AGE) == 2)
                { 
    				if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
                    {
                        if (!worldIn.isRemote)
                        {
                        	EntityFallingCoconut entityfallingcoconut = new EntityFallingCoconut(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                            this.onStartFalling(entityfallingcoconut);
                            worldIn.spawnEntity(entityfallingcoconut);
                        }
                    }
                    else
                    {
                        worldIn.setBlockToAir(pos);
                        
                        for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down())
                        { ; }
                        if (blockpos.getY() > 0)
                        { worldIn.setBlockState(blockpos.up(), state); }
                    }
                }
    			else {worldIn.setBlockToAir(pos);}
            }
        }
	}

    private void onStartFalling(EntityFallingCoconut fallingcoconut) { }

	public int tickRate(World worldIn)
    { return 2; }

    public static boolean canFallThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(16) == 0)
        {
            BlockPos blockpos = pos.down();
            if (canFallThrough(worldIn.getBlockState(blockpos)) && stateIn.getValue(AGE) == 2)
            {
            	double ranInRange = rand.nextFloat() * (0.65625 - 0.34375) + 0.34375;
                double d0 = (double)((float)pos.getX() + ranInRange);
                double d1 = (double)pos.getY() + 0.5D;
                double d2 = (double)((float)pos.getZ() + ranInRange);
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(stateIn));
            }
        }
    }

	public void onBroken(World world, BlockPos pos) 
	{
		world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
    	
    	if (!world.isRemote)
        { spawnAsEntity(world, pos, new ItemStack(OEItems.COCONUT_OPEN, 2, 0)); }
    }

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) 
	{ int i = state.getValue(AGE); return i < 2 || i == 2 && state.getValue(HANGING); }

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) 
	{ int i = state.getValue(AGE); return i < 2 || i == 2 && state.getValue(HANGING); }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) 
	{
		int i = ((Integer)state.getValue(AGE)).intValue();
		if (i != 2 || i > 2)
		{ 
			if(rand.nextInt(5) <= 1)
			{ worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1))); }
		}
		
		if (i == 2 && state.getValue(HANGING))
		{
			worldIn.setBlockState(pos, state.withProperty(HANGING, false)); 
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}
	}
	
	public boolean isFullCube(IBlockState state)
    { return false; }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }
    
    public boolean isOpaqueCube(IBlockState state)
    { return false; }
    
	public void onEndFalling(World world, BlockPos blockpos1, IBlockState fallTile, IBlockState iblockstate) { }
	
	 public IBlockState getStateFromMeta(int meta)
	    { return this.getDefaultState().withProperty(AGE, Integer.valueOf((meta & 3))).withProperty(HANGING, (meta & 4) != 0); }
	 
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((Integer)state.getValue(AGE)).intValue();
        
        if (((Boolean)state.getValue(HANGING)).booleanValue())
        { i |= 4; }
        return i;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AGE, HANGING); }
}