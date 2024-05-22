package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.core.SpellAction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public record DataGenContext(BootstapContext<SpellAction> ctx) {

	public Holder<DamageType> damage(ResourceKey<DamageType> key) {
		return ctx.lookup(Registries.DAMAGE_TYPE).getOrThrow(key);
	}

}
