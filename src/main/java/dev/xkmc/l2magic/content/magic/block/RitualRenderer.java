package dev.xkmc.l2magic.content.magic.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.math.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RitualRenderer implements BlockEntityRenderer<RitualTE> {

	public RitualRenderer(BlockEntityRendererProvider.Context dispatcher) {

	}

	@Override
	public void render(RitualTE te, float partial, PoseStack matrix, MultiBufferSource buffer, int light, int overlay) {
		RenderUtils.renderItemAbove(te.getItem(0), 1.5, te.getLevel(), partial, matrix, buffer, light, overlay);
	}

}
