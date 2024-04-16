package com.sirsquidly.oe.event;

import java.util.Random;

import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/** 
 * 	Adds a new trade for the Fisherman Villager, to buy Pearls
 * */
@Mod.EventBusSubscriber
public class AddTradesEvent
{
	@SubscribeEvent
    public static void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event)
    {
		VillagerProfession farmerProf = VillagerRegistry.FARMER;
		
		if (farmerProf != null)
		{
            VillagerCareer fisherman = farmerProf.getCareer(1);

            if (ConfigHandler.item.pearl.enablePearlTrade) fisherman.addTrade(2, new PearlTrade());
        }
    }
	
	public static class PearlTrade implements ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = 4 + random.nextInt(3);

            recipeList.add(new MerchantRecipe(new ItemStack(OEItems.PEARL, 1), new ItemStack(Items.EMERALD, emeraldCount)));
        }
    }
}