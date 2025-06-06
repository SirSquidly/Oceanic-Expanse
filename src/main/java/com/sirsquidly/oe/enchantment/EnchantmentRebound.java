package com.sirsquidly.oe.enchantment;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.items.ItemHeavyBoots;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EnchantmentRebound extends Enchantment
{
	public EnchantmentRebound(Rarity rarityIn)
	{
		super(rarityIn, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET});
        this.setName(Main.MOD_ID + "." + "rebound");
        this.setRegistryName(Main.MOD_ID, "rebound");
		OEEnchants.ENCHANTMENTS.add(this);
	}

	public int getMinEnchantability(int enchantmentLevel)
    { return 10 + 20 * (enchantmentLevel - 1); }

    public int getMaxEnchantability(int enchantmentLevel)
    { return this.getMinEnchantability(enchantmentLevel) + 50; }

    public int getMaxLevel()
    { return 2; }
    
    public boolean canApplyTogether(Enchantment ench)
    { return super.canApplyTogether(ench); }

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntityLiving() == null || event.getEntityLiving().world.isRemote) return;

		ItemStack boots = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if (!boots.hasTagCompound()) boots.setTagCompound(new NBTTagCompound());
		NBTTagCompound itemNBT = boots.getTagCompound();

		/* This tracks if the Boots have stored the beginning Y-level of the Player's fall. */
		boolean hasStoredFall = itemNBT != null && itemNBT.hasKey("fallStartValue");
		int reboundLevel = EnchantmentHelper.getEnchantmentLevel(OEEnchants.REBOUND, boots);

		if (reboundLevel <= 0) return;

		if (event.getEntityLiving().onGround)
		{
			if (hasStoredFall)
			{
				int bounceTimes = itemNBT.getInteger("bounceTimes");
				double bounceHeight = itemNBT.getDouble("fallStartValue") - event.getEntityLiving().posY;

				if (bounceHeight > 3 && bounceTimes < reboundLevel && !(event.getEntityLiving().isSneaking()))
				{
					event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.NEUTRAL, 1.0F, 1.0F);

					event.getEntityLiving().motionY = Math.sqrt(2 * 0.115 * bounceHeight);
					event.getEntityLiving().velocityChanged = true;

					itemNBT.setInteger("bounceTimes", bounceTimes + 1);
				} else
				{
					itemNBT.removeTag("bounceTimes");
				}

				itemNBT.removeTag("fallStartValue");
			}
		}
		else if (!hasStoredFall && event.getEntityLiving().motionY < 0.0)
		{ itemNBT.setDouble("fallStartValue", event.getEntityLiving().posY); }
	}

	@SubscribeEvent
	public static void onFall(LivingFallEvent event)
	{
		if (event.getEntityLiving() == null || event.getEntityLiving().world.isRemote || event.getEntityLiving().isSneaking()) return;

		ItemStack boots = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET);
		int reboundLevel = EnchantmentHelper.getEnchantmentLevel(OEEnchants.REBOUND, boots);

		if (reboundLevel > 0)
		{
			NBTTagCompound itemNBT = boots.getTagCompound();
			if (itemNBT == null) return;
			int bounceTimes = itemNBT.getInteger("bounceTimes");

			if (itemNBT.hasKey("fallStartValue") && bounceTimes < reboundLevel)
			{ event.setDistance(0.0F); }
		}
	}


	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{ return ConfigHandler.enchant.mobStomp.enableMobStompOnAnyBoots ? super.canApplyAtEnchantingTable(stack) : stack.getItem() instanceof ItemHeavyBoots; }
}