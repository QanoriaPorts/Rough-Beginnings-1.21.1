package net.roughbeginnings.handsoff;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.config.RoughBeginningsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = RoughBeginningsMod.MODID)
public class HandsOffEvents {

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        if (RoughBeginningsConfig.CONFIG.requirePreferredTool) {
            HandsOffTagCache.refreshCache();
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        ItemStack stack = player.getMainHandItem();
        BlockState state = level.getBlockState(pos);

        if (RoughBeginningsConfig.CONFIG.alwaysAllowInstaBreak && state.getDestroySpeed(level, pos) == 0.0f) {
            return;
        }

        if (!RoughBeginningsConfig.CONFIG.forceInCreative && player.getAbilities().instabuild) {
            return;
        }

        boolean needsCorrectTool = false;

        if (RoughBeginningsConfig.CONFIG.requirePreferredTool) {
            boolean hasMineableTag = false;
            boolean hasCorrectTool = false;

            for (var entry : HandsOffTagCache.getPreferredMap().entrySet()) {
                TagKey<Block> mineableTag = entry.getKey();

                if (state.is(mineableTag)) {
                    hasMineableTag = true;
                    TagKey<Item> requiredToolTag = entry.getValue();

                    if (stack.is(requiredToolTag)) {
                        hasCorrectTool = true;
                        break;
                    }
                }
            }

            needsCorrectTool = hasMineableTag && !hasCorrectTool;
        }

        if (!state.is(HandsOffTags.Blocks.BREAKABLE) && (!stack.is(HandsOffTags.Items.CANBREAK) || needsCorrectTool)) {
            event.setCanceled(true);
        }
    }
}
