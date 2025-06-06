package com.sirsquidly.oe.network;

import com.sirsquidly.oe.Main;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class OEPacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MOD_ID);
	
	public static void registerMessages()
	{
		int messageId = 0;
		
		CHANNEL.registerMessage(OEPacketSpawnParticles.Handler.class, OEPacketSpawnParticles.class, messageId++, Side.CLIENT);
		CHANNEL.registerMessage(OEPacketRiptide.Handler.class, OEPacketRiptide.class, messageId++, Side.CLIENT);
	}
}
