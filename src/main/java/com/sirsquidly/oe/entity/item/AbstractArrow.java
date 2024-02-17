package com.sirsquidly.oe.entity.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbstractArrow extends EntityArrow implements IProjectile
{
	private int xTile;
    private int yTile;
    private int zTile;
    protected Block inTile;
    private int inData;
    protected boolean inGround;
    protected int timeInGround;
    public PickupStatus pickupStatus;
    protected int ticksInGround;
    @SuppressWarnings("unused")
	private int ticksInAir;
    protected float damage;
    // Custom stuff below
	protected boolean alwaysBounce;
	protected boolean canEntityCollide;
	protected double bounceStrength;
	protected double bounceYStrength;
	protected float airSpeed;
	protected float waterSpeed;
	 
	public AbstractArrow(World worldIn)
	{
		super(worldIn);
		this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0F;
        this.canEntityCollide = true;
        this.bounceStrength = -0.1D;
        this.bounceYStrength = -0.1D;
        this.airSpeed = 0.99F;
        this.waterSpeed = 0.6F;
        this.pickupStatus = PickupStatus.DISALLOWED;
	}
	
	public AbstractArrow(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }
	
	public AbstractArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer)
        {
        	this.pickupStatus = PickupStatus.ALLOWED;
        }
    }

	@Override
	public void onUpdate()
    {
		if (!this.world.isRemote)
        {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR)
        {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround && this.noClip != true)
        {
        	++this.timeInGround;

        	int j = block.getMetaFromState(iblockstate);
        	
            if ((block != this.inTile || j != this.inData) &&!this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05D)))
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                
            }
            else
            {
            	++this.ticksInGround;
            	
                if (this.ticksInGround >= 1200)
                {
                    this.setDead();
                }
            }
        }
        else
        {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null)
            {
                vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }

            if (this.canEntityCollide)
            {
            	Entity entity = this.findEntityOnPath(vec3d1, vec3d);

            	RayTraceResult entitycheck = null;
            	
                if (entity != null)
                { entitycheck = new RayTraceResult(entity); }

                if (entitycheck != null)
                {
                	if (entitycheck.entityHit instanceof EntityPlayer)
                	{
                		EntityPlayer entityplayer = (EntityPlayer)entitycheck.entityHit;

                        if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
                        {
                        	raytraceresult = null;
                        }
                	}
                	
                	if (this.shootingEntity != null && entitycheck.entityHit == this.shootingEntity.getRidingEntity())
                	{ entitycheck = null; }
                	
                	
                	if (entitycheck != null)
                    { raytraceresult = entitycheck; }
                }

            } 

            if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
            {
                this.onHit(raytraceresult);
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            //float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

            // What a hack.
            if (!(this instanceof EntityTrident) || !((Boolean)this.dataManager.get(EntityTrident.RETURNING)).booleanValue())
            {
            	while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
                {
                    this.prevRotationPitch += 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw < -180.0F)
                {
                    this.prevRotationYaw -= 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
                {
                    this.prevRotationYaw += 360.0F;
                }
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f1 = airSpeed;

            if (this.isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }
                
                f1 = waterSpeed;
            }

            if (this.isWet() && this.isBurning())
            {
            	for (int i = 0; i < 10; ++i)
                {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, 0, 0, 0);
                }
            	
                this.extinguish();
            }

            this.motionX *= (double)f1;
            this.motionY *= (double)f1;
            this.motionZ *= (double)f1;

            if (!this.hasNoGravity())
            {
                this.motionY -= 0.05000000074505806D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            
            if (this.noClip != true)
            {
            	this.doBlockCollisions();	
            }
        }
    }

	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (this.noClip != true)
        {
        	if (entity != null)
            {
                DamageSource damagesource;

                if (this.shootingEntity == null)
                {
                    damagesource = DamageSource.causeArrowDamage(this, this);
                }
                else
                {
                    damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
                }

                if (this.isBurning() && !(entity instanceof EntityEnderman))
                {
                    entity.setFire(5);
                }
                
                if (entity.attackEntityFrom(damagesource, this.damage))
                {
                    if (entity instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

                        if (this.shootingEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                            EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                        }

                        //this.applyEnchantments(this, entitylivingbase);
                        this.missileHit(entitylivingbase);
                        
                        if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                        }
                    }
                    
                    playSoundHitEntity();
                    
                    if (this.alwaysBounce)
                    {
                    	this.motionX *= bounceStrength;
                        this.motionY *= bounceStrength;
                        this.motionZ *= bounceStrength;
                        this.rotationPitch += 180.F;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                    }
                }
                else
                {
                    this.motionX *= bounceStrength;
                    this.motionY *= bounceStrength;
                    this.motionZ *= bounceStrength;
                    this.rotationPitch += 180.F;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;
                    this.ticksInAir = 0;
                    
                    if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D)
                    {
                        if (this.pickupStatus == PickupStatus.ALLOWED)
                        {
                            //this.entityDropItem(this.getArrowStack(), 0.1F);
                        }

                        //this.setDead();
                    }
                }
            }
            else
            {
                BlockPos blockpos = raytraceResultIn.getBlockPos();
                this.xTile = blockpos.getX();
                this.yTile = blockpos.getY();
                this.zTile = blockpos.getZ();
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                this.inTile = iblockstate.getBlock();
                this.inData = this.inTile.getMetaFromState(iblockstate);
                this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
                this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
                this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
                float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
                this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
                this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
                playSoundHit();
                this.inGround = true;
                this.arrowShake = 7;
                this.setIsCritical(false);

                missileLand();
                
                if (iblockstate.getMaterial() != Material.AIR)
                {
                    this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
                }
            }
        }
    }
	
	public void missileHit(EntityLivingBase living)
    {
    }
	
	public void missileLand()
    {
    }
	
	public void playSoundHit()
	{
		this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}
	
	public void playSoundHitEntity()
	{
		this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.pickupStatus == PickupStatus.ALLOWED || this.pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

            if (this.pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack()))
            {
                flag = false;
            }

            if (flag)
            {
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

	@Override
	protected ItemStack getArrowStack()
	{
		return null;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
	}
}