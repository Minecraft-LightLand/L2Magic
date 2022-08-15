package dev.xkmc.l2magic.content.common.effect.force;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class HeavyEffect extends InherentEffect {

	public static final UUID ID = MathHelper.getUUIDFromString("lightland:heavy");

	public HeavyEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.FLYING_SPEED, ID.toString(), -0.45f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, ID.toString(), -0.45f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ID.toString(), -0.45f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ID.toString(), -0.45f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), ID.toString(), -0.45f, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

}
