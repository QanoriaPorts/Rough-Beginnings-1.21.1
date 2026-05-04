package net.roughbeginnings.handsoff;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class HandsOffTags {

    public static class Blocks {
        public static final TagKey<Block> BREAKABLE = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("rough_beginnings", "breakable"));
    }

    public static class Items {
        public static final TagKey<Item> CANBREAK = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("rough_beginnings", "canbreak"));
    }
}
