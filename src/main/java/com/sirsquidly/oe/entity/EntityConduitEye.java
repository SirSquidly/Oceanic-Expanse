package com.sirsquidly.oe.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityConduitEye extends EntityAnimal
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
		super.entityInit(); 
		this.dataManager.register(HUNTING, Boolean.valueOf(false));
	}
	
	protected void applyEntityAttributes()
   {
       super.applyEntityAttributes();
       this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
   }
	
	public boolean getHunting()
    { return ((Boolean)this.dataManager.get(HUNTING)).booleanValue(); }

    public void setHunting(boolean shake)
    { this.dataManager.set(HUNTING, Boolean.valueOf(shake)); }
    
	public boolean canBreatheUnderwater()
   { return true; }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{ return null; }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.dataManager.set(HUNTING, Boolean.valueOf(compound.getBoolean("Hunting")));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("Hunting", ((Boolean)this.dataManager.get(HUNTING)).booleanValue());
	}
}