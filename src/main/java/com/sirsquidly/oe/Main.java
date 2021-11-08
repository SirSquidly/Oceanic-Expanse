package com.sirsquidly.oe;

import java.io.File;
import com.sirsquidly.oe.common.CreativeTab;
import com.sirsquidly.oe.proxy.CommonProxy;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static File config;
	
	public static final String MOD_ID = "oe";
	public static final String NAME = "Oceanic Expanse";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2,)";
	public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.oe.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.sirsquidly.oe.proxy.CommonProxy";
	
	public static CreativeTabs OCEANEXPTAB = new CreativeTab("oceanictab");
	
	@Instance 
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		proxy.preInitRegisteries(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRegistries(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{}
}