package dev.xkmc.l2magic.content.common.capability;

import dev.xkmc.l2foundation.init.registrate.LFEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.compat.api.MagicBehaviorHandler;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.network.packets.CapToClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
@SerialClass
public class MagicAbility {

	public static final int ACTIVATION = 600, SYNC_PERIOD = 100, ENCHANT_FACTOR = 4;
	public static final DamageSource LOAD = new DamageSource("spell_load").bypassArmor().bypassMagic();

	private final MagicData parent;
	@SerialClass.SerialField
	public CompoundTag arcane_type = new CompoundTag();
	@SerialClass.SerialField
	public ListTag spell_activation = new ListTag();
	@SerialClass.SerialField
	public int magic_level, spell_level = MagicBehaviorHandler.INSTANCE.getDefaultSpellSlot(), tick;
	@SerialClass.SerialField
	public int magic_mana, spell_load;

	public int time_after_sync = 0;

	public MagicAbility(MagicData parent) {
		this.parent = parent;
	}

	public void giveMana(int mana) {
		magic_mana = Mth.clamp(magic_mana + mana, 0, getMaxMana());
	}

	public void addSpellLoad(int load) {
		spell_load = Math.max(spell_load + load, 0);
	}

	public void tick() {
		tick++;
		time_after_sync++;
		if (tick % 20 == 0) {
			int armor_cost = MagicBehaviorHandler.INSTANCE.getArmorLoad(parent.player);
			int mana_restore = getManaRestoration();
			int spell_restore = getSpellReduction();
			int t0 = Math.min(armor_cost, mana_restore);
			armor_cost -= t0;
			mana_restore -= t0;
			spell_restore -= armor_cost;
			spell_restore = Math.max(-getMaxSpellEndurance() / 5, spell_restore);

			magic_mana = Mth.clamp(magic_mana + mana_restore, 0, getMaxMana());
			spell_load = Math.max(spell_load - spell_restore, 0);
			tick = 0;
			int load = spell_load / getMaxSpellEndurance();
			if (load == 1) {
				add(MobEffects.MOVEMENT_SLOWDOWN, 2);
				add(MobEffects.CONFUSION, 0);
				parent.player.hurt(LOAD, 1);
			}
			if (load == 2) {
				add(LFEffects.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 4);
			}
			if (load == 3) {
				add(LFEffects.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 16);
			}
			if (load >= 4) {
				add(LFEffects.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 64);
			}
			if (!parent.world.isClientSide() && time_after_sync >= SYNC_PERIOD) {
				new CapToClient(CapToClient.Action.MAGIC_ABILITY, parent).toClientPlayer((ServerPlayer) parent.player);
			}
		}
		for (int i = 0; i < getMaxSpellSlot(); i++) {
			ItemStack stack = parent.player.getInventory().getItem(i);
			if (spell_activation.size() == i)
				spell_activation.add(new CompoundTag());
			CompoundTag tag = spell_activation.getCompound(i);
			tickSpell(stack, tag);
		}
	}

	private void add(MobEffect eff, int lv) {
		MobEffectInstance ins = new MobEffectInstance(eff, 40, lv);
		EffectUtil.addEffect(parent.player, ins, EffectUtil.AddReason.SELF, parent.player);
	}

	private void tickSpell(ItemStack stack, CompoundTag tag) {
		if (stack.getItem() instanceof MagicScroll) {
			String tag_spell = tag.getString("spell");
			Spell<?, ?> spell = MagicScroll.getSpell(stack);
			if (spell != null) {
				if (tag_spell.equals(spell.getID())) {
					int tick = tag.getInt("time");
					tag.putInt("time", tick + 1);
				} else {
					tag.putString("spell", spell.getID());
					tag.putInt("time", 0);
				}
				return;
			}
		}
		tag.putString("spell", "");
		tag.putInt("time", 0);
	}

	public int getMaxMana() {
		return (int) parent.player.getAttributeValue(MagicRegistry.MAX_MANA.get());
	}

	public int getManaRestoration() {
		return (int) (getMana() * parent.player.getAttributeValue(MagicRegistry.MANA_RESTORE.get()));
	}

	public int getMaxSpellSlot() {
		return spell_level;
	}

	public int getMaxSpellEndurance() {
		return (int) parent.player.getAttributeValue(MagicRegistry.MAX_SPELL_LOAD.get());
	}

	public int getSpellLoad() {
		return (int) Math.round(getMana() * parent.player.getAttributeValue(MagicRegistry.MANA_RESTORE.get()));
	}

	public int getSpellReduction() {
		return (int) Math.round(spell_level * parent.player.getAttributeValue(MagicRegistry.LOAD_RESTORE.get()));
	}

	public boolean isArcaneTypeUnlocked(ArcaneType type) {
		return MagicBehaviorHandler.INSTANCE.unlockAll() || new NBTObj(arcane_type).getSub(type.getID()).tag.getInt("level") > 0;
	}

	public void unlockArcaneType(ArcaneType type, boolean force) {
		if (!isArcaneTypeUnlocked(type) && (force || MagicBehaviorHandler.INSTANCE.doLevelArcane(parent.player))) {
			new NBTObj(arcane_type).getSub(type.getID()).tag.putInt("level", 1);
		}
	}

	public double getSpellActivation(int id) {
		if (id < 0 || id >= getMaxSpellSlot())
			return 1;
		int time = spell_activation.getCompound(id).getInt("time");
		if (time >= ACTIVATION)
			return 0;
		return 1.0 * (ACTIVATION - time) / ACTIVATION;
	}

	public int getMana() {
		return magic_mana;
	}

}
