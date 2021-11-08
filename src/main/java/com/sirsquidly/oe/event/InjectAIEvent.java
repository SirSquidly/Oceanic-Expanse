package com.sirsquidly.oe.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sirsquidly.oe.entity.ai.EntityAISquidFlop;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

@Mod.EventBusSubscriber
public class InjectAIEvent 
{
	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
		Entity spawn = event.getEntity();
		
		if(ConfigHandler.block.turtleEgg.zombiesTrample && spawn instanceof EntityZombie)
		{
			EntityZombie zombie  = (EntityZombie)event.getEntity();			

			zombie.tasks.addTask(3, new EntityAIStompTurtleEgg(zombie, 1.0D));
		}		
		if(event.getEntity() instanceof EntitySquid)
		{
			EntitySquid squid  = (EntitySquid)event.getEntity();			

			squid.tasks.addTask(1, new EntityAISquidFlop(squid, 80));
		}
	}
}