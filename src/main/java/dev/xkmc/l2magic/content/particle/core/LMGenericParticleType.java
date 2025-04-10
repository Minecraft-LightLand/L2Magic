package dev.xkmc.l2magic.content.particle.core;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class LMGenericParticleType extends ParticleType<LMGenericParticleOption> {

	public LMGenericParticleType() {
		super(true);
	}

	@Override
	public MapCodec<LMGenericParticleOption> codec() {
		return LMGenericParticleOption.CODEC;
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, LMGenericParticleOption> streamCodec() {
		return LMGenericParticleOption.STREAM_CODEC;
	}
}
