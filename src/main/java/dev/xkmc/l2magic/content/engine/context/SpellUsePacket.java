package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record SpellUsePacket(
		int user, long seed, ResourceLocation spell,
		Vec3 origin, Vec3 facing, Vec3 normal,
		double tickUsing, double power, int delay
) implements SerialPacketBase<SpellUsePacket> {


	public static SpellUsePacket of(Holder<SpellAction> sp, SpellContext ctx) {
		var key = sp.unwrapKey().orElseThrow().location();
		return new SpellUsePacket(ctx.user().getId(), ctx.seed(), key,
				ctx.origin(), ctx.facing().forward(), ctx.facing().normal(),
				ctx.tickUsing(), ctx.power(), ctx.delay());
	}

	@Override
	public void handle(Player player) {
		ClientSpellHandler.useSpell(user, spell, origin, Orientation.of(facing, normal), seed, tickUsing, power, delay);
	}

}
