package dev.xkmc.l2magic.content.item.spell;

import dev.xkmc.l2library.content.raytrace.FastItem;
import dev.xkmc.l2library.content.raytrace.IGlowingTarget;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nullable;

public class WandItem extends Item implements IGlowingTarget, FastItem {

	private static final String KEY = "l2magic:spell";

	@Nullable
	public static ResourceKey<SpellAction> getSpellId(ItemStack stack) {
		var str = LMItems.SPELL.get(stack);
		if (str == null) return null;
		var rl = ResourceLocation.tryParse(str);
		if (rl == null) return null;
		return ResourceKey.create(EngineRegistry.SPELL, rl);
	}

	@Nullable
	public static Holder<SpellAction> getSpell(ItemStack stack) {
		var id = getSpellId(stack);
		if (id == null) return null;
		var reg = CommonHooks.resolveLookup(EngineRegistry.SPELL);
		if (reg == null) return null;
		var ans = reg.get(id);
		return ans.orElse(null);
	}

	public static ItemStack setSpell(ItemStack stack, ResourceKey<SpellAction> id) {
		LMItems.SPELL.set(stack, id.location().toString());
		return stack;
	}

	public WandItem(Properties prop) {
		super(prop);
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var spell = getSpell(stack);
		if (spell != null) {
			if (spell.value().castType() == SpellCastType.INSTANT) {
				if (castSpell(stack, level, player, spell, 0, false)) {
					if (!level.isClientSide) {
						player.getCooldowns().addCooldown(this, 10);
					}
					return InteractionResultHolder.consume(stack);
				} else {
					return InteractionResultHolder.fail(stack);
				}
			} else {
				return ItemUtils.startUsingInstantly(level, player, hand);
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remain) {
		super.onUseTick(level, user, stack, remain);
		var spell = getSpell(stack);
		if (spell != null) {
			if (spell.value().castType() == SpellCastType.CONTINUOUS) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
			if (spell.value().castType() == SpellCastType.CHARGE) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, true);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remain) {
		var spell = getSpell(stack);
		if (spell != null) {
			if (spell.value().castType() == SpellCastType.CHARGE) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (selected && entity instanceof Player player && level.isClientSide()) {
			var spell = getSpell(stack);
			if (spell != null && spell.value().triggerType() == SpellTriggerType.TARGET_ENTITY) {
				RayTraceUtil.clientUpdateTarget(player, getDistance(stack));
			}
		}
	}

	private boolean castSpell(ItemStack stack, Level level, LivingEntity user, Holder<SpellAction> spell, int useTick, boolean charging) {
		SpellContext ctx = SpellContext.castSpell(user, spell.value(), useTick, charging ? 0 : 1, getDistance(stack), 0);
		if (ctx == null) return false;
		if (!level.isClientSide()) {
			spell.value().execute(spell, ctx);
		}
		return true;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity e) {
		return 72000;
	}

	@Override
	public int getDistance(ItemStack itemStack) {
		return 64;
	}

	@Override
	public boolean isFast(ItemStack itemStack) {
		return true;
	}

}
