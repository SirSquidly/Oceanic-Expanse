package com.sirsquidly.oe.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.blocks.BlockCoconut;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

public class EntityFallingCoconut extends EntityFallingBlock
{
    private final IBlockState fallTile = OEBlocks.COCONUT.getDefaultState();
	private boolean dontSetBlock;
    private int fallHurtMax = ConfigHandler.coconutFallMaxDamage;
    private float fallHurtAmount = (float) ConfigHandler.coconutFallDamage;
    private float breakFrom = (float) ConfigHandler.coconutFallBreak;
    
	public EntityFallingCoconut(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }
	
	public EntityFallingCoconut(World worldIn, double x, double y, double z, IBlockState fallingBlockState)
    { 
		super(worldIn, x, y, z, fallingBlockState); 
    }

	@Override
	public void onUpdate()
    {
		Block block = this.fallTile.getBlock();

        if (this.fallTile.getMaterial() == Material.AIR)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.fallTime++ == 0)
            {
                BlockPos blockpos = new BlockPos(this);

                if (this.world.getBlockState(blockpos).getBlock() == block)
                {
                    this.world.setBlockToAir(blockpos);
                }
                else if (!this.world.isRemote)
                {
                    this.setDead();
                    return;
                }
            }

            if (!this.hasNoGravity())
            { this.motionY -= 0.03999999910593033D; }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            if (!this.world.isRemote)
            {
                BlockPos blockpos1 = new BlockPos(this);

                if (!this.onGround)
                {
                    if (this.fallTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
                    {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
                        {
                            this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
                        }

                        this.setDead();
                    }
                }
                else
                {
                    IBlockState iblockstate = this.world.getBlockState(blockpos1);

                    if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
                    if (BlockCoconut.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
                    {
                        this.onGround = false;
                        return;
                    }

                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
                    {
                        this.setDead();

                        if (!this.dontSetBlock)
                        {
                            if (this.world.mayPlace(block, blockpos1, true, EnumFacing.UP, (Entity)null) && (!BlockCoconut.canFallThrough(this.world.getBlockState(blockpos1.down()))) && this.world.setBlockState(blockpos1, this.fallTile, 3))
                            {
                                if (block instanceof BlockCoconut)
                                {
                                    ((BlockCoconut)block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate);
                                }

                                if (this.tileEntityData != null && block.hasTileEntity(this.fallTile))
                                {
                                    TileEntity tileentity = this.world.getTileEntity(blockpos1);

                                    if (tileentity != null)
                                    {
                                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                                        for (String s : this.tileEntityData.getKeySet())
                                        {
                                            NBTBase nbtbase = this.tileEntityData.getTag(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                                            {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }

                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
                            {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
                            }
                        }
                        else if (block instanceof BlockCoconut)
                        {
                            ((BlockCoconut)block).onBroken(this.world, blockpos1);
                        }
                    }
                }
            }

            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
        }
    }
	
	@Override
	public void fall(float distance, float damageMultiplier)
    {
        int i = MathHelper.ceil(distance - 1.0F);
        int j = MathHelper.ceil(distance - breakFrom);

        if (i > 0)
        {
            List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));

            for (Entity e : list)
            {
                e.attackEntityFrom(DamageSource.FALLING_BLOCK, (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
                
                if (ConfigHandler.coconutHitSound && e instanceof EntityLivingBase && !(e instanceof EntityArmorStand))
                { this.playSound(SoundHandler.COCONUT_HIT, 1.0F, 1.0F); }
            }
        }
        if (breakFrom != -1 && j > 0)
        { 
        	this.dontSetBlock = true; 
        }
    }
	
	@Override
	public IBlockState getBlock() {
		return fallTile;
	}
}