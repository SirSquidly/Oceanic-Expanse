package com.sirsquidly.oe.event;

import java.util.List;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class UseInkEvent 
{
	@SubscribeEvent
	public static void useInkSac(PlayerInteractEvent.RightClickItem event)
	{
		if (!ConfigHandler.vanillaTweak.inkSquirting.enableInkSquirting) return;
		
		EntityPlayer player = event.getEntityPlayer();
		boolean glowInk = false;
		
		if (!(player.getHeldItemMainhand().getItem() == Items.DYE && player.getHeldItemMainhand().getMetadata() == 0 || player.getHeldItemMainhand().getItem() == OEItems.GLOW_INK)) return;
		
		if (player.getHeldItemMainhand().getItem() == OEItems.GLOW_INK) glowInk = true;
		
		//Vec3d eyePosition = player.getPositionEyes(1.0F);
        Vec3d lookVector = player.getLookVec();
        
        player.swingArm(EnumHand.MAIN_HAND);
        player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), ConfigHandler.vanillaTweak.inkSquirting.inkSquirtingCooldown);
		if (!player.capabilities.isCreativeMode) player.getHeldItemMainhand().shrink(1);
        
        for (int i = 0; i < ConfigHandler.vanillaTweak.squidInking.squidParticleAmount; ++i)
        {
        	double quickX = lookVector.x/8 + (player.getRNG().nextDouble() / 8 - player.getRNG().nextDouble() / 8);
        	double quickY = lookVector.y/8 + (player.getRNG().nextDouble() / 8 - player.getRNG().nextDouble() / 8);
        	double quickZ = lookVector.z/8 + (player.getRNG().nextDouble() / 8 - player.getRNG().nextDouble() / 8);
        	
        	if (glowInk)Main.proxy.spawnParticle(2, player.world, player.posX + lookVector.x, player.posY + 1 + lookVector.y, player.posZ + lookVector.z, quickX, quickY, quickZ, 5, 128, 255, 192);
        	else Main.proxy.spawnParticle(2, player.world, player.posX + lookVector.x, player.posY + 1 + lookVector.y, player.posZ + lookVector.z, quickX, quickY, quickZ, 5);
        }
        
        player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, OESounds.ITEM_INK_SAC_SQUIRT, SoundCategory.PLAYERS, 1.0F, (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F + 1.0F);
        
        if (ConfigHandler.vanillaTweak.squidInking.inkBlindnessLength > 0 && !player.world.isRemote)
        {
        	List<Entity> checkInkArea = player.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(player.getPosition()).offset(lookVector.x * 2, lookVector.y * 2, lookVector.z * 2).grow(0.4));
        	
        	for (Entity e : checkInkArea) 
	    	{
				//if (e instanceof EntitySquid) continue;
				if (e instanceof EntityLivingBase)
				{
					if (ConfigHandler.vanillaTweak.inkSquirting.inkSquirtingUserImmune && e == player) continue;
					
					((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, ConfigHandler.vanillaTweak.squidInking.inkBlindnessLength * 20 + 10, 0));
					
					if (glowInk) ((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.GLOWING, ConfigHandler.vanillaTweak.squidInking.inkBlindnessLength * 20 + 10, 0));
				}
			}	
        }
	}
}