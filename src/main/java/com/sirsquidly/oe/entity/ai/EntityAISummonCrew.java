package com.sirsquidly.oe.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAISummonCrew extends EntityAIBase
{
    EntityCreature entityIn;
    EntityLivingBase owner;

    public EntityAISummonCrew(EntityCreature entityIn, EntityLivingBase ownerIn)
    {
        this.entityIn = entityIn;
        this.owner = ownerIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    { return owner != null; }

    public void updateTask()
    {
        this.entityIn.getLookHelper().setLookPositionWithEntity(owner, 10.0F, 10.0F);

        entityIn.setAttackTarget(owner.getLastAttackedEntity());

        /* Swim up to be with the owner! */
        if (this.owner.posY > this.entityIn.posY && this.entityIn.isInWater())
        { this.entityIn.motionY += 0.001D; entityIn.velocityChanged = true;}

        if (this.entityIn.getDistance(owner) > 16)
        { this.entityIn.getNavigator().tryMoveToEntityLiving(this.owner, 2.5F); }
        else if (this.entityIn.getDistance(owner) > 6)
        { this.entityIn.getNavigator().tryMoveToEntityLiving(this.owner, 1.5F); }
        else
        { this.entityIn.getNavigator().clearPath(); }
    }
}