package com.sirsquidly.oe.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOESkull extends ItemBlock
{
	public ItemOESkull(Block block)
    {
		super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
        
     // Weird implimentation, re-re-observe examples
     		this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter()
     		{
     			@SideOnly(Side.CLIENT)
     			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
     			{
     				NBTTagCompound nbttagcompound = stack.getTagCompound();
     				
     				if (entityIn != null && entityIn.isWet())
     				{
     					return 1.0F;
     				}
     				
     				return 0.0F;
     			}
     		});
    }
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing == EnumFacing.DOWN) {
			return EnumActionResult.FAIL;
		} else {
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				facing = EnumFacing.UP;
				pos = pos.down();
			}
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag) {
				if (!worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.isSideSolid(pos, facing, true)) {
					return EnumActionResult.FAIL;
				}

				pos = pos.offset(facing);
			}

			ItemStack itemstack = playerIn.getHeldItem(hand);

			if (playerIn.canPlayerEdit(pos, facing, itemstack) && OEBlocks.PICKLED_HEAD.canPlaceBlockAt(worldIn, pos)) {
				if (worldIn.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					worldIn.setBlockState(pos, OEBlocks.PICKLED_HEAD.getDefaultState().withProperty(BlockSkull.FACING, facing), 11);
					int i = 0;

					if (facing == EnumFacing.UP) {
						i = MathHelper.floor((double) (playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
					}

					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntitySkull) {
						TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;

						tileentityskull.setType(itemstack.getMetadata());

						tileentityskull.setSkullRotation(i);
					}

					if (playerIn instanceof EntityPlayerMP) {
						CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, pos, itemstack);
					}

					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}
	
	public int getMetadata(int damage)
    { return damage; }

	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
	{ return armorType == EntityEquipmentSlot.HEAD; }

	@Override
	@Nullable
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack)
	{ return EntityEquipmentSlot.HEAD; }
}