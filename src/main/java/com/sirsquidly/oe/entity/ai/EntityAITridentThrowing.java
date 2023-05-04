package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class EntityAITridentThrowing<T extends EntityMob> extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;
    private final float minAttackDistance;

    public EntityAITridentThrowing(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn, float minAttackDistanceIn)
    {
        this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn, minAttackDistanceIn);
    }

    public EntityAITridentThrowing(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn, float minAttackDistanceIn)
    {
        this.rangedAttackTime = -1;

        if (!(attacker instanceof EntityLivingBase))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
        	this.rangedAttackEntityHost = attacker;
            this.entityHost = (EntityLiving)attacker;
            this.entityMoveSpeed = movespeed;
            this.attackIntervalMin = p_i1650_4_;
            this.maxRangedAttackTime = maxAttackTime;
            this.attackRadius = maxAttackDistanceIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.minAttackDistance = minAttackDistanceIn * minAttackDistanceIn;
            this.setMutexBits(3);
        }
    }

    protected boolean tridentInMainhand()
    {
        return this.entityHost.getHeldItemMainhand().getItem() == OEItems.TRIDENT_ORIG;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        
        if (entitylivingbase == null || entitylivingbase.isDead)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
            
            if (d0 <= (double)this.minAttackDistance)
            {
            	return false;
            }
            else
            {
            	return tridentInMainhand();
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
        
        if (rangedAttackEntityHost instanceof EntityZombie)
        { ((EntityZombie) this.rangedAttackEntityHost).setArmsRaised(false); }
        
        this.entityHost.resetActiveHand();
    }

    public void startExecuting()
    {
        this.rangedAttackTime = this.maxRangedAttackTime;
        this.entityHost.setActiveHand(EnumHand.MAIN_HAND);
        if (rangedAttackEntityHost instanceof EntityZombie)
        { ((EntityZombie) this.rangedAttackEntityHost).setArmsRaised(true); }
        
        super.startExecuting();
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        this.entityHost.setActiveHand(EnumHand.MAIN_HAND);
        
        if (flag)
        {
            ++this.seeTime;
        }
        else
        {
        	this.entityHost.resetActiveHand();
            this.seeTime = 0;
        }

        if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
        {
            this.entityHost.getNavigator().clearPath();
        }
        else
        {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

        if (this.entityHost.isHandActive())
        {
        	if (--this.rangedAttackTime == 0)
            {
                if (!flag)
                {
                	this.entityHost.resetActiveHand();
                    return;
                }
                if (rangedAttackEntityHost instanceof EntityZombie)
                {
                	((EntityZombie) this.rangedAttackEntityHost).setArmsRaised(true);
                }
                
                this.entityHost.resetActiveHand();
                float f = MathHelper.sqrt(d0) / this.attackRadius;
                float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
                this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
                this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
            }
        }
        else if (--this.rangedAttackTime <= 0)
        {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
        }
    }
}