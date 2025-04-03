package dev.xkmc.l2magic.content.command;

import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public class CommandSpellExecutor {

	public static boolean execute(LivingEntity le, Holder<SpellAction> spell, int time, double power, int distance) {
		if (spell.value().castType() == SpellCastType.INSTANT) {
			var val = SpellContext.castSpell(le, spell.value(), time, power, distance, 0);
			if (val == null) return false;
			spell.value().execute(spell, val);
		} else {
			SchedulerHandler.schedulePersistent(new CommandSpellExecutor(le, spell, time, power, distance)::tick);
		}
		return true;
	}

	private final LivingEntity le;
	private final Holder<SpellAction> spell;
	private final int duration;
	private final double power;
	private final int distance;

	private int time;

	public CommandSpellExecutor(LivingEntity le, Holder<SpellAction> spell, int time, double power, int distance) {
		this.le = le;
		this.spell = spell;
		this.duration = time;
		this.power = power;
		this.distance = distance;
	}

	public boolean tick() {
		double p = spell.value().castType() == SpellCastType.CHARGE && time < duration ? 0 : power;
		var val = SpellContext.castSpell(le, spell.value(), time, p, distance, 0);
		if (val != null) {
			spell.value().execute(spell, val);
		}
		time++;
		return time > duration;
	}

}
