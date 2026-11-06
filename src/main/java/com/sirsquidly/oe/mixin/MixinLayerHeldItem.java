package com.sirsquidly.oe.mixin;

import com.sirsquidly.oe.client.ClientMixinMethods;
import com.sirsquidly.oe.items.ItemTrident;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Spins the Trident item around in the player's hand
 */
@SideOnly(Side.CLIENT)
@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem
{
    @Shadow
    @Final
    protected RenderLivingBase<?> livingEntityRenderer;

    @Inject(method = "renderHeldItem", at = @At( value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"),
            cancellable = true)
    public void renderTrident(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transforms, EnumHandSide hand, CallbackInfo callback)
    {
        if (entity == null) return;
        if (entity.getActiveItemStack() != stack || !(stack.getItem() instanceof ItemTrident)) return;
        if (!(livingEntityRenderer.getMainModel() instanceof ModelBiped)) return;
        ClientMixinMethods.preformTridentItemRotations3edPerson(entity, stack);
    }
}