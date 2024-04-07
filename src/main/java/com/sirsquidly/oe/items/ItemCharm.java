package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEPotions;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCharm extends Item
{
	public ItemCharm()
	{
		super();
		this.setMaxDamage(180 - 1);
	}
	
	/** Called each Inventory Tick, same thing the Map does. This auto-sets the random sound if it doesn't have one.*/
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    	if (isSelected || entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).getHeldItemOffhand() == stack)
        { this.grantConduit(worldIn, entityIn, stack); }
    }

	public void grantConduit(World worldIn, Entity entityIn, ItemStack stack)
    {
		EntityLivingBase user = (EntityLivingBase) entityIn;
		
		if (!user.isPotionActive(OEPotions.CONDUIT_POWER) || user.getActivePotionEffect(OEPotions.CONDUIT_POWER).getDuration() <= 9)
		{
			user.addPotionEffect(new PotionEffect(OEPotions.CONDUIT_POWER, 19, 0, true, false));
			
			if (worldIn.getBlockState(entityIn.getPosition().up()).getMaterial() == Material.WATER)
    		{
				boolean isSpectator = user instanceof EntityPlayer && ((EntityPlayer)user).isSpectator();
				boolean isCreative = user instanceof EntityPlayer && ((EntityPlayer)user).capabilities.isCreativeMode;

				if (!isCreative && !isSpectator) stack.damageItem(1, user);
				if (!isSpectator)  spawnParticles(entityIn.world, user);
    		} 
		}
    }
	
	@SideOnly(Side.CLIENT)
	public void spawnParticles(World worldIn, Entity entityIn)
	{	
        for (int i = 0; i < 6; i++)
		{
			Main.proxy.spawnParticle(0, entityIn.posX + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * (double)entityIn.width, entityIn.posY + 1.0D + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * (double)entityIn.height, entityIn.posZ + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * (double)entityIn.width, 0, 0, 0, 6);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.GRAY + I18n.format("Gives Conduit Power when outside of a Conduit's Range."));
	}
}