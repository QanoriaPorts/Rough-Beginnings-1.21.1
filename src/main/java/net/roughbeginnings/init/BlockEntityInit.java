package net.roughbeginnings.init;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.block.entity.CraftingRockBlockEntity;
import net.roughbeginnings.block.entity.SieveBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RoughBeginningsMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SieveBlockEntity>> SIEVE_ENTITY = BLOCK_ENTITIES.register("sieve_entity",
            () -> BlockEntityType.Builder.of(SieveBlockEntity::new, BlockInit.SIEVE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingRockBlockEntity>> CRAFTING_ROCK_ENTITY = BLOCK_ENTITIES.register("crafting_rock_entity",
            () -> BlockEntityType.Builder.of(CraftingRockBlockEntity::new, BlockInit.CRAFTING_ROCK.get()).build(null));
}
