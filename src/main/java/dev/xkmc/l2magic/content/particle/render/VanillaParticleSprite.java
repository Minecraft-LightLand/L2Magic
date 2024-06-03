package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.mixin.ParticleEngineAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;

public record VanillaParticleSprite(ResourceLocation type) implements SpriteData {

	@Override
	public SpriteSet spriteSet() {
		return ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).getSpriteSets().get(type);
	}

}
