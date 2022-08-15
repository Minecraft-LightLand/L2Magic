package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.MenuEntry;
import dev.xkmc.l2magic.content.magic.gui.craft.*;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * handles container menu
 */
@MethodsReturnNonnullByDefault
public class LMMenu {

	public static final MenuEntry<ArcaneInjectContainer> MT_ARCANE = L2Magic.REGISTRATE.menu("arcane_inject",
			ArcaneInjectContainer::new, () -> ArcaneInjectScreen::new).lang(LMMenu::getLangKey).register();
	public static final MenuEntry<DisEnchanterContainer> MT_DISENC = L2Magic.REGISTRATE.menu("disenchanter",
			DisEnchanterContainer::new, () -> DisEnchanterScreen::new).lang(LMMenu::getLangKey).register();
	public static final MenuEntry<SpellCraftContainer> MT_SPCRAFT = L2Magic.REGISTRATE.menu("spell_craft",
			SpellCraftContainer::new, () -> SpellCraftScreen::new).lang(LMMenu::getLangKey).register();

	public static void register() {

	}

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = Objects.requireNonNull(ForgeRegistries.MENU_TYPES.getKey(menu));
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

}
