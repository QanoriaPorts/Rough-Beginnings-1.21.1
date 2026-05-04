package net.roughbeginnings.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.roughbeginnings.RoughBeginningsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DataLoader implements PreparableReloadListener {

    private static final Logger LOGGER = LogManager.getLogger("RoughBeginnings");

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler, ProfilerFiller applyProfiler, Executor prepExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> manager, prepExecutor)
                .thenCompose(barrier::wait)
                .thenAcceptAsync(this::apply, applyExecutor);
    }

    private void apply(ResourceManager manager) {
        RoughBeginningsMod.SIEVE_DROP_TEMPLATES.clear();

        manager.listResources("sieve_drops", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try (InputStream stream = resourceRef.open()) {
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (int u = 0; u < data.getAsJsonArray("drops").size(); u++) {
                    JsonObject data2 = (JsonObject) data.getAsJsonArray("drops").get(u);
                    if (BuiltInRegistries.BLOCK.get(ResourceLocation.parse(data2.get("block_id").getAsString())) == Blocks.AIR) {
                        LOGGER.warn("Block Id: " + data2.get("block_id").getAsString() + " is not a valid block id");
                        continue;
                    }

                    Item blockItem = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(data2.get("block_id").getAsString())).asItem();

                    List<Item> blockDrops = new ArrayList<>();
                    List<Float> dropChances = new ArrayList<>();
                    List<Integer> rollCount = new ArrayList<>();

                    if (GsonHelper.getAsBoolean(data2, "replace", false)) {
                        for (int i = 0; i < RoughBeginningsMod.SIEVE_DROP_TEMPLATES.size(); i++) {
                            if (RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getBlockItem() == blockItem) {
                                RoughBeginningsMod.SIEVE_DROP_TEMPLATES.remove(i);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < RoughBeginningsMod.SIEVE_DROP_TEMPLATES.size(); i++) {
                            if (RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getBlockItem() == blockItem) {
                                blockDrops.addAll(RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getBlockDrops());
                                dropChances.addAll(RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getDropChances());
                                rollCount.addAll(RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getRollCount());
                                RoughBeginningsMod.SIEVE_DROP_TEMPLATES.remove(i);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < data2.getAsJsonArray("block_drops").size(); i++) {
                        JsonObject data3 = (JsonObject) data2.getAsJsonArray("block_drops").get(i);
                        blockDrops.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(data3.get("item_id").getAsString())));
                        dropChances.add(data3.get("chance").getAsFloat());
                        rollCount.add(data3.get("rolls").getAsInt());
                    }
                    RoughBeginningsMod.SIEVE_DROP_TEMPLATES.add(new SieveDropTemplate(blockItem, blockDrops, dropChances, rollCount));
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });
    }
}
