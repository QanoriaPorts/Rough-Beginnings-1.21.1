package net.roughbeginnings.block;

import net.roughbeginnings.block.entity.SieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public class SieveBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SieveBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
        if (blockEntity instanceof SieveBlockEntity sieveBlockEntity) {
            Container inventory = sieveBlockEntity;
            ItemStack blockStack = inventory.getItem(0);
            if (blockStack.isEmpty()) {
                if (inventory.canPlaceItem(0, itemStack)) {
                    if (!level.isClientSide()) {
                        inventory.setItem(0, new ItemStack(itemStack.getItem(), 1));
                        if (!player.isCreative())
                            itemStack.shrink(1);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
                return ItemInteractionResult.FAIL;
            } else {
                sieveBlockEntity.sieve();
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SieveBlockEntity sieveBlockEntity) {
            Containers.dropContents(level, pos, (Container) sieveBlockEntity);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!level.isClientSide() && !level.getBlockState(pos.above()).isAir() && level.getBlockEntity(pos) instanceof SieveBlockEntity sieveBlockEntity) {
            Containers.dropContents(level, pos, (Container) sieveBlockEntity);
            sieveBlockEntity.clearContent();
        }
        super.neighborChanged(state, level, pos, sourceBlock, sourcePos, notify);
    }
}
