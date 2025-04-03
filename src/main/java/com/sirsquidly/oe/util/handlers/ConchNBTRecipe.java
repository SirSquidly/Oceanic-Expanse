package com.sirsquidly.oe.util.handlers;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sirsquidly.oe.init.OEItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * 	Custom recipe type used to transfer the Conch's NBT tags over to the Magic Conch properly.
 *
 * 	Based on the `RecipeArmorDyes` class, with countless liberties taken.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class ConchNBTRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack conchItem = ItemStack.EMPTY;
        ItemStack pearlItem = ItemStack.EMPTY;

        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() == OEItems.CONCH)
                {
                    if (conchItem.isEmpty()) conchItem = itemstack1;
                    else return false;
                }
                else
                {
                    if (itemstack1.getItem() == OEItems.PEARL)
                    {
                        if (pearlItem.isEmpty()) pearlItem = itemstack1;
                        else return false;
                    }
                }
            }
        }
        return !conchItem.isEmpty() && !pearlItem.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack resultStack = new ItemStack(OEItems.CONCH_MAGIC);

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack stack = inv.getStackInSlot(k);

            if (!stack.isEmpty() && stack.getItem() == OEItems.CONCH)
            {
                if (stack.hasTagCompound())
                {
                    resultStack.setTagCompound(stack.getTagCompound().copy());
                }
                break;
            }
        }

        return resultStack;
    }

    public ItemStack getRecipeOutput() { return ItemStack.EMPTY; }

    public boolean isDynamic() { return true; }

    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            return new ConchNBTRecipe();
        }
    }
}
