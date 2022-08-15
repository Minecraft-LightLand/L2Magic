package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.ItemBuilder;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2magic.content.arcane.item.ArcaneAxe;
import dev.xkmc.l2magic.content.arcane.item.ArcaneSword;
import dev.xkmc.l2magic.content.common.item.misc.ContainerBook;
import dev.xkmc.l2magic.content.common.item.misc.ScreenBook;
import dev.xkmc.l2magic.content.magic.gui.tree.MagicTreeScreen;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.item.MagicWand;
import dev.xkmc.l2magic.content.magic.item.ManaStorage;
import dev.xkmc.l2magic.content.magic.item.PotionCore;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.xkmc.l2magic.init.LightLand.REGISTRATE;

@SuppressWarnings({"rawtypes", "unchecked", "unsafe"})
@MethodsReturnNonnullByDefault
public class LLItems {

	public static class Tab extends CreativeModeTab {

		private final Supplier<ItemEntry> icon;

		public Tab(String id, Supplier<ItemEntry> icon) {
			super(LightLand.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().asStack();
		}
	}

	public static final Tab TAB_MAIN = new Tab("magic", () -> LLItems.MAGIC_WAND);

	static {
		REGISTRATE.creativeModeTab(() -> TAB_MAIN);
	}

	// -------- common --------
	public static final ItemEntry<ScreenBook> MAGIC_BOOK;
	public static final ItemEntry<ContainerBook> ARCANE_INJECT_BOOK, DISENC_BOOK, SPCRAFT_BOOK;


	//TODO public static final FluidEntry<VirtualFluid> CLEANSE_WATER, HOLY_WATER;

	static {
		// Books
		{
			MAGIC_BOOK = REGISTRATE.item("magic_book", p -> new ScreenBook(p, () -> MagicTreeScreen::new)).defaultModel().defaultLang().register();
			ARCANE_INJECT_BOOK = REGISTRATE.item("arcane_inject_book", p -> new ContainerBook(p, () -> LLMenu.MT_ARCANE)).defaultModel().defaultLang().register();
			DISENC_BOOK = REGISTRATE.item("disenchant_book", p -> new ContainerBook(p, () -> LLMenu.MT_DISENC)).defaultModel().defaultLang().register();
			SPCRAFT_BOOK = REGISTRATE.item("spell_craft_book", p -> new ContainerBook(p, () -> LLMenu.MT_SPCRAFT)).defaultModel().defaultLang().register();
		}
	}

	// -------- magic --------
	public static final int MANA = 256;

	public static final ItemEntry<ArcaneSword> ARCANE_SWORD_GILDED;
	public static final ItemEntry<ArcaneAxe> ARCANE_AXE_GILDED;
	public static final ItemEntry<MagicWand> MAGIC_WAND;
	public static final ItemEntry<PotionCore> POTION_CORE;
	public static final ItemEntry<MagicScroll> SPELL_CARD, SPELL_PARCHMENT, SPELL_SCROLL;
	public static final ItemEntry<ManaStorage> ENC_GOLD_NUGGET, ENC_GOLD_INGOT, COOKIE, MELON, CARROT, APPLE;
	public static final ItemEntry<Item> MAGICIUM_INGOT, MAGICIUM_NUGGET, ETHERNIUM_NUGGET, ETHERNIUM_INGOT;

