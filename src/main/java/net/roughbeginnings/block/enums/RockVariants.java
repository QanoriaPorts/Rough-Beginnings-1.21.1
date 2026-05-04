package net.roughbeginnings.block.enums;

import net.minecraft.util.StringRepresentable;

public enum RockVariants implements StringRepresentable {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    EXTRA_LARGE("extra_large");

    private final String name;

    RockVariants(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
