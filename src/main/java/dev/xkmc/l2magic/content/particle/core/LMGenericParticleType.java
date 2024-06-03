package dev.xkmc.l2magic.content.particle.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class LMGenericParticleType extends ParticleType<LMGenericParticleOption> {

	public LMGenericParticleType() {
		super(true, new LMGenericParticleDeserializer());
	}

	@Override
	public Codec<LMGenericParticleOption> codec() {
		return null;//TODO
	}

}
