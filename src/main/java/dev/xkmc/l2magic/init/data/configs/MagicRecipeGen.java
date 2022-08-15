package dev.xkmc.l2magic.init.data.configs;

import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.info.DisplayInfo;
import dev.xkmc.l2magic.content.magic.products.recipe.DefMagicRecipe;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.special.ArcaneRegistry;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.init.special.SpellRegistry;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MagicRecipeGen {

	public static void addRecipe(BiConsumer<String, BaseConfig> adder) {
		addArcane((e, screen, func) -> reg(adder, "arcane", e.getRegistryName(), screen, MagicRegistry.MPT_ARCANE.get(), func));
		addEnchantments((e, screen, func) -> reg(adder, "enchantment", ForgeRegistries.ENCHANTMENTS.getKey(e), screen, MagicRegistry.MPT_ENCH.get(), func));
		addSpells((e, screen, func) -> reg(adder, "spell", e.getRegistryName(), screen, MagicRegistry.MPT_SPELL.get(), func));
		addEffects((e, screen, func) -> reg(adder, "effect", ForgeRegistries.MOB_EFFECTS.getKey(e), screen, MagicRegistry.MPT_EFF.get(), func));
		addCrafts((e, screen, func) -> reg(adder, "craft", ForgeRegistries.ITEMS.getKey(e), screen, MagicRegistry.MPT_CRAFT.get(), func));
	}

	private static ResourceLocation reg(BiConsumer<String, BaseConfig> adder,
										String prefix,
										@Nullable ResourceLocation rl, DisplayInfo screen,
										MagicProductType<?, ?> type,
										Function<DefMagicRecipe, IMagicRecipe> func) {
		assert rl != null;
		String path = prefix + "/" + rl.getPath();
		ResourceLocation id = new ResourceLocation(L2Magic.MODID, "magic_data/" + path);
		DefMagicRecipe config = new DefMagicRecipe(type, rl, screen);
		adder.accept(path, func.apply(config));
		return id;
	}

	private static int x, y;

	private static void addArcane(Builder<Arcane> con) {
		MagicElement f = MagicRegistry.ELEM_FIRE.get();
		MagicElement w = MagicRegistry.ELEM_WATER.get();
		MagicElement e = MagicRegistry.ELEM_EARTH.get();
		MagicElement a = MagicRegistry.ELEM_AIR.get();
		MagicElement q = MagicRegistry.ELEM_QUINT.get();

		x = 0;
		y = 0;

		con.apply(ArcaneRegistry.ALIOTH_WINDBLADE.get(), getPos(),
				r -> r.addElements(w, e, q, q, f, f).setFlow("A->B", "B->CD", "CD->EF", "EF->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
		);
		con.apply(ArcaneRegistry.MERAK_THUNDER.get(), getPos(),
				r -> r.addElements(f, w, a, q).setFlow("ABCD|")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
		);
		con.apply(ArcaneRegistry.ALKAID_THUNDER.get(), getPos(),
				r -> r.addElements(f, w, a, e, q).setFlow("ABCDE|")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
		);
		con.apply(ArcaneRegistry.ALKAID_MARKER.get(), getPos(),
				r -> r.addElements(f, w, q, a, e, q).setFlow("ABCDEF|")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
		);
	}

	private static void addEnchantments(Builder<Enchantment> con) {
		MagicElement f = MagicRegistry.ELEM_FIRE.get();
		MagicElement w = MagicRegistry.ELEM_WATER.get();
		MagicElement e = MagicRegistry.ELEM_EARTH.get();
		MagicElement a = MagicRegistry.ELEM_AIR.get();
		MagicElement q = MagicRegistry.ELEM_QUINT.get();

		// bows
		y = -2;
		x = -1;
		{
			con.apply(Enchantments.POWER_ARROWS, getPos(),
					r -> r.addElements(a, a, f, a, a, f).setFlow("ABDE<->CF")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
			);

			con.apply(Enchantments.FLAMING_ARROWS, getPos(),
					r -> r.addElements(a, a, f, a, a, f).setFlow("ABDE->C", "C->F", "F->ABDE")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);

			con.apply(Enchantments.INFINITY_ARROWS, getPos(),
					r -> r.addElements(a, a, a, q, q, q).setFlow("ABC<->DEF")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
			);
		}


		// offensive
		y = -1;
		x = -1;
		{
			con.apply(Enchantments.BANE_OF_ARTHROPODS, getPos(),
					r -> r.addElements(e, e, w, w, f, f).setFlow("AB->E", "E->F", "F->CD", "CD->AB")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);

			con.apply(Enchantments.SMITE, getPos(),
					r -> r.addElements(e, e, f, f, q).setFlow("AB->CD", "CD->E", "E->AB")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
			);

			con.apply(Enchantments.FIRE_ASPECT, getPos(),
					r -> r.addElements(e, e, e, e, f, f).setFlow("ABCD->E", "E->F", "F->ABCD")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);
			con.apply(Enchantments.SHARPNESS, getPos(),
					r -> r.addElements(e, e, e, e, f, f).setFlow("ABCD<->EF")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);
		}

		// all
		y = 0;
		x = 0;
		{
			con.apply(Enchantments.MENDING, getPos(),
					r -> r.addElements(q, e, w, a, f).setFlow("ABCDE|")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(q, 1))
			);
			con.apply(Enchantments.UNBREAKING, getPos(),
					r -> r.addElements(e, e, e, e).setFlow("ABCD|")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(e, 1))
			);
		}

		// armors
		y = 1;
		x = -2;
		{
			con.apply(Enchantments.BLAST_PROTECTION, getPos(),
					r -> r.addElements(e, e, e, w, w).setFlow("ABC<->DE")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(w, 1))
			);

			con.apply(Enchantments.FALL_PROTECTION, getPos(),
					r -> r.addElements(a, a, a, a, e, w).setFlow("AB->E", "CD->F", "E->CD", "F->AB")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
			);

			con.apply(Enchantments.PROJECTILE_PROTECTION, getPos(),
					r -> r.addElements(a, a, e, e, q, q).setFlow("AB->CD", "CD->EF", "EF->AB")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
			);

			con.apply(Enchantments.FIRE_PROTECTION, getPos(),
					r -> r.addElements(e, e, e, f, f, q).setFlow("F->ABC", "ABC->DE", "DE->F")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);

			con.apply(Enchantments.ALL_DAMAGE_PROTECTION, getPos(),
					r -> r.addElements(e, e, e, q).setFlow("ABC<->D")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(e, 1))
			);

			con.apply(Enchantments.THORNS, getPos(),
					r -> r.addElements(q, e, e, f, f, f).setFlow("A->BC", "BC->DEF", "DEF->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);
		}

	}

	private static void addSpells(Builder<Spell<?, ?>> con) {
		MagicElement f = MagicRegistry.ELEM_FIRE.get();
		MagicElement w = MagicRegistry.ELEM_WATER.get();
		MagicElement e = MagicRegistry.ELEM_EARTH.get();
		MagicElement a = MagicRegistry.ELEM_AIR.get();
		MagicElement q = MagicRegistry.ELEM_QUINT.get();

		// air
		y = -2;
		x = -1;
		{
			ResourceLocation rl = con.apply(SpellRegistry.BLADE_SIDE.get(), getPos(),
					r -> r.addElements(f, f, e, e, a, a).setFlow("AB->CD", "CD->EF", "EF->AB")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
			);
			con.apply(SpellRegistry.BLADE_FRONT.get(), getPos(),
					r -> r.addElements(f, e, a, a, a, a).setFlow("A->B", "B->CDEF", "CDEF->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
							.setPredecessors(rl));
		}

		// fire
		y = -1;
		x = -1;
		{
			ResourceLocation rain = con.apply(SpellRegistry.FIRE_RAIN.get(), getPos(),
					r -> r.addElements(w, a, a, f, f).setFlow("A->BC", "BC->DE", "DE->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
			);

			ResourceLocation expr = con.apply(SpellRegistry.EXPLOSION_RAIN.get(), getPos(),
					r -> r.addElements(e, a, a, f, f, f).setFlow("A->BC", "BC->DEF", "DEF->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
							.setPredecessors(rain));

			con.apply(SpellRegistry.FIRE_EXPLOSION.get(), getPos(),
					r -> r.addElements(e, f, f, f, e, e).setFlow("A->BCD", "BCD->EF", "EF->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
							.setPredecessors(expr));
		}

		// water
		y = 0;
		x = -1;
		{
			con.apply(SpellRegistry.FANG_ROUND.get(), getPos(),
					r -> r.addElements(w, a, a, a).setFlow("A<->BCD")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
			);

			ResourceLocation trap = con.apply(SpellRegistry.WATER_TRAP_0.get(), getPos(),
					r -> r.addElements(e, e, a, a, a).setFlow("AB<->CDE")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1)));

			con.apply(SpellRegistry.WATER_TRAP_1.get(), getPos(),
					r -> r.addElements(e, f, a, a, a, a).setFlow("A->CD", "CD->B", "B->EF", "EF->A")
							.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
							.setPredecessors(trap));
		}

	}

	private static void addEffects(Builder<MobEffect> con) {
		MagicElement f = MagicRegistry.ELEM_FIRE.get();
		MagicElement w = MagicRegistry.ELEM_WATER.get();
		MagicElement e = MagicRegistry.ELEM_EARTH.get();
		MagicElement a = MagicRegistry.ELEM_AIR.get();
		MagicElement q = MagicRegistry.ELEM_QUINT.get();

		y = -2;
		x = -1;

		con.apply(MobEffects.HARM, getPos(),
				r -> r.addElements(w, f, f, w, q, q).setFlow("A->BC", "BC->D", "D->EF", "EF->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
		);
		con.apply(MobEffects.HEAL, getPos(),
				r -> r.addElements(w, e, e, w, q, q).setFlow("A->BF", "B->C", "F->E", "CE->D", "D->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(e, 1))
		);
		con.apply(MobEffects.POISON, getPos(),
				r -> r.addElements(w, a, a, w, f, f).setFlow("A->BF", "BF->CE", "CE->D", "D->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(w, 1))
		);
		con.apply(MobEffects.REGENERATION, getPos(),
				r -> r.addElements(w, a, a, w, q, q).setFlow("A<->BF", "B<->C", "F<->E", "CE<->D", "D<->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(w, 1))
		);

		y++;
		x = -1;

		con.apply(MobEffects.DAMAGE_RESISTANCE, getPos(),
				r -> r.addElements(w, e, e, w, q, q).setFlow("A->B", "B->C", "C->D", "D<->A", "F->A", "E->F", "D->E")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(e, 1))
		);
		con.apply(MobEffects.MOVEMENT_SLOWDOWN, getPos(),
				r -> r.addElements(e, e, w, w, f).setFlow("A->C", "C->E", "E->B", "B->D", "D->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(e, 1))
		);
		con.apply(MobEffects.MOVEMENT_SPEED, getPos(),
				r -> r.addElements(a, a, w, w, f, f).setFlow("A->C", "B->D", "C->E", "D->F", "E->A", "F->B")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(a, 1))
		);
		con.apply(MobEffects.WEAKNESS, getPos(),
				r -> r.addElements(e, e, w, w, a, a).setFlow("A->B", "B->C", "C->D", "D->E", "E->F", "F->A")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(w, 1))
		);
		con.apply(MobEffects.DAMAGE_BOOST, getPos(),
				r -> r.addElements(f, f, w, w, e).setFlow("A->C", "B->D", "CD->E", "E->AB")
						.addElemRequirement(new IMagicRecipe.ElementalMastery(f, 1))
		);


	}

	private static void addCrafts(Builder<Item> con) {
		MagicElement f = MagicRegistry.ELEM_FIRE.get();
		MagicElement w = MagicRegistry.ELEM_WATER.get();
		MagicElement e = MagicRegistry.ELEM_EARTH.get();
		MagicElement a = MagicRegistry.ELEM_AIR.get();
		MagicElement q = MagicRegistry.ELEM_QUINT.get();

		y = -2;
		x = -1;

		con.apply(Items.DIRT, getPos(), r -> r.addElements(e, w).setFlow("A->B"));
		con.apply(Items.COBBLESTONE, getPos(), r -> r.addElements(e, e, a).setFlow("AB->C"));
		con.apply(Items.STONE, getPos(), r -> r.addElements(e, e, w).setFlow("C->AB"));
		con.apply(Items.ANDESITE, getPos(), r -> r.addElements(e, a, w).setFlow("A->B", "B->C", "C->A"));
		con.apply(Items.DIORITE, getPos(), r -> r.addElements(e, e, w).setFlow("AB<->C"));
		con.apply(Items.GRANITE, getPos(), r -> r.addElements(e, e, e, w).setFlow("D->ABC"));
	}

	private static DisplayInfo getPos() {
		return new DisplayInfo(x++, y, FrameType.TASK, Items.PAPER);
	}

	interface Builder<T> {

		ResourceLocation apply(T t, DisplayInfo info, Function<DefMagicRecipe, IMagicRecipe> func);

	}

}
