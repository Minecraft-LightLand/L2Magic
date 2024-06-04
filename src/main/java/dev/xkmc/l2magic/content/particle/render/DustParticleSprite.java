package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record DustParticleSprite(RenderTypePreset renderType, Vector3f color) implements VanillaParticleSprite {

	private static final ResourceLocation ID = new ResourceLocation("dust");

	@Override
	public ResourceLocation particle() {
		return ID;
	}

	@Override
	public void onParticleInit(LMGenericParticle e) {
		VanillaParticleSprite.super.onParticleInit(e);
		e.setColor(color.x, color.y, color.z);
	}

}
