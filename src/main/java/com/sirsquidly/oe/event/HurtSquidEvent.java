package com.sirsquidly.oe.event;

import java.util.List;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityGlowSquid;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HurtSquidEvent 
{
	/**
	 * Makes squids spew ink when hurt
	 */
	@SubscribeEvent
	public static void HurtSquid(LivingHurtEvent event)
    {
		Entity squid = event.getEntity();
		if (!ConfigHandler.vanillaTweak.squidInking.enableSquidInking) return;

	    if (squid != null && squid instanceof EntitySquid && event.getSource().getTrueSource() instanceof EntityLivingBase && event.getEntityLiving().world instanceof WorldServer)
	    {
	    	boolean isBaby = false;
	    	EntitySquid squiddi  = (EntitySquid)event.getEntity();	
	    	
	    	if (squid instanceof EntityLivingBase) isBaby = ((EntityLivingBase) squid).isChild();
	    	
	    	// Pitch used to properly rotate the ink spray to be near the squid mouth
	        float pitchRaw = squiddi.squidPitch / 90;
	        float pitch1 = pitchRaw < 0 ? pitchRaw * -1 : pitchRaw;
	        // Yaw used for the same purpose
	        double yawX = Math.cos((squid.rotationYaw + 270) * Math.PI / 180.0D);
			double yawZ = Math.sin((squid.rotationYaw + 270) * Math.PI / 180.0D);
	        
			for (int i = 0; isBaby ? i < ConfigHandler.vanillaTweak.squidInking.babySquidParticleAmount : i < ConfigHandler.vanillaTweak.squidInking.squidParticleAmount; ++i)
            {
				double x = squid.posX + (squid.world.rand.nextDouble() / 3 - squid.world.rand.nextDouble() / 3) + (yawX * (isBaby ? 0.1D : 0.7D) * pitch1);
				double y = squid.posY + 0.7 - (pitchRaw + 1);
				double z = squid.posZ + (squid.world.rand.nextDouble() / 3 - squid.world.rand.nextDouble() / 3) + (yawZ * (isBaby ? 0.1D : 0.7D) * pitch1);

	        	double quickx = (yawX * 0.1 + (squid.world.rand.nextDouble() / 8 - squid.world.rand.nextDouble() / 8)) * pitch1;
		        double quickz = (yawZ * 0.1 + (squid.world.rand.nextDouble() / 8 - squid.world.rand.nextDouble() / 8)) * pitch1;
		        
		        double quicky = squid.world.rand.nextDouble() / 8 - squid.world.rand.nextDouble() / 8;
		        
		        if (squid instanceof EntityGlowSquid)
		        {
		        	Main.proxy.spawnParticle(2, squid.world, x, y, z, quickx, quicky, quickz, isBaby ? 3 : 5, 128, 255, 192);
		        }
		        else
		        {
		        	Main.proxy.spawnParticle(2, squid.world, x, y, z, quickx, quicky, quickz, isBaby ? 3 : 5);
		        }
			
				
		        if (ConfigHandler.vanillaTweak.squidInking.inkBlindnessLength > 0 && !isBaby)
		        {
		        	List<Entity> checkInkArea = squid.world.getEntitiesWithinAABBExcludingEntity(squid, squid.getEntityBoundingBox().offset(x - squid.posX, y - squid.posY, z - squid.posZ));
		        	
		        	for (Entity e : checkInkArea) 
			    	{
						if (e instanceof EntitySquid) continue;
						if (e instanceof EntityLivingBase) ((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, ConfigHandler.vanillaTweak.squidInking.inkBlindnessLength * 20 + 10, 0));
					}	
		        }
            }
	    }
    }
}
