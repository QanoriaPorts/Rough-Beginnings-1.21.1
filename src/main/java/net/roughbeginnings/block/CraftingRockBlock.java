package net.roughbeginnings.block;

import net.roughbeginnings.block.entity.CraftingRockBlockEntity;
import net.roughbeginnings.block.inventory.CraftingRockInventory;
import net.roughbeginnings.init.TagInit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class CraftingRockBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape BOTTOM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    public static final int CRAFT_HITS = 5;
    public static final int MAX_CRAFT_HITS = 70;

    public CraftingRockBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CraftingRockBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOTTOM_SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CraftingRockBlockEntity craftingRockBlockEntity) {
            if (Math.abs(hit.getLocation().y % 1) < 0.505D && Math.abs(hit.getLocation().y % 1) > 0.495D) {
                if (itemStack.is(TagInit.ROCK_ITEMS)) {
                    if (!craftingRockBlockEntity.isEmpty()) {
                        if (!level.isClientSide()) {
                            if (craftingRockBlockEntity.getCraftHits() - 1 <= 0) {
                                tryCraftItem(level, player, craftingRockBlockEntity);
                                craftingRockBlockEntity.setCraftHits(CRAFT_HITS + level.getRandom().nextInt(CRAFT_HITS / 2));
                            } else {
                                craftingRockBlockEntity.decreaseCraftHits(player);
                            }
                        }
                        level.playSound(player, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide());
                    }
                    return ItemInteractionResult.FAIL;
                }
                double xPos = hit.getLocation().x < 0D ? 1.0D + hit.getLocation().x % 1 : hit.getLocation().x % 1;
                double zPos = hit.getLocation().z < 0D ? 1.0D + hit.getLocation().z % 1 : hit.getLocation().z % 1;
                int slot = getSlot(xPos, zPos);
                if (craftingRockBlockEntity.getItem(slot).isEmpty() && !itemStack.isEmpty()) {
                    if (!level.isClientSide()) {
                        craftingRockBlockEntity.setItem(slot, new ItemStack(itemStack.getItem(), 1));
                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                        }
                        craftingRockBlockEntity.setCraftHits(CRAFT_HITS + level.getRandom().nextInt(CRAFT_HITS / 2));
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                } else if (!craftingRockBlockEntity.getItem(slot).isEmpty()) {
                    if (!level.isClientSide()) {
                        if (!player.isCreative()) {
                            player.getInventory().placeItemBackInInventory(craftingRockBlockEntity.getItem(slot));
                        }
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F,
                                ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        craftingRockBlockEntity.setItem(slot, ItemStack.EMPTY);
                        craftingRockBlockEntity.setCraftHits(CRAFT_HITS + level.getRandom().nextInt(CRAFT_HITS / 2));
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return ItemInteractionResult.FAIL;
    }

    private int getSlot(double x, double z) {
        int slot = 0;
        for (int i = 2; i >= 0; i--) {
            for (int u = 2; u >= 0; u--) {
                if (x > i * 0.33D && z > u * 0.33D) {
                    return slot;
                }
                slot++;
            }
        }
        return 0;
    }

    private void tryCraftItem(Level level, Player player, CraftingRockBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            CraftingRockInventory craftingInventory = null;
            Optional<RecipeHolder<CraftingRecipe>> optional = Optional.empty();
            for (int i = 0; i < 4; i++) {
                craftingInventory = new CraftingRockInventory(blockEntity, i);
                optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory.createRecipeInput(), level);
                if (optional.isPresent()) {
                    break;
                }
            }
            if (optional.isPresent()
                    && (optional.get().value().isSpecial() || !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || ((ServerPlayer) player).getRecipeBook().contains(optional.get()))) {
                ItemStack assembled = optional.get().value().assemble(craftingInventory.createRecipeInput(), level.registryAccess());
                blockEntity.clearContent();
                blockEntity.setItem(4, assembled);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CraftingRockBlockEntity craftingRockBlockEntity) {
            Containers.dropContents(level, pos, craftingRockBlockEntity);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        boolean shiftHeld = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (shiftHeld) {
            tooltip.add(Component.translatable("block.rough_beginnings.crafting_rock.tooltip"));
        } else {
            tooltip.add(Component.translatable("rough_beginnings.moreinfo.tooltip",
                            Component.translatable("rough_beginnings.tooltip.keyShift").withStyle(net.minecraft.ChatFormatting.GRAY))
                    .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

}
