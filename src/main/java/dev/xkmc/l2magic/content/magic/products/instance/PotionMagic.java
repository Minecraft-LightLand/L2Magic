package dev.xkmc.l2magic.content.magic.products.instance;

import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.content.common.capability.player.MagicHolder;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class PotionMagic extends MagicProduct<MobEffect, PotionMagic> {

	public PotionMagic(MagicHolder player, NBTObj tag, ResourceLocation rl, IMagicRecipe r) {
		super(MagicRegistry.MPT_EFF.get(), player, tag, rl, r);
	}
}
