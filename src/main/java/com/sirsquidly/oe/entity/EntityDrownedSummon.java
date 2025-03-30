package com.sirsquidly.oe.entity;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIMagicConch;
import com.sirsquidly.oe.entity.ai.EntityAITridentThrowing;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.items.ItemCharm;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class EntityDrownedSummon extends EntityDrowned
{
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityDrownedSummon.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDrownedSummon.class, DataSerializers.VARINT);

    private static final Set<Item> HEAD_ALLOWED = Sets.newHashSet(Item.getItemFromBlock(Blocks.PUMPKIN), Item.getItemFromBlock(Blocks.GLASS), Item.getItemFromBlock(Blocks.STAINED_GLASS), Items.SKULL, Item.getItemFromBlock(OEBlocks.PICKLED_HEAD));
    private static final Set<Item> OFFHAND_ALLOWED = Sets.newHashSet(Items.SHIELD, Items.TOTEM_OF_UNDYING, Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW, OEItems.CHARM, OEItems.CONCH, OEItems.CONCH_MAGIC, OEItems.NAUTILUS_SHELL);
    /** If the Drowned is holding a Charm in their Offhand. Should ONLY be set by equipment changing! */
    private boolean hasCharm;

    public EntityDrownedSummon(World worldIn) {
        super(worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
        this.dataManager.register(VARIANT, 0);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityDrowned.DrownedAIGettoWater(this, 1.0D, 35));
        this.tasks.addTask(1, new EntityDrowned.DrownedAISwimToTarget(this));
        this.tasks.addTask(2, new EntityAIMagicConch(this));
        this.tasks.addTask(2, new EntityAITridentThrowing<EntityDrowned>(this, 1.0D, 40, 20.0F, (float) ConfigHandler.entity.drowned.drownedTridentMeleeRange));
        this.tasks.addTask(3, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(4, new DrownedFollowOwner(this));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderUnderwater(this, 1.0D, 80, false));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    @Override
    protected void applyEntityAI()
    {
        this.targetTasks.addTask(1, new ShipmateTargetingAI(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    public void onUpdate()
    {
        super.onUpdate();

        /* The Charm operates on Inventory Ticks, so we need to manually apply the effects for other entities to use it! */
        if (hasCharm)
        {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() instanceof ItemCharm)
            { ((ItemCharm)this.getHeldItemOffhand().getItem()).grantConduit(world, this, this.getHeldItemOffhand()); }
            else { hasCharm = false; }
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {

        if (player == this.getOwner())
        {
            player.swingArm(EnumHand.MAIN_HAND);
            performEquipmentSwap(player);
            return true;
        }

        return super.processInteract(player, hand);
    }

    /**
     * Preforms the swapping of items, including dropping any currently held items
     * Drowned Shipmates can only utilize their head and hand slots, so we only check those!
     * */
    public void performEquipmentSwap(EntityPlayer player)
    {
        ItemStack playerItem = player.getHeldItem(EnumHand.MAIN_HAND);
        EntityEquipmentSlot slot = getDrownedSlotForItem(playerItem);

        if (!this.getItemStackFromSlot(slot).isEmpty() && !world.isRemote)
        { this.entityDropItem(this.getItemStackFromSlot(slot), 1.0F); }

        ItemStack itemCopy = playerItem.copy();
        itemCopy.setCount(1);
        this.setItemStackToSlot(slot, itemCopy);

        this.setDropChance(slot, 100);
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F);

        hasCharm = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() instanceof ItemCharm;
        if (!player.capabilities.isCreativeMode) { playerItem.shrink(1); }
    }

    /**
     * Returns which Equipment Slot to place an item in, default returns Main Hand.
     * Drowned Shipmates can only utilize their head and hand slots, so we only check those!
     * */
    public EntityEquipmentSlot getDrownedSlotForItem(ItemStack stack)
    {
        EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;

        if (!stack.isEmpty())
        {
            if (HEAD_ALLOWED.contains(stack.getItem()) || stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) slot = EntityEquipmentSlot.HEAD;
            if (OFFHAND_ALLOWED.contains(stack.getItem()) || stack.getItem().isShield(stack, null)) slot = EntityEquipmentSlot.OFFHAND;
        }
        else
        {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty())
            {
                slot = EntityEquipmentSlot.OFFHAND;
                if (this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) slot = EntityEquipmentSlot.HEAD;
            }
        }

        return slot;
    }

    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
    { return target != null && !(target instanceof EntityCreeper) && !(target instanceof EntityGhast); }

    public void onDeath(DamageSource cause)
    {
        if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP)
        { this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage()); }
        super.onDeath(cause);
    }

    /** Used to force Drowned Shipmates to no obey Zombie calls for help, because VANILLA doesn't have an easy way to do this! */
    public boolean isOnSameTeam(Entity entityIn)
    { return entityIn instanceof EntityPlayer || entityIn instanceof EntityDrownedSummon || super.isOnSameTeam(entityIn); }

    @Nullable
    public UUID getOwnerId()
    { return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull(); }

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
        { return null; }
    }

    public boolean isOwner(EntityLivingBase entityIn)
    { return entityIn == this.getOwner(); }

    public int getVariant()
    { return this.dataManager.get(VARIANT); }

    public void setVariant(int state)
    { this.dataManager.set(VARIANT, state); }

    /** Summoned Drowned damage their Tridents by using them! */
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        super.attackEntityWithRangedAttack(target, distanceFactor);
        this.getHeldItemMainhand().damageItem(1, this);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        if (this.getOwnerId() == null)
        { compound.setString("OwnerUUID", ""); }
        else
        { compound.setString("OwnerUUID", this.getOwnerId().toString()); }
        compound.setInteger("Variant", this.getVariant());
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
        this.setVariant(compound.getInteger("Variant"));
        //if (this.aiSit != null) { this.aiSit.setSitting(compound.getBoolean("Sitting")); }
        //this.setSitting(compound.getBoolean("Sitting"));
    }

    public class DrownedFollowOwner extends EntityAIBase
    {
        EntityDrownedSummon entityIn;
        EntityLivingBase owner;

        public DrownedFollowOwner(EntityDrownedSummon entityIn)
        {
            this.entityIn = entityIn;
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute()
        {
            EntityLivingBase owner = this.entityIn.getOwner();

            if (owner == null)
            { return false; }
            else if (owner instanceof EntityPlayer && ((EntityPlayer)owner).isSpectator())
            { return false; }
            else
            {
                this.owner = owner;
                return true;
            }
        }

        public void updateTask()
        {
            this.entityIn.getLookHelper().setLookPositionWithEntity(owner, 10.0F, 10.0F);

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

    /**
     * Targetting AI for the Drowned Shipmate
     * */
    public class ShipmateTargetingAI extends EntityAITarget
    {
        private final EntityDrownedSummon drowned;
        private int revengeTimerOld;
        private int attackTimerOld;

        public ShipmateTargetingAI(EntityDrownedSummon drowned) {
            super(drowned, false);
            this.drowned = drowned;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase owner = drowned.getOwner();
            if (owner == null) return false;

            if (owner.getRevengeTimer() != revengeTimerOld && isGoodShipmateTarget(owner.getRevengeTarget(), false))
            {
                drowned.setAttackTarget(owner.getRevengeTarget());
                revengeTimerOld = owner.getRevengeTimer();
                return true;
            }

            if (owner.getLastAttackedEntityTime() != attackTimerOld && isGoodShipmateTarget(owner.getLastAttackedEntity(), false))
            {
                drowned.setAttackTarget(owner.getLastAttackedEntity());
                attackTimerOld = owner.getLastAttackedEntityTime();
                return true;
            }
            return false;
        }

        private boolean isGoodShipmateTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
        {
            if (target == null || target == this.drowned || !target.isEntityAlive() || !drowned.canAttackClass(target.getClass()) || drowned.isOnScoreboardTeam(target.getTeam()) || !drowned.shouldAttackEntity(target, drowned.getOwner()))
            { return false; }
            if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer) target).capabilities.disableDamage)
            { return false; }
            return drowned.getEntitySenses().canSee(target);
        }
    }
}
