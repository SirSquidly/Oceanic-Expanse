package com.sirsquidly.oe.enchantment;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.items.ItemHeavyBoots;
import com.sirsquidly.oe.proxy.CommonProxy;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EnchantmentMobStomp extends Enchantment
{
	public EnchantmentMobStomp(Rarity rarityIn)
	{
		super(rarityIn, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET});
        this.setName(Main.MOD_ID + "." + "mobstomp");
        this.setRegistryName(Main.MOD_ID, "mobstomp");
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
    	if (!event.getEntity().world.isRemote && event.getEntity() != null && event.getEntity() instanceof EntityLivingBase)
    	{
    		boolean hasStomped =  ((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound() != null ? ((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound().hasKey("hasStomped") : false;
    		int stomp_level = EnchantmentHelper.getEnchantmentLevel(OEEnchants.MOBSTOMP, ((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.FEET));
        
	    	if (stomp_level > 0 && !hasStomped && event.getEntity().fallDistance > 3.0F)
	    	{
	    		//** The damage the enchantment will deal to the mod it connects to*/
	    		float damage = (event.getEntity().fallDistance - 3) * (stomp_level * 1.0F);
	    		
	    		float fallWidthCheck = event.getEntity().width;
	    		float fallHeightCheck = (-(float)event.getEntity().motionY * 0.9F);

	    		Entity entity = event.getEntity().world.findNearestEntityWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntity().getPosition()).grow(fallWidthCheck, fallHeightCheck, fallWidthCheck), event.getEntity());
	    		
	    		if (entity != null && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0)
	    		{    		
	    			entity.attackEntityFrom(CommonProxy.causeMobStompDamage(event.getEntity()), damage);
	    			
	    			event.getEntity().world.playSound(null, event.getEntity().getPosition(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.NEUTRAL, 1.0F, 1.0F);
	        		
	    			event.getEntity().fallDistance = event.getEntity().fallDistance / 2 - 3 - (0.5F + Math.min(100, damage) * 0.02F);
    				((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound().setBoolean("hasStomped", true);
	
	        		event.getEntity().motionY = 0.5F + Math.min(100, damage) * 0.02F;
	        		event.getEntity().velocityChanged = true;
	    		}
	    	}
	    	if (hasStomped && event.getEntity().onGround)
	    	{
	    		((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.FEET).getTagCompound().removeTag("hasStomped");
	    	}
    	}
	}
    
    @Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{ return stack.getItem() instanceof ItemHeavyBoots; }
}