package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sirsquidly.oe.potion.PotionBase;
import com.sirsquidly.oe.util.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class OEPotions 
{
	public static final Potion CONDUIT_POWER = new PotionBase("conduit_power", false, 1950417, 0);
	public static final Potion DESCENT = new PotionBase("descent", true, 10053222, 1);
	
	public static PotionType TURTLE_MASTER_POTION;
	public static PotionType LONG_TURTLE_MASTER_POTION;
	public static PotionType STRONG_TURTLE_MASTER_POTION;
	public static final PotionType DESCENT_POTION = new PotionType("descent", new PotionEffect[] { new PotionEffect(DESCENT, 2400)} ).setRegistryName("descent");
	//public static final PotionType LONG_EYE_IRRITATION_POTION = new PotionType("eye_irritation", new PotionEffect[] { new PotionEffect(EYE_IRRITATION_EFFECT, 4800)}).setRegistryName("long_eye_irritation");
	
	public static void registerPotionMixes()
	{
		//PotionHelper.addMix(EYE_IRRITATION_POTION, ModItems.ONION, LONG_EYE_IRRITATION_POTION);
		//PotionHelper.addMix(PotionTypes.AWKWARD, ModItems.ONION_SLICE, EYE_IRRITATION_POTION);
	}
	
	@SubscribeEvent
	public static void onPotionTypeRegister(RegistryEvent.Register<PotionType> event)
	{
		List<PotionEffect> effects = new ArrayList<>();
		
		effects.add( new PotionEffect(MobEffects.RESISTANCE, 20 * 20, 3));
		effects.add( new PotionEffect(MobEffects.SLOWNESS, 20 * 20, 3));
		TURTLE_MASTER_POTION = new PotionType("turtle_master",  effects.toArray(new PotionEffect[0])).setRegistryName("turtle_master");;
		event.getRegistry().register(OEPotions.TURTLE_MASTER_POTION);
		
		effects.clear();
		effects.add( new PotionEffect(MobEffects.RESISTANCE, 40 * 20, 3));
		effects.add( new PotionEffect(MobEffects.SLOWNESS, 40 * 20, 3));
		LONG_TURTLE_MASTER_POTION = new PotionType("turtle_master_long",  effects.toArray(new PotionEffect[0])).setRegistryName("turtle_master_long");;
		event.getRegistry().register(OEPotions.LONG_TURTLE_MASTER_POTION);
		
		effects.clear();
		effects.add( new PotionEffect(MobEffects.RESISTANCE, 20 * 20, 5));
		effects.add( new PotionEffect(MobEffects.SLOWNESS, 20 * 20, 5));
		STRONG_TURTLE_MASTER_POTION = new PotionType("turtle_master_strong",  effects.toArray(new PotionEffect[0])).setRegistryName("turtle_master_strong");;
		event.getRegistry().register(OEPotions.STRONG_TURTLE_MASTER_POTION);

		
		PotionHelper.addMix(PotionTypes.AWKWARD, OEItems.TURTLE_HELMET, OEPotions.TURTLE_MASTER_POTION);
		PotionHelper.addMix(OEPotions.TURTLE_MASTER_POTION, Items.REDSTONE, OEPotions.LONG_TURTLE_MASTER_POTION);
		PotionHelper.addMix(OEPotions.TURTLE_MASTER_POTION, Items.GLOWSTONE_DUST, OEPotions.STRONG_TURTLE_MASTER_POTION);
		
		event.getRegistry().register(OEPotions.DESCENT_POTION);
	}
	
	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<Potion> event)
	{
		event.getRegistry().register(OEPotions.CONDUIT_POWER);
		event.getRegistry().register(OEPotions.DESCENT);
	}
}