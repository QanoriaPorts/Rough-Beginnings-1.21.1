package net.roughbeginnings.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class RoughBeginningsConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue REQUIRE_PREFERRED_TOOL;
    private static final ModConfigSpec.BooleanValue ALWAYS_ALLOW_INSTA_BREAK;
    private static final ModConfigSpec.BooleanValue FORCE_IN_CREATIVE;

    static {
        BUILDER.push("blockBreaking");
        BUILDER.comment(
                "Hands Off block-breaking rules.",
                "Blocks listed in the data tag rough_beginnings:breakable can always be broken by hand.",
                "Items listed in the data tag rough_beginnings:canbreak count as tools that bypass the rule.",
                "Edit those tags via a datapack to customize which blocks can be hand-broken."
        );

        REQUIRE_PREFERRED_TOOL = BUILDER
                .comment("If true, you have to use the preferred tool to harvest blocks (default: false)")
                .define("requirePreferredTool", false);

        ALWAYS_ALLOW_INSTA_BREAK = BUILDER
                .comment("If true, you can always break insta-breakable blocks by hand (default: true)")
                .define("alwaysAllowInstaBreak", true);

        FORCE_IN_CREATIVE = BUILDER
                .comment("If true, you also have to use a tool to break blocks in creative mode (default: false)")
                .define("forceInCreative", false);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public boolean requirePreferredTool = false;
    public boolean alwaysAllowInstaBreak = true;
    public boolean forceInCreative = false;

    public static final RoughBeginningsConfig CONFIG = new RoughBeginningsConfig();

    @SubscribeEvent
    public static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        CONFIG.requirePreferredTool = REQUIRE_PREFERRED_TOOL.get();
        CONFIG.alwaysAllowInstaBreak = ALWAYS_ALLOW_INSTA_BREAK.get();
        CONFIG.forceInCreative = FORCE_IN_CREATIVE.get();
    }
}