	static {
		ARCANE_SWORD_GILDED = REGISTRATE.item("gilded_arcane_sword", p ->
						new ArcaneSword(Tiers.IRON, 5, -2.4f, p.stacksTo(1).setNoRepair(), 50))
				.model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();
		ARCANE_AXE_GILDED = REGISTRATE.item("gilded_arcane_axe", p ->
						new ArcaneAxe(Tiers.IRON, 8, -3.1f, p.stacksTo(1).setNoRepair(), 50))
				.model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();
		MAGIC_WAND = REGISTRATE.item("magic_wand", MagicWand::new)
				.defaultModel().defaultLang().register();
		POTION_CORE = REGISTRATE.item("potion_core", PotionCore::new)
				.defaultModel().defaultLang().register();
		SPELL_CARD = REGISTRATE.item("spell_card", p ->
						new MagicScroll(MagicScroll.ScrollType.CARD, p))
				.defaultModel().defaultLang().register();
		SPELL_PARCHMENT = REGISTRATE.item("spell_parchment", p ->
						new MagicScroll(MagicScroll.ScrollType.PARCHMENT, p))
				.defaultModel().defaultLang().register();
		SPELL_SCROLL = REGISTRATE.item("spell_scroll", p ->
						new MagicScroll(MagicScroll.ScrollType.SCROLL, p))
				.defaultModel().defaultLang().register();
		COOKIE = REGISTRATE.item("enchant_cookie", p -> new ManaStorage(p.food(Foods.COOKIE), Items.COOKIE, MANA / 8))
				.defaultModel().defaultLang().register();
		MELON = REGISTRATE.item("enchant_melon", p -> new ManaStorage(p.food(Foods.MELON_SLICE), Items.MELON_SLICE, MANA))
				.defaultModel().defaultLang().register();
		CARROT = REGISTRATE.item("enchant_carrot", p -> new ManaStorage(p.food(Foods.GOLDEN_CARROT), Items.GOLDEN_CARROT, MANA * 8))
				.defaultModel().defaultLang().register();
		APPLE = REGISTRATE.item("enchant_apple", p -> new ManaStorage(p.food(Foods.GOLDEN_APPLE), Items.GOLDEN_APPLE, MANA * 72))
				.defaultModel().defaultLang().register();
		ENC_GOLD_NUGGET = REGISTRATE.item("enchant_gold_nugget", p -> new ManaStorage(p, Items.GOLD_NUGGET, MANA))
				.defaultModel().defaultLang().register();
		ENC_GOLD_INGOT = REGISTRATE.item("enchant_gold_ingot", p -> new ManaStorage(p, Items.GOLD_INGOT, MANA * 9))
				.defaultModel().defaultLang().register();

		MAGICIUM_INGOT = simpleItem("magicium_ingot");
		MAGICIUM_NUGGET = simpleItem("magicium_nugget");
		ETHERNIUM_INGOT = simpleItem("ethernium_ingot");
		ETHERNIUM_NUGGET = simpleItem("ethernium_nugget");
	}


	public static void register() {
	}

	public static <T extends ArmorItem> ArmorItems<T> genArmor(String id, BiFunction<EquipmentSlot, Item.Properties, T> sup, Function<ItemBuilder<T, L2Registrate>, ItemBuilder<T, L2Registrate>> func) {
		return new ArmorItems<>(REGISTRATE, id, sup, func);
	}

	public static <T extends Item> void createDoubleLayerModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
		ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "minecraft:generated");
		builder.texture("layer0", "item/" + ctx.getName());
		builder.texture("layer1", "item/" + ctx.getName() + "_overlay");
	}

	public static ItemEntry<Item> simpleItem(String id) {
		return REGISTRATE.item(id, Item::new).defaultModel().defaultLang().register();
	}

	public static class ArmorItems<T extends ArmorItem> {

		public final String prefix;
		public final ItemEntry<T>[] armors = new ItemEntry[4];

		public ArmorItems(L2Registrate reg, String id, BiFunction<EquipmentSlot, Item.Properties, T> sup, Function<ItemBuilder<T, L2Registrate>, ItemBuilder<T, L2Registrate>> func) {
			this.prefix = reg.getModid() + ":" + id;
			armors[0] = func.apply(reg.item(id + "_helmet", p -> sup.apply(EquipmentSlot.HEAD, p))).defaultLang().register();
			armors[1] = func.apply(reg.item(id + "_chestplate", p -> sup.apply(EquipmentSlot.CHEST, p))).defaultLang().register();
			armors[2] = func.apply(reg.item(id + "_leggings", p -> sup.apply(EquipmentSlot.LEGS, p))).defaultLang().register();
			armors[3] = func.apply(reg.item(id + "_boots", p -> sup.apply(EquipmentSlot.FEET, p))).defaultLang().register();
		}

	}

}
