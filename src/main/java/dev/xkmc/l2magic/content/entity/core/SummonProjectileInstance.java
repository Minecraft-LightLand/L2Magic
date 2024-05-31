package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;

public record SummonProjectileInstance(
		DoubleVariable speed,
		boolean bypassWall,
		boolean bypassEntity
) {
}
