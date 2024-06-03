package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.mixin.ParticleEngineAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record VanillaParticleSprite(RenderType renderType, ResourceLocation particle) implements SpriteData {

	@OnlyIn(Dist.CLIENT)
	@Override
	public SpriteSet spriteSet() {
		return ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).getSpriteSets().get(particle);
	}

}
