package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;

public enum RenderTypePreset {
	NORMAL, LIT, TRANSLUCENT, BLOCK, BLOCK_LIT;

	public static final Codec<RenderTypePreset> CODEC =
			EngineHelper.enumCodec(RenderTypePreset.class, RenderTypePreset.values());
}
