package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEPotions;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCharm extends Item
{
	public ItemCharm()
	{
		super();
		this.setMaxDamage(ConfigHandler.item.conduitCharm.conduitCharmDurability - 1);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	/** Called each Inventory Tick, same thing the Map does. This auto-sets the random sound if it doesn't have one.*/
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		if (!(entityIn instanceof EntityLivingBase)) return;
		EntityLivingBase user = (EntityLivingBase) entityIn;

    	if (isSelected)
        { this.grantConduit(worldIn, user, stack); }
    }

	public void grantConduit(World worldIn, EntityLivingBase user, ItemStack stack)
    {
		if (worldIn.isRemote) return;
		
		user.addPotionEffect(new PotionEffect(OEPotions.CONDUIT_POWER, 1, 0, true, true));
		
		if (!user.isPotionActive(OEPotions.CONDUIT_POWER) || user.getActivePotionEffect(OEPotions.CONDUIT_POWER).getDuration() <= 9)
		{
			if (user.isInsideOfMaterial(Material.WATER))
    		{
				if (ConfigHandler.item.conduitCharm.enableConduitCharmPulseSound) worldIn.playSound(null, user.getPosition(), OESounds.BLOCK_CONDUIT_BEAT, SoundCategory.BLOCKS, 1.0f, 1.0f);
				user.addPotionEffect(new PotionEffect(OEPotions.CONDUIT_POWER, 119, 0, true, true));
				giftNearbyEffect(worldIn, user.getPosition());
				
				boolean isSpectator = user instanceof EntityPlayer && ((EntityPlayer)user).isSpectator();
				boolean isCreative = user instanceof EntityPlayer && ((EntityPlayer)user).capabilities.isCreativeMode;

				if (!isCreative && !isSpectator) stack.damageItem(1, user);
				if (!isSpectator)  spawnParticles(user.world, user);
    		} 
		}
    }
	
	/** Handles giving out the Conduit Power effect to nearby Players. Copied from TileConduit*/
	public void giftNearbyEffect(World worldIn, BlockPos pos)
	{
		int effectRange = ConfigHandler.item.conduitCharm.conduitCharmConduitRange;
		
		for(EntityPlayer player : worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-effectRange, -effectRange, -effectRange), pos.add(effectRange + 1, effectRange + 1, effectRange + 1))))
		{
			int sphereCheck = (effectRange + 1) * (effectRange + 1);
			if (player.getDistanceSqToCenter(pos) < sphereCheck && player.isWet())
			{
				player.addPotionEffect(new PotionEffect(OEPotions.CONDUIT_POWER, 5 * 20 + 18, 0, true, true));
			}
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
		if (enchantment == Enchantments.MENDING) return false;
		if (enchantment == Enchantments.UNBREAKING) return false;
		
		return super.canApplyAtEnchantingTable(stack, enchantment);
    }

	public void spawnParticles(World worldIn, Entity entityIn)
	{
		double d0 = entityIn.posX;
        double d1 = entityIn.posY;
        double d2 = entityIn.posZ;

        for (int i = -1; i <= 1; ++i)
        {
        	for (int j = -1; j <= 1; ++j)
        	{
        		for (int k = -1; k <= 1; ++k)
        		{
        			double d3 = (double)j + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * 0.5D;
        			double d4 = (double)i + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * 0.5D;
        			double d5 = (double)k + (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * 0.5D;
        			double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / 3 + worldIn.rand.nextGaussian() * 0.05D;

        			Main.proxy.spawnParticle(0, d0, d1 + entityIn.height, d2, d3 / d6, d4 / d6, d5 / d6, 6);
                }
            }
        }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.BLUE + I18n.format("description.oe.charm.name"));
	}
}