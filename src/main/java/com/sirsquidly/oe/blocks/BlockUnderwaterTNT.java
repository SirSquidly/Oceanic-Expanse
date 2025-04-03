package com.sirsquidly.oe.blocks;

import com.sirsquidly.oe.entity.item.EntityUnderwaterTNTPrimed;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockUnderwaterTNT extends Block
{
    /** Causes the TNT to explode upon being mined by the player. */
    public static final PropertyBool UNSTABLE = PropertyBool.create("unstable");

    public BlockUnderwaterTNT()
    {
        super(Material.TNT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UNSTABLE, Boolean.FALSE));
        this.setSoundType(SoundType.PLANT);
    }

    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    public void explode(World worldIn, BlockPos pos, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            EntityUnderwaterTNTPrimed underwaterTNTPrimed = new EntityUnderwaterTNTPrimed(worldIn, (float)pos.getX() + 0.5F, pos.getY(), (float)pos.getZ() + 0.5F, igniter);
            underwaterTNTPrimed.setFuse(80);
            worldIn.spawnEntity(underwaterTNTPrimed);
            worldIn.playSound((EntityPlayer)null, underwaterTNTPrimed.posX, underwaterTNTPrimed.posY, underwaterTNTPrimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * This entire section handles every way the TNT can be activated.
     * */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) this.onPlayerDestroy(worldIn, pos, state.withProperty(UNSTABLE, true));
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    { if (worldIn.isBlockPowered(pos)) this.onPlayerDestroy(worldIn, pos, state.withProperty(UNSTABLE, true)); }

    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockToAir(pos);
        if (state.getValue(UNSTABLE)) this.explode(worldIn, pos, null);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE))
        {
            this.explode(worldIn, pos, playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (itemstack.getItem() == Items.FLINT_AND_STEEL)
            { itemstack.damageItem(1, playerIn); }
            else if (!playerIn.capabilities.isCreativeMode)
            { itemstack.shrink(1); }

            return true;
        }
        else
        { return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ); }
    }

    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;

            if (entityarrow.isBurning())
            {
                this.explode(worldIn, pos, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if (!worldIn.isRemote)
        {
            EntityUnderwaterTNTPrimed underwaterTNTPrimed = new EntityUnderwaterTNTPrimed(worldIn, (float)pos.getX() + 0.5F, pos.getY(), (float)pos.getZ() + 0.5F, explosionIn.getExplosivePlacedBy());
            underwaterTNTPrimed.setFuse(worldIn.rand.nextInt(20) + 10);
            worldIn.spawnEntity(underwaterTNTPrimed);
        }
    }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(UNSTABLE, (meta & 1) > 0); }

    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(UNSTABLE) ? 1 : 0;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, UNSTABLE); }
}