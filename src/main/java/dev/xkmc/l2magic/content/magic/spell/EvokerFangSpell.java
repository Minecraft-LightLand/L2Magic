package dev.xkmc.l2magic.content.magic.spell;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2magic.content.common.entity.SpellEntity;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.internal.ActivationConfig;
import dev.xkmc.l2magic.content.magic.spell.internal.SimpleSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class EvokerFangSpell extends SimpleSpell<EvokerFangSpell.Config> {

	@Override
	public int getDistance(Player player) {
		return 0;
	}

	@Override
	public Config getConfig(Level world, Player player) {
		return SpellConfig.get(this, world, player);
	}

	@Override
	protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
		SpellEntity e = new SpellEntity(world);
		e.setData(player, config.spell_time, SpellEntity.SpellPlane.VERTICAL);
		world.addFreshEntity(e);
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		for (int i = 0; i < config.layers.length; i++) {
			Layer layer = config.layers[i];
			for (int j = 0; j < layer.count; j++) {
				double angle = layer.angle * Math.PI / 180 + 2f * Math.PI * j / layer.count;
				double x0 = x + layer.radius * Math.cos(angle);
				double z0 = z + layer.radius * Math.sin(angle);
				world.addFreshEntity(new EvokerFangs(world, x0, y, z0, (float) angle, layer.delay, player));
			}
		}
	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public Layer[] layers;

	}

	public record Layer(int count, int delay, double angle, double radius) {

	}

	@DataGenOnly
	public static class EvokerFangBuilder extends SpellConfig.SpellConfigBuilder<EvokerFangBuilder, Config> {

		private final ArrayList<Layer> list = new ArrayList<>();

		public EvokerFangBuilder(MagicScroll.ScrollType type, int duration, int mana_cost, int spell_load, SpellConfig.SpellDisplay display) {
			super(new Config(), type, duration, mana_cost, spell_load, display);
		}

		public EvokerFangBuilder addLayer(int count, int delay, double angle, double radius) {
			list.add(new Layer(count, delay, angle, radius));
			return this;
		}

		@Override
		public Config end() {
			config.layers = list.toArray(new Layer[0]);
			return config;
		}
	}

}
