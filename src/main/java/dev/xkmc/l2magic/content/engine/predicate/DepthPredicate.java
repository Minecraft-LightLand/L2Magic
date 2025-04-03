package dev.xkmc.l2magic.content.engine.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ContextPredicate;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.core.PredicateType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public record DepthPredicate(
		IntVariable maxDepth,
		IPredicate child,
		@Nullable String index
) implements ContextPredicate<DepthPredicate> {

	public static final MapCodec<DepthPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("max_depth", DepthPredicate::maxDepth),
			IPredicate.CODEC.fieldOf("child").forGetter(DepthPredicate::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index))
	).apply(i, (d, c, index) ->
			new DepthPredicate(d, c, index.orElse(null))));

	@Override
	public PredicateType<DepthPredicate> type() {
		return EngineRegistry.DEPTH.get();
	}

	@Override
	public boolean test(EngineContext ctx) {
		int maxDepth = maxDepth().eval(ctx);
		for (int i = 0; i < maxDepth; i++) {
			if (ctx.test(ctx.loc().add(new Vec3(0, -i, 0)), index, i, child))
				return true;
		}
		return false;
	}

	@Override
	public Set<String> verificationParameters() {
		if (index == null) return Set.of();
		return Set.of(index);
	}

}
