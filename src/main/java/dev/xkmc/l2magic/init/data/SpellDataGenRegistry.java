package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.init.data.spell.*;
import dev.xkmc.l2magic.init.data.spell.fire.*;
import dev.xkmc.l2magic.init.data.spell.ground.*;
import dev.xkmc.l2magic.init.data.spell.ice.*;

import java.util.List;

public class SpellDataGenRegistry {

	public static final List<SpellDataGenEntry> LIST = List.of(
			new WinterStorm(),
			new FlameSpells(),
			new ArrowSpells(),
			new MagnetCore(),
			new MasterSpark(),
			new IcyFlash(),
			new EarthSpike(),
			new EchoSpells()
	);

}
