package com.sirsquidly.oe.init;

import com.sirsquidly.oe.util.handlers.FurnaceFuelHandler;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class OERecipies 
{
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) 
	{
		GameRegistry.addSmelting(OEBlocks.SEA_PICKLE, new ItemStack(Items.DYE, 1, EnumDyeColor.LIME.getDyeDamage()), 0.2F);
		GameRegistry.addSmelting(OEBlocks.KELP_TOP, new ItemStack(OEItems.DRIED_KELP, 1), 0.1F);
		GameRegistry.addSmelting(OEBlocks.DULSE, new ItemStack(OEItems.DRIED_DULSE, 1), 0.1F);
		
		FurnaceFuelHandler fuelReg = new FurnaceFuelHandler();
        GameRegistry.registerFuelHandler(fuelReg);
        
		fuelReg.addFuel(OEBlocks.DRIED_KELP_BLOCK, 4000);
	}
}
