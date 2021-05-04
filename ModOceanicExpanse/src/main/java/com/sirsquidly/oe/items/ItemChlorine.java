package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChlorine extends ItemBase
{

	public ItemChlorine(String name) {
		super(name);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);
		
        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        if (player.canPlayerEdit(pos, facing, itemstack))
        {
        	for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -2, -4), pos.add(4, 2, 4)))
    		{
    			if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == OEBlocks.SEAGRASS || worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == OEBlocks.TALL_SEAGRASS)
        		{
        			worldIn.setBlockState(blockpos$mutableblockpos, Blocks.WATER.getDefaultState());
        		}
    		}
        	if (!player.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
    		return EnumActionResult.SUCCESS;
        }
        else {return EnumActionResult.FAIL;}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + I18n.format("description.chlorine.name"));
	}
}