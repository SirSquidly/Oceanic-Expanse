package com.sirsquidly.oe.entity.ai;

import java.util.List;

import com.sirsquidly.oe.entity.EntityCrab;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

public class EntityAICrabDig extends EntityAIBase
{
	/** The delay between crab actions (pause, eating, removing item [in ticks]) */
    int digTimer;
	private final EntityCrab crab;
	int chance = 1000;
	
	public EntityAICrabDig(EntityCrab crabIn)
    { this.crab = crabIn; }

    public boolean shouldExecute()
    {
    	ItemStack offHand = this.crab.getHeldItemOffhand();
    	
	    if (this.crab.getRNG().nextInt(chance) != 0 || this.crab.isChild() || !(offHand.isEmpty()) || this.crab.isAngry()) 
	    { return false; }
	    
	    else
	    {
	    	BlockPos blockpos = new BlockPos(this.crab.posX, this.crab.posY, this.crab.posZ);
	    	
	    	return this.crab.world.getBlockState(blockpos.down()).getMaterial() == Material.SAND;
	    }
    }
    
    public void resetTask()
    { this.digTimer = 0; this.crab.setAnimationState(0);}
    
    public void startExecuting()
    {
        this.digTimer = 60;
        this.crab.getNavigator().clearPath();
    }

    public boolean shouldContinueExecuting()
    { 
    	ItemStack offHand = this.crab.getHeldItemOffhand();
    	return this.digTimer > 0 && offHand.isEmpty(); 
    }
    
    
    
    public void updateTask()
    {
    	ItemStack mainHand = this.crab.getHeldItemMainhand();
    	
    	this.crab.setAnimationState(1);
    	
    	this.digTimer = Math.max(0, this.digTimer - 1);
    	
    	if (this.digTimer == 1)
        {
    		if (mainHand.isEmpty())
            {
            	LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.crab.world);
            	List<ItemStack> result = this.crab.world.getLootTableManager().getLootTableFromLocation(LootTableHandler.GAMEPLAY_CRAB_DIG).generateLootForPools(this.crab.getRNG(), lootcontext$builder.build());
            	
                for (ItemStack itemstack : result)
                { 
                	this.crab.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack); 

                if (!(itemstack.isEmpty())) { this.crab.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F); }
                }
            }
    		else
    		{
    			this.crab.playSound(SoundEvents.BLOCK_SAND_PLACE, 1.0F, 1.0F);
    			this.crab.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
    		}
        }
    }
}