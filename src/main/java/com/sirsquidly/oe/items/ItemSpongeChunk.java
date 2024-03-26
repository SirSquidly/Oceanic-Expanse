package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpongeChunk extends Item 
{
	private int maxWater = 20;
	
	public ItemSpongeChunk()
	{ 
		super();
		
		this.addPropertyOverride(new ResourceLocation("full"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            { return isFull(stack) ? 1.0F : 0.0F; }
        });
	}
	
	public String getItemStackDisplayName(ItemStack stack)
    {
		if (this.isFull(stack))
		{ return I18n.format("item.oe.sponge_chunk_full.name"); }

        return super.getItemStackDisplayName(stack);
    }
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (player.isSneaking()) return EnumActionResult.FAIL;
		
		if (this.isFull(itemstack))
		{
			if (worldIn.provider.doesWaterVaporize())
        	{
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
                
				this.changeSpongeItem(itemstack, hand, player, 0);
				
				for (int k = 0; k < 8; ++k)
                {
					if (worldIn.isRemote) worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }
        	}
		}
		else
		{
			if (!worldIn.isRemote && player.canPlayerEdit(pos, facing, itemstack))
			{
				int effectMath = 3 / 2;
				int collectedWater = 0;
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
            	
				for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-effectMath, -effectMath, -effectMath), pos.add(effectMath, effectMath, effectMath)))
				{
					Block block = worldIn.getBlockState(blockpos$mutableblockpos).getBlock();
					if (block == Blocks.WATER)
					{
						worldIn.setBlockState(blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
						collectedWater++;
					}
					else if (block == Blocks.FLOWING_WATER)
					{ worldIn.setBlockState(blockpos$mutableblockpos, Blocks.AIR.getDefaultState()); }
        		}
				this.changeSpongeItem(itemstack, hand, player, this.getWaterCount(itemstack) + collectedWater);
            }
        	player.setActiveHand(hand);
        	player.getCooldownTracker().setCooldown(this, 5);
        	
        	return EnumActionResult.SUCCESS;
		}
	
		
		
		return EnumActionResult.FAIL;
	}
	
	public int getWaterCount(ItemStack stack)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound != null && tagCompound.hasKey("WaterCount", 3))
		{ return tagCompound.getInteger("WaterCount"); }
		
		return 0;
    }

	public boolean isFull(ItemStack stack)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound != null && tagCompound.hasKey("WaterCount", 3))
		{ return tagCompound.getInteger("WaterCount") >= maxWater; }
		
		return false;
	}
	
	private void setWaterCount(ItemStack stack, int count)
	{
		if (!stack.hasTagCompound())
		{ stack.setTagCompound(new NBTTagCompound()); }
		stack.getTagCompound().setInteger("WaterCount", Math.min(count, maxWater));
    }
	
	/** Swaps the sponge chunks between the normal and wet versions. */
	protected void changeSpongeItem(ItemStack heldStack, EnumHand hand, EntityPlayer player, int waterCount)
    {
		ItemStack wetSponge = waterCount > 0 ? new ItemStack(OEItems.SPONGE_CHUNK_WET) : new ItemStack(OEItems.SPONGE_CHUNK);
		
		heldStack.shrink(1);
        player.addStat(StatList.getObjectUseStats(this));
        if (waterCount > 0) this.setWaterCount(wetSponge, waterCount);
        
        if (heldStack.isEmpty())
        { player.setHeldItem(hand, wetSponge); }
        else
        {
            if (!player.inventory.addItemStackToInventory(wetSponge))
            {
                player.dropItem(wetSponge, false);
            }
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		int i = 0;
		
		if (nbttagcompound != null && nbttagcompound.hasKey("WaterCount")) { i = nbttagcompound.getInteger("WaterCount"); }
		
		tooltip.add(TextFormatting.GRAY + I18n.format("description.oe.hand_sponge_water.name") + " " + i + " / " + maxWater);
	}
}