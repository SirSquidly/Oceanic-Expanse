package com.sirsquidly.oe.entity;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.items.ItemNautilusArmor;
import com.sirsquidly.oe.util.CapabilityUtil;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class EntityNautilus extends AbstractFish implements IMeleeAnimal
{
    private static final Set<Item>FEEDING_ITEMS = Sets.newHashSet(Items.FISH, Items.COOKED_FISH);
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(OEItems.LOBSTER_COOKED);
    /* Wow that's a lot of variables used for Dashing. */
    private static final DataParameter<Float> DASH_SPEED = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DASH_COOLDOWN = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DASHING = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DASH_ATTACKING = EntityDataManager.createKey(EntityNautilus.class, DataSerializers.VARINT);

    protected static final DataParameter<Byte> TAMED = EntityDataManager.<Byte>createKey(EntityNautilus.class, DataSerializers.BYTE);

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
        this.dataManager.register(DASH_COOLDOWN, 40);
        this.dataManager.register(DASH_SPEED, 1.0F);
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
    }

    protected SoundEvent getAmbientSound()
    { return OESounds.ENTITY_NAUTILUS_AMBIENT; }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_NAUTILUS_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_NAUTILUS_DEATH; }

    public SoundEvent getDashSound()
    { return OESounds.ENTITY_NAUTILUS_DASH; }

    public SoundEvent getDashReadySound()
    { return OESounds.ENTITY_NAUTILUS_DASH_READY; }

    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_DOLPHIN; }

    public boolean canFlop() { return false; }

	public void onEntityUpdate()
    {
        super.onEntityUpdate();

        if (this.isInWater())
        {
            if (getDashing() > 0 || this.rand.nextInt(20) == 0)
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
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!this.isTamed())
        {
            this.setTamed(true);
        }
        else
        {
            if (itemstack.getItem() instanceof ItemNautilusArmor)
            {
                this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                this.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemstack.copy());
                this.setDropChance(EntityEquipmentSlot.CHEST, 0);
                itemstack.shrink(1);
            }
            else if (!this.isBeingRidden() && !player.isSneaking())
            {
                if (!this.world.isRemote) player.startRiding(this);
                return true;
            }
        }


        return super.processInteract(player, hand);
    }

    public boolean attackEntityAsMob(Entity entityIn)
    { return normalAttack(this, entityIn); }


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
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }

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
                this.setAIMoveSpeed(speed);
                super.travel(rider.moveStrafing * getCurrentSpeedType, 0, rider.moveForward * getCurrentSpeedType);

                if (this.isInWater())
                {
                    if (rider.moveForward != 0)
                    {
                        if (rider.moveForward > 0) this.motionY = look.y * swimSpeed * 0.15F;
                        else this.motionY = -look.y * swimSpeed * 0.15F;
                    }
                    else if (this.getDashing() <= 0) this.motionY = 0;
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
                    //this.setDashAttacking((int) (CapabilityUtil.getChargeStrength(player) * 15));
                    preformDash(moveVec);

                    CapabilityUtil.alterChargeStrength(player, 0, true, false);
                    CapabilityUtil.setChargeCooldown(player, this.getDashCooldown());
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


    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("DashCooldown", this.getDashCooldown());
        compound.setFloat("DashSpeed", this.getDashSpeed());
        compound.setBoolean("Tamed", this.isTamed());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setDashCooldown(compound.getInteger("DashCooldown"));
        this.setDashSpeed(compound.getFloat("DashSpeed"));
        this.setTamed(compound.getBoolean("Tamed"));
    }
}