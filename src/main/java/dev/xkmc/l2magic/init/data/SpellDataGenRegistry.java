package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.init.data.spell.ArrowSpells;
import dev.xkmc.l2magic.init.data.spell.FlameSpells;
import dev.xkmc.l2magic.init.data.spell.WinterStorm;

import java.util.List;

public class SpellDataGenRegistry {

	public static final List<SpellDataGenEntry> LIST = List.of(
			new WinterStorm(), new FlameSpells(), new ArrowSpells()
	);

}
