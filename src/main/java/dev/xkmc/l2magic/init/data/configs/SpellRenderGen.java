package dev.xkmc.l2magic.init.data.configs;

import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2magic.content.magic.render.SpellComponent;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.network.config.SpellEntityConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class SpellRenderGen {

	public static final ResourceLocation TEST_MAIN = new ResourceLocation(L2Magic.MODID, "test_spell");

	public static void add(BiConsumer<String, BaseConfig> adder) {
		ResourceLocation test_sub_0 = new ResourceLocation(L2Magic.MODID, "test_spell_0");
		adder.accept("test", new SpellEntityConfig()
				.add(TEST_MAIN, new SpellComponent()
						.addStroke(new SpellComponent.Stroke(64, 1, "0xFFFF0000", 1, 48, 0, 0))
						.addStroke(new SpellComponent.Stroke(7, 2, "0xFFFF0000", 1, 47, 0, 0))
						.addLayer(new SpellComponent.Layer(
								SpellComponent.Value.constant(4),
								SpellComponent.Value.constant(1),
								SpellComponent.Value.sin(32, 8, 60),
								SpellComponent.Value.linear(0, 1),
								SpellComponent.Value.sin(0.8f, 0.2f, 60)
						).add(6, test_sub_0)))
				.add(test_sub_0, new SpellComponent()
						.addStroke(new SpellComponent.Stroke(28, 1, "0xFFFF0000", 1, 9, 0, 0))
						.addStroke(new SpellComponent.Stroke(7, 2, "0xFFFF0000", 1, 8, 0, 0)))
		);

	}

}
