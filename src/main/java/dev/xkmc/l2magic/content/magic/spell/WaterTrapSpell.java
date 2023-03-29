package dev.xkmc.l2magic.content.magic.spell;

import dev.xkmc.l2complements.compat.TeamAccessor;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2magic.content.common.entity.SpellEntity;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.internal.ActivationConfig;
import dev.xkmc.l2magic.content.magic.spell.internal.SimpleSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class WaterTrapSpell extends SimpleSpell<WaterTrapSpell.Config> {

	@Override
	public Config getConfig(Level world, Player player) {
		return SpellConfig.get(this, world, player);
	}

	@Override
	public int getDistance(Player player) {
		return 64;
	}

	@Override
	protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
		SpellEntity e = new SpellEntity(world);
		e.setData(activation, config.spell_time);
		e.setAction(spell -> {
			int t = spell.tickCount - config.spell_time.setup();
			if (t != 0)
				return;
			world.getEntities(player, new AABB(spell.blockPosition()).inflate(config.radius),
					ent -> ent instanceof LivingEntity le &&
							!TeamAccessor.arePlayerAndEntityInSameTeam(player, le) &&
							le.position().distanceTo(spell.position()) < config.radius
			).forEach(le -> EffectUtil.addEffect((LivingEntity) le,
					new MobEffectInstance(LCEffects.STONE_CAGE.get(), config.effect_time, config.effect_level),
					EffectUtil.AddReason.SKILL, player));
		});
		world.addFreshEntity(e);
	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public int effect_time, effect_level;

		@SerialClass.SerialField
		public float radius;

	}

	@DataGenOnly
	public static class Builder extends SpellConfig.SpellConfigBuilder<Builder, Config> {

		public Builder(MagicScroll.ScrollType type, int duration, int mana_cost, int spell_load,
					   SpellConfig.SpellDisplay display, int effect_time, int effect_level, float radius) {
			super(new Config(), type, duration, mana_cost, spell_load, display);
			config.effect_time = effect_time;
			config.effect_level = effect_level;
			config.radius = radius;
		}
	}

}
