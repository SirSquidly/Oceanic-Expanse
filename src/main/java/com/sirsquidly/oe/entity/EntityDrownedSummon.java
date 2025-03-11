package com.sirsquidly.oe.entity;

import com.google.common.base.Optional;
import com.sirsquidly.oe.entity.ai.EntityAITridentThrowing;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityDrownedSummon extends EntityDrowned
{
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityDrownedSummon.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityDrownedSummon(World worldIn)
    {
        super(worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityDrowned.DrownedAIGettoWater(this, 1.0D, 35));
        this.tasks.addTask(1, new EntityDrowned.DrownedAISwimToTarget(this));
        this.tasks.addTask(2, new EntityAITridentThrowing<EntityDrowned>(this, 1.0D, 40, 20.0F, (float) ConfigHandler.entity.drowned.drownedTridentMeleeRange));
        this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderUnderwater(this, 1.0D, 80, false));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void applyEntityAI()
    {
        this.targetTasks.addTask(1, new DrownedOwnerTargetting(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }


    @Nullable
    public UUID getOwnerId()
    {
        return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID p_184754_1_)
    { this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_)); }

    @Nullable
    public EntityLivingBase getOwner()
    {
        try
        {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        }
        catch (IllegalArgumentException var2)
        {
            return null;
        }
    }

    public boolean isOwner(EntityLivingBase entityIn)
    {
        return entityIn == this.getOwner();
    }

    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
    { return !(target instanceof EntityCreeper) && !(target instanceof EntityGhast); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.getOwnerId() == null)
        { compound.setString("OwnerUUID", ""); }
        else
        { compound.setString("OwnerUUID", this.getOwnerId().toString()); }

        //compound.setBoolean("Sitting", this.isSitting());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        String s;

        if (compound.hasKey("OwnerUUID", 8))
        { s = compound.getString("OwnerUUID"); }
        else
        {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty())
        {
            try
            { this.setOwnerId(UUID.fromString(s)); }
            catch (Throwable ignored)
            { }
        }

        //if (this.aiSit != null) { this.aiSit.setSitting(compound.getBoolean("Sitting")); }
        //this.setSitting(compound.getBoolean("Sitting"));
    }

    public class DrownedOwnerTargetting extends EntityAITarget
    {
        EntityDrownedSummon drowned;
        EntityLivingBase attacker;
        private int timestamp;
        private boolean isRevenge;

        public DrownedOwnerTargetting(EntityDrownedSummon theDefendingTameableIn)
        {
            super(theDefendingTameableIn, false);
            this.drowned = theDefendingTameableIn;
            this.setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.drowned.getOwner();

            if (entitylivingbase == null) return false;

            this.attacker = entitylivingbase.getLastAttackedEntity();
            int i = entitylivingbase.getLastAttackedEntityTime();
            this.isRevenge = false;

            if (i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.drowned.shouldAttackEntity(this.attacker, entitylivingbase)) return true;

            this.attacker = entitylivingbase.getRevengeTarget();
            i = entitylivingbase.getRevengeTimer();
            this.isRevenge = true;

            return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.drowned.shouldAttackEntity(this.attacker, entitylivingbase);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.taskOwner.setAttackTarget(this.attacker);
            EntityLivingBase entitylivingbase = this.drowned.getOwner();

            if (entitylivingbase != null)
            { this.timestamp = this.isRevenge ? entitylivingbase.getRevengeTimer() : entitylivingbase.getLastAttackedEntityTime(); }

            super.startExecuting();
        }
    }
}
