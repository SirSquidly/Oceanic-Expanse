package com.sirsquidly.oe.util.handlers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

@SuppressWarnings("deprecation")
public class FurnaceFuelHandler implements IFuelHandler
{
	 private Map<Item, Integer> fuelList = new HashMap<Item, Integer>();
	    
	    @Override
	    public int getBurnTime(ItemStack fuel) {   
	        Item item = fuel.getItem();
	        if (fuel == null || item == null) {return 0;}
	        
	        if (fuelList.containsKey(item))
	        {
	            return fuelList.get(item);
	        }
	        return 0;
	    }

	    /** Adding Fuel, or converting Blocks to Items then adding it **/
	    public void addFuel(Item item, int value)
	    { fuelList.put(item, value); }

	    public void addFuel(Block block, int value)
	    { addFuel(Item.getItemFromBlock(block), value); }  
}