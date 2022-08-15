package dev.xkmc.l2magic.content.magic.spell;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.common.entity.FireArrowEntity;
import dev.xkmc.l2magic.content.common.entity.MagicFireBallEntity;
import dev.xkmc.l2magic.content.common.entity.SpellEntity;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.internal.ActivationConfig;
import dev.xkmc.l2magic.content.magic.spell.internal.SimpleSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireArrowSpell extends SimpleSpell<FireArrowSpell.Config> {

	@Override
	public int getDistance(Player player) {
		return 64;
	}

	@Override
	public Config getConfig(Level world, Player player) {
		return SpellConfig.get(this, world, player);
	}

	@Override
	protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
		SpellEntity e = new SpellEntity(world);
		e.setData(activation, config.spell_time);
		e.setAction(spell -> {
			int t = spell.tickCount - config.spell_time.setup();
			if (t < 0 || t > config.spell_time.duration() - config.spell_time.close())
				return;
			if (t % config.period != 0)
				return;
			for (int i = 0; i < config.repeat; i++) {
				Vec3 target = activation.pos;
				float angle = (float) (Math.random() * 360);
				float radius = (float) (Math.random() * config.radius);
				target = RayTraceUtil.getRayTerm(target, 0, angle, radius);
				if (config.explosion == 0) {
					addArrow(target, player, world, config);
				} else {
					addFireball(target, player, world, config);
				}
			}
		});
		world.addFreshEntity(e);
	}

	private void addArrow(Vec3 target, Player player, Level world, Config config) {
		Arrow e = new FireArrowEntity(world, player);
		e.pickup = AbstractArrow.Pickup.DISALLOWED;
		e.setSecondsOnFire(100);
		Vec3 pos = target.add(0, config.distance, 0);
		e.setPos(pos.x, pos.y, pos.z);
		Vec3 velocity = new Vec3(0, -config.velocity, 0);
		e.setDeltaMovement(velocity);
		e.setCritArrow(true);
		e.setBaseDamage(config.damage);
		world.addFreshEntity(e);
	}

	private void addFireball(Vec3 target, Player player, Level world, Config config) {
		Vec3 pos = target.add(0, config.distance, 0);
		MagicFireBallEntity e = new MagicFireBallEntity(world, player, pos, config.size);
		Vec3 velocity = new Vec3(0, -config.velocity, 0);
		e.setDeltaMovement(velocity);
		e.explosionPower = config.explosion;
		world.addFreshEntity(e);
	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public int period, repeat, explosion;

		@SerialClass.SerialField
		public float damage, distance, velocity, radius, size;

	}

	@DataGenOnly
	public static class Builder extends SpellConfig.SpellConfigBuilder<Builder, Config> {

		public Builder(MagicScroll.ScrollType type, int duration, int mana_cost, int spell_load, SpellConfig.SpellDisplay display) {
			super(new Config(), type, duration, mana_cost, spell_load, display);
		}

		public Builder set(int period, int repeat, float radius, float distance, float velocity) {
			config.period = period;
			config.repeat = repeat;
			config.radius = radius;
			config.distance = distance;
			config.velocity = velocity;
			return this;
		}

		public Config fireball(int explosion, float size) {
			config.explosion = explosion;
			config.size = size;
			return config;
		}

		public Config arrow(float damage) {
			config.damage = damage;
			return config;
		}

	}

}
