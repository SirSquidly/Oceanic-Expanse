package com.sirsquidly.oe.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.entity.EntityTurtle;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
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
	public static final PropertyInteger CRACK = PropertyInteger.create("crack", 0, 2);
	public static final PropertyInteger AMOUNT = PropertyInteger.create("amount", 1, 4);
	protected static final AxisAlignedBB[] EGGS_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.4375D, 0.75D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.4375D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.4375D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.4375D, 0.9375D)};
	
	public BlockTurtleEgg() 
	{
		super(Material.GOURD);
		
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AMOUNT, Integer.valueOf(1)).withProperty(CRACK, Integer.valueOf(0)));
	}
	
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (worldIn instanceof WorldServer && entityIn instanceof EntityLivingBase && worldIn.rand.nextInt(100) == 0 && !(entityIn instanceof EntityZombie))
        {
        	onBroken(worldIn, pos, false);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		int partiCheck = ConfigHandler.block.turtleEgg.particlesOnFall;
			
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
		IBlockState iblockstate = world.getBlockState(pos);
		int j = world.getBlockState(pos).getValue(AMOUNT);
		
		int AIbreak = ConfigHandler.block.turtleEgg.amountOnTrample;
		int partiCheck = ConfigHandler.block.turtleEgg.puffOnTrample;
		
		if (world instanceof WorldServer && (partiCheck == 2 || partiCheck !=0 && wasAI))
        {
            ((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 20, 0.02D, 0.02D, 0.02D, 0.05D);
        }
		
		world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		
		if (wasAI && AIbreak >= j || !(wasAI) && j <= 1)
		{ 
			world.setBlockToAir(pos); 
		}
		if (wasAI && AIbreak < j)
		{ 
			world.setBlockState(pos, iblockstate.withProperty(AMOUNT, Integer.valueOf(j - AIbreak)));
		}
		if (j > 1 && j != 0 && !(wasAI))
		{ 
			world.setBlockState(pos, iblockstate.withProperty(AMOUNT, Integer.valueOf(j - 1)));
		}
    }
	
	public boolean checkSand(World world, BlockPos pos) 
	{
		if (world.isRemote) return false;
		if(world.getBlockState(pos.down()).getBlock() instanceof BlockSand) return true;
		return false;
    }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if (checkSand(worldIn, pos))
		{
			int t = (int)(worldIn.getWorldTime() % 24000L);
			if (t >= 21600 && t <= 22550)
			{
				doCrack(worldIn, pos);
			}
			else if (worldIn.rand.nextInt(500) == 0)
			{
				doCrack(worldIn, pos);
			}
		}
    }
	
	// FUNNY
	public void doCrack(World world, BlockPos pos) 
	{
		IBlockState iblockstate = world.getBlockState(pos);
		int j = iblockstate.getValue(CRACK);
		
		if (j < 2)
		{ 
			world.setBlockState(pos, iblockstate.withProperty(CRACK, Integer.valueOf(j + 1)));
		}
		else
		{
			for (int l = 1; l <= iblockstate.getValue(AMOUNT); l++)
			{
				EntityTurtle entityturtle = new EntityTurtle(world);
				entityturtle.setGrowingAge(-24000);
				entityturtle.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
	            world.spawnEntity(entityturtle);
			}
			world.setBlockToAir(pos);
		}
    }
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
        {
        	spawnAsEntity(worldIn, pos, new ItemStack(this, (Integer)state.getValue(AMOUNT), 0));
        }
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
    { return this.getDefaultState().withProperty(AMOUNT, Integer.valueOf((meta & 3) + 1)).withProperty(CRACK, Integer.valueOf((meta & 15) >> 2)); }

    public int getMetaFromState(IBlockState state)
    {
    	int i = 0;
        i = i | (state.getValue(AMOUNT)).intValue() - 1;
        i |= state.getValue(CRACK).intValue() << 2;
        
        return i;
    }
    
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AMOUNT, CRACK); }
}