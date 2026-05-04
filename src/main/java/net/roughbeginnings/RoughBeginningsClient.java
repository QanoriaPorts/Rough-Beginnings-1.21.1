package net.roughbeginnings;

import net.roughbeginnings.block.render.CraftingRockBlockRenderer;
import net.roughbeginnings.block.render.SieveBlockRenderer;
import net.roughbeginnings.init.BlockEntityInit;
import net.roughbeginnings.init.EntityInit;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = RoughBeginningsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RoughBeginningsClient {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.SIEVE_ENTITY.get(), SieveBlockRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.CRAFTING_ROCK_ENTITY.get(), CraftingRockBlockRenderer::new);
        event.registerEntityRenderer(EntityInit.THROWN_ROCK.get(), ThrownItemRenderer::new);
    }
}
