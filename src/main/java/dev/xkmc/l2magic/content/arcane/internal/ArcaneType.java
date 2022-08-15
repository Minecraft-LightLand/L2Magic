package dev.xkmc.l2magic.content.arcane.internal;

import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.arcane.item.ArcaneAxe;
import dev.xkmc.l2magic.content.arcane.item.ArcaneSword;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.LMItems;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 天枢，天璇，天玑，天权，玉衡，开阳，摇光
 * DUBHE, MERAK, PHECDA, MEGREZ, ALIOTH, MIZAR, ALKAID
 */
public class ArcaneType extends NamedEntry<ArcaneType> {

	public static final RegistryEntry<ArcaneType> DUBHE = reg("dubhe", Weapon.AXE, Hit.LIGHT, Mana.ACTIVE);
	public static final RegistryEntry<ArcaneType> MERAK = reg("merak", Weapon.AXE, Hit.CRITICAL, Mana.ACTIVE);
	public static final RegistryEntry<ArcaneType> PHECDA = reg("phecda", Weapon.AXE, Hit.CRITICAL, Mana.PASSIVE);
	public static final RegistryEntry<ArcaneType> MEGREZ = reg("megrez", Weapon.AXE, Hit.LIGHT, Mana.PASSIVE);
	public static final RegistryEntry<ArcaneType> ALIOTH = reg("alioth", Weapon.SWORD, Hit.LIGHT, Mana.PASSIVE);
	public static final RegistryEntry<ArcaneType> MIZAR = reg("mizar", Weapon.SWORD, Hit.CRITICAL, Mana.PASSIVE);
	public static final RegistryEntry<ArcaneType> ALKAID = reg("alkaid", Weapon.SWORD, Hit.NONE, Mana.ACTIVE);
	public final Weapon weapon;
	public final Hit hit;
	public final Mana mana;

	private final ItemStack stack;

	public ArcaneType(Weapon weapon, Hit hit, Mana mana) {
		super(MagicRegistry.ARCANE_TYPE);
		this.weapon = weapon;
		this.hit = hit;
		this.mana = mana;
		stack = (weapon == Weapon.AXE ? LMItems.ARCANE_AXE_GILDED : LMItems.ARCANE_SWORD_GILDED).get().getDefaultInstance();
		if (mana == Mana.ACTIVE) {
			stack.getOrCreateTag().putBoolean("foil", true);
		}
	}

	private static RegistryEntry<ArcaneType> reg(String str, Weapon w, Hit h, Mana m) {
		return L2Magic.REGISTRATE.generic(MagicRegistry.ARCANE_TYPE, str, () -> new ArcaneType(w, h, m)).defaultLang().register();
	}

	@OnlyIn(Dist.CLIENT)
	public ItemStack getStack() {
		return stack;
	}

	public enum Weapon {
		SWORD(ArcaneSword.class), AXE(ArcaneAxe.class);

		private final Class<?> cls;

		Weapon(Class<?> cls) {
			this.cls = cls;
		}

		public boolean isValid(ItemStack stack) {
			return cls.isInstance(stack.getItem());
		}
	}

	public enum Hit {
		LIGHT, CRITICAL, NONE
	}

	public enum Mana {
		PASSIVE, ACTIVE
	}

	public static void register() {
	}

}
