package dev.xkmc.l2magic.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum LMLangData {
	CMD_INVALID_SPELL("command.invalid_spell", "Invalid spell ID %s", 1),
	CMD_WRONG_TYPE("command.wrong_type", "Spell %s expects type %s", 2),
	CMD_SUCCESS("command.success", "Spell %s is executed successfully", 1),
	CMD_SUCCESS_COUNT("command.success_count", "Spell %s is executed by %s entities successfully", 2),
	CMD_FAIL("command.fail", "Spell %s failed to execute", 1)
	;

	final String id, def;
	final int count;

	LMLangData(String id, String def, int count) {
		this.id = id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != count)
			throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
		return translate(L2Magic.MODID + "." + id, objs);
	}


	public static void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.WINTER.location()), "Winter Storm");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.FLAME.location()), "Hell Mark");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.QUAKE.location()), "Lava Burst");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.ARROW_RING.location()), "Sword of Seven");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.ARROW.location()), "Angelic Judgement");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.CIRCULAR.location()), "Three Bodies");
		pvd.add(SpellAction.lang(LMDatapackRegistriesGen.RAINARROW.location()), "Rain Arrow");
	}

	public static MutableComponent translate(String key, Object... objs) {
		return Component.translatable(key, objs);
	}

}
