package com.sirsquidly.oe.client.render.tileentity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.tileentity.ModelNautilusShellBlock;
import com.sirsquidly.oe.common.items.ItemNautilusArmor;
import com.sirsquidly.oe.common.tileentity.TileNautilusShellBlock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNautilusShellBlock extends TileEntitySpecialRenderer<TileNautilusShellBlock>
{
    public static final RenderNautilusShellBlock INSTANCE = new RenderNautilusShellBlock();
    private ModelNautilusShellBlock model = new ModelNautilusShellBlock();
    public static final ResourceLocation TEXTURES = new ResourceLocation(Main.MOD_ID + ":textures/entities/nautilus/nautilus.png");

    public void render(TileNautilusShellBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!(te instanceof TileNautilusShellBlock)) return;

        EnumFacing enumfacing = EnumFacing.byIndex(te.getBlockMetadata() & 7);
        float animation = te.getAnimationProgress(partialTicks);
        ResourceLocation armor = getArmorTexture(te);

        GlStateManager.pushMatrix();
        this.renderNautilusShellBlock((float)x, (float)y, (float)z, enumfacing, (float)(te.getSkullRotation() * 360) / 16.0F, te.getSkullType(), destroyStage, animation, armor != null ? armor : TEXTURES);
        GlStateManager.popMatrix();
    }

    public void renderNautilusShellBlock(float x, float y, float z, EnumFacing facing, float rotationIn, int skullType, int destroyStage, float animateTicks, ResourceLocation textureIn)
    {
        ModelNautilusShellBlock modelbase = this.model;

        if(destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        { this.bindTexture(textureIn); }

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (facing == EnumFacing.UP)
        {
            GlStateManager.translate(x + 0.5F, y + 1.41F, z + 0.5F);
        }
        else
        {
            switch (facing)
            {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 1.41F, z + 0.5F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 1.41F, z + 0.55F);
                    rotationIn = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.5F, y + 1.41F, z + 0.5F);
                    rotationIn = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.5F, y + 1.41F, z + 0.5F);
                    rotationIn = 90.0F;
            }
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        float size = 0.9375F;
        GlStateManager.scale(size, size, size);
        modelbase.render((Entity)null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    public ResourceLocation getArmorTexture(TileNautilusShellBlock te)
    {
        ResourceLocation texture = null;

        if (!te.getArmorStack().isEmpty() && te.getArmorStack().getItem() instanceof ItemNautilusArmor)
        {
            ItemNautilusArmor armor = (ItemNautilusArmor) te.getArmorStack().getItem();
            texture = armor.getTexture();
        }
        return texture;
    }
}