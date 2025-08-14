package com.sirsquidly.oe.entity.item;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.projectile.EntityArrow;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.CommonProxy;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityTrident extends AbstractArrow
{
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityTrident.class, DataSerializers.ITEM_STACK);
	static final DataParameter<Boolean> RETURNING = EntityDataManager.<Boolean>createKey(EntityTrident.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> DID_LIGHTNING = EntityDataManager.<Boolean>createKey(EntityTrident.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> DID_HIT = EntityDataManager.<Boolean>createKey(EntityTrident.class, DataSerializers.BOOLEAN);
	/** This saves the true owner of a Trident, so projectile reflection doesn't confuse Loyalty at all. */
	public Entity trueOwner;

	public EntityTrident(World worldIn)
	{
		super(worldIn);
		//this.pickupStatus = PickupStatus.DISALLOWED;
		this.damage = ConfigHandler.item.trident.tridentThrowDamage;
		this.alwaysBounce = true;
		this.bounceStrength = -0.01D;
		this.bounceYStrength = -0.1D;
		this.waterSpeed = 0.99F;
	}

	public EntityTrident(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

	public EntityTrident(World worldIn, EntityLivingBase shooter)
	{ this(worldIn, shooter, shooter); }

	public EntityTrident(World worldIn, EntityLivingBase shooter, EntityLivingBase trueOwnerIn)
	{
		this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
		this.shootingEntity = shooter;
		this.trueOwner = trueOwnerIn;

		if (trueOwnerIn instanceof EntityPlayer)
		{
			this.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
		}
	}

	protected void entityInit()
    {
		super.entityInit();

        this.dataManager.register(ITEM, ItemStack.EMPTY);
        this.dataManager.register(RETURNING, Boolean.valueOf(false));
        this.dataManager.register(DID_LIGHTNING, Boolean.valueOf(false));
        this.dataManager.register(DID_HIT, Boolean.valueOf(false));
    }

	public void playSoundHit()
	{
		this.playSound(OESounds.ENTITY_TRIDENT_IMPACT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	public void playSoundHitEntity()
	{
		this.playSound(OESounds.ENTITY_TRIDENT_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	/**
     * Only runs after the Trident has hit an entity
     */
	public void missileHit(EntityLivingBase living)
    {
		damageItem(1);
		this.doChanneling(living);
		this.doVanillaEnchants(living);
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
		if ((ConfigHandler.enchant.loyalty.loyaltyVoidReturn && this.posY < ConfigHandler.enchant.loyalty.loyaltyVoidReturnLevel || this.ticksInGround >= 8) && !isReturning())
        {
			this.checkLoyalty();
        }
		
		if (!world.isRemote && isReturning())
		{
			/* These variables are set specifically so Loyalty Tridents can be picked back up on world Reloading */
			this.arrowShake = -1;
			this.noClip = true;
			this.setNoGravity(true);

			if (this.trueOwner != null && !this.trueOwner.isDead)
	        {
				int i = EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem());
				double d0 = this.trueOwner.posX - this.posX;
	            double d1 = this.trueOwner.posY + (double)this.trueOwner.getEyeHeight() - this.posY;
	            double d2 = this.trueOwner.posZ - this.posZ;
	            double d3 = 0.15 + i * 0.05D;
	            
	            this.rotationPitch = 180.0F;
	            this.motionX = d0 * d3;
	            this.motionY = d1 * d3;
	            this.motionZ = d2 * d3;
	        }
			else
			{
				if (this.trueOwner instanceof EntityPlayer)
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
            	if (e == this.trueOwner && !(e instanceof EntityPlayer))
            	{
            		this.setDead();
            	}
            }
		}

		if (this.getItem() == ItemStack.EMPTY || this.getItem().getItemDamage() >= this.getItem().getMaxDamage())
		{ this.setDead(); }
		
		super.onUpdate();
    }
	
	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (this.noClip == true) return;

        if (!((Boolean)this.dataManager.get(DID_HIT)).booleanValue()) damageItem(1);
        this.dataManager.set(DID_HIT, Boolean.valueOf(true));
        if (((Boolean)this.dataManager.get(DID_HIT)).booleanValue()) this.canEntityCollide = false;
        
        if (entity != null)
        {
            DamageSource damagesource;

            damagesource = CommonProxy.causeTridentDamage(this, this.shootingEntity == null ? this : this.shootingEntity);

            float f = this.damage;
            if (entity instanceof EntityLivingBase) f += EnchantmentHelper.getModifierForCreature(this.getItem(), ((EntityLivingBase)entity).getCreatureAttribute());
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, this.getItem()) > 0) f += EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, this.getItem()) * 0.5D + 0.5D;
            
            if (entity.attackEntityFrom(damagesource, f))
            {
            	if (this.isBurning() && !(entity instanceof EntityEnderman))
                {		
                    entity.setFire(5);
                }
            	
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
                    this.motionY *= bounceYStrength;
                    this.motionZ *= bounceStrength;
                    this.rotationPitch += 180.F;
                    //this.rotationYaw += 180.0F;
                    //this.prevRotationYaw += 180.0F;
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
	
	/**
     * HAndles damaging the Trident upon landing AND/OR hitting an Entity. Also handles breaking and removing the Trident if the item breaks.
     */
	public void damageItem(int amount)
    {
		boolean alwaysBreak = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, this.getItem()) > 0;
		int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this.getItem());
		int unbreakingReduction = 0;
		
		if (!getItem().isItemStackDamageable() && !alwaysBreak) return;

        for (int k = 0; unbreakingLvl > 0 && k < amount; ++k)
        {
            if (EnchantmentDurability.negateDamage(this.getItem(), unbreakingLvl, rand))
            {
                ++unbreakingReduction;
            }
        }

        amount -= unbreakingReduction;

		getItem().setItemDamage(getItem().getItemDamage() + amount);

		
		
		if (getItem().getItemDamage() > getItem().getMaxDamage() - 1 || alwaysBreak)
		{
        	this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, this.getSoundCategory(), 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);

            for (int i = 0; i < 10; ++i)
            {
            	if (this.world instanceof WorldServer)
                    ((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX - this.motionX * this.rand.nextDouble(), this.posY - this.motionY * this.rand.nextDouble(), this.posZ - this.motionZ * this.rand.nextDouble(), 0, this.motionX, this.motionY, this.motionZ, 0.0D, Item.getIdFromItem(getItem().getItem()), getItem().getMetadata());
                else
                    this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, Item.getIdFromItem(getItem().getItem()), getItem().getMetadata());
            }
        	
            this.setDead();
		}
    }
	
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote)
        {
        	if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem()) > 0)
        	{
        		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, this.getItem()) > 0) this.setDead();
        		if (entityIn == this.trueOwner)
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
	
	/**
     * Plays the Loyalty Sound, and sets this entity to do all of the returning behavior
     */
	public void checkLoyalty()
    {
		if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, this.getItem()) > 0)
        {
			this.playSound(OESounds.ENTITY_TRIDENT_RETURN, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.dataManager.set(RETURNING, Boolean.valueOf(true));
			this.noClip = true;
			this.setNoGravity(true);
        }
    }

	public void doChanneling(EntityLivingBase target)
    {
		if (EnchantmentHelper.getEnchantmentLevel(OEEnchants.CHANNELING, this.getItem()) > 0)
		{
			if (world.isThundering() && world.canSeeSky(this.getPosition()) && !isReturning())
			{
				if (target != null && (!ConfigHandler.enchant.channeling.waterCheck || !target.isInWater()) && (!ConfigHandler.enchant.channeling.lavaCheck || !target.isInLava()))
				{
					if (!ConfigArrayHandler.ridingBlacklist.contains(EntityList.getKey(target.getLowestRidingEntity())))
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
	
	/**
     * Applies Vanilla Enchantment effects to the hit entity
     */
	public void doVanillaEnchants(EntityLivingBase target)
    {
		int fireAspectLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, this.getItem());
    	int knockbackLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, this.getItem());
    	int punchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, this.getItem());
		
    	if (fireAspectLvl > 0)
        { target.setFire(fireAspectLvl * 4); }
		
    	if (knockbackLvl > 0)
        { target.knockBack(this, (float)knockbackLvl * 0.5F, (double)-MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F))); }
    	
    	
    	if (punchLvl > 0)
        {
            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (f1 > 0.0F)
            { target.addVelocity(this.motionX * punchLvl * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * punchLvl * 0.6000000238418579D / (double)f1); }
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
	
	public boolean isReturning()
	{ return ((Boolean)this.dataManager.get(RETURNING)).booleanValue(); }

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
        if (nbttagcompound != null && !nbttagcompound.isEmpty())
        {
            this.setItem(new ItemStack(nbttagcompound));
        }

		if (compound.hasKey("TrueOwner", 8))
		{
			String uuidS = compound.getString("TrueOwner");

			if (!uuidS.isEmpty())
			{
				try
				{
					this.trueOwner =  this.world.getPlayerEntityByUUID(UUID.fromString(uuidS));
				}
				catch (Throwable e)
				{ }
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setBoolean("Returning", isReturning());
		compound.setBoolean("Did_Lightning", ((Boolean)this.dataManager.get(DID_LIGHTNING)).booleanValue());
		if (!this.getItem().isEmpty())
        {
            compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
        }
		if(this.trueOwner instanceof EntityPlayer)
		{
			compound.setString("TrueOwner", this.trueOwner.getUniqueID().toString());
		}
	}
}