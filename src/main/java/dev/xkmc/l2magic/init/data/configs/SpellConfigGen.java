package dev.xkmc.l2magic.init.data.configs;

import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2magic.content.common.entity.SpellEntity;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.EvokerFangSpell;
import dev.xkmc.l2magic.content.magic.spell.FireArrowSpell;
import dev.xkmc.l2magic.content.magic.spell.WaterTrapSpell;
import dev.xkmc.l2magic.content.magic.spell.WindBladeSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import dev.xkmc.l2magic.init.special.SpellRegistry;
import dev.xkmc.l2magic.network.config.SpellDataConfig;

import java.util.function.BiConsumer;

public class SpellConfigGen {

	public static void add(BiConsumer<String, BaseConfig> adder) {
		String test_spell = SpellRenderGen.TEST_MAIN.toString();
		adder.accept("evoker_fang", new SpellDataConfig()
				.add(SpellRegistry.FANG_ROUND.get(), new EvokerFangSpell.EvokerFangBuilder(
						MagicScroll.ScrollType.PARCHMENT, 30, 30, 50,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.addLayer(4, 30, 0, 1)
						.addLayer(8, 35, 22.5, 2)
						.addLayer(12, 40, 0, 3)
						.addLayer(16, 45, 11.25, 4)
						.addLayer(20, 50, 0, 5)
						.setFactor(2).end())
		);

		adder.accept("fire_arrow", new SpellDataConfig()
				.add(SpellRegistry.FIRE_RAIN.get(), new FireArrowSpell.Builder(
						MagicScroll.ScrollType.SCROLL, 30, 30, 50,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.set(1, 2, 6, 16, 3)
						.setFactor(4).arrow(5))
				.add(SpellRegistry.EXPLOSION_RAIN.get(), new FireArrowSpell.Builder(
						MagicScroll.ScrollType.SCROLL, 30, 100, 150,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.set(4, 1, 6, 16, 3)
						.setFactor(5).fireball(3, 0))
				.add(SpellRegistry.FIRE_EXPLOSION.get(), new FireArrowSpell.Builder(
						MagicScroll.ScrollType.SCROLL, 30, 100, 150,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.set(100, 1, 0, 16, 3)
						.setFactor(5).fireball(8, 2))
		);

		adder.accept("water_trap", new SpellDataConfig()
				.add(SpellRegistry.WATER_TRAP_0.get(), new WaterTrapSpell.Builder(
						MagicScroll.ScrollType.PARCHMENT, 30, 50, 70,
						new SpellConfig.SpellDisplay(test_spell, 50, 20, 20),
						300, 0, 6).setFactor(4).end())
				.add(SpellRegistry.WATER_TRAP_1.get(), new WaterTrapSpell.Builder(
						MagicScroll.ScrollType.PARCHMENT, 30, 50, 70,
						new SpellConfig.SpellDisplay(test_spell, 50, 20, 20),
						200, 1, 3).setFactor(4).end())
		);

		adder.accept("wind_blade", new SpellDataConfig()
				.add(SpellRegistry.BLADE_SIDE.get(), new WindBladeSpell.Builder(
						MagicScroll.ScrollType.PARCHMENT, 30, 50, 70,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.set(SpellEntity.SpellPlane.HORIZONTAL, 4, 0, 5, 64, 3)
						.setFactor(4).end())
				.add(SpellRegistry.BLADE_FRONT.get(), new WindBladeSpell.Builder(
						MagicScroll.ScrollType.PARCHMENT, 30, 50, 70,
						new SpellConfig.SpellDisplay(test_spell, 100, 30, 30))
						.set(SpellEntity.SpellPlane.NORMAL, 100, 1, 20, 64, 3.5f)
						.setFactor(4).end())
		);

	}

}
