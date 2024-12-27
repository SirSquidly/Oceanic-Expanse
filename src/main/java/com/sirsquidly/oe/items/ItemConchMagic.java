package com.sirsquidly.oe.items;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityDolphin;
import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.entity.ai.EntityAISummonCrew;
import com.sirsquidly.oe.init.OEPotions;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemConchMagic extends ItemConch
{
	private int defaultCooldown = ConfigHandler.item.conch.conchCooldown;

	public ItemConchMagic()
	{
		super();
		/* As they have Durability, they cannot be stacked like normal Conchs*/
		this.maxStackSize = 1;
		this.setMaxDamage(31);
	}
	
	/** Called each Inventory Tick, same thing the Map does. This auto-sets the random resonance if it doesn't have one.*/
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (!worldIn.isRemote)
        {
        	if (nbttagcompound == null || !nbttagcompound.hasKey("Resonance"))
    		{ 
            	addRandomTrait(stack, worldIn.rand);
    		}
        }
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		NBTTagCompound nbttagcompound = itemstack.getTagCompound();

		if (nbttagcompound != null || nbttagcompound.hasKey("Resonance"))
		{
			switch (nbttagcompound.getInteger("Resonance"))
			{
				case 0:
					doKnockbackBurst(playerIn, 8);
					break;
				case 1:
					doSinkEffects(playerIn, 16);
					break;
				case 2:
					doDrownedSummon(playerIn);
					break;
				case 3:
					doPositiveEffects(playerIn, 16);
					break;
			}
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
    }
	
	/** Adds a random resonance to the Magic Conch.*/
	public void addRandomTrait(ItemStack stack, Random rand)
	{
	    NBTTagCompound nbt;
	    if (stack.hasTagCompound())
	    { nbt = stack.getTagCompound(); }
	    else
	    { nbt = new NBTTagCompound(); }

	    nbt.setInteger("Resonance", rand.nextInt(4));
	    
	    stack.setTagCompound(nbt);
	}

	/** Throws nearby mobs away from the conch user. */
	private static void doKnockbackBurst(EntityLivingBase user, int range)
	{
		if (user.world.isRemote) return;

		for (EntityLivingBase nearbyEntity : user.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(user.getPosition()).grow(range, range, range)))
		{
			if (nearbyEntity == user) continue;

			double knockbackResist = 1.0D - ((EntityLivingBase)nearbyEntity).getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();

			double d12 = nearbyEntity.getDistance(nearbyEntity.posX, nearbyEntity.posY, nearbyEntity.posZ) / (double) range;
			if (d12 <= 1.0)
			{
				double dx = nearbyEntity.posX - user.posX;
				double dy = nearbyEntity.posY + (nearbyEntity.height / 2) - user.posY + (user.height / 2);
				double dz = nearbyEntity.posZ - user.posZ;

				double reCheckDistance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
				if (reCheckDistance != 0.0)
				{
					dx /= reCheckDistance;
					dy /= reCheckDistance;
					dz /= reCheckDistance;
					double kmult = (1.1 - d12);

					nearbyEntity.motionX += dx * kmult * knockbackResist;
					nearbyEntity.motionY += dy * kmult * knockbackResist;
					nearbyEntity.motionZ += dz * kmult * knockbackResist;
					nearbyEntity.velocityChanged = true;
				}
			}
		}
	}

	/** Applies Descent or Slowness onto nearby mobs. */
	private static void doSinkEffects(EntityLivingBase user, int range)
	{
		if (user.world.isRemote) return;

		for (EntityLivingBase nearbyEntity : user.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(user.getPosition()).grow(range, range, range)))
		{
			if (nearbyEntity == user) continue;

			if (nearbyEntity.getDistanceSq(user) <= range * range)
			{
				nearbyEntity.addPotionEffect(new PotionEffect(OEPotions.DESCENT, 210, 0));
				nearbyEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 210, 0));
			}
		}
	}

	/** Summons a Drowned, who is friendly to the player. */
	private static void doDrownedSummon(EntityLivingBase user)
	{
		if (user.world.isRemote) return;

		BlockPos pos = new BlockPos(user);
		int maxAttempts = 80;

		for (int i = 0; i < maxAttempts; i++)
		{
			EntityDrowned entity = new EntityDrowned(user.world);

			int offsetX = user.world.rand.nextInt(10 * 2) - 10;
			int offsetZ = user.world.rand.nextInt(10 * 2) - 10;
			int offsetY = user.world.rand.nextInt(5) - 2;
			BlockPos spawnPos = pos.add(offsetX, offsetY, offsetZ);

			if (spawnPos.distanceSq(pos) < 2 * 2) continue;

			entity.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, user.world.rand.nextFloat() * 360.0F, 0.0F);

			if (user.world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity) && user.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty())
			{
				BlockPos groundPos = spawnPos.down();

				if (user.world.getBlockState(spawnPos).getMaterial() == Material.WATER || i > maxAttempts / 2 && user.world.getBlockState(groundPos).isSideSolid(user.world, groundPos, EnumFacing.UP))
				{
					entity.targetTasks.taskEntries.clear();
					entity.targetTasks.addTask(3, new EntityAIHurtByTarget(entity, false));
					entity.tasks.addTask(4, new EntityAISummonCrew(entity, user));

					for (int j = 0; j < 80; j++)
					{
						Main.proxy.spawnParticle(2, user.world, entity.posX + (user.world.rand.nextFloat() - user.world.rand.nextFloat()), entity.posY + 1 + (user.world.rand.nextFloat() - user.world.rand.nextFloat()), entity.posZ + (user.world.rand.nextFloat() - user.world.rand.nextFloat()), 0, 0, 0, 4, 128, 255, 192);
					}

					entity.setAttackTarget(user.getLastAttackedEntity());

					user.world.spawnEntity(entity);
					break;
				}
			}
		}
	}

	/** Applies Regeneration onto nearby mobs. */
	private static void doPositiveEffects(EntityLivingBase user, int range)
	{
		if (user.world.isRemote) return;

		for (EntityLivingBase nearbyEntity : user.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(user.getPosition()).grow(range, range, range)))
		{
			if (nearbyEntity == user) continue;

			if (nearbyEntity.getDistanceSq(user) <= range * range)
			{
				nearbyEntity.addPotionEffect(new PotionEffect(OEPotions.DOLPHINS_GRACE, 210, 0));
				nearbyEntity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 210, 0));
			}
		}
	}

	/** Makes nearby Dolphins come to the player */
	private static void doDolphinCall(EntityLivingBase user, int range)
	{
		if (user.world.isRemote) return;

		for (EntityDolphin nearbyDolphin : user.world.getEntitiesWithinAABB(EntityDolphin.class, new AxisAlignedBB(user.getPosition()).grow(range)))
		{
			if (nearbyDolphin == user) continue;

			nearbyDolphin.getNavigator().tryMoveToEntityLiving(user, 4.0D);
			nearbyDolphin.setAttackTarget(user.getLastAttackedEntity());
		}
	}

	public EnumRarity getRarity(ItemStack stack)
	{ return EnumRarity.UNCOMMON; }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		boolean didSet = false;
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		
		if (nbttagcompound != null && nbttagcompound.hasKey("Resonance"))
		{
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("description.oe.conch_resonance" + nbttagcompound.getInteger("Resonance") + ".name"));
		}
	}
}