package com.sirsquidly.oe.client.model.entity;

import com.sirsquidly.oe.entity.AbstractFish;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDolphin extends ModelBase
{
    private final ModelRenderer main;
    private final ModelRenderer finL_r1;
    private final ModelRenderer finR_r1;
    private final ModelRenderer finDorsal_r1;
    /* The head is never actually rotated, it is separate due to older presented footage (Minecon Earth 2017) showing Dolphins with head rotation. */
    private final ModelRenderer head;
    private final ModelRenderer tail;
    private final ModelRenderer tailFin;

    public ModelDolphin() {
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);
        main.cubeList.add(new ModelBox(main, 22, 0, -4.0F, -7.0F, -6.0F, 8, 7, 13, 0.0F, false));

        finL_r1 = new ModelRenderer(this);
        finL_r1.setRotationPoint(3.0F, -1.5F, -3.0F);
        main.addChild(finL_r1);
        setRotationAngle(finL_r1, 0.0F, -0.5236F, 0.3491F);
        finL_r1.cubeList.add(new ModelBox(finL_r1, 32, 20, 0.0F, -0.5F, -2.0F, 7, 1, 4, 0.0F, false));

        finR_r1 = new ModelRenderer(this);
        finR_r1.setRotationPoint(-3.0F, -1.5F, -3.0F);
        main.addChild(finR_r1);
        setRotationAngle(finR_r1, 0.0F, 0.5236F, -0.3491F);
        finR_r1.cubeList.add(new ModelBox(finR_r1, 32, 25, -7.0F, -0.5F, -2.0F, 7, 1, 4, 0.0F, false));

        finDorsal_r1 = new ModelRenderer(this);
        finDorsal_r1.setRotationPoint(0.0F, -6.0F, -1.0F);
        main.addChild(finDorsal_r1);
        setRotationAngle(finDorsal_r1, -0.4363F, 0.0F, 0.0F);
        finDorsal_r1.cubeList.add(new ModelBox(finDorsal_r1, 0, 13, -0.5F, -5.0F, -2.0F, 1, 5, 4, 0.0F, false));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -3.5F, -6.0F);
        main.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -3.5F, -6.0F, 8, 7, 6, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 22, 0, -1.0F, 1.5F, -10.0F, 2, 2, 4, 0.0F, false));

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, -2.5F, 7.0F);
        main.addChild(tail);
        tail.cubeList.add(new ModelBox(tail, 0, 13, -2.0F, -2.5F, 0.0F, 4, 5, 11, 0.0F, false));

        tailFin = new ModelRenderer(this);
        tailFin.setRotationPoint(0.0F, 0.0F, 9.0F);
        tail.addChild(tailFin);
        tailFin.cubeList.add(new ModelBox(tailFin, 0, 29, -5.0F, -0.5F, 0.0F, 10, 1, 6, 0.0F, false));
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    { main.render(f5); }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.tail.rotateAngleX = 0;
        this.tailFin.rotateAngleX = 0;

        AbstractFish fish = (AbstractFish) entityIn;
        float swim = MathHelper.cos((ageInTicks) * 0.3F);

        this.main.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
        this.main.rotateAngleY = netHeadYaw * (float) (Math.PI / 180.0);

        if ((fish.motionX * fish.motionX + fish.motionY * fish.motionY + fish.motionZ * fish.motionZ > 0.01F))
        {
            this.main.rotateAngleX -= 0.05F * swim;
            this.tail.rotateAngleX = -0.2F * swim;
            this.tailFin.rotateAngleX = -0.3F * swim;
        }
    }
}
