package com.sirsquidly.oe.common.entity;

import com.sirsquidly.oe.common.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.common.items.ItemNautilusArmor;
import com.sirsquidly.oe.common.items.ItemSpawnBucket;
import com.sirsquidly.oe.util.CapabilityUtil;
import com.sirsquidly.oe.util.Utilities;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityNautilus extends AbstractFish implements IMeleeAnimal
{
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    /* Wow that's a lot of variables used for Dashing. */
    private static final DataParameter<Integer> DASHING = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DASH_ATTACKING = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DASH_COOLDOWN = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DASH_RECHARGE_TIME = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DASH_SPEED = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.FLOAT);

    private static final DataParameter<ItemStack> SADDLE_STACK = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<Byte> TAMED = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.BYTE);

	public EntityNautilus(World worldIn)
    {
		super(worldIn);
        this.setSize(0.9F, 0.9F);
	}

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(DASHING, 0);
        this.dataManager.register(DASH_ATTACKING, 0);
        this.dataManager.register(DASH_COOLDOWN, 0);
        this.dataManager.register(DASH_RECHARGE_TIME, 40);
        this.dataManager.register(DASH_SPEED, 1.0F);
        this.dataManager.register(SADDLE_STACK, ItemStack.EMPTY);
        this.dataManager.register(TAMED, (byte) 0);
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.09D);
        this.getEntityAttribute(EntityLivingBase.SWIM_SPEED).setBaseValue(1.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }
	
	protected void initEntityAI()
    {
        this.tasks.addTask(4, new EntityAIWanderUnderwater(this, 1.0D, 10, true));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPufferfish.class, true));
    }

    protected SoundEvent getAmbientSound() { return OESounds.ENTITY_NAUTILUS_AMBIENT; }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return OESounds.ENTITY_NAUTILUS_HURT; }

    protected SoundEvent getDeathSound() { return OESounds.ENTITY_NAUTILUS_DEATH; }

    public SoundEvent getDashSound() { return OESounds.ENTITY_NAUTILUS_DASH; }

    public SoundEvent getDashReadySound() { return OESounds.ENTITY_NAUTILUS_DASH_READY; }

    public SoundEvent getEatSound() { return OESounds.ENTITY_NAUTILUS_EAT; }

    public SoundEvent getSaddleSound() { return this.isInWater() ? OESounds.ITEM_SADDLE_NAUTILUS_EQUIP_UNDERWATER : OESounds.ITEM_SADDLE_NAUTILUS_EQUIP; }

    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_DOLPHIN; }

    public boolean canFlop() { return false; }

	public void onEntityUpdate()
    {
        super.onEntityUpdate();

        if (this.isInWater())
        {
            if (getDashing() > 0 || this.rand.nextInt(10) == 0)
            {
                double yawRad = Math.toRadians(this.rotationYawHead);
                double backX = -Math.sin(yawRad);
                double backZ = Math.cos(yawRad);

                double particleSpawnHeight = this.isChild() ? 0.1D : 0.2D;
                double particleSpawnDistance = this.isChild() ? 0.5D : 0.9D;

                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - backX * particleSpawnDistance, this.posY + particleSpawnHeight, this.posZ - backZ * particleSpawnDistance, 0, 0.15D, 0);
            }
        }

        if (getDashing() > 0)
        { setDashing(getDashing() - 1); }

        if (getDashAttacking() > 0)
        {
            setDashAttacking(getDashAttacking() - 1);

            for (EntityLivingBase livingEntity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPosition()).grow(1, 1, 1)))
            {
                if (livingEntity != this && livingEntity != this.getControllingPassenger() && attackEntityAsMob(livingEntity))
                {
                    setDashAttacking(0);
                }
            }
        }

        if (getDashCooldown() > 0)
        {
            setDashCooldown(getDashCooldown() - 1);
            if (getDashCooldown() == 0) this.playSound(getDashReadySound(), 1.0F, 1.0F);
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && !this.isTamed())
        {
            if (this.getGrowingAge() == 0)
            {
                if (isTamingItem(itemstack))
                {
                    player.swingArm(hand);
                    this.consumeItemFromStack(player, itemstack);
                    this.setTamed(true);
                    this.world.setEntityState(this, (byte)18);
                    return true;
                }

                /* Breeding isn't allowed for untamed Nautiluses. */
                if (isBreedingItem(itemstack)) return false;
            }
        }

        if (this.isTamed())
        {
            /* Skip any additional logic if the player is trying to feed the Nautilus. */
            if (isBreedingItem(itemstack) && !this.isInLove() && !player.getCooldownTracker().hasCooldown(itemstack.getItem())) return super.processInteract(player, hand);

            if (itemstack.getItem() instanceof ItemSaddle && this.getSaddle().isEmpty())
            {
                this.playSound(this.getSaddleSound(), 1.0F, 1.0F);
                this.setSaddle(itemstack.copy());
                itemstack.shrink(1);
                return true;
            }
            else if (itemstack.getItem() instanceof ItemNautilusArmor)
            {
                setArmor(itemstack);
                itemstack.shrink(1);
                return true;
            }
            else if (itemstack.getItem() instanceof ItemShears)
            {
                if (!this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty())
                {
                    if (!world.isRemote) this.entityDropItem(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST), 0.5F);
                    setArmor(ItemStack.EMPTY);
                    return true;
                }
                else if (!this.getSaddle().isEmpty())
                {
                    this.playSound(OESounds.ITEM_SADDLE_NAUTILUS_UNEQUIP, 1.0F, 1.0F);
                    if (!world.isRemote) this.entityDropItem(this.getSaddle(), 0.5F);
                    this.setSaddle(ItemStack.EMPTY);
                    return true;
                }
            }
            else if (!this.getSaddle().isEmpty() && !this.isBeingRidden() && !player.isSneaking())
            {
                if (!this.world.isRemote) player.startRiding(this);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    // TODO: Fix bucket inventory placement
    /** Handles all eating! */
    protected void consumeItemFromStack(EntityPlayer player, ItemStack stack)
    {
        playSound(getEatSound(), 1.0F, 1.0F);

        /* The *same* lazy solution used to prevent the item (spawn buckets) from preforming their right-click functions. */
        player.getCooldownTracker().setCooldown(stack.getItem(), 1);

        if (!this.world.isRemote)
        {
            boolean isBucket = stack.getItem() instanceof ItemSpawnBucket;

            if (stack.getItem() instanceof ItemFood)
            {
                ItemFood itemfood = (ItemFood)stack.getItem();
                this.heal((float)itemfood.getHealAmount(stack));
            }
            else this.heal(1);

            if (!player.capabilities.isCreativeMode)
            {
                if (isBucket)
                {
                    ItemStack newStack = new ItemStack(Items.BUCKET);
                    if (stack.isEmpty())
                    { player.setHeldItem(EnumHand.MAIN_HAND, newStack); }
                    else if (!player.inventory.addItemStackToInventory(newStack))
                    { player.dropItem(newStack, false); }
                }
                stack.shrink(1);
            }

            /* Spawn bucket crack textures look bad, just use Bonemeal if it's a bucket. */
            ItemStack particleStack = isBucket ? new ItemStack(Items.DYE, 1, 15) : stack;
            double yawRad = Math.toRadians(this.rotationYaw);
            double backX = -Math.sin(yawRad);
            double backZ = Math.cos(yawRad);

            double particleSpawnHeight = this.isChild() ? 0.1D : 0.2D;
            double particleSpawnDistance = this.isChild() ? 0.5D : 0.9D;

            ((WorldServer)this.world).spawnParticle(
                    EnumParticleTypes.ITEM_CRACK,
                    this.posX - backX * particleSpawnDistance, this.posY + particleSpawnHeight, this.posZ - backZ * particleSpawnDistance,
                    10,
                    0.2D, 0.2D, 0.2D,
                    0.05D,  Item.getIdFromItem(particleStack.getItem()), particleStack.getMetadata()
            );

        }
    }


    public boolean attackEntityAsMob(Entity entityIn)
    { return normalAttack(this, entityIn); }

    /** Always drop the Saddle and Armor on Death. */
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (!this.world.isRemote)
        {
            if (!this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) this.entityDropItem(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST), 0.5F);

            if (!this.getSaddle().isEmpty()) this.entityDropItem(this.getSaddle(), 0.5F);
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        /* 10% chance to be a BABY */
        if (this.rand.nextFloat() <= 0.1F)
        { this.setGrowingAge(-24000); }
        return livingdata;
    }

	
	public EntityNautilus createChild(EntityAgeable ageable)
    { return new EntityNautilus(this.world); }

    /** If the given item is any form of Pufferfish. */
    public boolean isTamingItem(ItemStack stack)
    { return stack.getItem() == Items.FISH && ItemFishFood.FishType.byItemStack(stack) == ItemFishFood.FishType.PUFFERFISH || Utilities.spawnBucketContainsAnyEntity(stack, EntityPufferfish.class); }

    /** Breeding requires the Nautilus to be Tamed, AND to be . */
	public boolean isBreedingItem(ItemStack stack)
    { return stack.getItem() == Items.FISH || stack.getItem() == Items.COOKED_FISH || Utilities.spawnBucketContainsAnyEntity(stack, EntityCod.class, EntitySalmon.class, EntityPufferfish.class, EntityTropicalFish.class); }

    public float getEyeHeight() { return this.height * 0.35F; }

    //** Chunk of code dedicated to Riding Behavior*/
    protected float getWaterSlowDown()
    { return this.isBeingRidden() ? 0.9F : 0.8F; }

    @Nullable
    public Entity getControllingPassenger()
    { return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0); }

    public boolean canBeSteered()
    {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof EntityLivingBase) || entity == null) return false;
        return true;
    }

    public void travel(float strafe, float vertical, float forward)
    {
        //Entity rider = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);

        if (this.isBeingRidden() && this.canBeSteered())
        {

            EntityLivingBase rider = (EntityLivingBase)getControllingPassenger();

            this.rotationYaw = rider.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = -rider.rotationPitch * 0.2F;
            setRotation(rotationYaw, rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            Vec3d look = rider.getLookVec();

            float speed = (float) (getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            float swimSpeed = (float) (getEntityAttribute(EntityLivingBase.SWIM_SPEED).getAttributeValue());

            float getCurrentSpeedType = this.isInWater() ? swimSpeed : speed;

            if (this.canPassengerSteer())
            {
                double pitchFactor = MathHelper.cos((float) Math.toRadians(rider.rotationPitch));
                pitchFactor = Math.pow(Math.abs(pitchFactor), 0.1);

                this.setAIMoveSpeed(speed);
                super.travel(rider.moveStrafing * getCurrentSpeedType, 0, rider.moveForward * getCurrentSpeedType);

                if (this.isInWater())
                {
                    if (rider.moveForward != 0)
                    {
                        if (rider.moveForward > 0) this.motionY = look.y * swimSpeed * 0.15F;
                        else this.motionY = -look.y * swimSpeed * 0.15F;
                    }
                    else if (this.getDashing() <= 0)
                    {
                        double damping = this.motionY < 0 ? 0.1D : 0.96D;
                        this.motionY *= damping;

                        if (Math.abs(this.motionY) < 0.003D) this.motionY = 0.0D;
                    }

                    this.motionX *= pitchFactor;
                    this.motionZ *= pitchFactor;
                }

                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }
            else
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }

            if (rider instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)rider;

                if (!CapabilityUtil.getHoldingSpace(player) && CapabilityUtil.getChargeStrength(player) > 0)
                {
                    Vec3d moveVec = player.getLookVec().scale((CapabilityUtil.getChargeStrength(player)) * this.getDashSpeed());
                    moveVec = moveVec.scale(this.inWater ? 1.0 : 0.5);

                    this.setDashing((int) (CapabilityUtil.getChargeStrength(player) * 15));
                    this.setDashAttacking((int) (CapabilityUtil.getChargeStrength(player) * 15));
                    preformDash(moveVec);

                    CapabilityUtil.alterChargeStrength(player, 0, true, false);
                    this.setDashCooldown(this.getDashRechargeTime());
                }
            }
        }
        else
        { super.travel(strafe, vertical, forward); }
    }

    public void preformDash(Vec3d direction)
    {
        this.playSound(getDashSound(), 1.0F, 1.0F);

        if (!this.world.isRemote) return;
        if (this.canBePushed())
        {
            this.motionX = direction.x;
            this.motionY = direction.y;
            this.motionZ = direction.z;
            this.velocityChanged = true;
        }
    }

    public double getMountedYOffset()
    {
        return this.height * 0.9;
    }
    
    public boolean shouldDismountInWater(Entity rider) { return false; }

    public int getDashing() { return this.dataManager.get(DASHING); }
    public void setDashing(int state) { this.dataManager.set(DASHING, state); }

    public int getDashAttacking() { return this.dataManager.get(DASH_ATTACKING); }
    public void setDashAttacking(int state) { this.dataManager.set(DASH_ATTACKING, state); }

    public int getDashCooldown() { return this.dataManager.get(DASH_COOLDOWN); }
    public void setDashCooldown(int state) { this.dataManager.set(DASH_COOLDOWN, state); }

    public int getDashRechargeTime() { return this.dataManager.get(DASH_RECHARGE_TIME); }
    public void setDashRechargeTime(int state) { this.dataManager.set(DASH_RECHARGE_TIME, state); }

    public float getDashSpeed() { return this.dataManager.get(DASH_SPEED); }
    public void setDashSpeed(float state) { this.dataManager.set(DASH_SPEED, state); }

    public boolean isTamed()
    {
        return (this.dataManager.get(TAMED) & 4) != 0;
    }
    public void setTamed(boolean tamed)
    {
        byte b0 = this.dataManager.get(TAMED);

        if (tamed)
        { this.dataManager.set(TAMED, (byte) (b0 | 4)); }
        else
        { this.dataManager.set(TAMED, (byte) (b0 & -5)); }

        this.setupTamedAI();
    }

    /* Currently unused, investigate if Nautili have AI differences when tamed. */
    protected void setupTamedAI()
    {}

    /** When Nautilus Armor is equipped, adds  the given Protection to the mob. */
    public void setArmor(ItemStack stack)
    {
        this.playSound(stack.isEmpty() ? OESounds.ITEM_ARMOR_NAUTILUS_UNEQUIP : OESounds.ITEM_ARMOR_NAUTILUS_EQUIP, 1.0F, 1.0F);

        ItemStack armor = stack.copy();
        armor.setCount(1);
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, armor);
        this.setDropChance(EntityEquipmentSlot.CHEST, 0);

        System.out.print("Armor Updated to :" + stack);

        if (!this.world.isRemote)
        {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);

            int i = 0;

            if (!stack.isEmpty() && stack.getItem() instanceof ItemNautilusArmor)
            {
                ItemNautilusArmor nautilusArmorStack = (ItemNautilusArmor) armor.getItem();
                i = nautilusArmorStack.getProtection();
            }

            System.out.print("Detected this as the protection: " + i);
            if (i != 0)
            {
                System.out.print("Armor Attribute was ALSO UPDATED TO: " + i);
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Nautilus Armor bonus", i, 0)));
            }
        }
    }

    public ItemStack getSaddle() { return this.dataManager.get(SADDLE_STACK); }
    public void setSaddle(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            stack = stack.copy();
            stack.setCount(1);
        }

        this.dataManager.set(SADDLE_STACK, stack);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("DashCooldown", this.getDashCooldown());
        compound.setFloat("DashRechargeTime", getDashRechargeTime());
        compound.setFloat("DashSpeed", this.getDashSpeed());

        if (!this.getSaddle().isEmpty())
        { compound.setTag("SaddleItem", this.getSaddle().writeToNBT(new NBTTagCompound())); }

        compound.setBoolean("Tamed", this.isTamed());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setDashCooldown(compound.getInteger("DashCooldown"));
        this.setDashRechargeTime(compound.getInteger("DashRechargeTime"));
        this.setDashSpeed(compound.getFloat("DashSpeed"));

        if (!compound.getCompoundTag("SaddleItem").isEmpty())
        { this.setSaddle(new ItemStack(compound.getCompoundTag("SaddleItem"))); }

        this.setTamed(compound.getBoolean("Tamed"));
    }
}