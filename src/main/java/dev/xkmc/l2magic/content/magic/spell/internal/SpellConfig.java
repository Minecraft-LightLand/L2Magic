package dev.xkmc.l2magic.content.magic.spell.internal;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.network.config.SpellDataConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class SpellConfig {

	@SerialClass.SerialField
	public int duration, mana_cost, spell_load;
	@SerialClass.SerialField
	public float factor = 1f;
	@SerialClass.SerialField
	public SpellDisplay spell_time;
	@SerialClass.SerialField
	public MagicScroll.ScrollType type;

	public static <C extends SpellConfig> C get(Spell<C, ?> spell, Level world, Player player) {
		C ans = SpellDataConfig.getConfig(spell.getRegistryName());

		IMagicRecipe r = IMagicRecipe.getMap(MagicRegistry.MPT_SPELL.get()).get(spell);
		if (r == null)
			return ans;
		MagicProduct<?, ?> p = MagicData.get(player).magicHolder.getProduct(r);
		if (!p.usable())
			return ans;
		ans = makeCopy(ans);
		ans.mana_cost += p.getCost() * ans.factor;
		ans.spell_load += p.getCost() * ans.factor;

		return ans;
	}

	public static <C extends SpellConfig> C makeCopy(C config) {
		return TagCodec.fromTag(TagCodec.toTag(new CompoundTag(), config), config.getClass());
	}

	public record SpellDisplay(String id, int duration, int setup, int close) {

	}

	@DataGenOnly
	public static class SpellConfigBuilder<B extends SpellConfigBuilder<B, C>, C extends SpellConfig> {

		public final C config;

		public SpellConfigBuilder(C config, MagicScroll.ScrollType type, int duration, int mana_cost, int spell_load, SpellDisplay display) {
			this.config = config;
			config.type = type;
			config.duration = duration;
			config.mana_cost = mana_cost;
			config.spell_load = spell_load;
			config.spell_time = display;
		}

		public B setFactor(float factor) {
			config.factor = factor;
			return getThis();
		}

		public B getThis() {
			return Wrappers.cast(this);
		}

		public C end() {
			return config;
		}

	}

}
