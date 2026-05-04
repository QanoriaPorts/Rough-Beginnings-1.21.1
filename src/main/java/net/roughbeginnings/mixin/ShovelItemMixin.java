package net.roughbeginnings.mixin;

import net.roughbeginnings.block.RockBlock;
import net.roughbeginnings.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void useOnMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> info, Level level, BlockPos blockPos, BlockState blockState) {
        if (blockState.is(BlockInit.ROCK.get())) {
            if (!level.isClientSide()) {
                ((RockBlock) blockState.getBlock()).cycleState(blockState, level, blockPos);
            }
            info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}
