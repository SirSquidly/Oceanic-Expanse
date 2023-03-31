package com.sirsquidly.oe.event;

import com.sirsquidly.oe.entity.EntityBabyGlowSquid;
import com.sirsquidly.oe.entity.EntityBabySquid;
import com.sirsquidly.oe.entity.EntityGlowSquid;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** 
 * 	Used exclusively for spawning in Baby Squids.
 * 
 * 	This works by getting a squid that's spawned, running a chance, and spawning a baby squid if the chance passes.
 * */
@Mod.EventBusSubscriber
public class SpawnBabySquidEvent
{
	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
		Entity spawn = event.getEntity();
		
		if (!ConfigHandler.entity.babySquid.enableBabySquid && !ConfigHandler.entity.babyGlowSquid.enableBabyGlowSquid) return;
		
		if(spawn instanceof EntitySquid && !(event.getEntity() instanceof EntityBabySquid || event.getEntity() instanceof EntityBabyGlowSquid) && !spawn.world.isRemote)
		{
			EntitySquid squidBaby = new EntityBabySquid(spawn.world);
			boolean isGlowFlag = false;
			
			if (spawn instanceof EntityGlowSquid && ConfigHandler.entity.babyGlowSquid.enableBabyGlowSquid)
			{
				squidBaby = new EntityBabyGlowSquid(spawn.world);
				isGlowFlag = true;
			}
			else if (!ConfigHandler.entity.babySquid.enableBabySquid) return;
			
			if (spawn.world.rand.nextFloat() <= (isGlowFlag ? ConfigHandler.entity.babyGlowSquid.babyGlowSquidSpawnChance * 0.01F : ConfigHandler.entity.babySquid.babySquidSpawnChance * 0.01F)) 
			{
				squidBaby.setLocationAndAngles(spawn.posX, spawn.posY, spawn.posZ, spawn.rotationYaw, 0.0F);
	            spawn.world.spawnEntity(squidBaby);	
			}
		}
    }
}