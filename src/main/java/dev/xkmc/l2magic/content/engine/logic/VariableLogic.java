package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public record VariableLogic(String name, DoubleVariable var, ConfiguredEngine<?> child)
		implements ConfiguredEngine<VariableLogic> {

	public static MapCodec<VariableLogic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("name").forGetter(e -> e.name),
			DoubleVariable.codec("variable", e -> e.var),
			ConfiguredEngine.codec("child", VariableLogic::child)
	).apply(i, VariableLogic::new));

	@Deprecated
	public VariableLogic {
	}

	@Deprecated
	public VariableLogic(String name, String var, ConfiguredEngine<?> child) {
		this(name, DoubleVariable.of(var), child);
	}

	@Override
	public EngineType<VariableLogic> type() {
		return EngineRegistry.VAR.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var params = new HashMap<>(ctx.parameters());
		params.put(name, var.eval(ctx));
		ctx.execute(ctx.loc(), params, child);
	}

	public Set<String> verificationParameters() {
		Set<String> ans = new LinkedHashSet<>();
		ans.add(name);
		return ans;
	}

}
