package com.sirsquidly.oe.entity;

import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.google.common.collect.Sets;
import com.sirsquidly.oe.entity.ai.EntityAIWanderUnderwater;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.LootTableHandler;

public class EntityTropicalFish extends AbstractFish
{
	private static final Set<Item>BREEDING_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityTropicalFish.class, DataSerializers.VARINT);
	
	public EntityTropicalFish(World worldIn) {
		super(worldIn);
        this.setSize(0.5F, 0.4F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
	}
	
	protected void entityInit()
    { 
		super.entityInit(); 
		this.dataManager.register(VARIANT, 0);
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
    }
	
	protected void initEntityAI()
    {	
		this.tasks.addTask(1, new EntityAIWanderUnderwater(this, 1.0D, 20, true));
		this.tasks.addTask(2, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));
    }
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return OESounds.ENTITY_TROPICAL_FISH_HURT; }

    protected SoundEvent getDeathSound()
    { return OESounds.ENTITY_TROPICAL_FISH_DEATH; }
   
    public SoundEvent getFlopSound()
    { return OESounds.ENTITY_TROPICAL_FISH_FLOP; }
    
	@Override
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.ENTITIES_TROPICAL_FISH;
    }
	
	public void onEntityUpdate()
    {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater())
        {
            --i;
            this.setAir(i);

            if (this.getAir() == -20)
            {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 1.0F);
            }
        }
        else
        {
            this.setAir(150);
        }
    }
	
	@Override
	public boolean getCanSpawnHere()
    {
        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.getEntityBoundingBox().minY);
        int z = MathHelper.floor(this.posZ);

		return (checkNeighborSpawn(8, EntityTropicalFish.class) || super.checkBlockDown(x, y, z, 32, OEBlocks.BLUE_CORAL_BLOCK)) && !checkNearbyEntites(16, 20, null) && checkHeight((int)this.posY, this.world);
    }
	
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.getRandomTropicalFishVariant();

        this.setTropicalFishVariant(i);

        return livingdata;
    }

	/** Gets a random tropical fish variant, from the acceptable variants. */
    public int getRandomTropicalFishVariant()
    {
        if (this.rand.nextInt(101) < ConfigHandler.entity.tropicalFish.tropicalFishDefaultChance && ConfigHandler.entity.tropicalFish.defaultTropicalFishVariants.length > 0)
        {
        	int i = this.rand.nextInt(ConfigHandler.entity.tropicalFish.defaultTropicalFishVariants.length);
        	
        	return Integer.parseInt(ConfigHandler.entity.tropicalFish.defaultTropicalFishVariants[i]);
        }
        int possibleColors = ConfigHandler.entity.tropicalFish.enableBlackTropicalFish ? 16 : 15;
        
        return this.rand.nextInt(2) | this.rand.nextInt(6) << 8 | this.rand.nextInt(possibleColors) << 16 | this.rand.nextInt(possibleColors) << 24;
    }
    
    /** This generates the specific name of the tropical fish variant. */
    public static String getSpecificName(int variantInt)
    {
    	String fullName;
    	int patternNum = variantInt >> 8 & 255;
        String patternName = I18n.format("description.oe.tropical_fish_a_pattern" + patternNum + ".name");

        /** This overrides the name generator if the name override config includes the variant. */
    	for(String line : ConfigHandler.entity.tropicalFish.tropicalFishNameOverrides)
    	{
    		String[] split = line.split("=");
    		int what = 0;
			
    		try
    		{ what = Integer.parseInt(split[0]); }
    		catch(NumberFormatException e)
    		{ what = 0; }
    		
			if (what == variantInt)
			{
				return split[1];
			}
    	}
        
    	if ((variantInt & 255) != 0)
    	{
    		patternName = I18n.format("description.oe.tropical_fish_b_pattern" + patternNum + ".name");
    	}
    	
    	String color1 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 16 & 255).getDyeColorName());
    	String color2 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 24 & 255).getDyeColorName());
    	
    	if (ConfigHandler.entity.tropicalFish.tropicalFishBedrockColors)
    	{
    		int[] bedrockSpecials = new int[]{3, 6, 8, 9, 10}; 
    		
    		if (ArrayUtils.contains(bedrockSpecials, EnumDyeColor.byMetadata(variantInt >> 16 & 255).getMetadata()))
        	{ color1 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 16 & 255).getMetadata()); }
        	if (ArrayUtils.contains(bedrockSpecials, EnumDyeColor.byMetadata(variantInt >> 24 & 255).getMetadata()))
        	{ color2 = I18n.format("description.oe.tropical_fish_color." + EnumDyeColor.byMetadata(variantInt >> 24 & 255).getMetadata()); }
    	}
    	
    	if (!color1.equals(color2))
    	{
    		fullName = color1 + "-" + color2 + " " + patternName;
    	}
    	else
    	{
    		fullName = color1 + " " + patternName;
    	}

    	return fullName;
    }
    
	public float getEyeHeight()
    {
        return this.height * 0.6F;
    }
	
	public int getMaxSpawnedInChunk()
    { return 20; }
	
	public EntityTropicalFish createChild(EntityAgeable ageable)
    {
		EntityTropicalFish fish = new EntityTropicalFish(this.world);
		int i = this.getRandomTropicalFishVariant();

        fish.setTropicalFishVariant(i);
		
        return fish;
    }
	
	public boolean isBreedingItem(ItemStack stack)
    {
        return BREEDING_ITEMS.contains(stack.getItem());
    }
	
	public int getTropicalFishVariant()
    { return this.dataManager.get(VARIANT); }
	
	public void setTropicalFishVariant(int state)
    {
        this.dataManager.set(VARIANT, state);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getTropicalFishVariant());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setTropicalFishVariant(compound.getInteger("Variant"));
    }
}