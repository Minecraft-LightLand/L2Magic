package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.particle.render.DustParticleSprite;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record DustParticleData(
		RenderTypePreset renderType, ColorVariable color
) implements ParticleRenderData<DustParticleData> {

	public static final Codec<DustParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			ColorVariable.CODEC.fieldOf("color").forGetter(e -> e.color)
	).apply(i, DustParticleData::new));

	@Override
	public ParticleRenderType<DustParticleData> type() {
		return EngineRegistry.COLOR_RENDER.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new DustParticleSprite(renderType, color.eval(ctx));
	}

}
