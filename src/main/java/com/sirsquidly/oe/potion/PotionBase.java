package com.sirsquidly.oe.potion;

import com.sirsquidly.oe.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionBase extends Potion
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/potion_effects.png");
	
	public PotionBase(String name, boolean isBadEffectIn, int liquidColorIn, int icon)
	{
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName("effect." + name);
		this.setRegistryName(name);
		this.setIconIndex(icon % 8, icon / 8);
	}
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
		
		if(entity instanceof EntityPlayer)
        {
            entity.setPosition(entity.prevPosX, entity.posY, entity.prevPosZ);
        }
    }
	
	@Override
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		return super.getStatusIconIndex();
	}
}