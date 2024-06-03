package dev.xkmc.l2magic.content.particle.core;

import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface LMParticleData {

	Vec3 move(int age, Vec3 vec3, Vec3 vec31);

	int life();

	boolean doCollision();

	@OnlyIn(Dist.CLIENT)
	ParticleRenderer renderer();

}
