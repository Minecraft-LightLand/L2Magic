package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.common.particle.EmeraldParticle;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class LLParticle {

	public static final RegistryEntry<SimpleParticleType> EMERALD = LightLand.REGISTRATE.simple("emerald", ForgeRegistries.Keys.PARTICLE_TYPES, () -> new SimpleParticleType(false));

	public static void register() {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerClient() {
		Minecraft.getInstance().particleEngine.register(EMERALD.get(), new EmeraldParticle.Factory());
	}

}
