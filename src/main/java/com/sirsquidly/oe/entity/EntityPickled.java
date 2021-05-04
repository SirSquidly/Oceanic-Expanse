package com.sirsquidly.oe.entity;


import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityPickled extends EntityDrowned
{
	private static final DataParameter<Boolean> IS_DRY = EntityDataManager.<Boolean>createKey(EntityPickled.class, DataSerializers.BOOLEAN);

	public EntityPickled(World worldIn) {
		super(worldIn);
		this.setPathPriority(PathNodeType.WALKABLE, 0.0F);
		this.setPathPriority(PathNodeType.WATER, 10.0F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	@Override
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.ENTITIES_PICKLED;
    }

	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(IS_DRY, Boolean.valueOf(false));
    }
	
	public boolean isDry()
    {
        return ((Boolean)this.dataManager.get(IS_DRY)).booleanValue();
    }

    public void setDry(boolean dry)
    {
        this.dataManager.set(IS_DRY, Boolean.valueOf(dry));
    }
    
    @Override
    public void onLivingUpdate()
    {
        if (this.isEntityAlive())
        {
        	for (int i = 0; i < 2; ++i)
        	{
        		if (!(this.isWet()) && !(this.isDry()))
        		{
        		this.setDry(true);
        		}
        		else if (this.isWet() && this.isDry())
        		{
        			this.setDry(false);
        		}
        	}
        }
        
        super.onLivingUpdate();
    }
}
