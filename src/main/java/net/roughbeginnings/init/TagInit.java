package net.roughbeginnings.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class TagInit {

    public static final TagKey<Item> ROCK_ITEMS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("rough_beginnings", "rock_items"));

    public static final TagKey<Block> ROCK_FEATURE_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("rough_beginnings", "rock_feature_blocks"));

    public static final TagKey<Biome> ROCK_FEATURE_BIOMES = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("rough_beginnings", "rock_feature_biomes"));
}
