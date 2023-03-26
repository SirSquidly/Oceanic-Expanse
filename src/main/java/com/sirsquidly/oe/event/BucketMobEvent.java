package com.sirsquidly.oe.event;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.entity.EntityTropicalFish;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.items.ItemSpawnBucket;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BucketMobEvent
{
	@SubscribeEvent
	public static void RightClickItem(PlayerInteractEvent.RightClickItem event)
    {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		
		RayTraceResult rtresult = Minecraft.getMinecraft().objectMouseOver;
		Entity entity = null;
		
		if (rtresult != null && rtresult.typeOfHit == RayTraceResult.Type.ENTITY)
        {
			entity = rtresult.entityHit;
        }
		
		if (rtresult != null && rtresult.typeOfHit == RayTraceResult.Type.ENTITY)
        {
			if (stack.getItem() instanceof ItemBucket || stack.getItem() == OEItems.SPAWN_BUCKET) 
			{
				if (ArrayUtils.contains(ConfigHandler.item.spawnBucket.bucketableMobs, EntityList.getKey(entity).toString()))
				{
					event.setCanceled(true);
				}
			}
        }
    }
	
	@SubscribeEvent
	public static void onRightclickEntity(PlayerInteractEvent.EntityInteract event) 
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		
		if ((stack.getItem() instanceof ItemBucket || stack.getItem() == OEItems.SPAWN_BUCKET) && event.getTarget() != null && event.getTarget() instanceof EntityLivingBase) 
		{
			EntityLivingBase entity = (EntityLivingBase) event.getTarget();
			
			if (ArrayUtils.contains(ConfigHandler.item.spawnBucket.bucketableMobs, EntityList.getKey(entity).toString()) && !entity.isDead)
			{
				player.swingArm(EnumHand.MAIN_HAND);
				
				event.setCanceled(true);
				
				if (!event.getWorld().isRemote)
				{
	                if (!player.capabilities.isCreativeMode)
	                {
	                	stack.setCount(stack.getCount() - 1);
	                }
	                
	                ItemStack newStack = new ItemStack(OEItems.SPAWN_BUCKET);
	                
	                ItemSpawnBucket.recordEntityNBT(newStack, player, entity);
	                
	                if (entity.hasCustomName())
		            {
						newStack.setStackDisplayName(entity.getCustomNameTag());
		            }
	                
	                if (stack.isEmpty()) 
	                {
	                    player.setHeldItem(EnumHand.MAIN_HAND, newStack);
	                } 
	                else if (!player.inventory.addItemStackToInventory(newStack)) 
	                {
	                    player.dropItem(newStack, false);
	                }
	                // Lazy solution to the game trying to instantly use the spawn bucket when given to the survival player
	                player.getCooldownTracker().setCooldown(newStack.getItem(), 1);
	                
					event.getTarget().setDead();
				}				
			}
		}
	}
}