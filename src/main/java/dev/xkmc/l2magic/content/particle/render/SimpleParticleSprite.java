package dev.xkmc.l2magic.content.particle.render;

import net.minecraft.resources.ResourceLocation;

public record SimpleParticleSprite(RenderType renderType, ResourceLocation particle) implements VanillaParticleSprite {

}
