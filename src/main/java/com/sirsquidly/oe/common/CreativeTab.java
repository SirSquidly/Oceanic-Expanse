package com.sirsquidly.oe.common;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

	public class CreativeTab extends CreativeTabs
	{
		public CreativeTab(String label) 
		{
			super(label);
		}

		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(OEItems.GLOW_INK);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
	    public void displayAllRelevantItems(NonNullList<ItemStack> list)
	    {	
	        
			add(list, OEItems.TRIDENT_ORIG);
			
	        add(list, OEItems.GLOW_INK);
	        add(list, OEItems.SQUID_UNCOOKED);
	        add(list, OEItems.SQUID_COOKED);
	        add(list, OEItems.CHLORINE);
	        add(list, OEBlocks.SEAGRASS);
	        add(list, OEBlocks.TALL_SEAGRASS);
	        add(list, OEBlocks.SEA_PICKLE);
	        add(list, OEBlocks.KELP_TOP);
	        add(list, OEItems.DRIED_KELP);
	        add(list, OEBlocks.DRIED_KELP_BLOCK);
	        add(list, OEBlocks.COCONUT_SAPLING);
	        add(list, OEBlocks.COCONUT_LEAVES);
	        add(list, OEBlocks.COCONUT_LEAVES_FLOWERING);
	        add(list, OEBlocks.COCONUT);
	        add(list, OEItems.COCONUT_OPEN);
	        add(list, OEItems.SCUTE);
	        add(list, OEBlocks.BLUE_CORAL_BLOCK);
	        add(list, OEBlocks.PINK_CORAL_BLOCK);
	        add(list, OEBlocks.PURPLE_CORAL_BLOCK);
	        add(list, OEBlocks.RED_CORAL_BLOCK);
	        add(list, OEBlocks.YELLOW_CORAL_BLOCK);
	        add(list, OEBlocks.BLUE_CORAL);
	        add(list, OEBlocks.PINK_CORAL);
	        add(list, OEBlocks.PURPLE_CORAL);
	        add(list, OEBlocks.RED_CORAL);
	        add(list, OEBlocks.YELLOW_CORAL);
	        add(list, OEBlocks.BLUE_CORAL_FAN);
	        add(list, OEBlocks.PINK_CORAL_FAN);
	        add(list, OEBlocks.PURPLE_CORAL_FAN);
	        add(list, OEBlocks.RED_CORAL_FAN);
	        add(list, OEBlocks.YELLOW_CORAL_FAN);
	        add(list, OEBlocks.BLUE_CORAL_BLOCK_DEAD);
	        add(list, OEBlocks.PINK_CORAL_BLOCK_DEAD);
	        add(list, OEBlocks.PURPLE_CORAL_BLOCK_DEAD);
	        add(list, OEBlocks.RED_CORAL_BLOCK_DEAD);
	        add(list, OEBlocks.YELLOW_CORAL_BLOCK_DEAD);
	        add(list, OEBlocks.BLUE_CORAL_DEAD);
	        add(list, OEBlocks.PINK_CORAL_DEAD);
	        add(list, OEBlocks.PURPLE_CORAL_DEAD);
	        add(list, OEBlocks.RED_CORAL_DEAD);
	        add(list, OEBlocks.YELLOW_CORAL_DEAD);
	        add(list, OEBlocks.BLUE_CORAL_FAN_DEAD);
	        add(list, OEBlocks.PINK_CORAL_FAN_DEAD);
	        add(list, OEBlocks.PURPLE_CORAL_FAN_DEAD);
	        add(list, OEBlocks.RED_CORAL_FAN_DEAD);
	        add(list, OEBlocks.YELLOW_CORAL_FAN_DEAD);
	        add(list, OEBlocks.GUARDIAN_SPIKE);
	    }

		
		public void add(NonNullList<ItemStack> list, Item item)
		{
			list.add(new ItemStack(item));
		}
		
		public void add(NonNullList<ItemStack> list, Block block)
		{
			list.add(new ItemStack(block));
		}
		
		public void add(NonNullList<ItemStack> list, ItemStack stack)
		{
			list.add(stack);
		}
}
