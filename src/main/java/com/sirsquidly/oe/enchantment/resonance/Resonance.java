package com.sirsquidly.oe.enchantment.resonance;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

/*
 * This class handles the functionality of Resonances, and gets inherited by all Resonance classes.
 *
 * The class used for registry and util item check related to resonances is `ResonanceUtil`
 * */
public abstract class Resonance
{

    /** Used in localisation and stats. */
    protected String name;
    public final int resonanceRange;
    /** How much Durability each use of the Resonance costs the base item. */
    public final int durabilityCost;

    protected Resonance(String nameIn, int resonanceRangeIn)
    { this(nameIn, resonanceRangeIn, 1); }

    protected Resonance(String nameIn, int resonanceRangeIn, int durabilityCostIn)
    {
        this.name = nameIn;
        this.resonanceRange = resonanceRangeIn;
        this.durabilityCost = durabilityCostIn;
    }

    /** Called immediately when a Magic Conch is used. */
    public void onUse(EntityLivingBase user, ItemStack item)
    {
        if (user.world.isRemote) return;

        item.damageItem(getDurabilityCost(), user);

        if (getResonanceRange() <= 0) return;
        for (EntityLivingBase nearbyEntity : user.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(user.getPosition()).grow(getResonanceRange(), getResonanceRange(), getResonanceRange())))
        {
            if (nearbyEntity == user) continue;
            onEntitiesInRange(user, nearbyEntity, getResonanceRange());
        }
    }

    /** Called per each entity within range of the Magic Conch's effect when a Resonance is preformed. */
    public void onEntitiesInRange(EntityLivingBase user, EntityLivingBase target, int range)
    {}

    /** Spawns particles scattered around the given entity. */
    public void spawnResonanceManyParticles(EntityLivingBase user, float distance, int amount)
    {
        for (int i = 0; i <= amount; i++)
        { spawnResonanceParticle(user, distance); }
    }

    public void spawnResonanceParticle(EntityLivingBase user, float distance)
    {}

    /** Returns the name of the Resonance. */
    public String getName()
    { return "resonance." + this.name; }

    /** Returns the range of the Resonance. */
    public int getResonanceRange()
    { return this.resonanceRange; }

    /** Returns how much item damage each use of the Resonance costs the item. */
    public int getDurabilityCost()
    { return this.durabilityCost; }
}