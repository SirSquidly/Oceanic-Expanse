package com.sirsquidly.oe.entity;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigArrayHandler;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityPufferfish extends AbstractFish
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.MELON_SEEDS, Items.BEETROOT_SEEDS, Items.CARROT);
	private static final DataParameter<Integer> STATE = EntityDataManager.createKey(EntityPufferfish.class, DataSerializers.VARINT);
	/** Cooldown between puffing up, so stage 1 isn't skipped */
	private int puffCooldown;
	/** If reaches a number, lowers the PuffState, and is set back to 0. */
    private int calmCounter;

    public EntityPufferfish(World world)
    {
        super(world);
        this.setSize(0.6F, 0.6F);
        this.setPuffState(0);
    }

    public EntityPufferfish(World world, int stage)
    {
        this(world);
        this.setPuffState(stage);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAIWanderUnderwater(this, 1.0D, 20, true));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 0.0D, false));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));
    }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_PUFFERFISH_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_PUFFERFISH_DEATH; }
   
    public SoundEvent getFlopSound()
    { return OESounds.ENTITY_PUFFERFISH_FLOP; }
    
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_PUFFERFISH; }
	
	@Override
	public boolean getCanSpawnHere()
    {
        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.getEntityBoundingBox().minY);
        int z = MathHelper.floor(this.posZ);

		return (checkNeighborSpawn(8, EntityPufferfish.class) || super.checkBlockDown(x, y, z, 32, OEBlocks.BLUE_CORAL_BLOCK)) && !checkNearbyEntites(16, 20, null) && checkHeight((int)this.posY, this.world);
    }
	
	protected void collideWithEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase && this.getAttackTarget() != entityIn && !(entityIn instanceof EntityCreeper) && this.getRNG().nextInt(10) == 0)
        {
        	if ((entityIn instanceof EntityPlayer || !ConfigArrayHandler.pufferfishFriends.contains(EntityList.getKey(entityIn))))
        	{
        		this.setAttackTarget((EntityLivingBase)entityIn);
        	}
        }
        super.collideWithEntity(entityIn);
    }
	
    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATE, 0);
    }

    public EntityPufferfish createChild(EntityAgeable ageable)
    {
        return new EntityPufferfish(this.world);
    }
    
    public boolean isBreedingItem(ItemStack stack)
    {
        return BREEDING_ITEMS.contains(stack.getItem());
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        if(!this.isDead && !this.world.isRemote)
        {
        	this.setCalmCounter(this.getCalmCounter() + 1);
            this.setPuffCooldown(this.getPuffCooldown() + 1); 
            
        	if(this.getCalmCounter() >= 80)
            {
        		this.playSound(OESounds.ENTITY_PUFFERFISH_DEFLATE, 1.0F, 0.8F / (this.rand.nextFloat() * 0.4F + 0.8F));
            	this.setPuffState(this.getPuffState() - 1);
                this.setCalmCounter(0);
            }
        	
        	List<Entity> checkNonFriends = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(4, 4, 4)); 	
        	
        	for (Entity entity : checkNonFriends)
        	{
    			if (entity.isInvisible()) continue;
    			
    			
    			if (entity instanceof EntityLivingBase && (entity instanceof EntityPlayer || !ConfigArrayHandler.pufferfishFriends.contains(EntityList.getKey(entity))))
	        	{
	        		if (entity.isInvisible()) continue;
        			if(this.getPuffState() < 2)
        			{
        				if(this.getPuffCooldown() >= 20)
        				{
        					this.playSound(OESounds.ENTITY_PUFFERFISH_INFLATE, 1.0F, 0.8F / (this.rand.nextFloat() * 0.4F + 0.8F));
	        				this.setCalmCounter(0);
	        				this.setPuffCooldown(0);
	                    	this.setPuffState(this.getPuffState() + 1);
        				}
        			}
        			else 
        			{
	        			this.setCalmCounter(0);
	        			this.setPuffCooldown(0);
        			}
        		}
    		}
        }
    }
    
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag)
        {
        	this.playSound(OESounds.ENTITY_PUFFERFISH_STING, 1.0F, 1.0F);
        	
        	this.applyEnchantments(this, entityIn);
        	
        	if (entityIn instanceof EntityLivingBase && this.getPuffState() == 2)
        	{ ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, ConfigHandler.entity.pufferfish.pufferfishPoisonLength *  20, Math.min(ConfigHandler.entity.pufferfish.pufferfishPoisonAmplifier - 1, 0))); }
        }
        return flag;
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("PuffState", this.getPuffState());
        compound.setInteger("PuffCooldown", this.getPuffCooldown());
        compound.setInteger("CalmCounter", this.getCalmCounter());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setPuffState(compound.getInteger("PuffState"));
        this.setPuffCooldown(compound.getInteger("PuffCooldown"));
        this.setCalmCounter(compound.getInteger("CalmCounter"));
    }

    @Override
    protected boolean canTriggerWalking()
    { return false; }


    public int getPuffState()
    { return this.dataManager.get(STATE); }

    private int getCalmCounter()
    { return this.calmCounter; }

    private int getPuffCooldown()
    { return this.puffCooldown; }
    
    private void setPuffState(int state)
    {
        if(state < 0)
        { state = 0; }
        else if(state > 2)
        { state = 2; }

        this.dataManager.set(STATE, state);
    }

    private void setCalmCounter(int i)
    {
        this.calmCounter = i;
    }
    
    private void setPuffCooldown(int i)
    { this.puffCooldown = i; }
    
    
    
 // Warning, Guardian stuff ahead    
	
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	
    	if (ConfigHandler.entity.pufferfish.enablePufferfishInflateOnHit)
    	{
    		this.setCalmCounter(0);
    		this.setPuffCooldown(0);
    		
    		if (this.getPuffState() < 2)
    		{
    			this.playSound(OESounds.ENTITY_PUFFERFISH_INFLATE, 1.0F, 0.8F / (this.rand.nextFloat() * 0.4F + 0.8F));
    			this.setPuffState(2);
    		}
    	}

        if (source.getImmediateSource() instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)source.getImmediateSource();

            if (!source.isExplosion())
            {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
            }
        }

        if (this.wander != null)
        {
            this.wander.makeUpdate();
        }

        return super.attackEntityFrom(source, amount);
    }
}
