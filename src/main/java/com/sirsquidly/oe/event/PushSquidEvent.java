package com.sirsquidly.oe.event;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PushSquidEvent 
{
	@SubscribeEvent
	public static void onRightclickEntity(PlayerInteractEvent.EntityInteract event) 
	{
		EntityPlayer player = event.getEntityPlayer();

		if (ConfigHandler.vanillaTweak.squidPush && player.getHeldItemMainhand().isEmpty() && player.isSneaking() && event.getTarget() instanceof EntitySquid && !event.getTarget().isInWater()) 
		{
			Vec3d moveVec = player.getLookVec().scale(0.2);
			
			player.swingArm(EnumHand.MAIN_HAND);
			
			if (!event.getWorld().isRemote)
			{
				event.getTarget().addVelocity(moveVec.x, moveVec.y, moveVec.z);
				event.getTarget().velocityChanged = true;
				
				if (!event.getTarget().isAirBorne)
				{
					event.getTarget().motionY += 0.6;
				}
			}
		}
	}
}