package com.sirsquidly.oe.mixin;

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

import com.sirsquidly.oe.init.OEItems;

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
    		
    		Item mainItem = ((EntityLivingBase) entityIn).getHeldItem(EnumHand.MAIN_HAND).getItem();
    		Item offItem = ((EntityLivingBase) entityIn).getHeldItem(EnumHand.OFF_HAND).getItem();
    		
    		boolean mainIsRight = ((EntityLivingBase) entityIn).getPrimaryHand() == EnumHandSide.RIGHT;
    		
    		float smoothUseTime = (float)((EntityLivingBase) entityIn).getActiveItemStack().getMaxItemUseDuration() - ((float)((EntityLivingBase) entityIn).getItemInUseCount() - (ageInTicks - (float)entityIn.ticksExisted) + 1.0F);
    		
    		if (((EntityLivingBase) entityIn).getActiveItemStack().getItem() == OEItems.TRIDENT_ORIG && ((EntityLivingBase) entityIn).getItemInUseCount() > 0)
    		{
    			/** Used for the entire arm rotating up, instead of 'snapping' into place. **/
    			float flipUp = Math.min(smoothUseTime / 5.0F, 1.0F);
    			
    			if (mainItem == OEItems.TRIDENT_ORIG && mainIsRight || offItem == OEItems.TRIDENT_ORIG && !mainIsRight)
                {
                    this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5F + 0.2F - (float)Math.PI + (Math.min(smoothUseTime / 15.0F, 1.0F) * -0.5F)) * flipUp;
                    this.bipedRightArm.rotateAngleY = 0.0F;
                } 
                else if(mainItem == OEItems.TRIDENT_ORIG && !mainIsRight || offItem == OEItems.TRIDENT_ORIG && mainIsRight)
                {
                    this.bipedLeftArm.rotateAngleX = (this.bipedLeftArm.rotateAngleX * 0.5F + 0.2F - (float)Math.PI + (Math.min(smoothUseTime / 15.0F, 1.0F) * -0.5F)) * flipUp;
                    this.bipedLeftArm.rotateAngleY = 0.0F;
                }
    		}
    	}
    }
}