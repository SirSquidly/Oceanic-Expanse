package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.EntityCrab;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCrab extends ModelBase {
	public boolean holdingItem;
	public ModelRenderer body1;
    public ModelRenderer eyestlkl1;
    public ModelRenderer eyestlkr1;
    public ModelRenderer leglm1;
    public ModelRenderer leglf1;
    public ModelRenderer leglb1;
    public ModelRenderer clawl1;
    public ModelRenderer clawr1;
    public ModelRenderer legrf1;
    public ModelRenderer legrm1;
    public ModelRenderer legrb1;

    public ModelCrab() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.legrb1 = new ModelRenderer(this, 40, 18);
        this.legrb1.setRotationPoint(-4.5F, 1.0F, 4.0F);
        this.legrb1.addBox(-2.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(legrb1, 0.08726646259971647F, 0.4363323129985824F, 0.2617993877991494F);
        this.legrf1 = new ModelRenderer(this, 40, 0);
        this.legrf1.setRotationPoint(-4.5F, 1.0F, -1.0F);
        this.legrf1.addBox(-2.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(legrf1, -0.08726646259971647F, -0.4363323129985824F, 0.2617993877991494F);
        this.leglf1 = new ModelRenderer(this, 32, 0);
        this.leglf1.setRotationPoint(4.5F, 1.0F, -1.0F);
        this.leglf1.addBox(0.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leglf1, -0.08726646259971647F, 0.4363323129985824F, -0.2617993877991494F);
        this.eyestlkr1 = new ModelRenderer(this, 8, 0);
        this.eyestlkr1.setRotationPoint(-1.5F, -1.5F, -2.0F);
        this.eyestlkr1.addBox(-2.0F, -3.0F, 0.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(eyestlkr1, -0.17453292519943295F, 0.2617993877991494F, -0.0F);
        this.clawr1 = new ModelRenderer(this, 16, 17);
        this.clawr1.setRotationPoint(-5.0F, 1.0F, -2.0F);
        this.clawr1.addBox(0.0F, -1.5F, -3.0F, 5, 3, 3, 0.0F);
        this.setRotateAngle(clawr1, 0.0F, 0.0F, 0.0F);
        this.legrm1 = new ModelRenderer(this, 40, 9);
        this.legrm1.setRotationPoint(-4.5F, 1.0F, 1.5F);
        this.legrm1.addBox(-2.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(legrm1, 0.0F, 0.0F, 0.2617993877991494F);
        this.leglb1 = new ModelRenderer(this, 32, 18);
        this.leglb1.setRotationPoint(4.5F, 1.0F, 4.0F);
        this.leglb1.addBox(0.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leglb1, 0.08726646259971647F, -0.4363323129985824F, -0.2617993877991494F);
        this.body1 = new ModelRenderer(this, 0, 5);
        this.body1.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.body1.addBox(-4.5F, -1.5F, -2.0F, 9, 5, 7, 0.0F);
        this.clawl1 = new ModelRenderer(this, 0, 17);
        this.clawl1.setRotationPoint(5.0F, 1.0F, -2.0F);
        this.clawl1.addBox(-5.0F, -1.5F, -3.0F, 5, 3, 3, 0.0F);
        this.setRotateAngle(clawl1, 0.0F, -0.0F, 0.0F);
        this.leglm1 = new ModelRenderer(this, 32, 9);
        this.leglm1.setRotationPoint(4.5F, 1.0F, 1.5F);
        this.leglm1.addBox(0.0F, -1.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leglm1, 0.0F, 0.0F, -0.2617993877991494F);
        this.eyestlkl1 = new ModelRenderer(this, 0, 0);
        this.eyestlkl1.setRotationPoint(1.5F, -1.5F, -2.0F);
        this.eyestlkl1.addBox(0.0F, -3.0F, 0.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(eyestlkl1, -0.17453292519943295F, -0.2617993877991494F, 0.0F);
        this.body1.addChild(this.legrb1);
        this.body1.addChild(this.legrf1);
        this.body1.addChild(this.leglf1);
        this.body1.addChild(this.eyestlkr1);
        this.body1.addChild(this.clawr1);
        this.body1.addChild(this.legrm1);
        this.body1.addChild(this.leglb1);
        this.body1.addChild(this.clawl1);
        this.body1.addChild(this.leglm1);
        this.body1.addChild(this.eyestlkl1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body1.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	this.leglf1.rotateAngleZ = -0.2617993877991494F;
        this.leglm1.rotateAngleZ = -0.2617993877991494F;
        this.leglb1.rotateAngleZ = -0.2617993877991494F;
        this.legrf1.rotateAngleZ = 0.2617993877991494F;
        this.legrm1.rotateAngleZ = 0.2617993877991494F;
        this.legrb1.rotateAngleZ = 0.2617993877991494F;

        float l1 = Math.abs(MathHelper.sin(limbSwing * 0.8F + 0.0F) * 0.8F) * limbSwingAmount;
        float l2 = Math.abs(MathHelper.sin(limbSwing * 0.8F + 0.6F) * 0.8F) * limbSwingAmount;
        float l3 = Math.abs(MathHelper.sin(limbSwing * 0.8F + 1.0F) * 0.8F) * limbSwingAmount;
        
        this.leglf1.rotateAngleZ += -l1;
        this.leglm1.rotateAngleZ += -l2;
        this.leglb1.rotateAngleZ += -l3;
        this.legrf1.rotateAngleZ += l1;
        this.legrm1.rotateAngleZ += l2;
        this.legrb1.rotateAngleZ += l3;
        
        
        
        
        
        
        
        
        

    	EntityCrab crab = (EntityCrab) entityIn;

        float idle = MathHelper.sin((ageInTicks) * 0.07F) * 0.7F;
        float eat = MathHelper.sin(ageInTicks) * 0.9F;
        
        ItemStack mainItem = crab.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offItem = crab.getHeldItem(EnumHand.OFF_HAND);
        
        boolean isDigging = crab.getAnimationState() == 1;
        
        boolean rightHolding = false;
        boolean leftHolding = false;
        
        float f = MathHelper.sin(this.swingProgress * (float)Math.PI)*2;

        this.eyestlkr1.rotateAngleX = -0.17453292519943295F;
    	this.eyestlkl1.rotateAngleX = -0.17453292519943295F;
    	
        this.clawr1.rotateAngleX = 0.8F - f;
        this.clawl1.rotateAngleX = 0.8F - f;
        this.clawr1.rotateAngleY = 0.08726646259971647F;
        this.clawl1.rotateAngleY = -0.08726646259971647F;
        this.clawr1.rotateAngleZ = 0.15F + idle * 0.1F - f;
        this.clawl1.rotateAngleZ = -0.15F - idle * 0.1F + f;    
        
        this.clawr1.offsetZ = -0.0F;
        this.clawl1.offsetZ = -0.0F;
        
    	this.body1.offsetX = 0.0F;
    	this.body1.offsetY = 0.0F;
    	this.body1.offsetZ = 0.0F;
    	
    	this.clawr1.offsetX = 0.0F;
    	this.clawl1.offsetX = 0.0F;
    	
    	
        if (!(mainItem.isEmpty()))
        {
            if (crab.getPrimaryHand() == EnumHandSide.RIGHT)
            {
            	this.clawr1.rotateAngleX = 1.5F - f;
            	this.clawr1.rotateAngleY = 0.3F + idle*0.2F;
            	this.clawr1.rotateAngleZ = 0.0F - f;
            	rightHolding = true;
            	if (!isDigging) this.clawr1.offsetZ = -0.1F;
            }
            if (crab.getPrimaryHand() == EnumHandSide.LEFT)
            {
            	this.clawl1.rotateAngleX = 1.5F - f;
            	this.clawl1.rotateAngleY = -0.3F - idle*0.2F;
            	this.clawl1.rotateAngleZ = 0.0F - f;
            	leftHolding = true;
            	if (!isDigging) this.clawl1.offsetZ = -0.1F;
            }
        }
        if (!(offItem.isEmpty()))
        {
        	if (crab.getPrimaryHand() == EnumHandSide.RIGHT)
            {
        		this.clawl1.rotateAngleX = 1.5F - f;
            	this.clawl1.rotateAngleY = -0.3F - idle*0.2F;
            	this.clawl1.rotateAngleZ = 0.0F - f;
            	if (!isDigging) this.clawl1.offsetZ = -0.1F;
            	rightHolding = true;
            	if (crab.getAnimationState() == 2) { this.clawl1.rotateAngleY = 0.2F; this.clawl1.rotateAngleZ = 0.3F - eat*0.3F; }
            }
            if (crab.getPrimaryHand() == EnumHandSide.LEFT)
            {
            	
            	this.clawr1.rotateAngleX = 1.5F - f;
            	this.clawr1.rotateAngleY = 0.3F + idle*0.2F;
            	this.clawr1.rotateAngleZ = 0.0F - f;
            	if (!isDigging) this.clawr1.offsetZ = -0.1F;
            	leftHolding = true;
            	if (crab.getAnimationState() == 2) { this.clawr1.rotateAngleY = 0.2F; this.clawr1.rotateAngleZ = -0.3F + eat*0.3F; }
            }
        }

        if (!isDigging && !crab.isAngry() & crab.isPartying())
        {
			float danceSpeed = 1.1F;
			
        	float danceXSway = MathHelper.sin((ageInTicks * danceSpeed) * 0.4F) * 0.4F;
        	
        	this.body1.offsetY = -0.11F + danceXSway * danceXSway;
        	
        	this.body1.offsetX = danceXSway/2;
        	
        	this.leglf1.rotateAngleZ += danceXSway * 2.0F;
            this.leglm1.rotateAngleZ += danceXSway * 2.2F;
            this.leglb1.rotateAngleZ += danceXSway * 2.4F;
            this.legrf1.rotateAngleZ += danceXSway * 2.0F;
            this.legrm1.rotateAngleZ += danceXSway * 2.2F;
            this.legrb1.rotateAngleZ += danceXSway * 2.4F;
            
            
            if (!rightHolding)
        	{
        		this.clawr1.rotateAngleX -= 1.4F + danceXSway*4;
        		this.clawr1.rotateAngleY += 0.4F;
            	this.clawr1.rotateAngleZ -= 0.8F + danceXSway*4;
        	}
            else
            {
            	this.clawr1.offsetX = -this.body1.offsetX;
            }
        	if (!leftHolding)
        	{
        		this.clawl1.rotateAngleX -= 1.4F - danceXSway*4;
            	this.clawl1.rotateAngleY -= 0.4F;
            	this.clawl1.rotateAngleZ += 0.8F - danceXSway*4;
        	}
        	else
            {
            	this.clawl1.offsetX = -this.body1.offsetX;
            }
        }
        
        
        
        
        if (crab.isAngry())
        {
        	if (!rightHolding)
        	{
        		this.clawr1.rotateAngleX -= 1.4F;
        		this.clawr1.rotateAngleY += 0.4F;
            	this.clawr1.rotateAngleZ -= 0.8F;
        	}
        	if (!leftHolding)
        	{
        		this.clawl1.rotateAngleX -= 1.4F;
            	this.clawl1.rotateAngleY -= 0.4F;
            	this.clawl1.rotateAngleZ += 0.8F;
        	}
        	
        	this.clawr1.rotateAngleY += 0.4F;
        	this.clawl1.rotateAngleY -= 0.4F;
        }
        
        if (isDigging) 
        { 
        	this.eyestlkr1.rotateAngleX = 0.3F;
        	this.eyestlkl1.rotateAngleX = 0.3F;
        	
        	this.clawr1.rotateAngleX = 0.2F - eat;
        	this.clawr1.rotateAngleZ = 0.5F - eat; 
        	this.clawl1.rotateAngleX = -0.2F - eat;
        	this.clawl1.rotateAngleZ = -0.5F + eat;
        }
    }

    public void postRenderArm(float scale, EnumHandSide side)
    { this.getArmForSide(side).postRender(scale); }
    
    public ModelRenderer getArmForSide(EnumHandSide side)
    { return side == EnumHandSide.LEFT ? this.clawl1 : this.clawr1; }
    
    public void postRenderClaw(float scale, EnumHandSide side)
    { this.getClaw(side).postRender(scale); }
    
    public ModelRenderer getClaw(EnumHandSide side)
    { return side == EnumHandSide.LEFT ? this.clawl1 : this.clawr1; }
}