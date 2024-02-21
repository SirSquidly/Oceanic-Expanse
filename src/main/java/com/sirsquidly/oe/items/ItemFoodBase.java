package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
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
	/** Changes the max stacksize, the animation, and returns a bowl after eating. */
	public boolean isStew;
	
	public ItemFoodBase(int amount, float saturation)
	{ this(amount, saturation, false, 32); }
	
	public ItemFoodBase(int amount, float saturation, boolean isWolfFood)
	{ this(amount, saturation, isWolfFood, 32); }
	
	public ItemFoodBase(int amount, float saturation, boolean isWolfFood, int useDuration)
	{
		super(amount, saturation, isWolfFood);
		this.useDuration = useDuration;
	}
    
	@Override
    public int getMaxItemUseDuration(ItemStack stack)
    {  return this.useDuration; }
	
	public EnumAction getItemUseAction(ItemStack stack)
    { return isStew ? EnumAction.DRINK : EnumAction.EAT; }
	
	@Override 
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		if (worldIn.isRemote) return;
		
        if (stack.getItem() == OEItems.BLUE_SLIME_BALL)
        {
        	player.getFoodStats().setFoodLevel(Math.max(player.getFoodStats().getFoodLevel() - 3, 0));
            player.setAir(Math.min(player.getAir() + 120, 300));
        }
        if (isStew)
        { player.addItemStackToInventory(new ItemStack(Items.BOWL)); }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.getItem() == OEItems.BLUE_SLIME_BALL) tooltip.add(TextFormatting.BLUE + I18n.format("description.oe.blue_slime_ball.name"));
	}
	
    public ItemFood setIsSoup()
    { 
    	this.isStew = true;
    	this.setMaxStackSize(1);
    	return this;
    }
    
}