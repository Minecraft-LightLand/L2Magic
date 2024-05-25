package dev.xkmc.l2magic.content.command;

import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.world.entity.LivingEntity;

public class CommandSpellExecutor {

	public static boolean execute(LivingEntity le, SpellAction spell, int time, double power, int distance) {
		if (spell.castType() == SpellCastType.INSTANT) {
			var val = SpellContext.castSpell(le, spell, time, power, distance);
			if (val == null) return false;
			spell.execute(val);
		} else {
			GeneralEventHandler.schedulePersistent(new CommandSpellExecutor(le, spell, time, power, distance)::tick);
		}
		return true;
	}

	private final LivingEntity le;
	private final SpellAction spell;
	private final int duration;
	private final double power;
	private final int distance;

	private int time;

	public CommandSpellExecutor(LivingEntity le, SpellAction spell, int time, double power, int distance) {
		this.le = le;
		this.spell = spell;
		this.duration = time;
		this.power = power;
		this.distance = distance;
	}

	public boolean tick() {
		double p = spell.castType() == SpellCastType.CHARGE && time < duration ? 0 : power;
		var val = SpellContext.castSpell(le, spell, time, p, distance);
		if (val != null) {
			spell.execute(val);
		}
		time++;
		return time > duration;
	}

}
