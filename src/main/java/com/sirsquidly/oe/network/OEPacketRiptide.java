package com.sirsquidly.oe.network;

import com.sirsquidly.oe.capabilities.CapabilityRiptide;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** This is used so the Server can tell Clients when the Riptide Capability is altered, so animations are displayed. */
public class OEPacketRiptide  implements IMessage
{
    private int entityId;
    private boolean isActive;

    public OEPacketRiptide() {}

    public OEPacketRiptide(int entityId, boolean booleanAnimate)
    {
        this.entityId = entityId;
        this.isActive = booleanAnimate;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        isActive = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeBoolean(isActive);
    }

    public static class Handler implements IMessageHandler<OEPacketRiptide, IMessage>
    {
        @Override
        public IMessage onMessage(OEPacketRiptide message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
                    {
                        Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                        if(player.hasCapability(CapabilityRiptide.RIPTIDE_CAP, null))
                        {
                            CapabilityRiptide.ICapabilityRiptide riptide = player.getCapability(CapabilityRiptide.RIPTIDE_CAP, null);
                            riptide.setRiptideAnimate(message.isActive);
                        }
                        //Helper.setRiptideCapability(player, message.isActive);
                    }
            );
            return null;
        }
    }
}