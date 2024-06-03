package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record BlockSprite(BlockState state, BlockPos pos, boolean breaking) implements ModelSpriteData {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		var shaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
		e.setSprite(shaper.getTexture(state, e.level(), pos));
		int i = Minecraft.getInstance().getBlockColors().getColor(state, e.level(), pos, 0);
		e.setColor(
				(i >> 16 & 255) / 255.0F,
				(i >> 8 & 255) / 255.0F,
				(i & 255) / 255.0F
		);
		if (breaking) e.setGeom(SpriteGeom.breaking(e.random()));
	}

}
