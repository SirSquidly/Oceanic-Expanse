package com.sirsquidly.oe.common.network;

import com.sirsquidly.oe.common.capabilities.CapabilityNautilusCharge;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** This is used so the Server can tell Clients when the Riptide Capability is altered, so animations are displayed. */
public class OEPacketHoldingSpace implements IMessage
{
    private int entityId;
    private boolean isJumping;

    public OEPacketHoldingSpace() {}

    public OEPacketHoldingSpace(int entityIdIn, boolean isJumping)
    {
        this.entityId = entityIdIn;
        this.isJumping = isJumping;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        isJumping = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeBoolean(isJumping);
    }

    public static class Handler implements IMessageHandler<OEPacketHoldingSpace, IMessage>
    {
        @Override
        public IMessage onMessage(OEPacketHoldingSpace message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;

            if (player.hasCapability(CapabilityNautilusCharge.NAUTILUS_CHARGE_CAP, null))
            {
                player.getCapability(CapabilityNautilusCharge.NAUTILUS_CHARGE_CAP, null).setHoldingSpace(message.isJumping);
            }
            return null;
        }
    }
}