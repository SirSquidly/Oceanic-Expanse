package com.sirsquidly.oe.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHandSponge extends Item 
{
	public ItemHandSponge()
	{ super(); }
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		int effectMath = 3 / 2;
		pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (worldIn.isRemote)
        {
        	return EnumActionResult.SUCCESS;
        }
		else if (!player.isSneaking())
        {
        	if (player.canPlayerEdit(pos, facing, itemstack))
            {
            	worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
            	
            	for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-effectMath, -effectMath, -effectMath), pos.add(effectMath, effectMath, effectMath)))
        		{
        			if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.WATER)
            		{
        				worldIn.setBlockState(blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            		}
        			else if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.FLOWING_WATER)
        			{
        				worldIn.setBlockState(blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            		}
        		}
            	if (!player.capabilities.isCreativeMode)
                { itemstack.shrink(1); }
            	
        		return EnumActionResult.SUCCESS;
            }
        	
        	player.setActiveHand(hand);
        	player.getCooldownTracker().setCooldown(this, 10);
        	player.addStat(StatList.getObjectUseStats(this));
        }
		return EnumActionResult.FAIL;
    }
}