package com.sirsquidly.oe.mixin;

import com.sirsquidly.oe.items.ItemTrident;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

/**
 * Alters the player's arm rotation when using a Trident, in 3ed person
 */
@SideOnly(Side.CLIENT)
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase
{
    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public ModelRenderer bipedLeftArm;

    @Shadow
    public ModelBiped.ArmPose leftArmPose;

    @Shadow
    public ModelBiped.ArmPose rightArmPose;

    @Inject(method = "setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V",
            at = @At(value = "TAIL"))
    private void renderTrident(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo info)
    {
    	if (entityIn instanceof EntityLivingBase)
    	{
            EntityLivingBase living = (EntityLivingBase) entityIn;
    		Item mainItem = living.getHeldItem(EnumHand.MAIN_HAND).getItem();
    		Item offItem = living.getHeldItem(EnumHand.OFF_HAND).getItem();
    		
    		boolean mainIsRight = living.getPrimaryHand() == EnumHandSide.RIGHT;

    		if (living.getActiveItemStack().getItem() instanceof ItemTrident && (living.getItemInUseCount() > 0))
    		{
    			if (ConfigHandler.item.trident.trident3PsnAnimStyle == 2)
    			{
                    ItemTrident trident = (ItemTrident)living.getActiveItemStack().getItem();
                    float anim = trident.getAnimSpeed(living);
    				/** Used for the entire arm rotating up, instead of 'snapping' into place. **/
        			float flipUp = Math.min(anim / 5.0F, 1.0F);

        			if (mainItem instanceof ItemTrident && mainIsRight || offItem instanceof ItemTrident && !mainIsRight)
                    {
                        this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5F + 0.2F - (float)Math.PI + (Math.min(anim / 15.0F, 1.0F) * -0.5F)) * flipUp;
                        this.bipedRightArm.rotateAngleY = 0.0F;
                    } 
                    else if(mainItem instanceof ItemTrident && !mainIsRight || offItem instanceof ItemTrident && mainIsRight)
                    {
                        this.bipedLeftArm.rotateAngleX = (this.bipedLeftArm.rotateAngleX * 0.5F + 0.2F - (float)Math.PI + (Math.min(anim / 15.0F, 1.0F) * -0.5F)) * flipUp;
                        this.bipedLeftArm.rotateAngleY = 0.0F;
                    }
    			}
    			else if (ConfigHandler.item.trident.trident3PsnAnimStyle == 1)
    			{
    				if (mainItem instanceof ItemTrident && mainIsRight || offItem instanceof ItemTrident && !mainIsRight)
                    {
                        this.bipedRightArm.rotateAngleX = 3.1F;
                        this.bipedRightArm.rotateAngleY = 0.0F;
                    } 
                    else if(mainItem instanceof ItemTrident && !mainIsRight || offItem instanceof ItemTrident && mainIsRight)
                    {
                        this.bipedLeftArm.rotateAngleX = 3.1F;
                        this.bipedLeftArm.rotateAngleY = 0.0F;
                    }
    			}
    		}
    	}
    }
}