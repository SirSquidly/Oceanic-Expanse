package com.sirsquidly.oe.items;

import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemConch extends ItemBase 
{
	private int defaultCooldown = ConfigHandler.item.conchCooldown;
	 
	public ItemConch(String name)
	{
		super(name);
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
            
        float playPitch = playerIn.rotationPitch * .01F;
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();
		
        
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_CONCH_BLOW1, SoundCategory.PLAYERS, 4.0F, 1.0F - playPitch);
        
        //worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_CONCH_BLOW1, SoundCategory.PLAYERS, 4.0F, 0.8F + (itemRand.nextFloat() / 4));
        
        playerIn.setActiveHand(handIn);
        
        if (nbttagcompound != null && nbttagcompound.hasKey("Cooldown"))
		{
			playerIn.getCooldownTracker().setCooldown(this, nbttagcompound.getInteger("Cooldown"));
		}
		else
		{
			playerIn.getCooldownTracker().setCooldown(this, defaultCooldown);
		}
		
        
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
	
	
}