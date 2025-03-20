package com.sirsquidly.oe.items;

import com.sirsquidly.oe.enchantment.resonance.Resonance;
import com.sirsquidly.oe.util.ResonanceUtil;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemConchMagic extends ItemConch
{
	private int defaultCooldown = ConfigHandler.item.conch.conchCooldown;

	public ItemConchMagic()
	{
		super();
		/* As they have Durability, they cannot be stacked like normal Conchs*/
		this.maxStackSize = 1;
		this.setMaxDamage(ConfigHandler.item.magicConch.durability - 1);
	}
	
	/** Called each Inventory Tick, same thing the Map does. This auto-sets the random resonance if it doesn't have one.*/
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (!worldIn.isRemote)
        { if (ResonanceUtil.getResonance(stack) == null) ResonanceUtil.addRandomResonance(stack, worldIn.rand); }
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		Resonance resonance = ResonanceUtil.getResonance(itemstack);

		if (resonance != null) resonance.onUse(playerIn, itemstack);
		return super.onItemRightClick(worldIn, playerIn, handIn);
    }

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
	{
		if (enchantment == Enchantments.MENDING) return false;
		if (enchantment == Enchantments.UNBREAKING) return false;

		return super.canApplyAtEnchantingTable(stack, enchantment);
	}

	public EnumRarity getRarity(ItemStack stack)
	{ return EnumRarity.UNCOMMON; }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		Resonance resonance = ResonanceUtil.getResonance(stack);

		if (resonance != null)
		{ tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format(resonance.getName())); }
	}
}