package com.sirsquidly.oe.compat;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI implements IModPlugin
{
    @Override
    public void register(IModRegistry registry)
    {
    	registry.addIngredientInfo(new ItemStack(OEItems.SHELLS), VanillaTypes.ITEM, "oe.jei.barnacle_shells.desc");
    	registry.addIngredientInfo(new ItemStack(OEBlocks.COCONUT), VanillaTypes.ITEM, "oe.jei.coconut.desc");
        registry.addIngredientInfo(new ItemStack(OEBlocks.CONDUIT), VanillaTypes.ITEM, "oe.jei.conduit.desc");
        registry.addIngredientInfo(new ItemStack(OEBlocks.GUARDIAN_SPIKE), VanillaTypes.ITEM, "oe.jei.guardian_spike.desc");
        registry.addIngredientInfo(new ItemStack(OEBlocks.PALM_LEAVES_FLOWERING), VanillaTypes.ITEM, "oe.jei.palm_leaves_flowering.desc");
        registry.addIngredientInfo(new ItemStack(OEBlocks.SEA_OATS), VanillaTypes.ITEM, "oe.jei.sea_oats.desc");
        registry.addIngredientInfo(new ItemStack(OEBlocks.SEA_PICKLE), VanillaTypes.ITEM, "oe.jei.sea_pickle.desc");
        
        registry.addIngredientInfo(new ItemStack(OEBlocks.SHELL_SAND), VanillaTypes.ITEM, "oe.jei.shell_sand.desc");
        
        IIngredientBlacklist ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
		
		ingredientBlacklist.addIngredientToBlacklist(new ItemStack(OEBlocks.COQUINA_BRICK_SLAB_D));
		ingredientBlacklist.addIngredientToBlacklist(new ItemStack(OEBlocks.PALM_SLAB_D));
    }
}