package com.sirsquidly.oe.blocks;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockTurtleEgg extends Block
{
	public static final PropertyInteger AMOUNT = PropertyInteger.create("amount", 1, 4);
	protected static final AxisAlignedBB[] EGGS_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.4375D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.875D, 0.375D, 0.8125D), new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.4375D, 0.875D)};
	
	public BlockTurtleEgg() 
	{
		super(Material.GOURD);
		
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AMOUNT, Integer.valueOf(1)));
	}
	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		int partiCheck = ConfigHandler.particlesOnFall;
			
		if (worldIn instanceof WorldServer && (partiCheck == 2 && entityIn instanceof EntityLivingBase || partiCheck !=0 && entityIn instanceof EntityZombie))
        {
			((WorldServer)worldIn).spawnParticle(EnumParticleTypes.ITEM_CRACK, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 16, 0.01D, 0.01D, 0.01D, 0.15D, Item.getIdFromItem(Item.getItemFromBlock(this)));
        }
		
		worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
		
		if(!worldIn.isRemote && worldIn.rand.nextInt(3) == 0 && entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityZombie))
		{  onBroken(worldIn, pos, false); }
		
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }
	
	public void onBroken(World world, BlockPos pos, boolean wasAI) 
	{
		int j = world.getBlockState(pos).getValue(AMOUNT);
		
		int AIbreak = ConfigHandler.amountOnTrample;
		int partiCheck = ConfigHandler.puffOnTrample;
		
		if (world instanceof WorldServer && (partiCheck == 2 || partiCheck !=0 && wasAI))
        {
            ((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 20, 0.02D, 0.02D, 0.02D, 0.05D);
        }
		
		world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		
		if (wasAI && AIbreak >= j || !(wasAI) && j <= 1)
		{ world.setBlockToAir(pos); }
		if (wasAI && AIbreak < j)
		{ world.setBlockState(pos, OEBlocks.SEA_TURTLE_EGG.getDefaultState().withProperty(AMOUNT, Integer.valueOf(j) - AIbreak), 2); }
		if (j > 1 && j != 0 && !(wasAI))
		{ world.setBlockState(pos, OEBlocks.SEA_TURTLE_EGG.getDefaultState().withProperty(AMOUNT, Integer.valueOf(j) - 1), 2); }
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return EGGS_AABB[((Integer)state.getValue(AMOUNT)).intValue() - 1]; }
	
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    { return EGGS_AABB[((Integer)state.getValue(AMOUNT)).intValue() - 1]; }
	
	public boolean isFullCube(IBlockState state)
    { return false; }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }
    
    public boolean isOpaqueCube(IBlockState state)
    { return false; }
    
	public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(AMOUNT, Integer.valueOf((meta & 3) + 1)); }

    public int getMetaFromState(IBlockState state)
    {
    	int i = 0;
        i = i | (state.getValue(AMOUNT)).intValue() - 1;

        return i;
    }
    
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AMOUNT); }
}