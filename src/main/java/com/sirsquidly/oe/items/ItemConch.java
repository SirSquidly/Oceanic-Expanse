package com.sirsquidly.oe.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemConch extends Item 
{
	//** This is used for the Creative Tab adding every sound */
	public static int existingSounds = 4;
	
	private int defaultCooldown = ConfigHandler.item.conch.conchCooldown;
	 
	public ItemConch()
	{
		super();
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	/** Called each Inventory Tick, same thing the Map does. This auto-sets the random sound if it doesn't have one.*/
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (!worldIn.isRemote)
        {
        	if (nbttagcompound == null || !nbttagcompound.hasKey("Sound"))
    		{ 
            	addRandomSound(stack, worldIn.rand); 
    		}
        }
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        
        float playPitch = playerIn.rotationPitch * .01F;
        if (playerIn.isSneaking()) playPitch = 0.0F;
        
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();
		
		worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, findSound(nbttagcompound.getString("Sound")), SoundCategory.PLAYERS, ConfigHandler.item.conch.conchSoundDistance * 0.0625F, 1.0F - playPitch);

        playerIn.setActiveHand(handIn);
        
        if (nbttagcompound != null && nbttagcompound.hasKey("Cooldown"))
		{ playerIn.getCooldownTracker().setCooldown(this, nbttagcompound.getInteger("Cooldown")); }
		else
		{ playerIn.getCooldownTracker().setCooldown(this, defaultCooldown); }
		
        
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
	/** Adds a random sound to a new Conch.*/
	public void addRandomSound(ItemStack stack, Random rand)
	{
	    NBTTagCompound nbt;
	    if (stack.hasTagCompound())
	    { nbt = stack.getTagCompound(); }
	    else
	    { nbt = new NBTTagCompound(); }
	 
	    int conchNum = rand.nextInt(4) + 1;
	    nbt.setString("Sound", "oe:item.conch.conch_blow" + conchNum);
	    
	    stack.setTagCompound(nbt);
	}
	
	public static void setSound(ItemStack stack, int sound)
	{
	    NBTTagCompound nbt;
	    if (stack.hasTagCompound())
	    { nbt = stack.getTagCompound(); }
	    else
	    { nbt = new NBTTagCompound(); }

	    nbt.setString("Sound", "oe:item.conch.conch_blow" + sound);
	    
	    stack.setTagCompound(nbt);
	}
	
	/** Pilfers the sound registry to find the NBT sound effect.*/
	public SoundEvent findSound(String string)
	{ return SoundEvent.REGISTRY.getObject(new ResourceLocation(string)); }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		boolean didSet = false;
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		
		if (nbttagcompound != null && nbttagcompound.hasKey("Sound"))
		{

			for (int i = 1; i <= 4; i++)
			{
				if (nbttagcompound.getString("Sound").equals("oe:item.conch.conch_blow" + i) && ConfigHandler.item.conch.conchDisplayHorn)
				{
					tooltip.add(TextFormatting.GRAY + I18n.format("description.conch_blow" + i + ".name"));
					didSet = true;
				}
			}

			if (!didSet && ConfigHandler.item.conch.conchDisplayRawSound) tooltip.add(TextFormatting.GRAY + I18n.format(nbttagcompound.getString("Sound")));
		}
	}
}