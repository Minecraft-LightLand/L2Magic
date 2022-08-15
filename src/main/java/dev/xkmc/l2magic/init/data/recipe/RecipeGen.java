package dev.xkmc.l2magic.init.data.recipe;

import dev.xkmc.l2foundation.init.data.GenItem;
import dev.xkmc.l2foundation.init.registrate.LFBlocks;
import dev.xkmc.l2library.base.ingredients.MobEffectIngredient;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.data.recipe.ritual.*;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.BiFunction;

public class RecipeGen {

	private static String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {

		// gen tool and storage
		{
			currentFolder = "generated_tools/";

			currentFolder = "storage/";

			storage(pvd, LMItems.ENC_GOLD_NUGGET, LMItems.ENC_GOLD_INGOT, LMBlocks.ENCHANT_GOLD_BLOCK);
			storage(pvd, LMItems.MAGICIUM_NUGGET, LMItems.MAGICIUM_INGOT, LMBlocks.MAGICIUM_BLOCK);
		}

		currentFolder = "foundation/";
		{
			full(pvd, LMItems.ENC_GOLD_NUGGET.get(), LFBlocks.LAYLINE_HEAD.get().asItem(), GenItem.Mats.STEEL.getNugget(), GenItem.Mats.LAYLINE.getIngot(), 1);
			full(pvd, LMItems.ENC_GOLD_NUGGET.get(), LFBlocks.LAYROOT_HEAD.get().asItem(), Items.IRON_NUGGET, GenItem.Mats.LAYROOT.getIngot(), 1);
		}

		currentFolder = "magic_food/";
		{
			circle(pvd, Items.APPLE, LMItems.ENC_GOLD_INGOT.get(), LMItems.APPLE.get(), 1);
			circle(pvd, Items.CARROT, LMItems.ENC_GOLD_NUGGET.get(), LMItems.CARROT.get(), 1);
			circle(pvd, LMItems.ENC_GOLD_NUGGET.get(), Items.COOKIE, LMItems.COOKIE.get(), 8);
			unlock(pvd, new ShapedRecipeBuilder(LMItems.COOKIE.get(), 8)::unlockedBy,
					LMItems.ENC_GOLD_NUGGET.get()).pattern(" G ").pattern("ABA")
					.define('G', LMItems.ENC_GOLD_NUGGET.get())
					.define('A', Items.WHEAT).define('B', Items.COCOA_BEANS)
					.save(pvd, getID(LMItems.COOKIE.get(), "_fast"));
			unlock(pvd, new ShapelessRecipeBuilder(LMItems.MELON.get(), 1)::unlockedBy,
					LMItems.ENC_GOLD_NUGGET.get()).requires(LMItems.ENC_GOLD_NUGGET.get())
					.requires(Items.MELON_SLICE).save(pvd, getID(LMItems.MELON.get()));
		}

		currentFolder = "magic_misc/";
		{
			unlock(pvd, new ShapedRecipeBuilder(LMBlocks.B_RITUAL_CORE.get(), 1)::unlockedBy,
					LMItems.ENC_GOLD_INGOT.get()).pattern("BBB").pattern("DED").pattern("CCC")
					.define('E', ItemTags.PLANKS).define('D', Items.REDSTONE)
					.define('C', LMItems.ENC_GOLD_NUGGET.get())
					.define('B', LMItems.ENC_GOLD_INGOT.get())
					.save(pvd, getID(LMBlocks.B_RITUAL_CORE.get().asItem()));
			unlock(pvd, new ShapedRecipeBuilder(LMBlocks.B_RITUAL_SIDE.get(), 1)::unlockedBy,
					LMItems.ENC_GOLD_NUGGET.get()).pattern("BBB").pattern("DED").pattern("CCC")
					.define('E', ItemTags.PLANKS).define('D', Items.IRON_NUGGET)
					.define('C', LMItems.ENC_GOLD_NUGGET.get())
					.define('B', Items.IRON_INGOT)
					.save(pvd, getID(LMBlocks.B_RITUAL_SIDE.get().asItem()));
			unlock(pvd, new ShapedRecipeBuilder(LMItems.SPELL_CARD.get(), 64)::unlockedBy,
					LMItems.ENC_GOLD_NUGGET.get()).pattern("BAB").pattern("BAB").pattern("BAB")
					.define('B', Items.PAPER).define('A', LMItems.ENC_GOLD_NUGGET.get())
					.save(pvd, getID(LMItems.SPELL_CARD.get()));
			full(pvd, Items.LEATHER, Items.PAPER, LMItems.ENC_GOLD_NUGGET.get(), LMItems.SPELL_PARCHMENT.get(), 16);
			unlock(pvd, new ShapedRecipeBuilder(LMItems.SPELL_SCROLL.get(), 1)::unlockedBy,
					LMItems.ENC_GOLD_NUGGET.get()).pattern("CDC").pattern("ABA").pattern("CDC")
					.define('C', Items.PAPER).define('D', Items.STICK)
					.define('A', Items.LEATHER)
					.define('B', LMItems.ENC_GOLD_NUGGET.get())
					.save(pvd, getID(LMItems.SPELL_SCROLL.get()));
		}

		currentFolder = "ritual/";
		{
			unlock(pvd, new BasicRitualBuilder()::unlockedBy, Items.IRON_INGOT)
					.setCore(Ingredient.of(Items.IRON_INGOT),
							GenItem.Mats.ETERNIUM.getIngot().getDefaultInstance())
					.setSides(Ingredient.of(GenItem.Mats.LAYLINE.getIngot()),
							GenItem.Mats.LAYROOT.getIngot().getDefaultInstance(), 1, 3, 4, 6)
					.setSide(Enchantments.MENDING, 1, 0)
					.setSide(Enchantments.UNBREAKING, 3, 2)
					.setSide(Enchantments.INFINITY_ARROWS, 1, 5)
					.setSide(Enchantments.ALL_DAMAGE_PROTECTION, 4, 7)
					.save(pvd, getID(GenItem.Mats.ETERNIUM.getIngot()));

			unlock(pvd, new BasicRitualBuilder()::unlockedBy, Items.IRON_INGOT)
					.setCore(Ingredient.of(Items.IRON_INGOT),
							LMItems.MAGICIUM_INGOT.get().getDefaultInstance())
					.setSides(Ingredient.of(LMItems.ENC_GOLD_INGOT.get()),
							Items.GOLD_INGOT.getDefaultInstance(), 0, 2, 5, 7)
					.setSide(Enchantments.ALL_DAMAGE_PROTECTION, 4, 3)
					.setSide(Enchantments.UNBREAKING, 3, 4)
					.save(pvd, getID(LMItems.MAGICIUM_INGOT.get()));

		}

		currentFolder = "ritual_potion/";
		{
			unlock(pvd, new PotionCoreBuilder()::unlockedBy, Items.DIAMOND)
					.setCore(Ingredient.of(Items.DIAMOND),
							LMItems.POTION_CORE.asStack())
					.setSides(Items.NETHER_WART, 0, 2)
					.setSides(Items.REDSTONE, 3, 4)
					.setSides(Items.GLOWSTONE_DUST, 5, 7)
					.setSides(Items.BLAZE_POWDER, 6)
					.setSides(Items.POTION, Items.GLASS_BOTTLE, 1)
					.save(pvd, getID(LMItems.POTION_CORE.get(), "_1"));

			unlock(pvd, new PotionCoreBuilder()::unlockedBy, Items.DIAMOND)
					.setCore(Ingredient.of(Items.DIAMOND),
							LMItems.POTION_CORE.asStack())
					.setSides(Items.NETHER_STAR, 1)
					.setSides(Items.NETHER_WART, 3, 4, 6)
					.setSides(Items.POTION, Items.GLASS_BOTTLE, 0, 2, 5, 7)
					.save(pvd, getID(LMItems.POTION_CORE.get(), "_4"));

			unlock(pvd, new PotionSpellBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()), Items.DIAMOND.getDefaultInstance())
					.setSides(LMItems.SPELL_CARD.get(), 0, 1, 2, 3, 4, 5, 6, 7)
					.save(pvd, getID(LMItems.SPELL_CARD.get()));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setTarget(MagicScroll.TargetType.ALLY, LMItems.POTION_CORE.asStack()))
					.setSides(Items.LAPIS_LAZULI, 0, 2, 5, 7)
					.setSides(Items.REDSTONE, 3, 4)
					.setSides(Items.GOLDEN_CARROT, 1, 6)
					.save(pvd, getID("set_ally"));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setTarget(MagicScroll.TargetType.ENEMY, LMItems.POTION_CORE.asStack()))
					.setSides(Items.LAPIS_LAZULI, 0, 2, 5, 7)
					.setSides(Items.GLOWSTONE_DUST, 3, 4)
					.setSides(Items.SPIDER_EYE, 1, 6)
					.save(pvd, getID("set_enemy"));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setRadius(10, LMItems.POTION_CORE.asStack()))
					.setSides(Items.GUNPOWDER, 0, 1, 2, 3, 4, 5, 6, 7)
					.save(pvd, getID("set_radius_10"));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setRadius(20, LMItems.POTION_CORE.asStack()))
					.setSides(Items.GUNPOWDER, 1, 3, 4, 6)
					.setSides(LMItems.COOKIE.get(), 0, 2, 5, 7)
					.save(pvd, getID("set_radius_20"));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setRadius(30, LMItems.POTION_CORE.asStack()))
					.setSides(Items.GUNPOWDER, 0, 2, 5, 7)
					.setSides(LMItems.MELON.get(), 1, 6)
					.setSides(Items.DRAGON_BREATH, Items.GLASS_BOTTLE, 3, 4)
					.save(pvd, getID("set_radius_30"));

			unlock(pvd, new PotionModifyBuilder()::unlockedBy, LMItems.POTION_CORE.get())
					.setCore(Ingredient.of(LMItems.POTION_CORE.get()),
							MagicScroll.setRadius(40, LMItems.POTION_CORE.asStack()))
					.setSides(Items.TNT, 3, 4)
					.setSides(LMItems.CARROT.get(), 1)
					.setSides(Items.DIAMOND, 6)
					.setSides(Items.DRAGON_BREATH, Items.GLASS_BOTTLE, 0, 2, 5, 7)
					.save(pvd, getID("set_radius_40"));
		}

		currentFolder = "ritual_effects/";
		{
			potionBoost(pvd, MobEffects.MOVEMENT_SPEED, Items.SUGAR, Items.NETHER_WART, 100, 50, 37, 30, 26);
			potionBoost(pvd, MobEffects.MOVEMENT_SLOWDOWN, Items.SUGAR, Items.FERMENTED_SPIDER_EYE, 100, 36, 31, 26, 21, 16);
			potionBoost(pvd, MobEffects.DAMAGE_BOOST, Items.BLAZE_POWDER, Items.NETHER_WART, 100, 40, 30, 27, 26);
			potionBoost(pvd, MobEffects.WEAKNESS, Items.FERMENTED_SPIDER_EYE, Items.NETHER_WART, 100, 50, 24, 19);
			potionBoost(pvd, MobEffects.REGENERATION, Items.GHAST_TEAR, Items.NETHER_WART, 100, 60, 57);
			potionBoost(pvd, MobEffects.POISON, Items.SPIDER_EYE, Items.NETHER_WART, 100, 50, 35, 24);
			potionBoost(pvd, MobEffects.DAMAGE_RESISTANCE, Items.SHULKER_SHELL, Items.NETHER_WART, 100, 50, 35, 21);
			potionBoost(pvd, MobEffects.HEAL, Items.GLISTERING_MELON_SLICE, Items.NETHER_WART, 100, 50, 35, 25);
			potionBoost(pvd, MobEffects.HARM, Items.GLISTERING_MELON_SLICE, Items.FERMENTED_SPIDER_EYE, 100, 50, 35, 24);

		}

		currentFolder = "ritual_enchantments/";
		{

		}

	}

	private static ResourceLocation getID(Item item) {
		return new ResourceLocation(L2Magic.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
	}

	private static ResourceLocation getID(Item item, String suffix) {
		return new ResourceLocation(L2Magic.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
	}

	private static ResourceLocation getID(String suffix) {
		return new ResourceLocation(L2Magic.MODID, currentFolder + suffix);
	}

	private static void cross(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern(" S ").pattern("SCS").pattern(" S ")
				.define('S', side).define('C', core)
				.save(pvd, getID(out));
	}

	private static void full(RegistrateRecipeProvider pvd, Item core, Item side, Item corner, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern("CSC").pattern("SAS").pattern("CSC")
				.define('A', core).define('S', side).define('C', corner)
				.save(pvd, getID(out));
	}

	private static void circle(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern("SSS").pattern("SAS").pattern("SSS")
				.define('A', core).define('S', side)
				.save(pvd, getID(out));
	}

	private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
		storage(pvd, nugget::get, ingot::get);
		storage(pvd, ingot::get, block::get);
	}

	public static void storage(RegistrateRecipeProvider pvd, NonNullSupplier<ItemLike> from, NonNullSupplier<ItemLike> to) {
		unlock(pvd, new ShapedRecipeBuilder(to.get(), 1)::unlockedBy, from.get().asItem())
				.pattern("XXX").pattern("XXX").pattern("XXX").define('X', from.get())
				.save(pvd, getID(to.get().asItem()) + "_storage");
		unlock(pvd, new ShapelessRecipeBuilder(from.get(), 9)::unlockedBy, to.get().asItem())
				.requires(to.get()).save(pvd, getID(to.get().asItem()) + "_unpack");
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

	/* special */

	private static void potionBoost(RegistrateRecipeProvider pvd, MobEffect eff, Item a, Item b, int... levels) {
		potionUp(pvd, eff, a, b, levels);
		potionDown(pvd, eff, a, b, levels);
	}

	private static void potionUp(RegistrateRecipeProvider pvd, MobEffect eff, Item a, Item b, int... levels) {
		String path = ForgeRegistries.MOB_EFFECTS.getKey(eff).getPath();
		ResourceLocation rl = new ResourceLocation(L2Magic.MODID, "magic_data/effect/" + path);
		unlock(pvd, new PotionBoostBuilder(eff, 1, rl, levels)::unlockedBy, LMItems.POTION_CORE.get())
				.setCore(new MobEffectIngredient(LMItems.POTION_CORE.get(), eff, 0, 1),
						PotionUtils.setCustomEffects(LMItems.POTION_CORE.asStack(),
								List.of(new MobEffectInstance(eff, 1, 1))))
				.setSides(Items.GLOWSTONE_DUST, 0, 2, 5, 7)
				.setSides(a, 1, 6).setSides(b, 3, 4)
				.save(pvd, getID(path + "_up"));
	}

	private static void potionDown(RegistrateRecipeProvider pvd, MobEffect eff, Item a, Item b, int... levels) {
		String path = ForgeRegistries.MOB_EFFECTS.getKey(eff).getPath();
		ResourceLocation rl = new ResourceLocation(L2Magic.MODID, "magic_data/effect/" + path);
		unlock(pvd, new PotionBoostBuilder(eff, -1, rl, levels)::unlockedBy, LMItems.POTION_CORE.get())
				.setCore(new MobEffectIngredient(LMItems.POTION_CORE.get(), eff, 0, 1),
						LMItems.POTION_CORE.asStack())
				.setSides(Items.REDSTONE, 0, 2, 5, 7)
				.setSides(a, 1, 6).setSides(b, 3, 4)
				.save(pvd, getID(path + "_down"));
	}

}
