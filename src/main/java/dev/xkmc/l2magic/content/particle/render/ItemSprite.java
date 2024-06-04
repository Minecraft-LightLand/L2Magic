package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;

public record ItemSprite(RenderTypePreset renderType, ItemStack stack, SpriteGeom goem) implements ModelSpriteData {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		var model = Minecraft.getInstance().getItemRenderer().getModel(stack, e.level(), null, 0);
		var x = model.getOverrides().resolve(model, stack, e.level(), null, 0);
		e.setSprite((x == null ? model : x).getParticleIcon(ModelData.EMPTY));
		e.setGeom(goem);
	}

}
