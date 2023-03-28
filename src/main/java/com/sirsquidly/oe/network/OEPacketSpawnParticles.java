package com.sirsquidly.oe.network;

import io.netty.buffer.ByteBuf;

import com.sirsquidly.oe.Main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OEPacketSpawnParticles implements IMessage
{
	private int particleId;
	private double x;
	private double y;
	private double z;
	private double xSpeed;
	private double ySpeed;
	private double zSpeed;
	private int count = 1;
	private int[] parameters;

	public OEPacketSpawnParticles() {}

	public OEPacketSpawnParticles(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
	{
		this.particleId = particleId;
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		this.xSpeed = speedX;
		this.ySpeed = speedY;
		this.zSpeed = speedZ;
		this.parameters = parameters;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		particleId = buf.readInt();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		xSpeed = buf.readDouble();
		ySpeed = buf.readDouble();
		zSpeed = buf.readDouble();
		
		parameters = new int[ByteBufUtils.readVarInt(buf, 5)];
		for (int i = 0; i < this.parameters.length; i++)
		{
			this.parameters[i] = ByteBufUtils.readVarInt(buf, 5);
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(particleId);	
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(xSpeed);
		buf.writeDouble(ySpeed);
		buf.writeDouble(zSpeed);
		
		ByteBufUtils.writeVarInt(buf, this.parameters.length, 5);
		for (int i = 0; i < this.parameters.length; i++) {
			ByteBufUtils.writeVarInt(buf, this.parameters[i], 5);
		}
	}

	public static class Handler implements IMessageHandler<OEPacketSpawnParticles, IMessage>
	{
		@Override
		public IMessage onMessage(OEPacketSpawnParticles message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				for (int i = 0; i < message.count; i++)
				{
					Main.proxy.spawnParticle(message.particleId, message.x, message.y, message.z, message.xSpeed, message.ySpeed, message.zSpeed, message.parameters);
				}	
			});
			return null;
		}
	}
}
