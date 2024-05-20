package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ClientSpellHandler {

	public static void useSpell(int user, ResourceLocation spellId,
								Vec3 origin, Vec3 facing, double tickUsing, double power) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		var e = level.getEntity(user);
		if (!(e instanceof LivingEntity le)) return;
		var spell = level.registryAccess().registryOrThrow(EngineRegistry.SPELL)
				.get(spellId);
		if (spell == null) return;
		spell.execute(new SpellContext(le, origin, facing, tickUsing, power));
	}
}
