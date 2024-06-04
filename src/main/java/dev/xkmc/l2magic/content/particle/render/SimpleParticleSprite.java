package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.resources.ResourceLocation;

public record SimpleParticleSprite(RenderTypePreset renderType, ResourceLocation particle) implements VanillaParticleSprite {

}
