package com.sirsquidly.oe.items;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.sirsquidly.oe.entity.EntityTrident;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
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
				
				return worldIn == null ? 1.0F : 0.0F;
			}
		});
	}

	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
    
    public int getItemEnchantability()
    {
        return 22;
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        int r = EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, itemstack);
        
        if (r == 0 || r > 0 && playerIn.isWet())
        {
        	playerIn.setActiveHand(handIn);
        	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
    
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer && !(stack.getItemDamage() > stack.getMaxDamage() - 1))
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i < 0) return;

            float f = getArrowVelocity(i);
        	int r = EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, stack);
        	
        	if (f >= 0.6 && !worldIn.isRemote)
        	{ 
        		if (r <= 0)
            	{
    				EntityTrident entitytrient = new EntityTrident(worldIn, entityplayer);
                    entitytrient.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, 0.8F * 3.0F, 1.0F);

                    entitytrient.setIsCritical(true);
                    stack.damageItem(1, entityplayer);
                    entitytrient.setItem(stack);
                    
                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                    if (j > 0)
                    {
                    	entitytrient.setDamage(entitytrient.getDamage() + (double)j * 0.5D + 0.5D);
                    }
                    
                    if (!entityplayer.capabilities.isCreativeMode)
                    {
                    	stack.setCount(0);
                    }
                    else
                    {
                    	entitytrient.pickupStatus = PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.spawnEntity(entitytrient);	
    			
            	}
            	else if (entityplayer.isWet())
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
        		
        		 worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundHandler.ENTITY_TRIDENT_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        	}
                entityplayer.addStat(StatList.getObjectUseStats(this));
            }
        }
    
    /** Plays the appropriate Riptide song. Shoved to a seperate method for readability. */
    public static void playRiptideSound(World world, EntityLivingBase player, int level)
    {
        switch(level)
        {
        	case 1:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundHandler.ENTITY_TRIDENT_RIPTIDE1, SoundCategory.PLAYERS, 1.0F, 1.0F);
        	case 2:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundHandler.ENTITY_TRIDENT_RIPTIDE2, SoundCategory.PLAYERS, 1.0F, 1.0F);
        	case 3:
        		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundHandler.ENTITY_TRIDENT_RIPTIDE3, SoundCategory.PLAYERS, 1.0F, 1.0F);
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
