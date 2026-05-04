package net.roughbeginnings.handsoff;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandsOffTagCache {

    private static final List<TagKey<Block>> MINEABLE_TAGS = new ArrayList<>();
    private static final List<TagKey<Item>> TOOL_TAGS = new ArrayList<>();
    private static final Map<TagKey<Block>, TagKey<Item>> PREFERRED_MAP = new HashMap<>();

    public static void refreshCache() {
        MINEABLE_TAGS.clear();
        TOOL_TAGS.clear();
        PREFERRED_MAP.clear();

        BuiltInRegistries.BLOCK.getTagNames().forEach(tagKey -> {
            if (tagKey.location().getPath().contains("mineable")) {
                MINEABLE_TAGS.add(tagKey);
            }
        });

        BuiltInRegistries.ITEM.getTagNames().forEach(tagKey -> {
            ResourceLocation loc = tagKey.location();
            if ("c".equals(loc.getNamespace()) && loc.getPath().startsWith("tools/")) {
                TOOL_TAGS.add(tagKey);
            }
        });

        buildPreferredMap();
    }

    private static void buildPreferredMap() {
        for (TagKey<Block> blockTag : MINEABLE_TAGS) {
            String path = blockTag.location().getPath();
            String keyword = path.substring(path.lastIndexOf('/') + 1);
            String pluralKeyword = keyword + "s";

            for (TagKey<Item> itemTag : TOOL_TAGS) {
                String itemPath = itemTag.location().getPath();
                if (itemPath.endsWith("/" + keyword) || itemPath.endsWith("/" + pluralKeyword)) {
                    PREFERRED_MAP.put(blockTag, itemTag);
                    break;
                }
            }
        }
    }

    public static Map<TagKey<Block>, TagKey<Item>> getPreferredMap() {
        return PREFERRED_MAP;
    }
}
