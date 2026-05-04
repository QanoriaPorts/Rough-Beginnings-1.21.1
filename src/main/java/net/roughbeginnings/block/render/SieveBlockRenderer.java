package net.roughbeginnings.block.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.roughbeginnings.block.entity.SieveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SieveBlockRenderer implements BlockEntityRenderer<SieveBlockEntity> {

    public SieveBlockRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(SieveBlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (blockEntity != null && !blockEntity.isEmpty()) {
            matrices.pushPose();
            double height = switch (blockEntity.getSieveCount()) {
                case 0 -> 0.7D;
                case 1 -> 0.532D;
                case 2 -> 0.366D;
                default -> 0.2D;
            };
            matrices.translate(0.5D, height, 0.5D);
            matrices.scale(3.0f, 3.0f, 3.0f);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    blockEntity.getItem(0),
                    ItemDisplayContext.GROUND,
                    LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above()),
                    overlay,
                    matrices,
                    vertexConsumers,
                    blockEntity.getLevel(),
                    (int) blockEntity.getBlockPos().asLong());
            matrices.popPose();
        }
    }
}
