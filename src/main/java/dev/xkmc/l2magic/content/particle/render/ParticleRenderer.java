package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.client.Camera;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ParticleRenderer {

	@OnlyIn(Dist.CLIENT)
	void onParticleInit(LMGenericParticle e);

	@OnlyIn(Dist.CLIENT)
	void onPostTick(LMGenericParticle e);

	RenderTypePreset renderType();

	@OnlyIn(Dist.CLIENT)
	boolean specialRender(LMGenericParticle lmGenericParticle, VertexConsumer vc, Camera camera, float pTick);

	default boolean needOrientation(){
		return false;
	}

}
