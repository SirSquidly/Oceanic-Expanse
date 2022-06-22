package com.sirsquidly.oe.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sirsquidly.oe.entity.ai.EntityAISquidFlop;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;
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
		if(ConfigHandler.vanillaTweak.squidFlop && event.getEntity() instanceof EntitySquid)
		{
			EntitySquid squid  = (EntitySquid)event.getEntity();			

			squid.tasks.addTask(1, new EntityAISquidFlop(squid, 80));
		}
	}
	
	@SubscribeEvent
	public static void onEntityDrop(LivingDropsEvent event) 
	{
		EntityLivingBase target = event.getEntityLiving();
		
		if (target != null && ConfigHandler.block.guardianSpike.enableGuardianSpike)
		{
			if (target instanceof EntityElderGuardian)
			{ 
				if (target.world.rand.nextFloat() <= ((float)ConfigHandler.block.guardianSpike.guardianSpikeElderDropChance* 0.01F) + (((float)ConfigHandler.block.guardianSpike.guardianSpikeElderLooting* 0.01F) * event.getLootingLevel()))
				event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(OEBlocks.GUARDIAN_SPIKE))); 
			}
			if (target instanceof EntityGuardian)
			{ 
				if (target.world.rand.nextFloat() <= ((float)ConfigHandler.block.guardianSpike.guardianSpikeDropChance * 0.01F) + (((float)ConfigHandler.block.guardianSpike.guardianSpikeLooting* 0.01F) * event.getLootingLevel()))
				event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(OEBlocks.GUARDIAN_SPIKE))); 
			}
		}
		
		
	}
}