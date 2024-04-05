package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityCrab;
import com.sirsquidly.oe.entity.EntityLobster;
import com.sirsquidly.oe.entity.EntityTropicalFish;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemSpawnBucket extends ItemMonsterPlacer
{
	public ItemSpawnBucket()
	{
		super();
		this.maxStackSize = 1;
		
		for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
		{
			String thisEntity = entitylist$entityegginfo.spawnedID.toString();
			
			this.addPropertyOverride(new ResourceLocation(thisEntity), new IItemPropertyGetter()
			{
				@SideOnly(Side.CLIENT)
				public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
				{ 
					if (ItemMonsterPlacer.getNamedIdFrom(stack) != null && ItemMonsterPlacer.getNamedIdFrom(stack).toString().contains(thisEntity)) return 1.0F;
					else return 0.0F;
				}
			});
		}
		this.setCreativeTab(Main.OCEANEXPTAB);
	}

	/** Overrides the display name to show the Tropical Fish type. */
	public String getItemStackDisplayName(ItemStack stack)
    {
		if (pullSpecialNameFromMob(stack) != null && ConfigHandler.item.spawnBucket.spawnBucketTropicalFishSpecificNames)
		{
			return ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim() + " " + pullSpecialNameFromMob(stack);
		}

        return super.getItemStackDisplayName(stack);
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    { return EnumActionResult.PASS; }

	/** Overrides getSubItem so every possible bucketed mob doesn't flood the Search Creative Tab*/
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
            {
            	if (ArrayUtils.contains(ConfigHandler.item.spawnBucket.bucketableMobs, entitylist$entityegginfo.spawnedID.toString()) || ConfigHandler.item.spawnBucket.enableAllBucketsCreative)
				{
            		ItemStack itemstack = new ItemStack(this, 1);
                    applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                    items.add(itemstack);
				}
            }
        }
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult rtresult = this.rayTrace(worldIn, playerIn, false);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, rtresult);
        if (ret != null) return ret;

        if (rtresult == null || rtresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            BlockPos blockpos = rtresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            else
            {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockpos1 = flag1 && rtresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(rtresult.sideHit);

                if (!playerIn.canPlayerEdit(blockpos1, rtresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else if (!playerIn.isSneaking() && this.tryPlaceWater(playerIn, worldIn, blockpos1) || playerIn.isSneaking())
                {	
                    if (playerIn instanceof EntityPlayerMP)
                    { CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack); }
                    
                    if (!worldIn.isRemote)
                    {
                    	NBTTagCompound entityTag = (NBTTagCompound) itemstack.getTagCompound().getTag("EntityTag");
                    	
                    	Entity entity = EntityList.createEntityFromNBT(entityTag, worldIn);
                    	if(itemstack.hasDisplayName())
                    	{
                    		entity.setCustomNameTag(itemstack.getDisplayName());
                    	}
                    	
                    	if (entity instanceof EntityLiving) ((EntityLiving) entity).enablePersistence();
            			entity.setPosition((double)blockpos1.getX() + 0.5, (double)blockpos1.getY(), (double)blockpos1.getZ() + 0.5);
            			worldIn.spawnEntity(entity);
                    }
                    
                    playerIn.addStat(StatList.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET)) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
        }
    }

	public boolean tryPlaceWater(@Nullable EntityPlayer player, World worldIn, BlockPos posIn)
    {
		IBlockState iblockstate = worldIn.getBlockState(posIn);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

        if (!worldIn.isAirBlock(posIn) && !flag && !flag1)
        {
            return false;
        }
        else
        {
        	if (worldIn.provider.doesWaterVaporize())
        	{
        		int l = posIn.getX();
                int i = posIn.getY();
                int j = posIn.getZ();
                worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int k = 0; k < 8; ++k)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D);
                }
        	}
        	else
        	{
        		if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);
                }

                worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(posIn, Blocks.FLOWING_WATER.getDefaultState(), 11);
        	}
        	return true;
        }
    }

	/** This pulls the name of the Tropical Fish, and formats it as 'Bucket of [Tropical Fish Variant]' */
	public String pullSpecialNameFromMob(ItemStack stack)
    {
		ResourceLocation entityName = ItemMonsterPlacer.getNamedIdFrom(stack);
		
		if (entityName != null)
		{
			NBTTagCompound tags = stack.getTagCompound();

			if (tags.hasKey("EntityTag"))
			{
				tags = (NBTTagCompound) tags.getTag("EntityTag");
				
				if (tags.hasKey("Variant"))
				{
					switch (entityName.toString())
					{
						case "oe:crab":	
							return EntityCrab.getSpecificName(tags.getInteger("Variant"));
						case "oe:lobster":	
							return EntityLobster.getSpecificName(tags.getInteger("Variant"));
						case "oe:tropical_fish":	
							return EntityTropicalFish.getSpecificName(tags.getInteger("Variant"));
					}
				}
			}
		}
        return null;
    }
	
	public static void recordEntityNBT(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (!player.world.isRemote) 
		{
			NBTTagCompound tags = stack.getTagCompound();

			if (tags == null) 
			{
				tags = new NBTTagCompound();
				stack.setTagCompound(tags);
			}
			if (!tags.hasKey("EntityTag")) 
			{
				NBTTagCompound entityTag = new NBTTagCompound();
				entity.writeToNBTOptional(entityTag);
				entityTag.removeTag("Pos");
				entityTag.removeTag("Motion");
				//entityTag.removeTag("Rotation");
				entityTag.removeTag("Fire");
				entityTag.removeTag("FallDistance");
				entityTag.removeTag("Dimension");
				entityTag.removeTag("PortalCooldown");
				entityTag.removeTag("UUIDMost");
				entityTag.removeTag("UUIDLeast");
				entityTag.removeTag("Leashed");
				entityTag.removeTag("Leash");
				
				entity.setDead();

				tags.setTag("EntityTag", entityTag);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (pullSpecialNameFromMob(stack) != null && ConfigHandler.item.spawnBucket.spawnBucketTropicalFishTooltips)
		{
			tooltip.add(TextFormatting.ITALIC + pullSpecialNameFromMob(stack));
		}
	}
	
	public void registerItemModel()
	{
        for (EntityList.EntityEggInfo entityInfo : EntityList.ENTITY_EGGS.values())
        {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "entity=" + entityInfo.spawnedID));
        }
    }
}