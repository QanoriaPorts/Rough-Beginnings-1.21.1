package net.roughbeginnings.block.entity;

import net.roughbeginnings.RoughBeginningsMod;
import net.roughbeginnings.data.SieveDropTemplate;
import net.roughbeginnings.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SieveBlockEntity extends BlockEntity implements Container {

    private static final float SIEVE_CHANCE = 0.4f;
    private static final int SIEVE_THRESHOLD = 5;

    private final NonNullList<ItemStack> inventory;
    private int sieveCount;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SIEVE_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.clear();
        ContainerHelper.loadAllItems(tag, inventory, registries);
        sieveCount = tag.getInt("SieveCount");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, inventory, registries);
        tag.putInt("SieveCount", sieveCount);
    }

    public int getSieveCount() {
        return this.sieveCount;
    }

    public void refreshSieveCount() {
        this.sieveCount = 0;
    }

    public void sieve() {
        boolean advanced = level.random.nextFloat() < SIEVE_CHANCE;
        if (!advanced) {
            level.playSound(null, worldPosition, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 0.7f, 1.0f);
            spawnSieveParticles(2, false);
            return;
        }
        this.sieveCount++;
        if (this.sieveCount > SIEVE_THRESHOLD) {
            if (!level.isClientSide()) {
                for (int i = 0; i < RoughBeginningsMod.SIEVE_DROP_TEMPLATES.size(); i++) {
                    if (RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getBlockItem().equals(this.getItem(0).getItem())) {
                        SieveDropTemplate sieveDropTemplate = RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i);
                        for (int u = 0; u < sieveDropTemplate.getBlockDrops().size(); u++) {
                            for (int k = 0; k < sieveDropTemplate.getRollCount().get(u); k++) {
                                if (this.level.random.nextFloat() <= sieveDropTemplate.getDropChances().get(u)) {
                                    dropItem(sieveDropTemplate.getBlockDrops().get(u));
                                }
                            }
                        }
                        break;
                    }
                }
                spawnSieveParticles(15, true);
                this.clearContent();
            }
            level.playSound(null, worldPosition, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
            spawnSieveParticles(6, true);
            level.playSound(null, worldPosition, SoundEvents.COMPOSTER_FILL_SUCCESS, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    private void spawnSieveParticles(int count, boolean spawnBlockParticles) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        double cx = worldPosition.getX() + 0.5;
        double cy = worldPosition.getY() + 0.85;
        double cz = worldPosition.getZ() + 0.5;
        serverLevel.sendParticles(ParticleTypes.DUST_PLUME, cx, cy, cz, count, 0.2, 0.05, 0.2, 0.02);

        if (spawnBlockParticles) {
            ItemStack stack = this.getItem(0);
            if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState particleState = block.defaultBlockState();
                int blockCount = Math.max(2, count / 3);
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, particleState),
                        cx, worldPosition.getY() + 0.05, cz,
                        blockCount, 0.25, 0.0, 0.25, 0.0);
            }
        }
    }

    private void dropItem(Item item) {
        double d = level.random.nextFloat() * 0.7f + 0.15f;
        double e = level.random.nextFloat() * 0.7f + 0.06000000238418579 + 0.6;
        double g = level.random.nextFloat() * 0.7f + 0.15f;

        ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + d, worldPosition.getY() + e, worldPosition.getZ() + g, new ItemStack(item));
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        sendUpdate();
    }

    private void sendUpdate() {
        if (this.level != null) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(0);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.inventory, slot, 1);
        this.setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        this.setChanged();
        return ContainerHelper.takeItem(this.inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(0, stack);
        this.refreshSieveCount();
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (this.isEmpty() && this.level.getBlockState(worldPosition.above()).isAir()) {
            for (int i = 0; i < RoughBeginningsMod.SIEVE_DROP_TEMPLATES.size(); i++) {
                if (stack.getItem().equals(RoughBeginningsMod.SIEVE_DROP_TEMPLATES.get(i).getBlockItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
