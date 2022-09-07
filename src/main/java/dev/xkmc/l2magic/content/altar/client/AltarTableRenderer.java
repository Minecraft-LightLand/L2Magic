package dev.xkmc.l2magic.content.altar.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.xkmc.l2magic.content.altar.tile.structure.AltarTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class AltarTableRenderer<T extends AltarTableBlockEntity> implements BlockEntityRenderer<T> {

	public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

	public static void renderLightBeam(PoseStack mat, MultiBufferSource vcp, ResourceLocation id, float t1,
									   BeamRenderer br, double x, double y, double z) {
		double xz = Math.sqrt(x * x + z * z);
		float len = (float) Math.sqrt(xz * xz + y * y);
		mat.mulPose(Vector3f.YN.rotation((float) (Math.atan2(z, x) - Math.PI / 2)));
		mat.mulPose(Vector3f.XN.rotation((float) (Math.atan2(y, xz))));
		mat.mulPose(Vector3f.ZP.rotationDegrees(t1 * 4.5f));
		float t2 = t1 * 0.5f;
		float h = 0.05f;
		br.setUV(0, 1, t2, t2 + len / h);
		br.drawCube(mat, vcp.getBuffer(RenderType.beaconBeam(id, false)), 0, len, h);
	}

	public AltarTableRenderer(BlockEntityRendererProvider.Context dispatcher) {
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		BlockPos target = entity.getCorePos();
		if (target == null) return;
		Level level = entity.getLevel();
		long gameTime = level == null ? 0 : level.getGameTime();
		float time = Math.floorMod(gameTime, 80L) + partialTick;
		poseStack.pushPose();
		poseStack.translate(0.5D, 1.5D, 0.5D);
		BeamRenderer br = new BeamRenderer();
		br.setColorHSB(0, 0, 1);
		BlockPos p = target.subtract(entity.getBlockPos());
		int x = p.getX();
		int y = p.getY() - 1;
		int z = p.getZ();
		poseStack.pushPose();
		renderLightBeam(poseStack, source, BEAM_TEXTURE, time, br, x, y, z);
		poseStack.popPose();
		poseStack.popPose();
	}

	public boolean shouldRenderOffScreen(T entity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 64;
	}

}
