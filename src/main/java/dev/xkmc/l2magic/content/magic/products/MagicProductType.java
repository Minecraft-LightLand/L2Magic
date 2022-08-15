package dev.xkmc.l2magic.content.magic.products;

import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.content.common.capability.MagicHolder;
import dev.xkmc.l2magic.content.magic.products.info.TypeConfig;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.special.LightLandRegistry;
import dev.xkmc.l2magic.network.config.ProductTypeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class MagicProductType<I, P extends MagicProduct<I, P>> extends NamedEntry<MagicProductType<?, ?>> {

	public final Class<P> cls;
	public final MagicFactory<I, P> fac;
	public final Function<ResourceLocation, I> getter;
	public final Function<I, String> namer;
	public final Supplier<IForgeRegistry<I>> registry;
	public final MagicElement elem;

	public MagicProductType(Class<P> cls, MagicFactory<I, P> fac, Supplier<IForgeRegistry<I>> registry,
							Function<I, String> namer, MagicElement elem) {
		super(LightLandRegistry.PRODUCT_TYPE);
		this.cls = cls;
		this.fac = fac;
		this.getter = (s) -> registry.get().getValue(s);
		this.namer = namer;
		this.registry = registry;
		this.elem = elem;
	}

	public TypeConfig getDisplay() {
		return ProductTypeConfig.getConfig(getRegistryName());
	}

	@FunctionalInterface
	public interface MagicFactory<I, P extends MagicProduct<I, P>> {

		P get(@Nullable MagicHolder player, @Nullable NBTObj nbtManager, ResourceLocation rl, @Nullable IMagicRecipe r);

	}

}
