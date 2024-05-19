package dev.xkmc.l2magic.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2itemselector.init.data.L2ISTagGen;
import dev.xkmc.l2magic.content.item.CreativeWandItem;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.world.item.CreativeModeTab;

public class LMItems {

	public static final RegistryEntry<CreativeModeTab> TAB = L2Magic.REGISTRATE.buildModCreativeTab(
			"magic", "L2Magic",
			e -> e.icon(LMItems.CREATIVE_WAND::asStack));

	public static final ItemEntry<CreativeWandItem> CREATIVE_WAND = L2Magic.REGISTRATE
			.item("creative_wand", p -> new CreativeWandItem(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx))
			.tag(L2ISTagGen.SELECTABLE)
			.register();

	public static void register() {

	}

}
