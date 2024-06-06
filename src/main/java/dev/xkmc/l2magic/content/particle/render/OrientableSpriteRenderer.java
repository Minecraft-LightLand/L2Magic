package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.core.LMOrientation;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface OrientableSpriteRenderer extends ParticleRenderer {

	ParticleRenderer inner();

	@Override
	default void onParticleInit(LMGenericParticle e) {
		inner().onParticleInit(e);
	}

	@Override
	default void onPostTick(LMGenericParticle e) {
		inner().onPostTick(e);
	}

	@Override
	default RenderTypePreset renderType() {
		return inner().renderType();
	}

	@Override
	default boolean specialRender(LMGenericParticle e, VertexConsumer vc, Camera camera, float pTick) {
		inner().specialRender(e, vc, camera, pTick);
		render(e, vc, camera, pTick);
		return true;
	}

	@Override
	default boolean needOrientation() {
		return true;
	}

	default Quaternionf rotation(LMGenericParticle e, float pTick) {
		LMOrientation ori = e.getOrientation();
		return new Quaternionf().rotateYXZ((float) ori.getRY(pTick), (float) ori.getRX(pTick), (float) ori.getRZ(pTick));
	}

	default void render(LMGenericParticle e, VertexConsumer vc, Camera cam, float pTick) {
		if (e.age() == 0) return;
		LMOrientation ori = e.getOrientation();
		Vec3 vec3 = cam.getPosition();
		float f = (float) (ori.getX(pTick) - vec3.x());
		float f1 = (float) (ori.getY(pTick) - vec3.y());
		float f2 = (float) (ori.getZ(pTick) - vec3.z());
		Quaternionf rot = rotation(e, pTick);
		Vector3f[] vertexes = new Vector3f[]{
				new Vector3f(-1.0F, -1.0F, 0.0F),
				new Vector3f(1.0F, -1.0F, 0.0F),
				new Vector3f(1.0F, 1.0F, 0.0F),
				new Vector3f(-1.0F, 1.0F, 0.0F)};
		float f3 = e.getQuadSize(pTick);
		for (int i = 0; i < 4; ++i) {
			Vector3f vtx = vertexes[i];
			vtx.rotate(rot);
			vtx.mul(f3);
			vtx.add(f, f1, f2);
		}
		float u0 = e.getU0();
		float u1 = e.getU1();
		float v0 = e.getV0();
		float v1 = e.getV1();
		int light = e.getLightColor(pTick);

		vc.vertex(vertexes[0].x(), vertexes[0].y(), vertexes[0].z()).uv(u1, v1).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[1].x(), vertexes[1].y(), vertexes[1].z()).uv(u1, v0).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[2].x(), vertexes[2].y(), vertexes[2].z()).uv(u0, v0).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[3].x(), vertexes[3].y(), vertexes[3].z()).uv(u0, v1).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();

		vc.vertex(vertexes[3].x(), vertexes[3].y(), vertexes[3].z()).uv(u0, v1).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[2].x(), vertexes[2].y(), vertexes[2].z()).uv(u0, v0).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[1].x(), vertexes[1].y(), vertexes[1].z()).uv(u1, v0).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
		vc.vertex(vertexes[0].x(), vertexes[0].y(), vertexes[0].z()).uv(u1, v1).color(e.rCol(), e.gCol(), e.bCol(), e.alpha()).uv2(light).endVertex();
	}

}
