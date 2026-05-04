package net.roughbeginnings.block;

import net.roughbeginnings.block.enums.RockVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RockBlock extends Block {

    public static final EnumProperty<RockVariants> ROCK_TYPE = EnumProperty.create("type", RockVariants.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);

    public RockBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ROCK_TYPE, RockVariants.LARGE));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        return belowState.isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        net.minecraft.util.RandomSource random = ctx.getLevel().getRandom();
        int roll = random.nextInt(14);
        RockVariants variant;
        if (roll < 6) variant = RockVariants.SMALL;
        else if (roll < 10) variant = RockVariants.MEDIUM;
        else if (roll < 13) variant = RockVariants.LARGE;
        else variant = RockVariants.EXTRA_LARGE;

        Direction facing = Direction.from2DDataValue(random.nextInt(4));

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(ROCK_TYPE, variant);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        builder.add(FACING, ROCK_TYPE);
    }

    public void cycleState(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.cycle(ROCK_TYPE), Block.UPDATE_CLIENTS);
    }
}
