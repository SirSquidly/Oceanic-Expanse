package com.sirsquidly.oe.items;

import java.util.List;

import javax.annotation.Nullable;



import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChlorine extends ItemBase
{
	private int effectArea = 5;
	private int effectMath = effectArea / 2;
	
	public ItemChlorine() 
	{ super(); }

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);
		
        if (worldIn.isRemote)
        {
        	this.spawnParticles(worldIn, pos, 100 * effectMath); 
        	return EnumActionResult.SUCCESS;
        }
        if (player.canPlayerEdit(pos, facing, itemstack))
        {
        	worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
        	
        	for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-effectMath, -effectMath, -effectMath), pos.add(effectMath, effectMath, effectMath)))
    		{
    			if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == OEBlocks.SEAGRASS || worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == OEBlocks.TALL_SEAGRASS)
        		{
        			worldIn.setBlockState(blockpos$mutableblockpos, Blocks.WATER.getDefaultState());
        		}
    		}
        	if (!player.capabilities.isCreativeMode)
            { itemstack.shrink(1); }
        	
    		return EnumActionResult.SUCCESS;
        }
        else {return EnumActionResult.FAIL;}
    }
	
	@SideOnly(Side.CLIENT)
    public void spawnParticles(World worldIn, BlockPos pos, int amount)
    {
        if (amount == 0)
        { amount = 15; }

        for (int i = 0; i < amount; ++i)
        {
            double d0 = itemRand.nextGaussian() * 0.04D;
            double d1 = itemRand.nextGaussian() * 0.04D;
            double d2 = itemRand.nextGaussian() * 0.04D;
            
        	double p0 = (double)pos.getX() + itemRand.nextFloat() * effectArea - effectMath;
	        double p1 = (double)pos.getY() + itemRand.nextFloat() * effectArea - effectMath;
	        double p2 = (double)pos.getZ() + itemRand.nextFloat() * effectArea - effectMath;
	        
            worldIn.spawnParticle(EnumParticleTypes.SPELL, p0, p1, p2, d0, d1, d2);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + I18n.format("description.chlorine.name"));
	}
}