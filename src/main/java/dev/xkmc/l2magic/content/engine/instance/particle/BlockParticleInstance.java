package dev.xkmc.l2magic.content.engine.instance.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public record BlockParticleInstance(Block block, DoubleVariable speed)
		implements ParticleInstance<BlockParticleInstance> {

	public static final Codec<BlockParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ForgeRegistries.BLOCKS.getCodec().fieldOf("block").forGetter(e -> e.block),
			DoubleVariable.codec("speed", ParticleInstance::speed)
	).apply(i, BlockParticleInstance::new));

	@Override
	public EngineType<BlockParticleInstance> type() {
		return EngineRegistry.BLOCK_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState());
	}

}
