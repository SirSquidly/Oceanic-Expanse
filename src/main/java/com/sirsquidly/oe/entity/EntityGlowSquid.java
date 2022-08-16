package com.sirsquidly.oe.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGlowSquid extends EntitySquid
{
	public float brightCooldown = 0.0F;
	public float currBrightness = 1.0F;
	public int minBrightness = 983055 * ConfigHandler.entity.glowSquid.glowSquidBodyBright;
	
	public EntityGlowSquid(World worldIn) {
		super(worldIn);
        this.setSize(0.8F, 0.8F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	public static void registerFixesSquid(DataFixer fixer)
    { EntityLiving.registerFixesMob(fixer, EntityGlowSquid.class); }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityGlowSquid.AIMoveRandom(this));
        this.targetTasks.addTask(1, new EntityGlowSquid.AISpeedAway(this, 3.0, 100));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }
	
	@Override
    protected ResourceLocation getLootTable()
    { return LootTableHandler.ENTITIES_GLOW_SQUID; }
	
	@SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    { BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

    	if (this.world.isBlockLoaded(blockpos$mutableblockpos))
    	{
        blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + (double)this.getEyeHeight()));
        
        if (this.world.getCombinedLight(blockpos$mutableblockpos, 0) > (int)(this.minBrightness * this.currBrightness))
        { return this.world.getCombinedLight(blockpos$mutableblockpos, 0); }
        
        else { return (int)(this.minBrightness * this.currBrightness);  }
    	}
    return 0; 
    }
	
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		this.brightCooldown = 1.0F;
		this.currBrightness = 0.0F;
		return super.attackEntityFrom(source, amount);
    }
	
	public void onUpdate()
    {
		if (this.brightCooldown > 0.0)
        {
        	this.brightCooldown -= 0.02;
        }
        else if (this.currBrightness < 1.0)
        {
        	this.currBrightness += 0.1F;
        }
		
		
		if (this.world.isRemote)
        {
			for (int i = 0; i < 1; ++i)
            {
				Main.proxy.spawnParticle(1, this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * (double)this.width, 0.0D, 0.0D, 0.0D);
            }
			
        }
		
		super.onUpdate();
    }
	
	@Override
	public boolean getCanSpawnHere()
    {
        return this.posY < 30.0 && super.getCanSpawnHere();
    }
        
	static class AISpeedAway extends EntityAIBase
    {
			protected double speedMult;
			protected int fleeTics;
			protected int fleeTimer;
			
			protected final EntityGlowSquid glowSquid;

		    public AISpeedAway(EntityGlowSquid entityGlowSquid, double speedIn, int fleeTime)
		    {
		        this.glowSquid = entityGlowSquid;
		        this.speedMult = speedIn;
		        this.fleeTics = fleeTime;
		        this.fleeTimer = this.fleeTics;
		        this.setMutexBits(1);
		    }

		    public boolean shouldExecute()
		    {
		        if (this.glowSquid.getRevengeTarget() == null) { return false; }
		        return true;
		    }
		    
		    public void resetTask()
		    { this.fleeTimer = this.fleeTics; }
		    
		    public void updateTask()
		    {
		    	float speedNORM = (float) this.glowSquid.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
		    	
		    	if (this.fleeTimer > 1)
		    	{
		    		glowSquid.setAIMoveSpeed((float)(speedNORM * speedMult));	
		    		--this.fleeTimer;
		    	}
		    	if (this.fleeTimer == 1)
		    	{ glowSquid.setAIMoveSpeed((float)(speedNORM)); }
		    }
    }
	
	static class AIMoveRandom extends EntityAIBase
    {
        private final EntityGlowSquid glowSquid;

        public AIMoveRandom(EntityGlowSquid entity)
        { this.glowSquid = entity; }

        public boolean shouldExecute()
        { return true; }

        public void updateTask()
        {
        	int i = this.glowSquid.getIdleTime();
        	
            if (i > 100)
            { this.glowSquid.setMovementVector(0.0F, 0.0F, 0.0F); }
            else if (this.glowSquid.getRNG().nextInt(50) == 0 || !this.glowSquid.inWater || !this.glowSquid.hasMovementVector())
            {
                float f = this.glowSquid.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.glowSquid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.glowSquid.setMovementVector(f1, f2, f3);
            }
        }
    }
}