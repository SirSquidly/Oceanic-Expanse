package com.sirsquidly.oe.entity;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.proxy.CommonProxy;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTrident extends AbstractArrow
{
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityTrident.class, DataSerializers.ITEM_STACK);
	static final DataParameter<Boolean> RETURNING = EntityDataManager.<Boolean>createKey(EntityTrident.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> DID_LIGHTNING = EntityDataManager.<Boolean>createKey(EntityTrident.class, DataSerializers.BOOLEAN);
	
	public EntityTrident(World worldIn)
	{
		super(worldIn);
		this.pickupStatus = PickupStatus.DISALLOWED;
		this.damage = ConfigHandler.item.trident.tridentThrowDamage;
		this.alwaysBounce = true;
		this.bounceStrength = -0.01D;
		this.waterSpeed = 0.99F;
	}

	public EntityTrident(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

	public EntityTrident(World worldIn, EntityLivingBase shooter)
    {
        this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer)
        {
        	this.pickupStatus = PickupStatus.ALLOWED;
        }
    }

	protected void entityInit()
    {
		super.entityInit();

        this.dataManager.register(ITEM, ItemStack.EMPTY);
        this.dataManager.register(RETURNING, Boolean.valueOf(false));
        this.dataManager.register(DID_LIGHTNING, Boolean.valueOf(false));
    }

	public void playSoundHit()
	{
		this.playSound(OESounds.ENTITY_TRIDENT_IMPACT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	public void playSoundHitEntity()
	{
		this.playSound(OESounds.ENTITY_TRIDENT_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	public void missileHit(EntityLivingBase living)
    {
		this.getItem().damageItem(1, living);
		this.doChanneling(living);
		this.checkLoyalty();
    }
	
	public void missileLand()
    {
		this.doChanneling(null);
    }
	
	@Override
	protected ItemStack getArrowStack()
	{
		ItemStack stack = this.getItem(); 
		this.setNBT(stack, true);
		return stack;
	}

	public ItemStack getItem()
    {
        return (ItemStack)this.getDataManager().get(ITEM);
    }

	@Override
	public void onUpdate()
    {
		super.onUpdate();
		
		if ((ConfigHandler.item.trident.loyaltyVoidReturn && this.posY < ConfigHandler.item.trident.loyaltyVoidReturnLevel || this.ticksInGround >= 8) && !((Boolean)this.dataManager.get(RETURNING)).booleanValue())
        {
			this.checkLoyalty();
        }
		
		if (!world.isRemote && ((Boolean)this.dataManager.get(RETURNING)).booleanValue())
		{
			if (this.shootingEntity != null && !this.shootingEntity.isDead)
	        {
				int i = EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem());
				double d0 = this.shootingEntity.posX - this.posX;
	            double d1 = this.shootingEntity.posY + (double)this.shootingEntity.getEyeHeight() - this.posY;
	            double d2 = this.shootingEntity.posZ - this.posZ;
	            double d3 = 0.15 + i * 0.05D;
	            
	            this.rotationPitch = 180.0F;
	            this.motionX = d0 * d3;
	            this.motionY = d1 * d3;
	            this.motionZ = d2 * d3;
	        }
			else
			{
				if (this.shootingEntity instanceof EntityPlayer)
		        {
					this.entityDropItem(this.getArrowStack(), 0.1F);
					this.setDead();
				}
				else
				{
					this.setDead();
				}
			}
			
			List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(1, 1, 1)));

            for (Entity e : list)
            {
            	if (e == this.shootingEntity && !(e instanceof EntityPlayer))
            	{
            		this.setDead();
            	}
            }
		}
    }
	
	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (this.noClip == true)
        {
        	return;
        }
        
        if (entity != null)
        {
            DamageSource damagesource;

            damagesource = CommonProxy.causeTridentDamage(this, this.shootingEntity == null ? this : this.shootingEntity);

            if (this.isBurning() && !(entity instanceof EntityEnderman))
            {
                entity.setFire(5);
            }
            
            if (entity.attackEntityFrom(damagesource, this.damage))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

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
            }
        }
        else
        {
        	super.onHit(raytraceResultIn);
        }
    }
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote)
        {
        	if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem()) > 0)
        	{
        		if (entityIn == this.shootingEntity)
                {
                    super.onCollideWithPlayer(entityIn);
                }
        	}
        	else
        	{
        		super.onCollideWithPlayer(entityIn);
        	}
        }
    }
	
	public void checkLoyalty()
    {
		if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem()) > 0)
        {
			this.playSound(OESounds.ENTITY_TRIDENT_RETURN, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.dataManager.set(RETURNING, Boolean.valueOf(true));
			this.inGround = true;
			this.noClip = true;
			this.setNoGravity(true);
        }
    }

	public void doChanneling(EntityLivingBase target)
    {
		if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.CHANNELING, this.getItem()) > 0)
		{
			if (world.isThundering() && world.canSeeSky(this.getPosition()) && !((Boolean)this.dataManager.get(DID_LIGHTNING)).booleanValue())
			{
				if (target != null && (!ConfigHandler.enchant.channeling.waterCheck || !target.isInWater()) && (!ConfigHandler.enchant.channeling.lavaCheck || !target.isInLava()))
				{
					if (!ArrayUtils.contains(ConfigHandler.enchant.channeling.ridingBlacklist, EntityList.getKey(target.getLowestRidingEntity()).toString()) )
					{
						this.playSound(OESounds.ENTITY_TRIDENT_THUNDER, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
						world.addWeatherEffect(new EntityLightningBolt(world, posX, posY, posZ, false));
						this.dataManager.set(DID_LIGHTNING, Boolean.valueOf(true));
					}
				}
				else if (this.inGround && (!ConfigHandler.enchant.channeling.invertLightning && ArrayUtils.contains(ConfigHandler.enchant.channeling.lightningRodWhitelist, this.inTile.getRegistryName().toString()) || ConfigHandler.enchant.channeling.invertLightning && !ArrayUtils.contains(ConfigHandler.enchant.channeling.lightningRodWhitelist, this.inTile.getRegistryName().toString())))
				{
					this.playSound(OESounds.ENTITY_TRIDENT_THUNDER, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					world.addWeatherEffect(new EntityLightningBolt(world, posX, posY, posZ, false));
					this.dataManager.set(DID_LIGHTNING, Boolean.valueOf(true));
				}
			}
		}
    }

	public void setItem(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            stack = stack.copy();
            stack.setCount(1);
            this.setNBT(stack, false);
        }

        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
    }

	public void setNBT(ItemStack stack, boolean strip)
	{
	    NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
	 
	    if (nbt.hasKey("Thrown") && strip)
	    { nbt.removeTag("Thrown"); }
	    else
	    { nbt.setInteger("Thrown", 1); }
	    stack.setTagCompound(nbt);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		this.dataManager.set(RETURNING, Boolean.valueOf(compound.getBoolean("Returning")));
		this.dataManager.set(DID_LIGHTNING, Boolean.valueOf(compound.getBoolean("Did_Lightning")));
		NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        if (nbttagcompound != null && !nbttagcompound.hasNoTags())
        {
            this.setItem(new ItemStack(nbttagcompound));
        }
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setBoolean("Returning", ((Boolean)this.dataManager.get(RETURNING)).booleanValue());
		compound.setBoolean("Did_Lightning", ((Boolean)this.dataManager.get(DID_LIGHTNING)).booleanValue());
		if (!this.getItem().isEmpty())
        {
            compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
        }
	}
}