package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.ItemSprite;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public record ItemParticleInstance(
		Item item, DoubleVariable speed,
		DoubleVariable scale, IntVariable life, boolean breaking
) implements ParticleInstance<ItemParticleInstance> {

	public static final Codec<ItemParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(e -> e.item),
			DoubleVariable.codec("speed", ParticleInstance::speed),
			DoubleVariable.codec("scale", e -> e.scale),
			IntVariable.codec("life", e -> e.life),
			Codec.BOOL.fieldOf("breaking").forGetter(e -> e.breaking)
	).apply(i, ItemParticleInstance::new));

	@Override
	public EngineType<ItemParticleInstance> type() {
		return EngineRegistry.ITEM_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(
				life.eval(ctx), breaking, (float) scale.eval(ctx) * ClientParticleData.randSize(ctx), ctx,
				breaking() ? SimpleMotion.BREAKING : SimpleMotion.ZERO,
				new ItemSprite(RenderTypePreset.BLOCK, item.getDefaultInstance(),
						breaking ? SpriteGeom.breaking(ctx.rand()) : SpriteGeom.INSTANCE)
		));
	}

}
