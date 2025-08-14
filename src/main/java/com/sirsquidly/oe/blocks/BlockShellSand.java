package com.sirsquidly.oe.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockShellSand extends BlockFalling
{
	public BlockShellSand()
    {
        super(Material.SAND);
        this.setSoundType(SoundType.SAND);
    }
	
	@SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state)
    { return -2370656; }
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if (item instanceof ItemSpade)
        {
			if (!playerIn.world.isRemote) playerIn.world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(worldIn.getBlockState(pos)));

		    spawnPickedItems(worldIn, pos, facing);
		    
		    worldIn.setBlockState(pos, Blocks.SAND.getDefaultState());
		    itemstack.damageItem(1, playerIn);
		    
            return true;
        }
		return false;
    }
	
	/** Spawns the item from the Shell Sand Loot table. */
	public void spawnPickedItems(World worldIn, BlockPos pos, @Nullable EnumFacing facing)
	{
		if (!worldIn.isRemote)
    	{
			Random rand = worldIn.rand;
    		LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)worldIn);
        	List<ItemStack> result = worldIn.getLootTableManager().getLootTableFromLocation(LootTableHandler.GAMEPLAY_SHELL_COMB).generateLootForPools(rand, lootcontext$builder.build());
        	
    		for (ItemStack lootItem : result)
            { 
            	double d0 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d1 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d2 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                BlockPos itemDirection = pos;
                
                if (facing != null)
                {
                	switch (facing)
                    {
                    	case DOWN: d1 = 0.75D;
    					case EAST: d0 = 0.25D;
    					case NORTH: d2 = 0.25D;
    					case SOUTH: d2 = 0.25D;
    					case UP: d1 = 0.25D;
    					case WEST: d0 = 0.75D;
    					default: break;
                    }
                    
                    itemDirection = pos.offset(facing);
                	
                }     
                
            	EntityItem entityitem = new EntityItem(worldIn, (double)itemDirection.getX() + d0, (double)itemDirection.getY() + d1, (double)itemDirection.getZ() + d2, lootItem);
                entityitem.setDefaultPickupDelay();
                worldIn.spawnEntity(entityitem);
            }
    	}
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
        { spawnPickedItems(worldIn, pos, null); }
		super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Item.getItemFromBlock(Blocks.SAND); }
    
    protected boolean canSilkHarvest() { return true; }
}