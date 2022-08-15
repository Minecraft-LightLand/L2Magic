package dev.xkmc.l2magic.content.common.effect.skill;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.content.common.effect.SkillEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ArmorBreakerEffect extends InherentEffect implements SkillEffect<ArmorBreakerEffect> {

	public ArmorBreakerEffect(MobEffectCategory type, int color) {
		super(type, color);
	}
}
