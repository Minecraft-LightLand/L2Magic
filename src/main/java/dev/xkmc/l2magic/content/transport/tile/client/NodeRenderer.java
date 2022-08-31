package dev.xkmc.l2magic.content.transport.tile.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.math.RenderUtils;
import dev.xkmc.l2magic.content.transport.tile.base.CoolDownType;
import dev.xkmc.l2magic.content.transport.tile.base.IRenderableNode;
import dev.xkmc.l2magic.content.transport.tools.LinkerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class NodeRenderer<T extends BlockEntity & IRenderableNode> implements BlockEntityRenderer<T> {

	public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

	public static void renderLightBeam(PoseStack mat, MultiBufferSource vcp, ResourceLocation id, float t1,
									   BeamRenderer br, double x, double y, double z) {
		double xz = Math.sqrt(x * x + z * z);
		float len = (float) Math.sqrt(xz * xz + y * y);
		mat.pushPose();
		mat.mulPose(Vector3f.YN.rotation((float) (Math.atan2(z, x) - Math.PI / 2)));
		mat.mulPose(Vector3f.XN.rotation((float) (Math.atan2(y, xz))));
		mat.mulPose(Vector3f.ZP.rotationDegrees(t1 * 4.5f));
		float t2 = t1 * 0.5f;
		float h = 0.05f;
		br.setUV(0, 1, t2, t2 + len / h);
		br.drawCube(mat, vcp.getBuffer(RenderType.beaconBeam(id, false)), 0, len, h);
		mat.popPose();
	}

	public NodeRenderer(BlockEntityRendererProvider.Context dispatcher) {
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		float coolDown = Math.max(0, entity.getCoolDown() - partialTick);
		int max = Math.max(1, entity.getMaxCoolDown());
		float percentage = Mth.clamp(coolDown / max, 0, 1);
		Level level = entity.getLevel();
		long gameTime = level == null ? 0 : level.getGameTime();
		float time = Math.floorMod(gameTime, 80L) + partialTick;
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.5D, 0.5D);
		BeamRenderer br = new BeamRenderer();
		for (BlockPos target : entity.target()) {
			BlockPos p = target.subtract(entity.getBlockPos());
			int x = p.getX();
			int y = p.getY();
			int z = p.getZ();
			CoolDownType type = entity.getType(target);
			float hue = type == CoolDownType.GREEN ? 0.67f : 0;
			float sat = type == CoolDownType.GREY ? 0 : percentage;
			float bright = type == CoolDownType.GREY ? 0.5f : (percentage + 1) * 0.5f;
			br.setColorHSB(hue, sat, bright);
			renderLightBeam(poseStack, source, BEAM_TEXTURE, time, br, x, y, z);
		}
		ItemStack linker = Proxy.getPlayer().getMainHandItem();
		if (linker.getItem() instanceof LinkerItem) {
			BlockPos pos = LinkerItem.getPos(linker);
			if (pos != null && pos.equals(entity.getBlockPos())) {
				br.setColorHSB(0, 0, 0.5f);
				Vec3 p = Proxy.getPlayer().getEyePosition(partialTick);
				renderLightBeam(poseStack, source, BEAM_TEXTURE, time, br, p.x, p.y, p.z);
			}
		}
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
