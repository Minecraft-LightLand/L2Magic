package dev.xkmc.l2magic.content.common.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.xkmc.l2magic.content.common.render.LLRenderState;
import dev.xkmc.l2magic.content.magic.render.SpellComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {

	public SpellEntityRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public void render(SpellEntity entity, float yRot, float partial, PoseStack matrix, MultiBufferSource buffer, int light) {
		SpellComponent.RenderHandle handle = new SpellComponent.RenderHandle(matrix, buffer.getBuffer(LLRenderState.getSpell()), entity.tickCount + partial, light);
		matrix.pushPose();
		matrix.translate(0, 1.5f, 0);
		matrix.mulPose(Vector3f.YP.rotationDegrees(-entity.getYRot()));
		matrix.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		float scale = entity.getSize(partial);
		matrix.scale(scale / 16f, scale / 16f, scale / 16f);
		SpellComponent component = entity.getComponent();
		if (component != null) {
			component.render(handle);
		}
		matrix.popPose();
		super.render(entity, yRot, partial, matrix, buffer, light);

	}

	@Override
	public ResourceLocation getTextureLocation(SpellEntity entity) {
		return new ResourceLocation("");
	}
}
