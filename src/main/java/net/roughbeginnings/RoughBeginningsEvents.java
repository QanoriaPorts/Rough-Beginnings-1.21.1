package net.roughbeginnings;

import net.roughbeginnings.data.DataLoader;
import net.roughbeginnings.init.ItemInit;
import net.roughbeginnings.network.packet.SieveDropPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RoughBeginningsMod.MODID)
public class RoughBeginningsEvents {

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new DataLoader());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SieveDropPacket(RoughBeginningsMod.SIEVE_DROP_TEMPLATES));
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        ServerLevel level = event.getLevel();
        BlockState state = event.getState();
        float chance;

        if (state.is(Blocks.SHORT_GRASS)) {
            chance = 0.10f;
        } else if (state.is(Blocks.TALL_GRASS)) {
            chance = 0.15f;
        } else if (state.is(Blocks.FERN)) {
            chance = 0.10f;
        } else if (state.is(Blocks.LARGE_FERN)) {
            chance = 0.15f;
        } else {
            return;
        }

        if (level.getRandom().nextFloat() < chance) {
            ItemStack stack = new ItemStack(ItemInit.PLANT_FIBER.get(), 1);
            ItemEntity itemEntity = new ItemEntity(level,
                    event.getPos().getX() + 0.5,
                    event.getPos().getY() + 0.5,
                    event.getPos().getZ() + 0.5,
                    stack);
            itemEntity.setDefaultPickUpDelay();
            event.getDrops().add(itemEntity);
        }
    }
}
