package com.sirsquidly.oe.proxy;

import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class CommonProxy
{
	public void initRegistries(FMLInitializationEvent event)
	{
		SoundHandler.registerSounds();
	}
	
	public void registerItemRenderer(Item item, int meta, String id){}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
}
