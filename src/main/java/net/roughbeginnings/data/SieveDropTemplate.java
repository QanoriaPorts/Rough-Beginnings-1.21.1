package net.roughbeginnings.data;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SieveDropTemplate {

    private final Item blockItem;
    private final List<Item> blockDrops;
    private final List<Float> dropChances;
    private final List<Integer> rollCount;

    public SieveDropTemplate(Item blockItem, List<Item> blockDrops, List<Float> dropChances, List<Integer> rollCount) {
        this.blockItem = blockItem;
        this.blockDrops = new ArrayList<>(blockDrops);
        this.dropChances = new ArrayList<>(dropChances);
        this.rollCount = new ArrayList<>(rollCount);
    }

    public Item getBlockItem() {
        return blockItem;
    }

    public List<Item> getBlockDrops() {
        return blockDrops;
    }

    public List<Integer> getRollCount() {
        return rollCount;
    }

    public List<Float> getDropChances() {
        return dropChances;
    }
}
