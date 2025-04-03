package com.sirsquidly.oe.items;

import com.sirsquidly.oe.blocks.BlockSeaPickle;
import com.sirsquidly.oe.blocks.BlockTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSeaPickle extends ItemBlock {
	
	public ItemBlockSeaPickle(Block block)
	{
        super(block);
        this.setMaxDamage(0);
    }

	/**
     * This is mostly copied from ItemSnow
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack))
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = pos;

            if ((block != this.block) && !block.isReplaceable(worldIn, pos))
            {
                blockpos = pos.offset(facing);
                iblockstate = worldIn.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }

            if (block == this.block)
            {
                int i = (Integer) iblockstate.getValue(BlockSeaPickle.AMOUNT);

                if (i < 4)
                {
                    IBlockState iblockstate1 = iblockstate.withProperty(BlockSeaPickle.AMOUNT, i + 1);
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, blockpos);

                    if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && worldIn.setBlockState(blockpos, iblockstate1, 10))
                    {
                        SoundType soundtype = this.block.getSoundType(iblockstate1, worldIn, pos, player);
                        worldIn.playSound(player, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                        if (player instanceof EntityPlayerMP)
                        { CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack); }
                        /* Runs any `onBlockPlacedBy` logic associated with the given block*/
                        iblockstate.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate, player, itemstack);

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }

    public boolean getBlock(World world, BlockPos pos, Block block)
    {
    	IBlockState state = world.getBlockState(pos);
    	
    	if (block == OEBlocks.SEA_PICKLE)
    	{
    		return (Integer)state.getValue(BlockSeaPickle.AMOUNT) == 4;
    	}
    	else if (block == OEBlocks.SEA_TURTLE_EGG)
    	{
    		return (Integer)state.getValue(BlockTurtleEgg.AMOUNT) == 4;
    	}
    	else
    	{ return false; }
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this.block && !getBlock(world, pos, this.block) || super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }
}
