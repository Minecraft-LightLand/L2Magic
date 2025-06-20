package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public record OrientedTextureRenderer(
		ResourceLocation texture
) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void render(LMProjectile e, LMProjectileRenderer<?> r, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		pose.pushPose();
		pose.translate(0.0F, e.getBbHeight() / 2.0F, 0.0F);
		ProjTypeHolder.wrap(new LMProjectileType(texture)).create(r, e, pose, pTick);
		pose.popPose();
	}

}
