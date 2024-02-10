package com.sirsquidly.oe.entity.ai;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityCrab;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class EntityAICrabDig extends EntityAIBase
{
	/** The delay between crab actions (pause, eating, removing item [in ticks]) */
    int digTimer;
	private final EntityCrab crab;
	int chance = 10;
	
	public EntityAICrabDig(EntityCrab crabIn)
    { this.crab = crabIn; }

    public boolean shouldExecute()
    {
    	ItemStack offHand = this.crab.getHeldItemOffhand();
    	
	    if (!ConfigHandler.entity.crab.enableCrabDigging || !this.crab.canDig() || this.crab.getRNG().nextInt(chance) != 0 || this.crab.isChild() || !(offHand.isEmpty()) || this.crab.isAngry()) 
	    { return false; }
	    
	    else
	    {
	    	BlockPos blockpos = new BlockPos(this.crab.posX, this.crab.posY, this.crab.posZ);
	    	
	    	return ConfigArrayHandler.CRABDIGFROM.contains(this.crab.world.getBlockState(blockpos.down()));
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
    	if (!this.crab.canDig()) return false;
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
            	
            	IBlockState block = this.crab.world.getBlockState(this.crab.getPosition().down());
            	
            	if (block == null || ConfigArrayHandler.CRABDIGFROM.isEmpty()) return;
  
            	if (ConfigArrayHandler.CRABDIGFROM.contains(block))
            	{
            		LootTable loottable = this.crab.world.getLootTableManager().getLootTableFromLocation(ConfigArrayHandler.CRABDIGTO.get(ConfigArrayHandler.CRABDIGFROM.indexOf(block)));
            		
            		for (ItemStack itemstack : loottable.generateLootForPools(this.crab.getRNG(), lootcontext$builder.build()))
                    { 
                    	this.crab.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack); 

                        if (!(itemstack.isEmpty())) { this.crab.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F); }
                    }
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