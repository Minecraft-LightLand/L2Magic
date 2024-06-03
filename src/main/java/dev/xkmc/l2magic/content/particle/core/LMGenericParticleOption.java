package dev.xkmc.l2magic.content.particle.core;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class LMGenericParticleOption implements ParticleOptions {
	@Override
	public ParticleType<?> getType() {
		return null;//TODO
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		//TODO
	}

	@Override
	public String writeToString() {
		return null;//TODO
	}
}
