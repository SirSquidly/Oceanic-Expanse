package com.sirsquidly.oe.common.tileentity;

import com.sirsquidly.oe.common.blocks.BlockNautilusShellBlock;
import com.sirsquidly.oe.init.OESounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileNautilusShellBlock extends TileEntitySkull
{
    public ItemStack armorStack = ItemStack.EMPTY;
    public Vec3d direction = new Vec3d(0,0,0);
    public EnumFacing facing = EnumFacing.UP;
    public boolean facingWater = false;
    public boolean facingSolidBlock = false;
    public int redstoneLevel = 0;

    public boolean currentActive = false;
    public boolean currentPulling = true;
    public float currentDistance = 0;

    public void update()
    {
        /* Updating state doesn't need to be constantly checked. */
        if (world.getTotalWorldTime() % 40L == 0L) updateProperties();

        if (getRedstoneLevel() > 0)
        {
            if (!getFacingWater()) return;

            if (world.getTotalWorldTime() % 120L == 0L)
            { world.playSound(null, this.pos, OESounds.ENTITY_NAUTILUS_RIDE, SoundCategory.BLOCKS, 0.1F, 1.0F); }

            Vec3d direction = getDirection();

            if (this.world.isRemote)
            {
                double distance = getCurrentPulling() ? (0.3 + (getFacingSolid() ? 0.5 : 0.0)) : getCurrentDistance() + 1;

                double xzRange = 0.4 + (this.getCurrentActive() ? 0.4 : 0);
                double yRange = 0.2 + (this.getCurrentActive() ? 0.6 : 0);

                double cx = this.pos.getX() + 0.5D + (direction.x * distance) + ((world.rand.nextDouble() * xzRange) - (xzRange * 0.5));
                double cy = this.pos.getY() + 0.3D + direction.y * distance + ((world.rand.nextDouble() * yRange) - (yRange * 0.5));
                double cz = this.pos.getZ() + 0.5D + (direction.z * distance) + ((world.rand.nextDouble() * xzRange) - (xzRange * 0.5));

                double baseVel = getRedstoneLevel() * 0.5D * (getCurrentPulling() ? 1.0D : -1.0D);
                double vx = direction.x * baseVel + (this.world.rand.nextDouble() - 0.5D) * 0.02D;
                double vy = direction.y * baseVel + (this.world.rand.nextDouble() - 0.5D) * 0.02D;
                double vz = direction.z * baseVel + (this.world.rand.nextDouble() - 0.5D) * 0.02D;

                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, cx, cy, cz, vx, vy, vz);
            }

            if (getCurrentActive())
            {
                float distance = getCurrentDistance();
                AxisAlignedBB rangeBox = new AxisAlignedBB(pos.offset(getFacing())).expand(direction.x * distance, direction.y * distance, direction.z * distance);

                List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, rangeBox);

                for (Entity entity : entities)
                {
                    double strength = 0.03D;
                    Vec3d push = direction.scale(strength);

                    if (!getCurrentPulling()) push = push.scale(-1);

                    entity.addVelocity(push.x, 0, push.z);
                    entity.velocityChanged = true;
                }
            }
        }
    }

    public void updateProperties()
    {
        EnumFacing currentFacing = EnumFacing.byIndex(this.getBlockMetadata() & 7);
        int nearbyRedstone = this.world.getRedstonePowerFromNeighbors(this.pos);
        boolean isFacingwater = false;

        if (currentFacing != getFacing())
        {
            setFacing(currentFacing);
            setDirection(new Vec3d(currentFacing.getDirectionVec()).normalize());
        }

        if (nearbyRedstone != getRedstoneLevel()) setRedstoneLevel(nearbyRedstone);

        if (nearbyRedstone != 0)
        {
            BlockPos frontPos = this.pos.offset(currentFacing);
            IBlockState frontState = this.world.getBlockState(frontPos);

            if (frontState.isFullBlock())
            {
                setFacingSolid(true);
                if (this.world.getBlockState(frontPos.offset(currentFacing)).getMaterial().isLiquid()) isFacingwater = true;
            }
            else
            {
                setFacingSolid(false);
                if (frontState.getMaterial().isLiquid()) isFacingwater = true;
            }

            if (isFacingwater != getFacingWater()) setFacingWater(isFacingwater);
        }

        setCurrentActive(false);

        Block belowBlock = this.world.getBlockState(this.pos.down()).getBlock();

        if (belowBlock instanceof BlockMagma)
        {
            setCurrentActive(true);
            setCurrentPulling(true);
        }
        else if (belowBlock instanceof BlockSoulSand)
        {
            setCurrentActive(true);
            setCurrentPulling(false);
        }
        else if (belowBlock instanceof BlockNautilusShellBlock)
        {
            TileEntity tile = world.getTileEntity(pos.down());

            if (tile instanceof TileNautilusShellBlock)
            {
                TileNautilusShellBlock belowShell = (TileNautilusShellBlock)tile;

                setCurrentActive(belowShell.getCurrentActive());
                setCurrentPulling(belowShell.getCurrentPulling());
            }
        }

        if (getCurrentActive())
        {
            float distance = 0;
            for (distance = 1; distance <= getRedstoneLevel() * 0.5D; distance++)
            {
                BlockPos checkPos = pos.offset(facing, (int) distance);
                IBlockState state = world.getBlockState(checkPos);

                if (state.getMaterial().isSolid()) { break; }
            }
            setCurrentDistance(distance);
        }
    }

    public Vec3d getDirection() { return direction; }
    public void setDirection(Vec3d directionIn) { direction = directionIn; }

    public EnumFacing getFacing() { return facing; }
    public void setFacing(EnumFacing facingIn) { facing = facingIn; }

    public boolean getFacingWater() { return facingWater; }
    public void setFacingWater(boolean flagIn) { facingWater = flagIn; }

    public boolean getFacingSolid() { return facingSolidBlock; }
    public void setFacingSolid(boolean flagIn) { facingSolidBlock = flagIn; }

    public int getRedstoneLevel() { return redstoneLevel; }
    public void setRedstoneLevel(int levelIn) { redstoneLevel = levelIn; }

    public boolean getCurrentActive() { return currentActive; }
    public void setCurrentActive(boolean flagIn) { currentActive = flagIn; }

    public boolean getCurrentPulling() { return currentPulling; }
    public void setCurrentPulling(boolean flagIn) { currentPulling = flagIn; }

    public float getCurrentDistance() { return currentDistance; }
    public void setCurrentDistance(float distanceIn) { currentDistance = distanceIn; }

    public ItemStack getArmorStack() { return armorStack; }
    public void setArmorStack(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            stack = stack.copy();
            stack.setCount(1);
        }
        armorStack = stack;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("FacingSolid", getFacingSolid());
        compound.setBoolean("FacingWater", getFacingWater());
        compound.setInteger("RedstoneLevel", getRedstoneLevel());

        if (!this.getArmorStack().isEmpty())
        { compound.setTag("Item", this.getArmorStack().writeToNBT(new NBTTagCompound())); }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setFacingSolid(compound.getBoolean("FacingSolid"));
        setFacingWater(compound.getBoolean("FacingWater"));
        setRedstoneLevel(compound.getInteger("RedstoneLevel"));
        NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        if (nbttagcompound != null && !nbttagcompound.isEmpty())
        { this.setArmorStack(new ItemStack(nbttagcompound)); }
    }
}