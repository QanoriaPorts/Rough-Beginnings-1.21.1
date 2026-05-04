package net.roughbeginnings.init;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.item.RockBlockItem;
import net.roughbeginnings.item.material.RoughBeginningsToolMaterials;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RoughBeginningsMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, RoughBeginningsMod.MODID);

    public static final DeferredItem<SwordItem> FLINT_SWORD = ITEMS.register("flint_sword",
            () -> new SwordItem(RoughBeginningsToolMaterials.FLINT, new Item.Properties().attributes(SwordItem.createAttributes(RoughBeginningsToolMaterials.FLINT, 1, -2.4f))));
    public static final DeferredItem<ShovelItem> FLINT_SHOVEL = ITEMS.register("flint_shovel",
            () -> new ShovelItem(RoughBeginningsToolMaterials.FLINT, new Item.Properties().attributes(ShovelItem.createAttributes(RoughBeginningsToolMaterials.FLINT, 0f, -3.0f))));
    public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = ITEMS.register("flint_pickaxe",
            () -> new PickaxeItem(RoughBeginningsToolMaterials.FLINT, new Item.Properties().attributes(PickaxeItem.createAttributes(RoughBeginningsToolMaterials.FLINT, 0f, -2.8f))));
    public static final DeferredItem<AxeItem> FLINT_AXE = ITEMS.register("flint_axe",
            () -> new AxeItem(RoughBeginningsToolMaterials.FLINT, new Item.Properties().attributes(AxeItem.createAttributes(RoughBeginningsToolMaterials.FLINT, 1.0f, -3.2f))));
    public static final DeferredItem<HoeItem> FLINT_HOE = ITEMS.register("flint_hoe",
            () -> new HoeItem(RoughBeginningsToolMaterials.FLINT, new Item.Properties().attributes(HoeItem.createAttributes(RoughBeginningsToolMaterials.FLINT, 0f, -3.0f))));

    public static final DeferredItem<Item> PLANT_FIBER = ITEMS.register("plant_fiber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLANT_STRING = ITEMS.register("plant_string", () -> new Item(new Item.Properties()));

    public static final DeferredItem<RockBlockItem> ROCK_ITEM = ITEMS.register("rock",
            () -> new RockBlockItem(BlockInit.ROCK.get(), new Item.Properties()));
    public static final DeferredItem<?> CRAFTING_ROCK_ITEM = ITEMS.registerSimpleBlockItem(BlockInit.CRAFTING_ROCK);
    public static final DeferredItem<?> SIEVE_ITEM = ITEMS.registerSimpleBlockItem(BlockInit.SIEVE);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EARLYSTAGE_TAB = CREATIVE_TABS.register("item_group", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(FLINT_AXE.get()))
            .title(Component.translatable("item.rough_beginnings.item_group"))
            .displayItems((parameters, output) -> {
                ItemInit.ITEMS.getEntries().forEach(holder -> output.accept(holder.get()));
            })
            .build());
}
