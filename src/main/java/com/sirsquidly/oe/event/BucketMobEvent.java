package com.sirsquidly.oe.event;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.items.ItemSpawnBucket;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles all the relevant code to bucketing up a mob
 */
@Mod.EventBusSubscriber
public class BucketMobEvent
{
	/**
	 * This tries to cancel water placement if the player is using the bucket on a bucketable mob
	 */
	@SubscribeEvent
	public static void RightClickItem(PlayerInteractEvent.RightClickItem event)
    {
		EntityPlayer player = event.getEntityPlayer();
		
		ItemStack stack = player.getHeldItemMainhand();

		Vec3d eyePosition = player.getPositionEyes(1.0F);
        Vec3d lookVector = player.getLook(1.0F);
        double playerReach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d traceEnd = eyePosition.addVector(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        
		RayTraceResult rtresult = player.getEntityWorld().rayTraceBlocks(eyePosition, traceEnd, false, true, false);;
		
		if (rtresult == null) return;
		
		if (rtresult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
			World world = player.getEntityWorld();
			
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(rtresult.getBlockPos()).grow(0.01D));
			
			for (Entity entity1 : list)
	        {
	            if (!(entity1 instanceof EntityPlayer) && ArrayUtils.contains(ConfigHandler.item.spawnBucket.bucketableMobs, EntityList.getKey(entity1).toString()))
	            {
	                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox();
	                RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(eyePosition, traceEnd);

	                if (raytraceresult1 != null && stack.getItem() instanceof ItemBucket)
	                {
	                	event.setCanceled(true);
	                }
	            }
	        }
        }
    }
	
	/**
	 * This gets the right-clicked entity and records it to a spawn bucket
	 */
	@SubscribeEvent
	public static void onRightclickEntity(PlayerInteractEvent.EntityInteract event) 
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		
		if ((stack.getItem() instanceof ItemBucket) && event.getTarget() != null && event.getTarget() instanceof EntityLivingBase) 
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