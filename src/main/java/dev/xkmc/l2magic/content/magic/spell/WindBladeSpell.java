package dev.xkmc.l2magic.content.magic.spell;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.common.entity.SpellEntity;
import dev.xkmc.l2magic.content.common.entity.WindBladeEntity;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.internal.ActivationConfig;
import dev.xkmc.l2magic.content.magic.spell.internal.SimpleSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WindBladeSpell extends SimpleSpell<WindBladeSpell.Config> {

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
		e.setData(player, config.spell_time, config.plane);
		e.setAction(spell -> {
			int t = spell.tickCount - config.spell_time.setup();
			if (t < 0 || t > config.spell_time.duration() - config.spell_time.close())
				return;
			if (t % config.period != 0)
				return;
			Vec3 target = activation.target == null ? activation.pos :
					activation.target.position()
							.add(0, activation.target.getBbHeight() / 2, 0);
			for (int offset : config.offset)
				addBlade(config.normal, offset, player, world, spell, target, config);
		});
		world.addFreshEntity(e);

	}

	private void addBlade(float noffset, float soffset, ServerPlayer player, Level world, SpellEntity spell, Vec3 target, Config config) {
		WindBladeEntity blade = new WindBladeEntity(world);
		Vec3 pos = spell.position();
		pos = RayTraceUtil.getRayTerm(pos, spell.getXRot(), spell.getYRot(), noffset);
		pos = RayTraceUtil.getRayTerm(pos, spell.getYRot(), spell.getYRot() + 90, soffset);
		blade.setOwner(player);
		blade.setPos(pos.x, pos.y, pos.z);
		Vec3 velocity = target.subtract(pos).normalize().scale(config.velocity);
		blade.setDeltaMovement(velocity);
		blade.setProperties(config.damage, Math.round(config.distance / config.velocity), 0f, ItemStack.EMPTY);
		world.addFreshEntity(blade);
	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public int period, normal;

		@SerialClass.SerialField
		public int[] offset = {0};

		@SerialClass.SerialField
		public float damage, distance, velocity;

		@SerialClass.SerialField
		public SpellEntity.SpellPlane plane;

	}

	@DataGenOnly
	public static class Builder extends SpellConfig.SpellConfigBuilder<Builder, Config> {

		public Builder(MagicScroll.ScrollType type, int duration, int mana_cost, int spell_load, SpellConfig.SpellDisplay display) {
			super(new Config(), type, duration, mana_cost, spell_load, display);
		}

		public Builder set(SpellEntity.SpellPlane plane, int period, int normal, float damage, float distance, float velocity) {
			config.plane = plane;
			config.period = period;
			config.normal = normal;
			config.damage = damage;
			config.distance = distance;
			config.velocity = velocity;
			return this;
		}

		public Builder setOffset(int... offsets) {
			config.offset = offsets;
			return this;
		}

	}

}
