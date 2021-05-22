package com.sirsquidly.oe.proxy;

import com.sirsquidly.oe.init.OEEntities;
import com.sirsquidly.oe.util.handlers.RenderHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void preInitRegisteries(FMLPreInitializationEvent event)
	{
		OEEntities.registerEntities();
		RenderHandler.registerEntityRenders();
	}
	
	public void initRegistries(FMLInitializationEvent event)
	{
		OEEntities.registerEntitySpawns();
		SoundHandler.registerSounds();
	}
	
	public void registerItemRenderer(Item item, int meta, String id){}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
}
