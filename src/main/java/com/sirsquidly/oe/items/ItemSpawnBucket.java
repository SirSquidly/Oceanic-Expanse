package com.sirsquidly.oe.items;

import javax.annotation.Nullable;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.init.OEItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemSpawnBucket extends ItemMonsterPlacer
{
	public ItemSpawnBucket(String name) {
		super();
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
		this.setCreativeTab(null);
		
		this.addPropertyOverride(new ResourceLocation("check_entity"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				String s1 = EntityList.getTranslationName(getNamedIdFrom(stack));
				
		        if (s1 != null)
		        {
		        	s1 = I18n.translateToLocal("entity." + s1 + ".name");
		        	
		        	switch(s1) 
		        	{
		        		case "Cod": 
		        		{ return 0.01F; }
		        		case "Salmon": 
		        		{ return 0.02F; }
		        		case "Pufferfish": 
		        		{ return 0.03F; }
		        		case "Glow Squid": 
		        		{ return 0.11F; }
		        		case "Squid": 
		        		{ return 0.12F; }
		        		case "Drowned": 
		        		{ return 0.13F; }
		        		case "Pickled": 
		        		{ return 0.14F; }
		        		case "NULL": 
		        		{return 0.0F;}
		        	}
		        }
				return 0.0F;
			}
		});
		
		this.setCreativeTab(Main.OCEANEXPTAB);
		OEItems.ITEMS.add(this);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    { return EnumActionResult.PASS; }
	

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
                else if (this.tryPlaceWater(playerIn, worldIn, blockpos1))
                {	
                    if (playerIn instanceof EntityPlayerMP)
                    { CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack); }
                    
                    if (!worldIn.isRemote)
                    {
                    	Entity entity = spawnCreature(worldIn, getNamedIdFrom(itemstack), (double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D);
                        
                        if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
                        { entity.setCustomNameTag(itemstack.getDisplayName()); }
                        
                        applyItemEntityDataToEntity(worldIn, playerIn, itemstack, entity);	
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
}