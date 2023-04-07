package com.sirsquidly.oe.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityConduitEye;
import com.sirsquidly.oe.tileentity.TileConduit;
import com.sirsquidly.oe.client.model.tileentity.*;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

public class RenderConduit extends TileEntitySpecialRenderer<TileConduit>
{
	private static final ResourceLocation CONDUIT_OFF = new ResourceLocation(Main.MOD_ID + ":textures/entities/conduit/conduit_off.png");
    private final ModelBase modelConduitOff = new ModelConduitOff();
    private static final ResourceLocation CONDUIT_SHELL = new ResourceLocation(Main.MOD_ID + ":textures/entities/conduit/conduit_shell.png");
    private final ModelBase modelConduitShell = new ModelConduitShell();
    private final ModelBase modelConduitWind = new ModelJustABlock();
	
    public void render(TileConduit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {    	
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);

        GlStateManager.disableCull();
        
        if (te.isActive)
        {
        	/**Used for progressing the bobbing on the Eye and Shell, up and down.*/
            float bobbing = (float)te.bobTick + partialTicks;
            
            if (te.windTick <= 34)
            {
            	GlStateManager.pushMatrix();
            	this.bindTexture(new ResourceLocation(Main.MOD_ID + ":textures/entities/conduit/wind/wind" + te.windTick + ".png"));
            	GlStateManager.translate(0.0F, -0.5F, 0.0F);
            	
            	if(te.windTick > 0 && te.windTick <= 18)
            	{
	            	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
	            	GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	            	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            	}
            	
            	if(te.windTick > 18)
            	{
            		this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                	GlStateManager.translate(0.0F, -1.0F, -1.0F);
                	this.modelConduitWind.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
            	}
            	GlStateManager.popMatrix();
            }
        	
        	GlStateManager.translate(0.0F, MathHelper.sin(bobbing * 0.1F) * 0.2F, 0.0F);
            renderEye(te, x, y, z, partialTicks);
            renderShell(te, x, y, z, partialTicks);
        }
        else
        {
        	GlStateManager.pushMatrix();
        	GlStateManager.translate(0.0F, -0.5625F, 0.0F);
        	this.bindTexture(CONDUIT_OFF);
        	this.modelConduitOff.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
        	GlStateManager.popMatrix();
        }
        
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
    
    public void renderEye(TileConduit tile, double posX, double posY, double posZ, float partialTicks)
    {
        Entity entity = new EntityConduitEye(tile.getWorld());
        float scale = 1.0F;

        GlStateManager.translate(0.0F, 0.5F, 0.0F);
        GlStateManager.scale(scale, scale, scale);
        entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
        if (tile.canHunt) ((EntityConduitEye) entity).setHunting(true);
        if (ConfigHandler.block.conduit.enableConduitPulse) entity.ticksExisted = tile.bobTick;
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, -0.12D, 0.0D, 0.0F, partialTicks, false);
    }
    
    public void renderShell(TileConduit tile, double x, double y, double z, float partialTicks)
    {
    	float f1;

        for (f1 = tile.shellRotation - tile.shellRotationPrev; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F))
        {
            ;
        }
        while (f1 < -(float)Math.PI)
        {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = tile.shellRotationPrev + f1 * partialTicks;
        
        float scale = 0.5F;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(-f2 * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(f2 * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -1.0F, 0.0F);
        this.bindTexture(CONDUIT_SHELL);
        this.modelConduitShell.render((Entity)null, 0.0F, 3.0F, 0.2F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}