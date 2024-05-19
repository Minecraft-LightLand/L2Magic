package dev.xkmc.l2magic.content.engine.instance.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.*;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.List;

public record ListInstance(List<ConfiguredEngine<?>> children)
		implements ConfiguredEngine<ListInstance> {

	public static final Codec<ListInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(ConfiguredEngine.CODEC).fieldOf("children").forGetter(e -> e.children)
	).apply(i, ListInstance::new));

	@Override
	public EngineType<ListInstance> type() {
		return EngineRegistry.LIST.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		for (var e : children) {
			e.execute(ctx);
		}
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		boolean ans = ConfiguredEngine.super.verify(ctx);
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i) == null) {
				ctx.error("entry at index " + i + " is null");
				ans = false;
				continue;
			}
			ans &= children.get(i).verify(ctx.of("children[" + i + "]"));
		}
		return ans;
	}

}
