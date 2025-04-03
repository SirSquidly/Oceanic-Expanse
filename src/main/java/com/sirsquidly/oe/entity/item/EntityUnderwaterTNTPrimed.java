package com.sirsquidly.oe.entity.item;

import com.sirsquidly.oe.util.ExplosionUnderwater;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityUnderwaterTNTPrimed extends EntityTNTPrimed
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityUnderwaterTNTPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FUSE, 80);
    }

    public EntityUnderwaterTNTPrimed(World worldIn)
    { super(worldIn); }

    public EntityUnderwaterTNTPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        { this.motionY -= 0.03999999910593033D; }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        this.setFuse(this.getFuse() - 1);

        if (this.getFuse() <= 0)
        {
            this.setDead();

            this.explodeUnderwater();
        }
        else
        {
            this.handleWaterMovement();
            //this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explodeUnderwater()
    {
        float f = 4.0F;
        ExplosionUnderwater explosion = new ExplosionUnderwater(this.world, this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 4.0F, true, true);
        //if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this, explosion)) return explosion;
        explosion.setWaterDestructionChance(ConfigHandler.block.waterTNT.waterDestructionChance * 0.01F);
        explosion.setBlockDropChance(ConfigHandler.block.waterTNT.blockDropChance * 0.01F);
        explosion.preformStandardExplosion();
    }

    public int getFuse()
    { return this.dataManager.get(FUSE); }

    public void setFuse(int fuseIn)
    { this.dataManager.set(FUSE, fuseIn); }
}
