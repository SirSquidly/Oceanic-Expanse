package com.sirsquidly.oe.mixin;

import com.sirsquidly.oe.capabilities.CapabilityRiptide;
import com.sirsquidly.oe.client.render.entity.layer.LayerRiptideAnim;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a new layer for rendering the Riptide Animation when the player preforms it
 */
@SideOnly(Side.CLIENT)
@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RenderLivingBase<AbstractClientPlayer>
{
    float spinSpeed = 50.0F;

    public MixinRenderPlayer(RenderManager manager, ModelBase model, float shadowSize)
    { super(manager, model, shadowSize); }

    /** Slaps the new `LayerRiptideAnim` on the end of the layers. */
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V", at = @At(value = "TAIL"))
    private void addRiptideLayer(RenderManager manager, boolean useSmallArms, CallbackInfo info)
    { this.addLayer(new LayerRiptideAnim(this)); }

    /** Spins the player when riptide(ing?) by hijacking the rotations if Riptide is being used. */
    @Inject(method = "applyRotations", at = @At("HEAD"), cancellable = true)
    private void renderRotationsRiptideOverride(AbstractClientPlayer entity, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci)
    {
        if (entity.hasCapability(CapabilityRiptide.RIPTIDE_CAP, null))
        {
            CapabilityRiptide.ICapabilityRiptide capWindCharge = entity.getCapability(CapabilityRiptide.RIPTIDE_CAP, null);

            if (capWindCharge.getRiptideAnimate())
            {
                super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
                GlStateManager.rotate(-90.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate((entity.ticksExisted + partialTicks) * -spinSpeed, 0.0F, 1.0F, 0.0F);

                ci.cancel();
            }
        }
    }
}