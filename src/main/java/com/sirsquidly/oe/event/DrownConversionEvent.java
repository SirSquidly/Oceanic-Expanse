package com.sirsquidly.oe.event;

import java.util.Iterator;
import java.util.Random;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber
public class DrownConversionEvent
{
	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
		Entity spawn = event.getEntity();
		ResourceLocation entityID = EntityList.getKey(spawn);
		
		/** Bonus security check */
		if (spawn == null || !(spawn instanceof EntityLiving) || entityID == null) return;

		if(ConfigArrayHandler.NOSWIM.contains(entityID))
		{
			EntityLiving zombie  = (EntityLiving) event.getEntity();			
			
			Iterator<EntityAITasks.EntityAITaskEntry> iterator = zombie.tasks.taskEntries.iterator();

	        while (iterator.hasNext())
	        {
	            EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
	            EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

	            if (entityaibase instanceof EntityAISwimming)
	            {
                	iterator.remove();
                	((PathNavigateGround)zombie.getNavigator()).setCanSwim(false);
                }
	        }
		}		

	}
	
	@SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
		if (!ConfigHandler.vanillaTweak.drownConverting.enableDrownConverting) return;
		
		World world = event.getEntityLiving().getEntityWorld();
		
		if(world.getTotalWorldTime() % 20L != 0L) return;
        Entity entity = event.getEntity();
        if (entity == null || entity instanceof EntityPlayer || !(entity instanceof EntityLiving)) return;
        
        Random rand = world.rand;
        
        if (entity.world.getBlockState(new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ)).getMaterial() == Material.WATER)
        {
        	NBTTagCompound entityData = entity.getEntityData();
        	
        	if (ConfigArrayHandler.NODROWN.contains(EntityList.getKey(entity))) entity.setAir(100);
            
            if (ConfigArrayHandler.DROWNCONVERTFROM.contains(EntityList.getKey(entity)))
            {
	            if(entityData.hasKey("DrowningConversionTime"))
	            {
	                int conversionTime = entityData.getInteger("DrowningConversionTime") - 1;
	                
	                if (rand.nextFloat() > 0.5 && ConfigHandler.vanillaTweak.drownConverting.enableDrownParticles)
	                {
	                	Main.proxy.spawnParticle(2, world, entity.posX + (rand.nextFloat() - rand.nextFloat()), entity.posY + 1 + (rand.nextFloat() - rand.nextFloat()), entity.posZ + (rand.nextFloat() - rand.nextFloat()), 0, 0, 0, 1, 128, 255, 192);
	                }
	                
	                if(conversionTime <= 0)
	                { placeDrowned(world, (EntityLiving) entity, rand); }
	                
	                entityData.setInteger("DrowningConversionTime", conversionTime);
	            }
	            else
	            {
	            	if(!world.isRemote)
	                {
	                    int conversionTime = 14;
	                    entity.getEntityData().setInteger("DrowningConversionTime", conversionTime);
	                }
	            }
			}
            /** This is cleanup in case the config was being altered. */
            else if (entityData.hasKey("DrowningConversionTime"))
            {
            	entity.getEntityData().removeTag("DrowningConversionTime");
            }
        }
    }
	
	
	public static void placeDrowned(World worldIn, EntityLiving entity, Random rand)
	{
		Class<? extends Entity> toEntity = EntityList.getClass(ConfigArrayHandler.DROWNCONVERTTO.get(ConfigArrayHandler.DROWNCONVERTFROM.indexOf(EntityList.getKey(entity))));
		Entity drowned = EntityRegistry.getEntry(toEntity).newInstance(worldIn);
        
        drowned.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        ((EntityLiving) drowned).setNoAI(((EntityLiving) entity).isAIDisabled());
        
        
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
        {
        	drowned.setItemStackToSlot(slot, entity.getItemStackFromSlot(slot));
        	
            //entity.entityDropItem(entity.getItemStackFromSlot(slot), 0.0F);
        }
        
        ((EntityLiving) drowned).setCanPickUpLoot(((EntityLiving) entity).canPickUpLoot());
        
        if (drowned instanceof EntityZombie)
        {
        	((EntityZombie) drowned).setChild(entity.isChild());
        }
        
        ((EntityLivingBase) drowned).setHealth(entity.getHealth());
        ((EntityLiving) drowned).setLeftHanded(((EntityLiving) entity).isLeftHanded());

        if (entity.hasCustomName())
        {
        	drowned.setCustomNameTag(entity.getCustomNameTag());
        	drowned.setAlwaysRenderNameTag(entity.getAlwaysRenderNameTag());
        }
        
        worldIn.spawnEntity(drowned);
        entity.setDead();
        
        for (int i = 0; i < 80 && ConfigHandler.vanillaTweak.drownConverting.enableDrownParticles; i++)
        {
        	Main.proxy.spawnParticle(2, worldIn, drowned.posX + (rand.nextFloat() - rand.nextFloat()), drowned.posY + 1 + (rand.nextFloat() - rand.nextFloat()), drowned.posZ + (rand.nextFloat() - rand.nextFloat()), 0, 0, 0, 3, 128, 255, 192);
        }
    }

	/**
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
    public static void onRenderMob(RenderLivingEvent.Pre<EntityLivingBase> event)
	{
        EntityLivingBase entity = event.getEntity();
        NBTTagCompound entityData = entity.getEntityData();
        
        if(entityData.hasKey("DrowningConversionTime") || entity.isWet())
        {
        	GlStateManager.pushMatrix();
        	entity.renderYawOffset += (float)(Math.cos((double)entity.ticksExisted * 3.25D) * Math.PI * 0.25D);
        	entity.rotationYawHead += (float)(Math.cos((double)entity.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }
    }

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void renderLiving(RenderLivingEvent.Post<EntityLivingBase> event)
	{
		if(event.getEntity().getEntityData().hasKey("DrowningConversionTime") || event.getEntity().isWet())
		{
			GlStateManager.popMatrix();
		}
	}
	*/
}