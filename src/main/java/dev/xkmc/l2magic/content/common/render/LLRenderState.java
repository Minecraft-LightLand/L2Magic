package dev.xkmc.l2magic.content.common.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LLRenderState extends RenderStateShard {

	public static RenderType get2DIcon(ResourceLocation rl) {
		return RenderType.create(
				"entity_body_icon",
				DefaultVertexFormat.POSITION_TEX,
				VertexFormat.Mode.QUADS, 256, false, true,
				RenderType.CompositeState.builder()
						.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER)
						.setTextureState(new TextureStateShard(rl, false, false))
						.setTransparencyState(ADDITIVE_TRANSPARENCY)
						.setDepthTestState(NO_DEPTH_TEST)
						.createCompositeState(false)
		);
	}

	public static RenderType getSpell() {
		return RenderType.create(
				"spell_blend_notex",
				DefaultVertexFormat.POSITION_COLOR,
				VertexFormat.Mode.QUADS, 256, true, true,
				RenderType.CompositeState.builder()
						.setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
						.setTextureState(RenderStateShard.NO_TEXTURE)
						.setCullState(NO_CULL)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.createCompositeState(false)
		);
	}

	private LLRenderState(String str, Runnable a, Runnable b) {
		super(str, a, b);
	}
}
