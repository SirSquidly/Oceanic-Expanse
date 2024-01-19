package com.sirsquidly.oe.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sirsquidly.oe.Main;	

@SideOnly(Side.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRender {

    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"))
    private void renderTrident(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equippedProgress, CallbackInfo info){
        if (stack.getItemUseAction() == Main.SPEAR) {
        	System.out.println("This is running, we know it does, how cool and stuff whatever");
            boolean flag = hand == EnumHand.MAIN_HAND;
            EnumHandSide side = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
            int offset = side == EnumHandSide.RIGHT ? 1 : -1;
            this.transformSideFirstPerson(side, equippedProgress);
            GlStateManager.translate((float)offset * -0.5F, 0.7F, 0.1F);
            GlStateManager.rotate(-55.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((float)offset * 35.3F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)offset * -9.785F, 0.0F, 0.0F, 1.0F);
            float f5 = (float)stack.getMaxItemUseDuration() - ((float)this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
            float f7 = f5 / 10.0F;
            if (f7 > 1.0F) {
                f7 = 1.0F;
            }
            if (f7 > 0.1F) {
                float f9 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                float f2 = f7 - 0.1F;
                float f3 = f9 * f2;
                GlStateManager.translate(f3 * 0.0F, f3 * 0.004F, f3 * 0.0F);
            }
            GlStateManager.translate(0.0F, 0.0F, f7 * 0.2F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
            GlStateManager.rotate((float)offset * 45.0F, 0.0F, -1.0F, 0.0F);
        }
    }

    @Shadow
    protected abstract void transformSideFirstPerson(EnumHandSide hand, float equippedProgress);

    @Shadow
    public abstract void renderItemSide(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType type, boolean leftHanded);
}