package dev.xkmc.l2magic.content.transport.tile.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.math.RenderUtils;
import dev.xkmc.l2magic.content.transport.tile.base.IRenderableItemNode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemNodeRenderer<T extends BlockEntity & IRenderableItemNode> extends NodeRenderer<T> {

	public ItemNodeRenderer(BlockEntityRendererProvider.Context dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		super.render(entity, partialTick, poseStack, source, light, overlay);
		Level level = entity.getLevel();
		if (level != null && !entity.getItem().isEmpty()) {
			RenderUtils.renderItemAbove(entity.getItem(), 0.5, level, partialTick, poseStack, source, light, overlay);
		}
	}
}
