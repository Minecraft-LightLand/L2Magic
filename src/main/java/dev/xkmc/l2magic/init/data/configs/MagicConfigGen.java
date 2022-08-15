package dev.xkmc.l2magic.init.data.configs;

import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2magic.content.magic.products.info.TypeConfig;
import dev.xkmc.l2magic.init.LightLand;
import dev.xkmc.l2magic.init.registrate.LLItems;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.network.config.ProductTypeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;

public class MagicConfigGen {

	public static void addProductType(BiConsumer<String, BaseConfig> adder) {
		adder.accept("default", new ProductTypeConfig()
				.add(MagicRegistry.MPT_ENCH.get(), new TypeConfig(Items.ENCHANTED_BOOK,
						new ResourceLocation(LightLand.MODID,
								"textures/block/ritual_iron.png")))
				.add(MagicRegistry.MPT_EFF.get(), new TypeConfig(Items.GLASS_BOTTLE,
						new ResourceLocation(LightLand.MODID,
								"textures/block/ritual_iron.png")))
				.add(MagicRegistry.MPT_ARCANE.get(), new TypeConfig(LLItems.ARCANE_AXE_GILDED.get(),
						new ResourceLocation(LightLand.MODID,
								"textures/block/ritual_iron.png")))
				.add(MagicRegistry.MPT_SPELL.get(), new TypeConfig(LLItems.SPELL_CARD.get(),
						new ResourceLocation(LightLand.MODID,
								"textures/block/ritual_iron.png")))
				.add(MagicRegistry.MPT_CRAFT.get(), new TypeConfig(Items.CRAFTING_TABLE,
						new ResourceLocation(LightLand.MODID,
								"textures/block/ritual_iron.png")))
		);
	}

}
