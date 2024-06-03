package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;

public record ItemSprite(ItemStack stack, boolean breaking) implements ModelSpriteData {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		var model = Minecraft.getInstance().getItemRenderer().getModel(stack, e.level(), null, 0);
		var x = model.getOverrides().resolve(model, stack, e.level(), null, 0);
		e.setSprite((x == null ? model : x).getParticleIcon(ModelData.EMPTY));
		if (breaking) e.setGeom(SpriteGeom.breaking(e.random()));
	}

}
