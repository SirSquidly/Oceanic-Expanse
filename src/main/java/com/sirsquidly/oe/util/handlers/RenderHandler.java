package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.entity.*;
import com.sirsquidly.oe.entity.item.*;
import com.sirsquidly.oe.tileentity.*;
import com.sirsquidly.oe.client.render.entity.*;
import com.sirsquidly.oe.client.render.tileentity.*;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHandler 
{
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCod.class, RenderCod::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySalmon.class, RenderSalmon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPufferfish.class, RenderPufferfish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTropicalFish.class, RenderTropicalFish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, RenderTurtle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDolphin.class, RenderDolphin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDrowned.class, RenderDrowned::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityGlowSquid.class, RenderGlowSquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySquid.class, RenderBabySquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyGlowSquid.class, RenderBabyGlowSquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCrab.class, RenderCrab::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityClam.class, RenderClam::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDrownedSummon.class, RenderDrownedSummon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLobster.class, RenderLobster::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOEBoat.class, RenderOEBoat::new);	
		RenderingRegistry.registerEntityRenderingHandler(EntityGlowItemFrame.class, RenderGlowItemFrame::new);	
		RenderingRegistry.registerEntityRenderingHandler(EntityPickled.class, RenderPickled::new);	
		RenderingRegistry.registerEntityRenderingHandler(EntityTropicalSlime.class, RenderTropicalSlime::new);	
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);	
		RenderingRegistry.registerEntityRenderingHandler(EntityConduitEye.class, RenderConduitEye::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderwaterTNTPrimed.class, RenderUnderwaterTNT::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileConduit.class, new RenderConduit());

		ClientRegistry.bindTileEntitySpecialRenderer(TileStasis.class, new RenderStasis());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePickledSkull.class, new RenderPickledSkull());
	}
}