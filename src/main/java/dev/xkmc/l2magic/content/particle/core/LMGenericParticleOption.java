package dev.xkmc.l2magic.content.particle.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class LMGenericParticleOption implements ParticleOptions {

	public static final Codec<LMGenericParticleOption> CODEC = Codec.unit(LMGenericParticleOption::new);

	@Nullable
	private final ClientParticleData data;

	public LMGenericParticleOption(ClientParticleData data) {
		this.data = data;
	}

	public LMGenericParticleOption() {
		this.data = null;
	}

	@Override
	public ParticleType<?> getType() {
		return LMItems.GENERIC_PARTICLE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
	}

	@Override
	public String writeToString() {
		var rl = ForgeRegistries.PARTICLE_TYPES.getKey(this.getType());
		assert rl != null;
		return rl.toString();
	}

	@OnlyIn(Dist.CLIENT)
	public LMParticleData data() {
		return data == null ? ClientParticleData.DEFAULT : data;
	}

}
