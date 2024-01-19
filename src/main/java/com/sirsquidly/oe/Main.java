package com.sirsquidly.oe;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sirsquidly.oe.common.CreativeTab;
import com.sirsquidly.oe.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraftforge.common.util.EnumHelper;
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
	public static Logger logger = LogManager.getLogger(Main.MOD_ID);
	
	public static final String MOD_ID = "oe";
	public static final String NAME = "Oceanic Expanse";
	public static final String CONFIG_NAME = "oceanic_expanse";
	public static final String VERSION = "1.1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.oe.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.sirsquidly.oe.proxy.CommonProxy";
	
	public static CreativeTabs OCEANEXPTAB = new CreativeTab("oceanictab");
	public static final EnumAction SPEAR = EnumHelper.addAction("SPEAR");
	
	/** Moved to get initialized as soon as possible, due to weird mod conflicts otherwise. Commented out as I wish to experiment with it later, but it's not necessary.*/
	//public static final EnumCreatureType WATER_MONSTER = EnumHelper.addCreatureType("WATER_MONSTER", IMob.class, 70, Material.WATER, false, false);
	
	@Instance 
	public static Main instance;
	
	@SidedProxy(clientSide = Main.CLIENT_PROXY_CLASS, serverSide = Main.COMMON_PROXY_CLASS)
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
	public void PostInit(FMLPostInitializationEvent event) {}
}