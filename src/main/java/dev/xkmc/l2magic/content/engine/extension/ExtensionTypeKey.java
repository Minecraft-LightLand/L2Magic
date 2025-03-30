package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public record ExtensionTypeKey(ResourceLocation reg, ResourceLocation id) {

	public static ExtensionTypeKey of(Val<? extends ExtensionHolder<?>> val) {
		return new ExtensionTypeKey(val.key().registry(), val.key().location());
	}

	public ExtensionHolder<?> type() {
		return Wrappers.cast(BuiltInRegistries.REGISTRY.get(reg).get(id));
	}

}
