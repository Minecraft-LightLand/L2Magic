package dev.xkmc.l2magic.content.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class LMGenericParticleDeserializer implements ParticleOptions.Deserializer<LMGenericParticleOption> {

	@Override
	public LMGenericParticleOption fromCommand(ParticleType<LMGenericParticleOption> type, StringReader reader) throws CommandSyntaxException {
		return null;//TODO
	}

	@Override
	public LMGenericParticleOption fromNetwork(ParticleType<LMGenericParticleOption> type, FriendlyByteBuf buf) {
		return null;//TODO
	}

}
