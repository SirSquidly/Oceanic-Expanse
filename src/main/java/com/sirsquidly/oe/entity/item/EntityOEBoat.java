package com.sirsquidly.oe.entity.item;

import javax.annotation.Nonnull;

import com.sirsquidly.oe.init.OEItems;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
/**
 * Required for the Palm Boat to drop the proper item
 */
public class EntityOEBoat extends EntityBoat
{

	public EntityOEBoat(World worldIn)
	{
		super(worldIn);
	}

	public EntityOEBoat(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

	@Override
    public Item getItemBoat()
    { return OEItems.PALM_BOAT; }

    @Override
    @Nonnull
    public ItemStack getPickedResult(RayTraceResult target)
    { return new ItemStack(this.getItemBoat()); }
}