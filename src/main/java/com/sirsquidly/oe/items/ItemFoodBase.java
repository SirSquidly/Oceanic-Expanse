package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoodBase extends ItemFood 
{
	public boolean alwaysEdible;
	/** Number of ticks to run while 'EnumAction'ing until result. */
	public int useDuration;
	
	public ItemFoodBase(int amount, float saturation, int useDuration, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		this.useDuration = useDuration;
	}
    
	@Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return this.useDuration;
    }
	
	@Override 
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote && stack.getItem() == OEItems.BLUE_SLIME_BALL)
        {
        	player.getFoodStats().setFoodLevel(Math.max(player.getFoodStats().getFoodLevel() - 3, 0));
            player.setAir(Math.min(player.getAir() + 120, 300));
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.getItem() == OEItems.BLUE_SLIME_BALL) tooltip.add(TextFormatting.BLUE + I18n.format("description.oe.blue_slime_ball.name"));
	}
}