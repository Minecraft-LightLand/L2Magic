package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class LMProjectileRenderer<T extends LMProjectile> extends EntityRenderer<T> {

	public LMProjectileRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		//TODO
		super.render(e, yaw, pTick, pose, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(T e) {
		//TODO
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
