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
			new MagnetCore(),  // id:3000
			new MasterSpark(),
			new IcyFlash(),  // id:3100
			new EarthSpike(),  // id:3200
			new EchoSpells(),
			new FlameCharge()  // id:3300
	);

}
