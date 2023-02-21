package com.sirsquidly.oe.common;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.blocks.BlockPalmDoor;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.items.ItemConch;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

	public class CreativeTab extends CreativeTabs
	{
		public CreativeTab(String label) 
		{ super(label); }

		@Override
		public ItemStack getTabIconItem() 
		{ return new ItemStack(OEItems.GLOW_INK); }
		
		@Override
		@SideOnly(Side.CLIENT)
	    public void displayAllRelevantItems(NonNullList<ItemStack> list)
	    {	
	        
			if (ConfigHandler.item.trident.enableTrident) add(list, OEItems.TRIDENT_ORIG);
			
	        add(list, OEItems.GLOW_INK);
	        if (ConfigHandler.block.waterTorch.enableWaterTorch) add(list, OEBlocks.UNDERWATER_TORCH);
	        add(list, OEItems.CHLORINE);
	        add(list, OEBlocks.SEAGRASS);
	        add(list, OEBlocks.TALL_SEAGRASS);
	        add(list, OEBlocks.SEA_PICKLE);
	        add(list, OEBlocks.KELP_TOP);
	        add(list, OEItems.DRIED_KELP);
	        add(list, OEBlocks.DRIED_KELP_BLOCK);
	        add(list, OEBlocks.SEA_OATS);
	        add(list, OEBlocks.COCONUT_SAPLING);
	        add(list, OEBlocks.COCONUT_LEAVES);
	        add(list, OEBlocks.COCONUT_LEAVES_FLOWERING);
	        if (ConfigHandler.block.palmBlocks.enablePalmWoods) 
	        {
	        	add(list, OEBlocks.PALM_LOG);
	        	if (ConfigHandler.block.palmBlocks.enablePalmStrippedWoods) add(list, OEBlocks.PALM_LOG_STRIPPED);
		        add(list, OEBlocks.PALM_WOOD);
		        if (ConfigHandler.block.palmBlocks.enablePalmStrippedWoods) add(list, OEBlocks.PALM_WOOD_STRIPPED);
		        add(list, OEBlocks.PALM_PLANKS);
		        add(list, OEBlocks.PALM_SLAB);
		        add(list, OEBlocks.PALM_STAIRS);
		        add(list, OEBlocks.PALM_FENCE);
		        add(list, OEBlocks.PALM_FENCE_GATE);
		        add(list, ((BlockPalmDoor) OEBlocks.PALM_DOOR).getItemStack());
	        }
	        add(list, OEBlocks.COCONUT);
	        add(list, OEItems.COCONUT_OPEN);
	        add(list, OEItems.HEAVY_BOOTS);
	        add(list, OEItems.SCUTE);
	        if (ConfigHandler.item.turtleShell.enableTurtleShell) add(list, OEItems.TURTLE_HELMET);
	        add(list, OEBlocks.SEA_TURTLE_EGG);
	        add(list, OEBlocks.SHELL_SAND);
	        add(list, OEItems.SHELLS); 
	        add(list, OEBlocks.COQUINA);
	        add(list, OEBlocks.COQUINA_BRICK);
	        add(list, OEBlocks.COQUINA_BRICK_SLAB);
	        add(list, OEBlocks.COQUINA_BRICK_STAIRS);
	        add(list, OEBlocks.COQUINA_BRICK_WALL);
	        add(list, OEItems.CONCH);
	        for (int i = 1; i <= ItemConch.existingSounds; i++)
			{
	        	ItemStack conch = new ItemStack(OEItems.CONCH, 1);
	        	ItemConch.setSound(conch, i);
	            add(list, conch);
			}
	        add(list, OEItems.NAUTILUS_SHELL);
	        add(list, OEItems.HEART_OF_THE_SEA);
	        if (ConfigHandler.block.conduit.enableConduit) add(list, OEBlocks.CONDUIT);
	        add(list, OEItems.PEARL);
	        add(list, OEItems.CHARM);
	        if (ConfigHandler.block.blueIce.enableBlueIce) add(list, OEBlocks.BLUE_ICE);
	        add(list, OEBlocks.DULSE);
	        add(list, OEItems.DRIED_DULSE);
	        add(list, OEBlocks.DRIED_DULSE_BLOCK);
	        add(list, OEBlocks.SEASTAR);
	        add(list, OEBlocks.TUBE_SPONGE);
	        
	        if (ConfigHandler.block.coralBlocks.enableCoralBlock) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL_BLOCK);
		        add(list, OEBlocks.PINK_CORAL_BLOCK);
		        add(list, OEBlocks.PURPLE_CORAL_BLOCK);
		        add(list, OEBlocks.RED_CORAL_BLOCK);
		        add(list, OEBlocks.YELLOW_CORAL_BLOCK);
	        }
	        if (ConfigHandler.block.coralBlocks.enableCoral) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL);
		        add(list, OEBlocks.PINK_CORAL);
		        add(list, OEBlocks.PURPLE_CORAL);
		        add(list, OEBlocks.RED_CORAL);
		        add(list, OEBlocks.YELLOW_CORAL);
	        }
	        if (ConfigHandler.block.coralBlocks.enableCoralFan) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL_FAN);
		        add(list, OEBlocks.PINK_CORAL_FAN);
		        add(list, OEBlocks.PURPLE_CORAL_FAN);
		        add(list, OEBlocks.RED_CORAL_FAN);
		        add(list, OEBlocks.YELLOW_CORAL_FAN);
	        }
	        if (ConfigHandler.block.coralBlocks.enableCoralBlock) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL_BLOCK_DEAD);
		        add(list, OEBlocks.PINK_CORAL_BLOCK_DEAD);
		        add(list, OEBlocks.PURPLE_CORAL_BLOCK_DEAD);
		        add(list, OEBlocks.RED_CORAL_BLOCK_DEAD);
		        add(list, OEBlocks.YELLOW_CORAL_BLOCK_DEAD);
	        }
	        if (ConfigHandler.block.coralBlocks.enableCoral) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL_DEAD);
		        add(list, OEBlocks.PINK_CORAL_DEAD);
		        add(list, OEBlocks.PURPLE_CORAL_DEAD);
		        add(list, OEBlocks.RED_CORAL_DEAD);
		        add(list, OEBlocks.YELLOW_CORAL_DEAD);
	        }
	        if (ConfigHandler.block.coralBlocks.enableCoralFan) 
	        {
	        	add(list, OEBlocks.BLUE_CORAL_FAN_DEAD);
		        add(list, OEBlocks.PINK_CORAL_FAN_DEAD);
		        add(list, OEBlocks.PURPLE_CORAL_FAN_DEAD);
		        add(list, OEBlocks.RED_CORAL_FAN_DEAD);
		        add(list, OEBlocks.YELLOW_CORAL_FAN_DEAD);
	        }
	        if (ConfigHandler.block.guardianSpike.enableGuardianSpike) add(list, OEBlocks.GUARDIAN_SPIKE);
	        
	        /** I am amazed that this works! Wow! Nice! */
	        for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
            {
	        	if (ArrayUtils.contains(ConfigHandler.item.spawnBucket.bucketableMobs, entitylist$entityegginfo.spawnedID.toString()))
				{
	        		ItemStack bucketStack = new ItemStack(OEItems.SPAWN_BUCKET, 1);
	                ItemMonsterPlacer.applyEntityIdToItemStack(bucketStack, entitylist$entityegginfo.spawnedID);
	                add(list, bucketStack);
				}
            }
	    }

		
		public void add(NonNullList<ItemStack> list, Item item)
		{ list.add(new ItemStack(item)); }
		
		public void add(NonNullList<ItemStack> list, Block block)
		{ list.add(new ItemStack(block)); }
		
		public void add(NonNullList<ItemStack> list, ItemStack stack)
		{ list.add(stack); }
}