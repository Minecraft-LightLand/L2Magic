package dev.xkmc.l2magic.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

public abstract class SpellDataGenEntry {

	protected static ResourceKey<SpellAction> spell(String id) {
		return ResourceKey.create(EngineRegistry.SPELL, L2Magic.loc(id));
	}

	public abstract void register(BootstapContext<SpellAction> ctx);

	public abstract void genLang(RegistrateLangProvider pvd);

}
