package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.init.data.spell.*;

import java.util.List;

public class SpellDataGenRegistry {

	public static final List<SpellDataGenEntry> LIST = List.of(
			new WinterStorm(),
			new FlameSpells(),
			new ArrowSpells(),
			new MagnetCore(),
			new MasterSpark()
	);

}
