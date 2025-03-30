package com.sirsquidly.oe.entity;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OEPotions;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class EntityDolphin extends AbstractFish implements IMeleeAnimal
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(OEItems.LOBSTER_COOKED);
    private static final DataParameter<Integer> MOISTURE = EntityDataManager.createKey(EntityDolphin.class, DataSerializers.VARINT);

    /* Default is 4800*/
    int maxAir = 4800;
    /* Default is 2400*/
    int maxMoistness = 2400;

	public EntityDolphin(World worldIn)
    {
		super(worldIn);
        this.setSize(0.9F, 0.6F);
	}

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(MOISTURE, maxMoistness);
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }
	
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new DolphinAiGetToAir(this, 1.0D));
        this.tasks.addTask(2, new DolphinAiFollowPlayer(this, 4.0D));
        this.tasks.addTask(4, new EntityAIWanderUnderwater(this, 1.0D, 10, true));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(6, new DolphinAIFollowParent(this, 1.25D));
        this.tasks.addTask(7, new EntityAIAvoidEntity<>(this, EntityGuardian.class, 8.0F, 1.0D, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    protected SoundEvent getAmbientSound()
    { return OESounds.ENTITY_DOLPHIN_AMBIENT_WATER; }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_DOLPHIN_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_DOLPHIN_HURT; }

    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_DOLPHIN; }

    /** Dolphins do require normal breathing, unlike any other fish */
    public boolean canBreatheUnderwater()
    { return false; }

	public void onEntityUpdate()
    {
        super.onEntityUpdate();

        float swimParticleSpeedMin = 0.025F;
        if (this.isInWater() && (this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ > swimParticleSpeedMin))
        {
            double yawX = Math.cos((this.rotationYawHead) * Math.PI / 180.0D);
            double yawZ = Math.sin((this.rotationYawHead) * Math.PI / 180.0D);

            double particleFinOffset = this.isChild() ? 0.25D : 0.5D;

            this.world.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX - yawX * particleFinOffset, this.posY, this.posZ - yawZ * particleFinOffset, 0, 0, 0);
            this.world.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX + yawX * particleFinOffset, this.posY, this.posZ + yawZ * particleFinOffset, 0, 0, 0);
        }


        if (this.isEntityAlive())
        {
            if (!this.isMovementBlocked() && this.isServerWorld() && !this.isAIDisabled())
            {
                if (this.isWet()) this.setMoisture(maxMoistness);
                else
                {
                    int j = this.getMoisture();
                    --j;
                    this.setMoisture(j);

                    if (this.getMoisture() == -20)
                    {
                        this.setMoisture(0);
                        this.attackEntityFrom(DamageSource.DROWN, 1.0F);
                    }
                }

                if (ConfigHandler.entity.dolphin.dolphinsRequireAir)
                {
                    /* This is used to restore Air more generously than vanilla, so Dolphins are more likely to get their air */
                    if (!this.isInsideOfMaterial(Material.WATER) || this.isPotionActive(MobEffects.WATER_BREATHING))
                    { this.setAir(maxAir); }
                    else
                    {
                        if (this.world.getBlockState(new BlockPos(this.posX, this.posY + this.height + 0.2, this.posZ)).getMaterial() == Material.AIR)
                        { this.setAir(maxAir); }
                    }
                }
            }
            else
            {
                this.setMoisture(maxMoistness);
                this.setAir(maxAir);
            }
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    { return normalAttack(this, entityIn); }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        /* 10% chance to be a BABY DOLPHIN */
        if (this.rand.nextFloat() <= 0.1F)
        { this.setGrowingAge(-24000); }
        this.setMoisture(maxMoistness);
        return livingdata;
    }

	public float getEyeHeight()
    { return this.height * 0.6F; }
	
	public EntityDolphin createChild(EntityAgeable ageable)
    { return new EntityDolphin(this.world); }
	
	public boolean isBreedingItem(ItemStack stack)
    { return BREEDING_ITEMS.contains(stack.getItem()); }

    public int getMoisture() { return this.dataManager.get(MOISTURE); }

    public void setMoisture(int moistureIn) { this.dataManager.set(MOISTURE, moistureIn); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Moistness", this.getMoisture());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setMoisture(compound.getInteger("Moistness"));
    }








    public class DolphinAiGetToAir extends EntityAIBase
    {
        protected BlockPos destinationBlock = BlockPos.ORIGIN;
        EntityCreature dolphin;
        private final double movementSpeed;
        private int timeoutCounter;

        public DolphinAiGetToAir(EntityCreature creature, double speedIn)
        {
            this.dolphin = creature;
            this.movementSpeed = speedIn;
            this.setMutexBits(1);
        }

        public boolean shouldExecute()
        { return this.dolphin.getAir() < 250; }

        public boolean shouldContinueExecuting()
        { return this.destinationBlock != BlockPos.ORIGIN && this.dolphin.getNavigator().noPath(); }

        /* This sets the Destination Block for the Dolphin to head to. If Air cannot be found, the Dolphin just moves straight up. */
        @Override
        public void startExecuting()
        {
            if (!getNearestAir())
            { this.destinationBlock = this.dolphin.getPosition().up(8); }

            this.dolphin.getNavigator().tryMoveToXYZ( destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5D, this.movementSpeed );
        }

        /** Required, clears the destination if the task is reset. */
        public void resetTask()
        {
            this.destinationBlock = BlockPos.ORIGIN;
            this.dolphin.getNavigator().clearPath();
        }

        public void updateTask()
        {
            //this.dolphin.getLookHelper().setLookPosition(destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5D, 10.0F, 0.0F);
            if (this.dolphin.getDistanceSqToCenter(this.destinationBlock) > 1.0D)
            {
                if (++this.timeoutCounter % 20 == 0)
                {
                    /* If the navigator fails for any reason, override it with a simpler goal! */
                    if (this.dolphin.getNavigator().tryMoveToXYZ(destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5D, this.movementSpeed))
                    { this.destinationBlock = this.dolphin.getPosition().up(8); }
                }
            }
            else if (this.dolphin.isInWater())
            { this.dolphin.motionY = 0.2D; }
        }

        private boolean getNearestAir()
        {
            int maxRadiusXZ = 2;
            int maxRadiusY = 8;
            BlockPos bestBlock = null;
            double shortestDistance = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.getAllInBoxMutable( this.dolphin.getPosition().add(-maxRadiusXZ, 0, -maxRadiusXZ), this.dolphin.getPosition().add(maxRadiusXZ, maxRadiusY, maxRadiusXZ)))
            {
                if (shouldMoveTo(this.dolphin.world, pos) && this.dolphin.isWithinHomeDistanceFromPosition(pos))
                {
                    double distanceSq = this.dolphin.getDistanceSqToCenter(pos);

                    if (distanceSq < shortestDistance && this.dolphin.getNavigator().getPathToPos(pos) != null)
                    {
                        shortestDistance = distanceSq;
                        bestBlock = pos.toImmutable();
                    }
                }
            }
            if (bestBlock != null)
            {
                this.destinationBlock = bestBlock;
                return true;
            }
            return false;
        }

        /** Find air above water */
        protected boolean shouldMoveTo(World worldIn, BlockPos pos)
        { return worldIn.getBlockState(pos).getMaterial() == Material.WATER && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR; }
    }

    public class DolphinAiFollowPlayer extends EntityAIBase
    {
        EntityDolphin dolphin;
        double moveSpeed;
        EntityPlayer followPlayer;

        public DolphinAiFollowPlayer(EntityDolphin entityIn, double moveSpeedIn)
        {
            this.dolphin = entityIn;
            this.moveSpeed = moveSpeedIn;
            this.setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            this.followPlayer = this.dolphin.world.getClosestPlayerToEntity(this.dolphin, 9);
            return this.playerMatchesChecks(followPlayer) && this.dolphin.getAir() > 250;
        }

        public boolean shouldContinueExecuting()
        { return this.playerMatchesChecks(followPlayer) && this.dolphin.getAir() > 250 && this.dolphin.getDistance(followPlayer) < 256; }


        public void updateTask()
        {
            this.dolphin.getLookHelper().setLookPositionWithEntity(followPlayer, 10.0F, 10.0F);

            if (this.dolphin.getDistance(followPlayer) > 6)
            { this.dolphin.getNavigator().tryMoveToEntityLiving(this.followPlayer, this.moveSpeed); }
            else
            { this.dolphin.getNavigator().clearPath(); }

            if (ConfigHandler.entity.dolphin.dolphinsGiveGrace && this.dolphin.getDistance(followPlayer) <= 15)
            { this.followPlayer.addPotionEffect(new PotionEffect(OEPotions.DOLPHINS_GRACE, 110, 0)); }
        }

        /** Many checks for the player which get repeated */
        public boolean playerMatchesChecks(EntityPlayer player)
        { return player != null && player != this.dolphin.getAttackTarget() && player.isSprinting() && player.isInWater(); }
    }

    /** Allows child Dolphins to stop following their parents to get Air. */
    public class DolphinAIFollowParent extends EntityAIFollowParent
    {
        EntityAnimal childAnimal;

        public DolphinAIFollowParent(EntityAnimal animal, double speed)
        {
            super(animal, speed);
            this.childAnimal = animal;
        }

        public boolean shouldExecute()
        { return this.childAnimal.getAir() > 250 && super.shouldExecute(); }

        public boolean shouldContinueExecuting()
        { return this.childAnimal.getAir() > 250 && super.shouldContinueExecuting(); }
    }
}