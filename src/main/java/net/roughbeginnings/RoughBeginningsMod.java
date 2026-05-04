package net.roughbeginnings;

import net.roughbeginnings.config.RoughBeginningsConfig;
import net.roughbeginnings.data.SieveDropTemplate;
import net.roughbeginnings.init.BlockEntityInit;
import net.roughbeginnings.init.BlockInit;
import net.roughbeginnings.init.EntityInit;
import net.roughbeginnings.init.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

@Mod(RoughBeginningsMod.MODID)
public class RoughBeginningsMod {

    public static final String MODID = "rough_beginnings";

    public static final List<SieveDropTemplate> SIEVE_DROP_TEMPLATES = new ArrayList<>();

    public RoughBeginningsMod(IEventBus modEventBus, ModContainer modContainer) {
        BlockInit.BLOCKS.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        ItemInit.CREATIVE_TABS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        EntityInit.ENTITIES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, RoughBeginningsConfig.SPEC);
        modEventBus.addListener(RoughBeginningsConfig::onLoad);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
