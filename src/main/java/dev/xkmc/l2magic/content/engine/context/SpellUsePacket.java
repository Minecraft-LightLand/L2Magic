package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SpellUsePacket extends SerialPacketBase {

	@SerialClass.SerialField
	public int user;
	@SerialClass.SerialField
	public long seed;
	@SerialClass.SerialField
	public ResourceLocation spell;
	@SerialClass.SerialField
	public Vec3 origin, facing, normal;
	@SerialClass.SerialField
	public double tickUsing, power;

	@Deprecated
	public SpellUsePacket() {
	}

	public SpellUsePacket(SpellAction sp, SpellContext ctx) {
		user = ctx.user().getId();
		spell = ctx.user().level().registryAccess().registryOrThrow(EngineRegistry.SPELL).getKey(sp);
		origin = ctx.origin();
		facing = ctx.facing().forward();
		normal = ctx.facing().normal();
		tickUsing = ctx.tickUsing();
		power = ctx.power();
		seed = ctx.seed();
	}


	@Override
	public void handle(NetworkEvent.Context context) {
		ClientSpellHandler.useSpell(user, spell, origin, Orientation.of(facing, normal), seed, tickUsing, power);
	}
}
