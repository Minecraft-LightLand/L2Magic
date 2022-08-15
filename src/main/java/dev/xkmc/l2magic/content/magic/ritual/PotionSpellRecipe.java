package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.block.RitualSide;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.init.registrate.LMItems;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

@SerialClass
public class PotionSpellRecipe extends AbstractRitualRecipe<PotionSpellRecipe> {

	public PotionSpellRecipe(ResourceLocation id) {
		super(id, LMRecipes.RSP_SPELL.get());
	}

	@Override
	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack core = inv.core.getItem(0).copy();
		List<MobEffectInstance> list = PotionUtils.getCustomEffects(core);
		MagicScroll.TargetType target = MagicScroll.getTarget(core);
		double radius = MagicScroll.getRadius(core);
		inv.setItem(5, assemble(inv));
		for (RitualSide.TE te : inv.sides) {
			ItemStack stack = te.getItem(0);
			if (stack.getItem() == LMItems.SPELL_CARD.get()) {
				MagicScroll.initEffect(list, stack);
				MagicScroll.setTarget(target, stack);
				MagicScroll.setRadius(radius, stack);
			}
		}
	}

}
