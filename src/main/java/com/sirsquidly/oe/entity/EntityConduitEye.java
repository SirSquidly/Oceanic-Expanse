package com.sirsquidly.oe.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityConduitEye extends Entity
{	
	private static final DataParameter<Boolean> HUNTING = EntityDataManager.<Boolean>createKey(EntityConduitEye.class, DataSerializers.BOOLEAN);
	
	public EntityConduitEye(World worldIn)
	{
		super(worldIn);
		this.setSize(0.3F, 0.3F);
		this.rand.setSeed((long)(1 + this.getEntityId()));
	}

	protected void entityInit()
    { 
		this.dataManager.register(HUNTING, Boolean.valueOf(false));
	}
	
	public boolean getHunting()
    { return ((Boolean)this.dataManager.get(HUNTING)).booleanValue(); }

    public void setHunting(boolean shake)
    { this.dataManager.set(HUNTING, Boolean.valueOf(shake)); }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.dataManager.set(HUNTING, Boolean.valueOf(compound.getBoolean("Hunting")));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("Hunting", ((Boolean)this.dataManager.get(HUNTING)).booleanValue());
	}
}