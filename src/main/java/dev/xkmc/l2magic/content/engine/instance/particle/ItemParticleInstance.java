package dev.xkmc.l2magic.content.engine.instance.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public record ItemParticleInstance(Item item, DoubleVariable speed)
		implements ParticleInstance<ItemParticleInstance> {

	public static final Codec<ItemParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(e -> e.item),
			DoubleVariable.codec("speed", ParticleInstance::speed)
	).apply(i, ItemParticleInstance::new));

	@Override
	public EngineType<ItemParticleInstance> type() {
		return EngineRegistry.ITEM_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new ItemParticleOption(ParticleTypes.ITEM, item.getDefaultInstance());
	}

}
