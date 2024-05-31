package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LMDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {


	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(EngineRegistry.SPELL, ctx -> {
				for (var e : SpellDataGenRegistry.LIST) {
					e.register(ctx);
				}
			});

	public LMDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", L2Magic.MODID));
	}

	@NotNull
	public String getName() {
		return "L2Magic Spell Data";
	}

}
