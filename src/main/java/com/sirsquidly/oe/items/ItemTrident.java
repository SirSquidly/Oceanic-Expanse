package com.sirsquidly.oe.items;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.item.EntityTrident;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.init.OESounds;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrident extends Item
{
	public ItemTrident() {
		super();
		setFull3D();
		this.maxStackSize = 1;
		this.setMaxDamage(ConfigHandler.item.trident.tridentDurability - 1);
		
		// Weird implimentation, re-re-observe examples
		this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				NBTTagCompound nbttagcompound = stack.getTagCompound();
				
				if (nbttagcompound != null && nbttagcompound.hasKey("Thrown"))
				{
					return 0.0F;
				}
				if (entityIn != null && entityIn.getActiveItemStack().getItem() instanceof ItemTrident && entityIn.getItemInUseCount() > 0)
				{
					return worldIn == null ? 1.0F : 2.0F;
				}
				
				return worldIn == null ? 1.0F : 0.0F;
			}
		});
	}

	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }
	
	public EnumAction getItemUseAction(ItemStack stack)
    { return Main.SPEAR; }

    public int getMaxItemUseDuration(ItemStack stack)
    { return 72000; }
    
    public int getItemEnchantability()
    { return 22; }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        int r = EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, itemstack);
        
        if ((ConfigHandler.item.trident.tridentCanThrowBreak || itemstack.getItemDamage() < itemstack.getMaxDamage() - 1) && (r == 0 || canLetItRip(worldIn, playerIn)))
        {
        	playerIn.setActiveHand(handIn);
        	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
    
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer && !(stack.getItemDamage() > stack.getMaxDamage()))
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i < 0) return;

            float f = getArrowVelocity(i);
        	int r = EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, stack);
        	
        	if (f >= 0.6 && !worldIn.isRemote)
        	{ 
        		if (r <= 0 || entityplayer.isSneaking() && ConfigHandler.enchant.riptide.riptideSneakThrowing)
            	{
        			float velocity = r > 0 ? r * 0.8F : 0.8F;
        			
    				EntityTrident entitytrient = new EntityTrident(worldIn, entityplayer);
                    entitytrient.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

                    entitytrient.setIsCritical(true);
                    
                    entitytrient.setItem(stack);
                    
                    boolean infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                    if (j > 0)
                    {
                    	entitytrient.setDamage(entitytrient.getDamage() + (double)j * 0.5D + 0.5D);
                    }
                    
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack) > 0)
                    { entitytrient.setKnockbackStrength(EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack)); }
                    
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) entitytrient.setFire(100);
                    
                    if (!infinity && !entityplayer.capabilities.isCreativeMode)
                    {
                    	stack.setCount(0);
                    }
                    else
                    {
                    	if (infinity) stack.damageItem(1, entityplayer);
                    	entitytrient.pickupStatus = PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.spawnEntity(entitytrient);	
    			
            	}
            	else if (canLetItRip(worldIn, entityplayer))
            	{
            		stack.damageItem(1, entityplayer);
            		playRiptideSound(worldIn, entityplayer, r);
            		
            		Vec3d moveVec = entityplayer.getLookVec().scale(0.6 + (r * 1.2));

            		if (entityplayer.canBePushed())
            		{
            			entityplayer.motionX = moveVec.x;
            			entityplayer.motionY = moveVec.y/1.8;
            			entityplayer.motionZ = moveVec.z;
            			entityplayer.velocityChanged = true;
        			}
            		
            		/** This looks weird. This deals damage to nearby entities when using Riptide, and also damages the Trident for each hit. */
            		for (EntityLivingBase entitylivingbase :entityplayer.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityplayer.getPosition()).grow(1, 1, 1)))
            	    {
            	    	if (entitylivingbase != entityplayer && (entitylivingbase.getPassengers().isEmpty() || !(entitylivingbase.getPassengers().get(0) == entityplayer)))
                        {
            	    		if (entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(entityplayer),(float) (new EntityTrident(worldIn, entityplayer).getDamage())))
            	    		{
            	    			stack.damageItem(1, entityplayer);
            	    		}
                        }
                    }
            	}
        		
        		 worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, OESounds.ENTITY_TRIDENT_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        	}
                entityplayer.addStat(StatList.getObjectUseStats(this));
            }
       }

    /**
     * If the player can use a Trident that has Riptide on it.
     */
    public static boolean canLetItRip(World world, EntityLivingBase player)
    {
        if (player.isWet()) return true;
        
        if (player.isSneaking() && ConfigHandler.enchant.riptide.riptideSneakThrowing) return true;
        
        if (ConfigHandler.enchant.riptide.riptideIBroughtMyOwnWaterThankYou)
        {
        	if (player.getHeldItemMainhand().getItem() == Items.WATER_BUCKET || player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET )
        	{
        		return true;
        	}
        }

		return false;
    }
    
    /** Plays the appropriate Riptide song. Shoved to a seperate method for readability. */
    public static void playRiptideSound(World world, EntityLivingBase player, int level)
    {
        switch(level)
        {
        	case 1:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, OESounds.ENTITY_TRIDENT_RIPTIDE1, SoundCategory.PLAYERS, 1.0F, 1.0F);
        	case 2:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, OESounds.ENTITY_TRIDENT_RIPTIDE2, SoundCategory.PLAYERS, 1.0F, 1.0F);
        	case 3:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, OESounds.ENTITY_TRIDENT_RIPTIDE3, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }
    
    public static float getArrowVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }
    

    /**
     * Ahead is copied from ItemSword.
     */
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }

    
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(2, entityLiving);
        }

        return true;
    }
    
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        @SuppressWarnings("deprecation")
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)ConfigHandler.item.trident.tridentDamage - 1, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ConfigHandler.item.trident.tridentSpeed - 4.0D, 0));
        }

        return multimap;
    }
}