package com.sirsquidly.oe.event;

import com.sirsquidly.oe.init.OEPotions;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PotionEffectEvents
{
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase living = event.getEntityLiving();
		
		if(living.isPotionActive(OEPotions.CONDUIT_POWER))
		{	
			if ( living.getActivePotionEffect(OEPotions.CONDUIT_POWER).getDuration() > 1)
			{
				if (living.world.getBlockState(new BlockPos(living.posX, living.posY + living.getEyeHeight(), living.posZ)).getMaterial() == Material.WATER)
		    	{
					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 210, 0, true, false));

					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 0, true, false));
					
					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.HASTE, 210, living.getActivePotionEffect(OEPotions.CONDUIT_POWER).getAmplifier(), true, false));
		    	}
				else
				{ removeConduitEffects(event.getEntityLiving()); }
			}
			else
			{ removeConduitEffects(event.getEntityLiving()); }
        }
		
		if(living.isPotionActive(OEPotions.DESCENT))
		{
			int modDes = living.getActivePotionEffect(OEPotions.DESCENT).getAmplifier() + 1;
			
			if (living.isInWater())
			{
				if (living instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)living;
					/* This check is so players in Creative and Spectator aren't dragged down **/
					if (!entityplayer.capabilities.isFlying)
					{
						living.motionY -= (ConfigHandler.effect.descent.descentWaterPull * modDes - living.motionY) * 0.1D;
						
						/* Additional pull if Aqua Acrobatics is installed, as swimming is really fast with that mod. **/
						if (ConfigHandler.effect.descent.descentAqAcWaterPull != 0 && Loader.isModLoaded("aquaacrobatics") && living.world.getBlockState(living.getPosition().add(0, living.getEyeHeight(), 0)).getMaterial() != Material.AIR)
						{
							living.motionY = living.motionY - (ConfigHandler.effect.descent.descentAqAcWaterPull * modDes);
						}
					}
				}
				else living.motionY -= (ConfigHandler.effect.descent.descentWaterPull * modDes - living.motionY) * 0.1D;
			}
			else if (living.motionY < 0.0)
			{
				living.motionY -= (ConfigHandler.effect.descent.descentFallPull * modDes - living.motionY) * 0.1D;
			}
        }
	}
	
	public static void removeConduitEffects(EntityLivingBase entity)
	{
		if (entity.isPotionActive(MobEffects.WATER_BREATHING) && entity.getActivePotionEffect(MobEffects.WATER_BREATHING).getDuration() < 210) entity.removePotionEffect(MobEffects.WATER_BREATHING);
		if (entity.isPotionActive(MobEffects.NIGHT_VISION) && entity.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() < 210) entity.removePotionEffect(MobEffects.NIGHT_VISION);
		if (entity.isPotionActive(MobEffects.HASTE) && entity.getActivePotionEffect(MobEffects.HASTE).getDuration() < 210) entity.removePotionEffect(MobEffects.HASTE);
		return;
	}
}