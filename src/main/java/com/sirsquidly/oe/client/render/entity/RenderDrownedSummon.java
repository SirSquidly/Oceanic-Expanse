package com.sirsquidly.oe.client.render.entity;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.entity.EntityDrownedSummon;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDrownedSummon extends RenderDrowned<EntityDrownedSummon>
{
	public static final ResourceLocation DROWNED_ZOMBIE_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned.png");
	public static final ResourceLocation DROWNED_SUMMON_TEXTURE = new ResourceLocation(Main.MOD_ID + ":textures/entities/zombie/drowned_shipmate/drowned_shipmate.png");

	public RenderDrownedSummon(RenderManager manager)
    { super(manager); }
	
	protected ResourceLocation getEntityTexture(EntityDrownedSummon entity)
	{ return ConfigHandler.entity.drowned.drownedCaptain.enableDrownedCaptainTexture ? DROWNED_SUMMON_TEXTURE : DROWNED_ZOMBIE_TEXTURE; }
}