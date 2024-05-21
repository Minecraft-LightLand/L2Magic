package dev.xkmc.l2magic.content.engine.instance.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public record RandomVariableModifier(String name, int count, ConfiguredEngine<?> child)
		implements ConfiguredEngine<RandomVariableModifier> {

	public static Codec<RandomVariableModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("name").forGetter(e -> e.name),
			Codec.INT.fieldOf("count").forGetter(e -> e.count),
			ConfiguredEngine.codec("child", RandomVariableModifier::child)
	).apply(i, RandomVariableModifier::new));

	@Override
	public EngineType<RandomVariableModifier> type() {
		return EngineRegistry.RANDOM.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var params = new HashMap<>(ctx.parameters());
		for (int i = 0; i < count; i++) {
			params.put(name + i, ctx.user().rand().nextDouble());
		}
		child.execute(new EngineContext(ctx.user(), ctx.loc(), params));
	}

	public Set<String> verificationParameters() {
		Set<String> ans = new LinkedHashSet<>();
		for (int i = 0; i < count; i++) {
			ans.add(name + i);
		}
		return ans;
	}

}
