package com.sirsquidly.oe.event;

import com.sirsquidly.oe.capabilities.CapabilityNautilusCharge;
import com.sirsquidly.oe.capabilities.CapabilityRiptide;
import com.sirsquidly.oe.common.entity.EntityNautilus;
import com.sirsquidly.oe.network.OEPacketHandler;
import com.sirsquidly.oe.network.OEPacketHoldingSpace;
import com.sirsquidly.oe.network.OEPacketRiptide;
import com.sirsquidly.oe.util.CapabilityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CapabilitiesEvent
{
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(CapabilityRiptide.ID, new CapabilityRiptide.Provider(new CapabilityRiptide.RiptideMethods(), CapabilityRiptide.RIPTIDE_CAP, null));
            event.addCapability(CapabilityNautilusCharge.ID, new CapabilityNautilusCharge.Provider(new CapabilityNautilusCharge.NautilusChargeMethods(), CapabilityNautilusCharge.NAUTILUS_CHARGE_CAP, null));
        }
    }

    /** This informs the server that the Player has pressed Space. */
    @SubscribeEvent
    public static void onUpdateJump(InputUpdateEvent event)
    {
        boolean isJumping = event.getMovementInput().jump;

        if (isJumping != CapabilityUtil.getHoldingSpace(event.getEntityPlayer()))
        {
            OEPacketHandler.CHANNEL.sendToServer(new OEPacketHoldingSpace(event.getEntityPlayer().getEntityId(), isJumping));
            CapabilityUtil.setHoldingSpace(event.getEntityPlayer(), isJumping);
        }
    }

    /** Bonus security check */
    @SubscribeEvent
    public static void PlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = event.player;

            if (player.hasCapability(CapabilityRiptide.RIPTIDE_CAP, null))
            {
                CapabilityRiptide.ICapabilityRiptide capWindCharge = player.getCapability(CapabilityRiptide.RIPTIDE_CAP, null);

                if (capWindCharge.getRiptideTimer() > 0)
                {
                    capWindCharge.setRiptideTimer(capWindCharge.getRiptideTimer() - 1);

                    if (player.collidedHorizontally) haultRiptide(player);

                    if (capWindCharge.getRiptideTimer() <= 0) OEPacketHandler.CHANNEL.sendToAllTracking(new OEPacketRiptide(player.getEntityId(), false), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 0.0D));
                }
            }

            if (player.getRidingEntity() instanceof EntityNautilus)
            {
                if (CapabilityUtil.getHoldingSpace(player) && ((EntityNautilus) player.getRidingEntity()).getDashCooldown() == 0) CapabilityUtil.alterChargeStrength(player, 0.1F);
            }
        }
    }

    /** Bonus security check */
    public static void haultRiptide(EntityPlayer player)
    {
        player.motionX = 0;
        player.motionY = 0;
        player.motionZ = 0;
        CapabilityRiptide.ICapabilityRiptide capWindCharge = player.getCapability(CapabilityRiptide.RIPTIDE_CAP, null);

        capWindCharge.setRiptideTimer(0);
        OEPacketHandler.CHANNEL.sendToAllTracking(new OEPacketRiptide(player.getEntityId(), false), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 0.0D));
    }
}