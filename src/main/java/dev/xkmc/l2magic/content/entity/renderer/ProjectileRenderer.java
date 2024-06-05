package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public interface ProjectileRenderer {

	ResourceLocation getTexture();

	void render(LMProjectile e, float pTick, PoseStack pose, MultiBufferSource buffer, int light);

}
