package com.sirsquidly.oe.entity;

import com.sirsquidly.oe.init.OESounds;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityZombieNautilus extends EntityNautilus
{
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityZombieNautilus.class, DataSerializers.VARINT);

    public EntityZombieNautilus(World worldIn)
    { super(worldIn); }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
    }

    protected SoundEvent getAmbientSound() { return OESounds.ENTITY_ZOMBIE_NAUTILUS_AMBIENT; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return OESounds.ENTITY_ZOMBIE_NAUTILUS_HURT; }

    protected SoundEvent getDeathSound() { return OESounds.ENTITY_ZOMBIE_NAUTILUS_DEATH; }

    public SoundEvent getDashSound() { return OESounds.ENTITY_ZOMBIE_NAUTILUS_DASH; }

    public SoundEvent getDashReadySound() { return OESounds.ENTITY_ZOMBIE_NAUTILUS_DASH_READY; }

    public SoundEvent getEatSound() { return OESounds.ENTITY_ZOMBIE_NAUTILUS_EAT; }

    public void onLivingUpdate()
    {
        if (this.world.isDaytime() && !this.world.isRemote && !this.isChild())
        {
            float f = this.getBrightness();

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.isEmpty())
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) this.setFire(8);
            }
        }

        super.onLivingUpdate();
    }

    /* Zombie Nautilus is aroace. */
    public boolean canMateWith(EntityAnimal otherAnimal) { return false; }

    public boolean isGilled() { return false; }
    public EnumCreatureAttribute getCreatureAttribute() { return EnumCreatureAttribute.UNDEAD; }

    public int getVariant() { return this.dataManager.get(VARIANT); }
    public void setVariant(int state) { this.dataManager.set(VARIANT, state); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }
}