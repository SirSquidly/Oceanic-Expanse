package com.sirsquidly.oe.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.client.model.entity.ModelDrowned;
import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.client.render.entity.layer.LayerDrowned;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDrowned extends RenderLiving<EntityDrowned>
{
	public static final ResourceLocation DROWNED_ZOMBIE_CAPTAIN_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_captain.png");
	public static final ResourceLocation DROWNED_ZOMBIE_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned.png");
	
	public RenderDrowned(RenderManager manager)
    {
		super(manager, new ModelDrowned(), 0.5F);
        this.addLayer(new LayerCustomHead(new ModelDrowned().bipedHead));
        this.addLayer(new LayerElytra(this));
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
			        	boolean left = handSide == EnumHandSide.LEFT;
			        	
			            GlStateManager.pushMatrix();
			            if (entity.isSneaking())
			            {
			                GlStateManager.translate(0.0F, 0.2F, 0.0F);
			            }
			            this.translateToHand(handSide);
			            
			            if (item.getItem() == OEItems.TRIDENT_ORIG && ((EntityZombie)entity).isArmsRaised())
			            {
			            	GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			            	GlStateManager.translate(left ? 0.15F : -0.15F, 0.0F, 0.0F);
			            }
			            else
			            {
			            	GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			            }
			           
			            GlStateManager.translate((float)(left ? -1 : 1) / 16.0F, 0.125F, -0.625F);
			            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, item, camera, left);
			            GlStateManager.popMatrix();
			        }
			    }
			 
			 
        });
        
        if (ConfigHandler.entity.drowned.drownedGlowLayer)
        {
        	this.addLayer(new LayerDrowned(this));
        }
        
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelDrowned(0.5F, true);
                this.modelArmor = new ModelDrowned(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

	
	public ModelZombie getMainModel()
    {
        return (ModelZombie)super.getMainModel();
    }
	
	protected ResourceLocation getEntityTexture(EntityDrowned entity)
	{
		return entity.isCaptain() && ConfigHandler.entity.drowned.drownedCaptain.enableDrownedCaptainTexture ? DROWNED_ZOMBIE_CAPTAIN_TEXTURE : DROWNED_ZOMBIE_TEXTURE;
	}
	
	@Override
	protected void preRenderCallback(EntityDrowned entity, float f) 
	{
		float size = 0.9375F;
		float bobbing = MathHelper.cos(entity.ticksExisted * 0.09F) * 0.05F + 0.05F;
		
		if (entity.isInWater() && ConfigHandler.entity.drowned.enableDrownedSwimAnims)
		{
			GL11.glRotatef(-bobbing*20, 1F, 0F, 0F);
			GL11.glTranslatef(0F, bobbing/2 - 0.1F, 0F);
		}
		
		GlStateManager.scale(size, size, size);
	}
}
