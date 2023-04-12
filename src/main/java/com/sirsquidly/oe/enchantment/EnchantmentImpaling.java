package com.sirsquidly.oe.enchantment;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.items.ItemTrident;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EnchantmentImpaling extends Enchantment
{	
    private static final String[] DAMAGE_NAMES = new String[] {"is_watermob", "is_wet"};
    private static final int[] MIN_COST = new int[] {1, 5};
    private static final int[] LEVEL_COST = new int[] {11, 8};
    private static final int[] LEVEL_COST_SPAN = new int[] {20, 20};
    /** Defines the damage type, 0 = is_watermob, 1 = is_wet */
    public final int damageType;

    public EnchantmentImpaling(Enchantment.Rarity rarityIn, int damageTypeIn)
    {
        super(rarityIn, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        this.damageType = damageTypeIn;
        this.setName(getName());
        this.setRegistryName(Main.MOD_ID, DAMAGE_NAMES[this.damageType]);
		//OEEnchants.ENCHANTMENTS.add(this);
    }
		
    public int getMinEnchantability(int enchantmentLevel)
    {
        return MIN_COST[this.damageType] + (enchantmentLevel - 1) * LEVEL_COST[this.damageType];
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + LEVEL_COST_SPAN[this.damageType];
    }

    public int getMaxLevel()
    {
        return 5;
    }

    public String getName()
    {
        return "enchantment." + Main.MOD_ID + "." + DAMAGE_NAMES[this.damageType];
    }

    public boolean canApplyTogether(Enchantment ench)
    {
        return !(ench instanceof EnchantmentImpaling);
    }
    
    // I feel like this can be improved, but I cannot know for the life of me how.
    @SubscribeEvent
	public static void onAttackEvent(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase user = (EntityLivingBase) event.getSource().getTrueSource();
			EntityLivingBase target = event.getEntityLiving();
			int imp_level = EnchantmentHelper.getEnchantmentLevel(OEEnchants.IMPALING, user.getHeldItemMainhand());
			int jet_level = EnchantmentHelper.getEnchantmentLevel(OEEnchants.WATER_JET, user.getHeldItemMainhand());
			//ResourceLocation name = EntityList.getKey(event.getEntityLiving());
			
			if (imp_level > 0) 
			{
				if (ConfigHandler.enchant.impaling.enableWaterJet == 2 && target.isWet() || ConfigHandler.enchant.impaling.enableWaterJet != 2 && !(target instanceof EntityPlayer) && ArrayUtils.contains(ConfigHandler.enchant.impaling.aquaticMobs, EntityList.getKey(target).toString()))
				{
					event.setAmount(event.getAmount() + (imp_level * ConfigHandler.enchant.impaling.impalingDamage));
				}
			}
			if (jet_level > 0 && target.isWet()) 
			{
				event.setAmount(event.getAmount() + (jet_level * ConfigHandler.enchant.impaling.waterJetDamage));
			}
		}
	}
    
    @Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTrident;
	}
}