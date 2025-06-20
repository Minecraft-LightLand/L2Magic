package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.ProjectileRenderer;
import dev.xkmc.fastprojectileapi.render.RenderableProjectileType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.Consumer;

public record LMProjectileType(ResourceLocation tex)
		implements RenderableProjectileType<LMProjectileType, LMProjectileType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, List<Ins> list) {
		VertexConsumer vc = buffer.getBuffer(LMRenderStates.solid(tex));
		for (var e : list) {
			e.tex(vc);
		}
	}

	@Override
	public void create(Consumer<Ins> consumer, ProjectileRenderer<?> r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		pose.mulPose(r.cameraOrientation());
		pose.mulPose(Axis.YP.rotationDegrees(180.0F));
		PoseStack.Pose mat = pose.last();
		Matrix4f m4 = new Matrix4f(mat.pose());
		Matrix3f m3 = new Matrix3f(mat.normal());
		consumer.accept(new Ins(m3, m4));
	}

	public record Ins(Matrix3f m3, Matrix4f m4) {

		public void tex(VertexConsumer vc) {
			vertex(vc, m4, m3, 0, 0, 0, 1);
			vertex(vc, m4, m3, 1, 0, 1, 1);
			vertex(vc, m4, m3, 1, 1, 1, 0);
			vertex(vc, m4, m3, 0, 1, 0, 0);
		}

		private static void vertex(VertexConsumer vc, Matrix4f m4, Matrix3f m3, float x, int y, int u, int v) {
			vc.addVertex(m4, x - 0.5F, y - 0.5F, 0.0F).setUv(u, v);
		}

	}

}