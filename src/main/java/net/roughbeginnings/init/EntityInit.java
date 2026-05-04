package net.roughbeginnings.init;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.entity.ThrownRock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, RoughBeginningsMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownRock>> THROWN_ROCK = ENTITIES.register("thrown_rock",
            () -> EntityType.Builder.<ThrownRock>of(ThrownRock::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_rock"));
}
