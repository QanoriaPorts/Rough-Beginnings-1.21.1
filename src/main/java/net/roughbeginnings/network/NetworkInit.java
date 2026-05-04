package net.roughbeginnings.network;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.network.packet.SieveDropPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = RoughBeginningsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkInit {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SieveDropPacket.TYPE, SieveDropPacket.STREAM_CODEC, NetworkInit::handleSieveDrop);
    }

    private static void handleSieveDrop(SieveDropPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            RoughBeginningsMod.SIEVE_DROP_TEMPLATES.clear();
            RoughBeginningsMod.SIEVE_DROP_TEMPLATES.addAll(payload.templates());
        });
    }
}
