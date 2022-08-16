package com.sirsquidly.oe.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.client.model.entity.ModelPickled;
import com.sirsquidly.oe.entity.EntityPickled;
import com.sirsquidly.oe.client.render.entity.layer.LayerPickled;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPickled extends RenderLiving<EntityPickled>
{
	public static final ResourceLocation PICKLED_ZOMBIE_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled.png");
	public static final ResourceLocation PICKLED_ZOMBIE_DRY_TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled_dry.png");
	public static final ResourceLocation PICKLED_SECRET_TEXTURE1 = new ResourceLocation(Reference.MOD_ID + ":textures/entities/zombie/pickled_spec1.png");
	
	public RenderPickled(RenderManager manager)
    {
		super(manager, new ModelPickled(), 0.5F);
		this.addLayer(new LayerPickled(this));
		this.addLayer(new LayerHeldItem(this)
        {
        	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
            {
                boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
                ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
                ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

                if (!itemstack.isEmpty() || !itemstack1.isEmpty())
                {
                    GlStateManager.pushMatrix();

                    if (this.livingEntityRenderer.getMainModel().isChild)
                    {
                        GlStateManager.translate(0.0F, 0.75F, 0.0F);
                        GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    }

                    this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
                    this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
                    GlStateManager.popMatrix();
                }
            }
			
			 private void renderHeldItem(EntityLivingBase entity, ItemStack item, ItemCameraTransforms.TransformType camera, EnumHandSide handSide)
			    {
			        if (!item.isEmpty())
			        {
			            GlStateManager.pushMatrix();
			            if (entity.isSneaking())
			            {
			                GlStateManager.translate(0.0F, 0.2F, 0.0F);
			            }
			            this.translateToHand(handSide);
			            
			            if (item.getItem() == OEItems.TRIDENT_ORIG && ((EntityZombie)entity).isArmsRaised())
			            {
			            	GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			            	GlStateManager.translate(-0.15F, 0.0F, 0.0F);
			            }
			            else
			            {
			            	GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			            }
			            boolean flag = handSide == EnumHandSide.LEFT;
			            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
			            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, item, camera, flag);
			            GlStateManager.popMatrix();
			        }
			    }
			 
			 
        });
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }
	
	public ModelPickled getMainModel()
    {
        return (ModelPickled)super.getMainModel();
    }
	
	protected ResourceLocation getEntityTexture(EntityPickled entity) {
		String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());

        if (s != null && ("Rick".equals(s) || "Pickle Rick".equals(s) || "Pickled Rick".equals(s)))
        {
            return PICKLED_SECRET_TEXTURE1;
        }
        if(entity.isDry())
        {
        	return PICKLED_ZOMBIE_DRY_TEXTURE;
        }
		return PICKLED_ZOMBIE_TEXTURE;
	}
	
	@Override
	protected void preRenderCallback(EntityPickled entity, float f) {
		float size = 0.9375F;
		
		if (entity.isSwimming())
        {
			GL11.glRotatef(0F, 0F, 45F, 1F);
        }
		GlStateManager.scale(size, size, size);
	}
	
	protected void applyRotations(EntityPickled entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        if (entityLiving.isDry())
        {
            rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}
