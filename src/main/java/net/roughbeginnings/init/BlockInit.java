package net.roughbeginnings.init;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.block.CraftingRockBlock;
import net.roughbeginnings.block.RockBlock;
import net.roughbeginnings.block.SieveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RoughBeginningsMod.MODID);

    public static final DeferredBlock<RockBlock> ROCK = BLOCKS.register("rock",
            () -> new RockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak()));

    public static final DeferredBlock<SieveBlock> SIEVE = BLOCKS.register("sieve",
            () -> new SieveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COMPOSTER)));

    public static final DeferredBlock<CraftingRockBlock> CRAFTING_ROCK = BLOCKS.register("crafting_rock",
            () -> new CraftingRockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f)));
}
