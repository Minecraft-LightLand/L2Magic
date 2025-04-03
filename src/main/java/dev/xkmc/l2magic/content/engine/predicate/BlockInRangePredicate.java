package dev.xkmc.l2magic.content.engine.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ContextPredicate;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.core.PredicateType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record BlockInRangePredicate(
		DoubleVariable range,
		DoubleVariable height,
		IntVariable minCount,
		IPredicate child,
		@Nullable String variable
) implements ContextPredicate<BlockInRangePredicate> {

	public static final MapCodec<BlockInRangePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("range", BlockInRangePredicate::range),
			DoubleVariable.optionalCodec("height", BlockInRangePredicate::height),
			IntVariable.optionalCodec("min_count", BlockInRangePredicate::minCount),
			IPredicate.CODEC.fieldOf("child").forGetter(BlockInRangePredicate::child),
			Codec.STRING.optionalFieldOf("variable").forGetter(e -> Optional.ofNullable(e.variable))
	).apply(i, (a, h, m, c, d) -> new BlockInRangePredicate(
			a, h.orElse(a), m.orElse(IntVariable.of("1")), c, d.orElse(null))));

	public static ContextPredicate<?> circular(
			String radius,
			String height,
			String min,
			@Nullable String variable,
			IPredicate... predicates
	) {
		return new BlockInRangePredicate(
				DoubleVariable.of(radius),
				DoubleVariable.of(height),
				IntVariable.of(min),
				predicates.length == 1 ? predicates[0] : new AndPredicate(List.of(predicates)),
				variable);
	}

	public BlockInRangePredicate(
			DoubleVariable range,
			ContextPredicate<?> child,
			@Nullable String variable
	) {
		this(range, range, IntVariable.of("1"), child, variable);
	}

	@Override
	public PredicateType<BlockInRangePredicate> type() {
		return EngineRegistry.RANGE.get();
	}

	@Override
	public boolean test(EngineContext ctx) {
		double rad = range.eval(ctx);
		int step = (int) Math.ceil(rad);
		int h = (int) Math.ceil(height.eval(ctx));
		var p = ctx.loc().pos();
		int count = 0;
		BlockPos pos = BlockPos.containing(p);
		for (int x = -step; x <= step; x++) {
			for (int z = -step; z <= step; z++) {
				for (int y = -h; y <= h; y++) {
					if (onBlock(ctx, x, y, z, pos, p, rad)) {
						count++;
						if (count >= minCount.eval(ctx))
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean onBlock(EngineContext ctx, int x, int y, int z, BlockPos pos, Vec3 p, double rad) {
		var npos = pos.offset(x, y, z).getCenter();
		double dist = npos.distanceTo(p);
		if (dist > rad) return false;
		var dir = dist < 0.5 ? ctx.loc().dir() : npos.subtract(p).normalize();
		double dx = npos.x - p.x;
		double dy = npos.y - p.y;
		double dz = npos.z - p.z;
		return ctx.test(LocationContext.of(npos, dir),
				Map.of(
						variable + "_x", dx,
						variable + "_y", dy,
						variable + "_z", dz,
						variable + "_r", dist
				), child);
	}

	@Override
	public Set<String> verificationParameters() {
		if (variable == null) return Set.of();
		return Set.of(variable + "_x", variable + "_y", variable + "_z", variable + "_r");
	}
}
