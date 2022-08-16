package com.sirsquidly.oe.event;

import com.sirsquidly.oe.init.OEPotions;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
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
				if (living.world.getBlockState(new BlockPos(living.posX, living.posY + 0.5D, living.posZ).up()).getMaterial() == Material.WATER)
		    	{
					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 210, 0, true, false));

					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 0, true, false));
					
					((EntityLivingBase) living).addPotionEffect(new PotionEffect(MobEffects.HASTE, 210, 0, true, false));
		    	}
				else
				{ removeConduitEffects(event.getEntityLiving()); }
			}
			else
			{ removeConduitEffects(event.getEntityLiving()); }
        }
		
		
		
		if(living.isPotionActive(OEPotions.DESCENT))
		{
			if (living.motionY < 0.0 || living.isInWater())
				{
					if (living instanceof EntityPlayer)
					{
						EntityPlayer entityplayer = (EntityPlayer)living;
						
						if (!entityplayer.capabilities.isFlying) living.motionY -= (0.05D * (double)(living.getActivePotionEffect(OEPotions.DESCENT).getAmplifier() + 1) - living.motionY) * 0.2D;
					
					}
					else living.motionY -= (0.05D * (double)(living.getActivePotionEffect(OEPotions.DESCENT).getAmplifier() + 1) - living.motionY) * 0.2D;
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