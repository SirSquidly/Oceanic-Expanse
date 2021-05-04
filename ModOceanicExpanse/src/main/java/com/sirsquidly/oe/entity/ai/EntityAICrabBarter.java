package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.entity.EntityCrab;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class EntityAICrabBarter extends EntityAIBase
{
    /** The delay between crab actions (pause, eating, removing item [in ticks]) */
    int tradeTimer;
    private final EntityCrab crab;
	private float chance;

    public EntityAICrabBarter(EntityCrab crabIn)
    {
        this.crab = crabIn;
        this.chance = 0.3F;
    }

    public boolean shouldExecute()
    {
    if (this.crab.getRNG().nextFloat() >= this.chance || this.crab.isChild())
        {
            return false;
        }
    	return true;
    }
    
    public void resetTask()
    { this.tradeTimer = 0; this.crab.setAnimationState(0);}
    
    public void startExecuting()
    {
        this.tradeTimer = 60;
        this.crab.getNavigator().clearPath();
    }

    public boolean shouldContinueExecuting()
    {
        return this.tradeTimer > 0;
    }
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        ItemStack offered = this.crab.getHeldItemOffhand();
    	ItemStack offering = this.crab.getHeldItemMainhand();

        if (this.crab.isBarterItem(offered))
    	{
        	this.tradeTimer = Math.max(0, this.tradeTimer - 1);

            if (this.tradeTimer == 20)
            {
            	int i = 2;
        		
        		if (this.crab.getRNG().nextInt(i) == 0 && !offering.isEmpty())
        		{
                    EntityItem crabitem = this.crab.entityDropItem(offering, 0.5F);
                    crabitem.motionY += (double)(this.crab.getRNG().nextFloat() * 0.05F);
                    crabitem.motionX += (double)((this.crab.getRNG().nextFloat() - this.crab.getRNG().nextFloat()) * 0.1F);
                    crabitem.motionZ += (double)((this.crab.getRNG().nextFloat() - this.crab.getRNG().nextFloat()) * 0.1F);
                    
                    this.crab.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
        		}
            }
            
    		if (this.tradeTimer < 20 && this.tradeTimer != 0)
            { 
    			if (offered.getItem() instanceof ItemFood)
                { this.crab.setAnimationState(2); }
    		}
    		if (this.tradeTimer == 1)
            { 
    			ItemFood itemfood = (ItemFood)offered.getItem();
    			
    			this.crab.heal((float)itemfood.getHealAmount(offered));
    			this.crab.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY); 
    		}
    	}
    }
}