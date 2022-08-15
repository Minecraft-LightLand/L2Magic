package dev.xkmc.l2magic.content.common.effect.skill;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.content.common.effect.SkillEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class QuickPullEffect extends InherentEffect implements SkillEffect<QuickPullEffect> {

	public QuickPullEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

}
