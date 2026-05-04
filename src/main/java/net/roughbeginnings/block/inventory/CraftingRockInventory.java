package net.roughbeginnings.block.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

import java.util.List;

public class CraftingRockInventory implements CraftingContainer {

    private final NonNullList<ItemStack> stacks;

    public CraftingRockInventory(Container inventory, int variant) {
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            this.stacks.set(getSlot(variant, i), inventory.getItem(i));
        }
    }

    public CraftingInput createRecipeInput() {
        return CraftingInput.of(3, 3, this.stacks);
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.stacks) {
            if (!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= this.getContainerSize()) {
            return ItemStack.EMPTY;
        }
        return this.stacks.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.stacks, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.stacks, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public List<ItemStack> getItems() {
        return List.copyOf(this.stacks);
    }

    @Override
    public void fillStackedContents(net.minecraft.world.entity.player.StackedContents finder) {
        for (ItemStack stack : this.stacks) {
            finder.accountSimpleStack(stack);
        }
    }

    private int getSlot(int variant, int i) {
        if (variant == 0) return i;
        if (variant == 1) return Math.abs(i - 8);
        if (variant == 2) {
            return switch (i) {
                case 0 -> 6;
                case 1 -> 3;
                case 2 -> 0;
                case 3 -> 7;
                case 4 -> 4;
                case 5 -> 1;
                case 6 -> 8;
                case 7 -> 5;
                case 8 -> 2;
                default -> 0;
            };
        }
        if (variant == 3) {
            return switch (i) {
                case 0 -> 2;
                case 1 -> 5;
                case 2 -> 8;
                case 3 -> 1;
                case 4 -> 4;
                case 5 -> 7;
                case 6 -> 0;
                case 7 -> 3;
                case 8 -> 6;
                default -> 0;
            };
        }
        return 0;
    }
}
