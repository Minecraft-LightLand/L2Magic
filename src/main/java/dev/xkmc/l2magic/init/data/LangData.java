package dev.xkmc.l2magic.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;

public enum LangData {
	;

	public static void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.WINTER.location()), "Winter Storm");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.FLAME.location()), "Hell Mark");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.QUAKE.location()), "Lava Burst");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.ARROW_RING.location()), "Sword of Seven");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.ARROW.location()), "Angelic Judgement");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.CIRCULAR.location()), "Three Bodies");
	}

}
