package dev.xkmc.l2magic.content.common.effect.force;

import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class WaterTrapEffect extends InherentEffect implements IconOverlayEffect {

	public static final UUID ID = MathHelper.getUUIDFromString("lightland:water_trap");

	public WaterTrapEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.FLYING_SPEED, ID.toString(), -0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, ID.toString(), -0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ID.toString(), -0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), ID.toString(), -0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(LightLand.MODID, "textures/effect_overlay/water_trap.png");
	}

}
