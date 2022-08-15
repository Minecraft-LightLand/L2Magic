package dev.xkmc.l2magic.content.magic.spell.internal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public abstract class SimpleSpell<C extends SpellConfig> extends Spell<C, ActivationConfig> {

	@Override
	protected final ActivationConfig canActivate(Type type, Level world, ServerPlayer player) {
		return new ActivationConfig(world, player, getDistance(player));
	}

}
