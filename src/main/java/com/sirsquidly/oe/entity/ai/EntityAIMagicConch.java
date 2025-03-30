package com.sirsquidly.oe.entity.ai;

import com.sirsquidly.oe.enchantment.resonance.Resonance;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.items.ItemConch;
import com.sirsquidly.oe.util.ResonanceUtil;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class EntityAIMagicConch extends EntityAIBase
{
    private final EntityLiving entityHost;
    /** The time since the conch was last used */
    private int conchCooldownTime;
    private EntityLivingBase attackTarget;

    private ItemStack heldItemOffhand;

    public EntityAIMagicConch(EntityLiving entityIn)
    { this.entityHost = entityIn; }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null || entitylivingbase.isDead)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);

            float maxConchDistance = 32 * 32;
            if (d0 >= (double) maxConchDistance)
            { return false; }
            else
            { return getAndSetOffhand(); }
        }
    }

    /*
    * Both checks if the offhand item is a Conch, and saves the Offhand item for later use in code.
    * */
    protected boolean getAndSetOffhand()
    {
        this.heldItemOffhand = this.entityHost.getHeldItemOffhand();
        return this.heldItemOffhand.getItem() == OEItems.CONCH || this.isOffhandMagicConch();
    }

    protected boolean isOffhandMagicConch()
    { return this.heldItemOffhand.getItem() == OEItems.CONCH_MAGIC; }

    public void updateTask()
    {
        if (++this.conchCooldownTime >= 20 * 10)
        {
            this.entityHost.setActiveHand(EnumHand.OFF_HAND);

            NBTTagCompound nbttagcompound = this.heldItemOffhand.getTagCompound();
            this.entityHost.world.playSound((EntityPlayer)null, this.entityHost.posX, this.entityHost.posY, this.entityHost.posZ, ((ItemConch)this.heldItemOffhand.getItem()).findSound(nbttagcompound.getString("Sound")), SoundCategory.NEUTRAL, ConfigHandler.item.conch.conchSoundDistance * 0.0625F, 1.0F);

            if (this.isOffhandMagicConch())
            {
                Resonance resonance = ResonanceUtil.getResonance(this.heldItemOffhand);
                if (resonance != null) resonance.onUse(this.entityHost, this.heldItemOffhand);
            }

            this.entityHost.resetActiveHand();
            this.conchCooldownTime = 0;
        }
    }
}