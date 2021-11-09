package com.sirsquidly.oe.enchantment;

import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.items.ItemTrident;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EnchantmentLoyalty extends Enchantment
{	
    public EnchantmentLoyalty(Enchantment.Rarity rarityIn)
    {
        super(rarityIn, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        this.setName("loyalty");
        this.setRegistryName(Reference.MOD_ID, this.getName());
		OEEnchants.ENCHANTMENTS.add(this);
    }
		
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 5 + enchantmentLevel * 7;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return 50;
    }

    public int getMaxLevel()
    {
        return 3;
    }
    
    public boolean canApplyTogether(Enchantment ench)
    {
        return ench instanceof EnchantmentRiptide ? false : super.canApplyTogether(ench);
    }
    
    @Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTrident;
	}
}