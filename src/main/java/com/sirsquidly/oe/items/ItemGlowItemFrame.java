package com.sirsquidly.oe.items;

import javax.annotation.Nullable;

import com.sirsquidly.oe.entity.item.EntityGlowItemFrame;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGlowItemFrame extends Item
{
	public ItemGlowItemFrame()
    {
		this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				NBTTagCompound nbttagcompound = stack.getTagCompound();
				
				if (nbttagcompound != null)
				{
					if (nbttagcompound.hasKey("normal")) return 1.0F;
					if (nbttagcompound.hasKey("map")) return 2.0F;
				}
				
				return 0.0F;
			}
		});
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos blockpos = pos.offset(facing);

        boolean cork = true;
        
        if ((cork || facing.getAxis() != EnumFacing.Axis.Y) && player.canPlayerEdit(blockpos, facing, itemstack))
        {
            EntityHanging entityhanging = createEntity(worldIn, blockpos, facing);

            if (entityhanging != null && entityhanging.onValidSurface())
            {
                if (!worldIn.isRemote)
                {
                    entityhanging.playPlaceSound();
                    worldIn.spawnEntity(entityhanging);
                }

                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
    
    private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide)
    { return new EntityGlowItemFrame(worldIn, pos, clickedSide); }
}