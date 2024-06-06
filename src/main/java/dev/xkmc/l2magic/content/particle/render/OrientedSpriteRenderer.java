package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record OrientedSpriteRenderer(
		ParticleRenderer inner,
		Vec3 facing,
		Int2DoubleFunction roll
) implements OrientableSpriteRenderer {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		inner.onParticleInit(e);
		setRot(e);
		var ori = e.getOrientation();
		ori.preTick(ori.getX(1), ori.getY(1), ori.getZ(1));
	}

	@Override
	public void onPostTick(LMGenericParticle e) {
		inner.onPostTick(e);
		setRot(e);
	}

	private void setRot(LMGenericParticle e) {
		var ori = e.getOrientation();
		double rz = roll.get(e.age()) * Mth.DEG_TO_RAD;
		if (facing().lengthSqr() == 0) {
			ori.setRot(ori.getRX(1), ori.getRY(1), ori.getRZ(1) + rz);
		} else {
			var rot = ProjectileMovement.of(facing).rot();
			ori.setRot(rot.x, rot.y, rot.z + rz);
		}
		if (e.age() == 1) ori.preTick(ori.getX(1), ori.getY(1), ori.getZ(1));
	}

}
