package com.sirsquidly.oe.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sirsquidly.oe.Main;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This is a type of Explosion that functions underwater (Breaking blocks through Water)
 *
 * Basically ignores Water's Blast Resistance.
 *
 * */
public class ExplosionUnderwater extends Explosion
{
    private final boolean causesFire;
    private final boolean damagesTerrain;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
    private final float size;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
    private final Vec3d position;

    /** A list of water blocks to be compared against. */
    public static List<Block> waterBlocks = Lists.newArrayList(Blocks.WATER, Blocks.FLOWING_WATER );
    /** The chance for any blocks within `waterBlocks` to be destroyed by the explosion. */
    public double destroyChance = 0.05;

    /** The chance for any block to be dropped. */
    public float blockDropChance;

    public ExplosionUnderwater(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain)
    {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.random = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
        this.world = worldIn;
        this.exploder = entityIn;
        this.size = size;
        this.blockDropChance = 1.0F / size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = causesFire;
        this.damagesTerrain = damagesTerrain;
        this.position = new Vec3d(this.x, this.y, this.z);
    }

    /** This preforms all the steps of an explosion, bundled for ease of use. */
    public void preformStandardExplosion()
    {
        this.doExplosionBlockRecording();
        this.doExplosionEntityHarm();
        if (!this.world.isRemote) this.doExplosionSoundAndBreaking();
        else this.doExplosionParticles();
    }

    /** Records block positions to `affectedBlockPositions` to be utilized in the other parts of the explosion. */
    public void doExplosionBlockRecording()
    {
        Set<BlockPos> set = Sets.<BlockPos>newHashSet();
        int i = 16;

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                for (int l = 0; l < 16; ++l)
                {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
                    {
                        double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
                        {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = this.world.getBlockState(blockpos);

                            if (iblockstate.getMaterial() != Material.AIR)
                            {
                                float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(world, blockpos, (Entity)null, this);

                                /* Math required for getting the true blast resistance, as if Water was not there at all. */
                                f2 = this.getBaseBlockResistance(iblockstate.getBlock(), blockpos, f2);

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f)))
                            {
                                set.add(blockpos);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
    }

    /** Does damage and knockback to any entities within range of this explosion. */
    public void doExplosionEntityHarm()
    {
        float f3 = this.size * 2.0F;
        int k1 = MathHelper.floor(this.x - (double)f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double)f3 + 1.0D);

        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions())
            {
                double d12 = entity.getDistance(this.x, this.y, this.z) / (double)f3;

                if (d12 <= 1.0D)
                {
                    double d5 = entity.posX - this.x;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - this.y;
                    double d9 = entity.posZ - this.z;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double)this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f3 + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase)
                        { d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10); }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityplayer = (EntityPlayer)entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    /** Plays the Explosion sound and breaks/drops blocks previously recorded in `affectedBlockPositions`. */
    public void doExplosionSoundAndBreaking()
    {
        this.world.playSound((EntityPlayer)null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.damagesTerrain)
        {
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    if (this.isWaterBlock(block) && this.random.nextFloat() > destroyChance) continue;

                    if (block.canDropFromExplosion(this))
                    { block.dropBlockAsItemWithChance(this.world, blockpos, this.world.getBlockState(blockpos), this.blockDropChance, 0); }

                    block.onBlockExploded(this.world, blockpos, this);
                }
            }
        }
    }

    /** Spawns Particles for this Explosion. */
    public void doExplosionParticles()
    {
        EnumParticleTypes explosionType = this.size >= 2.0F && this.damagesTerrain ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE;
        this.world.spawnParticle(explosionType, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);

        if (this.damagesTerrain)
        {
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
                double d0 = blockpos.getX() + this.world.rand.nextFloat();
                double d1 = blockpos.getY() + this.world.rand.nextFloat();
                double d2 = blockpos.getZ() + this.world.rand.nextFloat();
                double d3 = d0 - this.x;
                double d4 = d1 - this.y;
                double d5 = d2 - this.z;
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 = d3 / d6;
                d4 = d4 / d6;
                d5 = d5 / d6;
                double d7 = 0.5D / (d6 / (double)this.size + 0.1D);
                d7 *= (double)(this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;

                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5);
                //this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    /** Sets the `destroyChance`. */
    public void setWaterDestructionChance(double chanceIn)
    { this.destroyChance = chanceIn; }

    /** Sets the `blockDropChance`. */
    public void setBlockDropChance(float chanceIn)
    { this.blockDropChance = chanceIn; }

    /** Returns an Adjusted version of Blast Resistance, to account for Water. Pure Water ALWAYS returns 0, meanwhile Fluidlogged Blocks return the blast resistance of their BASE block. */
    public float getBaseBlockResistance(Block block, BlockPos pos, float originalResistance)
    {
        if (this.isWaterBlock(block)) return 0.0F;
        if (!Main.proxy.fluidlogged_enable) return originalResistance;

        final FluidState fluidState = FluidloggedUtils.getFluidState(world, pos);
        return fluidState.isEmpty() ? originalResistance : block.getExplosionResistance(this.exploder);
    }

    /** If this given block is within `waterBlocks`. */
    public boolean isWaterBlock(Block block)
    { return waterBlocks.contains(block); }
}
