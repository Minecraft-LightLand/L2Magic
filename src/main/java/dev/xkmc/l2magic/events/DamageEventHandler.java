package dev.xkmc.l2magic.events;

import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.content.arcane.internal.IArcaneItem;
import dev.xkmc.l2magic.util.EffectAddUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class DamageEventHandler {

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		DamageSource source = event.getSource();
		LivingEntity target = event.getEntity();
		if ((source.getMsgId().equals("player") || source.getMsgId().equals("mob")) && source.getDirectEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				EffectAddUtil.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		} else if (source.getDirectEntity() instanceof LightningBolt && source.getEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				EffectAddUtil.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		}
	}

}
