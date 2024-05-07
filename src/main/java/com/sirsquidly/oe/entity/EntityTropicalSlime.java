package com.sirsquidly.oe.entity;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.items.ItemSpawnBucket;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityTropicalSlime extends EntitySlime
{
	/** Counts until a Tropical Slime Evaporates.*/
    public int evaporateTimer;
    
	public EntityTropicalSlime(World worldIn)
	{
		super(worldIn);
		
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	protected void initEntityAI()
    { 
        super.initEntityAI();
        
        this.targetTasks.removeTask(new EntityAIFindEntityNearestPlayer(this));
        
        //this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }
	
	/**
	 * Forcefully updates the Health of the Tropical Slime after size changes
	 */
	public void updateHealth(boolean resetHealth)
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(this.getSlimeSize() * this.getSlimeSize() * 2));
		if (resetHealth)
        { this.setHealth(this.getMaxHealth()); }
	}
	
	protected Item getDropItem()
    { return this.getSlimeSize() == 1 ? OEItems.BLUE_SLIME_BALL : null; }
	
	protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_TROPICAL_SLIME_DEATH; }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {  return OESounds.ENTITY_TROPICAL_SLIME_HURT; }
	
	protected SoundEvent getJumpSound()
    { return OESounds.ENTITY_TROPICAL_SLIME_JUMP; }
	
	protected SoundEvent getSquishSound()
    { return OESounds.ENTITY_TROPICAL_SLIME_SQUISH; }
	
	@Override
	protected EnumParticleTypes getParticleType()
    { return this.inWater ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.WATER_SPLASH; }
	
	@Nullable
    protected ResourceLocation getLootTable()
    { return this.getSlimeSize() > 1 ? LootTableHandler.ENTITIES_TROPICAL_SLIME : LootTableList.EMPTY; }
	
	@Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
		if (entitylivingbaseIn != null && !entitylivingbaseIn.isDead && !entitylivingbaseIn.isWet() || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {}
        else
        { super.setAttackTarget(entitylivingbaseIn); }
    }
	
	public void onUpdate()
    {
		EntityLivingBase attackTarget = this.getAttackTarget();
		super.onUpdate();

        if (this.world.provider.doesWaterVaporize() || this.isBurning())
        {
        	double l = this.posX;
        	double i = this.posY;
            double j = this.posZ;
            
            for (int k = 0; k < 2; ++k)
            { world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D); }
            
            if (!world.isRemote) this.evaporateTimer += 1;
            
            if (this.evaporateTimer >= 50)
            {
            	world.playSound(null, this.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            	this.setDead();
            }
        }
        
        //** The material check is to make sure they don't act odd when the player is bobbing on the surface of water. */
        if (!world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL || (attackTarget != null && (!attackTarget.isWet() && this.world.getBlockState(attackTarget.getPosition().down()).getMaterial() != Material.WATER)))
        {
        	setAttackTarget(null);
        }
    }
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (ConfigHandler.entity.tropicalSlime.tropicalSlimeBucketKill && itemstack.getItem() == Items.BUCKET)
        {
        	player.swingArm(EnumHand.MAIN_HAND);
			
        	double l = this.posX;
        	double i = this.posY;
            double j = this.posZ;
            
            for (int k = 0; k < 50; ++k)
            { world.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D); }
            
			if (!this.world.isRemote)
			{
                if (!player.capabilities.isCreativeMode)
                {
                	itemstack.setCount(itemstack.getCount() - 1);
                }
                
                ItemStack newStack = new ItemStack(ConfigHandler.entity.tropicalFish.enableTropicalFish ? OEItems.SPAWN_BUCKET : Items.WATER_BUCKET);
                
                if (ConfigHandler.entity.tropicalFish.enableTropicalFish)
                {
                	EntityTropicalFish tropicalFish = new EntityTropicalFish(world);
        			tropicalFish.setTropicalFishVariant(tropicalFish.getRandomTropicalFishVariant());
                    
                    ItemSpawnBucket.recordEntityNBT(newStack, player, tropicalFish);
                }
                else
                { spawnTropicalFish(world, this.getPosition(), true); }
                
                if (itemstack.isEmpty()) 
                { player.setHeldItem(EnumHand.MAIN_HAND, newStack); } 
                
                else if (!player.inventory.addItemStackToInventory(newStack)) 
                { player.dropItem(newStack, false); }
                
                // Lazy solution to the game trying to instantly use the spawn bucket when given to the survival player
                player.getCooldownTracker().setCooldown(newStack.getItem(), 1);
                
                this.isDead = true;
			}
			
			return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }
	
	public void spawnTropicalFish(World world, BlockPos pos, Boolean deadFish)
	{
		if (world.isRemote) return;
		
		if (deadFish || !ConfigHandler.entity.tropicalFish.enableTropicalFish)
		{
			ItemStack itemstack = new ItemStack(Items.FISH, 1, 2);
			
	    	this.entityDropItem(itemstack, 0.0F);
		}
		else
		{
			EntityTropicalFish tropicalFish = deadFish ? new EntityTropicalFish(world) : new EntityTropicalFish(world);
			
			tropicalFish.setTropicalFishVariant(tropicalFish.getRandomTropicalFishVariant());
	    	
	    	tropicalFish.setPosition((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
	    	
			world.spawnEntity(tropicalFish);
		}
	}
	
	public boolean canBreatheUnderwater()
    { return true; }
	
	public boolean isNotColliding()
    { return this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this); }
	
	protected boolean canDamagePlayer()
    { return !this.isSmallSlime() && this.getAttackTarget() != null; }
	
	@Override
	public boolean getCanSpawnHere()
    {
        BlockPos blockpos = new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        Chunk chunk = this.world.getChunk(blockpos);

        if (this.world.getWorldInfo().getTerrainType().handleSlimeSpawnReduction(rand, world))
        {
            return false;
        }
        else
        {
            if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
            	IBlockState iblockstate = this.world.getBlockState((new BlockPos(this)).down());
                Biome biome = this.world.getBiome(blockpos);

                if (ConfigHandler.entity.tropicalSlime.tropicalSlimeJungleSpawning && biome == Biomes.JUNGLE && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.world.getCurrentMoonPhaseFactor() && this.world.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8))
                {
                    return iblockstate.canEntitySpawn(this);
                }

                if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
                {
                    return iblockstate.canEntitySpawn(this);
                }
            }

            return false;
        }
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        if (this.rand.nextFloat() < 0.05F)
        { this.setLeftHanded(true); }
        else
        { this.setLeftHanded(false); }

        float f = difficulty.getClampedAdditionalDifficulty();
        
        this.setSlimeSize(this.rand.nextFloat() < f * 0.1F ? 4 : 2, true);
        this.updateHealth(true);
        return livingdata;
    }
	
	public void setDead()
    {
        int i = this.getSlimeSize();

        if (i <= 2) this.spawnTropicalFish(world, this.getPosition(), false);
        
        if (!this.world.isRemote && i > 2 && this.getHealth() <= 0.0F)
        {
            int j = 2 + this.rand.nextInt(3);
            
            for (int k = 0; k < j; ++k)
            {
                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
                EntityTropicalSlime entityTropicalslime = new EntityTropicalSlime(this.world);

                if (this.hasCustomName())
                { entityTropicalslime.setCustomNameTag(this.getCustomNameTag()); }

                if (this.isNoDespawnRequired())
                { entityTropicalslime.enablePersistence(); }

                entityTropicalslime.setSlimeSize(i / 2, true);
                this.updateHealth(true);
                entityTropicalslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.spawnEntity(entityTropicalslime);
            }
        }

        this.isDead = true;
    }
	
	/**
	 * We Override this so the hopping is unaffected by Water, and it does not try to float
	 */
	@Override
	public boolean isInWater()
    {
        return false;
    }
}