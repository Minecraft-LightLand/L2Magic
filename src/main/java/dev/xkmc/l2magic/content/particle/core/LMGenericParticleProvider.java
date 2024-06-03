package dev.xkmc.l2magic.content.particle.core;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.Nullable;

public class LMGenericParticleProvider implements ParticleProvider.Sprite<LMGenericParticleOption> {

	@Nullable
	@Override
	public TextureSheetParticle createParticle(
			LMGenericParticleOption option, ClientLevel level,
			double x, double y, double z, double vx, double vy, double vz) {
		return null;//TODO
	}

}
