package dev.xkmc.l2magic.compat;

import dev.xkmc.l2library.util.annotation.ServerOnly;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.UUID;

@ParametersAreNullableByDefault
public class TeamAccessor {

	public static final boolean PLAYER_HURT = false;

	@ServerOnly
	public static boolean arePlayersInSameTeam(ServerPlayer a, ServerPlayer b) {
		if (a == null || b == null) return false;
		if (b.isAlliedTo(a) || a.isAlliedTo(b)) return true;
		return playerSameTeam(a.getUUID(), b.getUUID());
	}

	@ServerOnly
	public static boolean arePlayerAndEntityInSameTeam(ServerPlayer a, LivingEntity b) {
		if (a == null || b == null) return false;
		if (b.isAlliedTo(a) || a.isAlliedTo(b)) return true;
		UUID bid = getPotentialOwner(b);
		return bid != null && playerSameTeam(a.getUUID(), bid);
	}

	@ServerOnly
	public static boolean areEntitiesInSameTeam(LivingEntity a, LivingEntity b) {
		if (a == null || b == null) return false;
		if (b.isAlliedTo(a) || a.isAlliedTo(b)) return true;
		UUID aid = getPotentialOwner(a);
		UUID bid = getPotentialOwner(b);
		if (aid != null && bid != null) {
			return playerSameTeam(aid, bid);
		}
		return false;
	}

	@Nullable
	public static UUID getPotentialOwner(Entity e) {
		if (e instanceof ServerPlayer) {
			return e.getUUID();
		}
		if (e instanceof TamableAnimal a) {
			return a.getOwnerUUID();
		}
		return null;
	}

	private static boolean playerSameTeam(UUID a, UUID b) {
		if (!PLAYER_HURT) return true;
		if (ModList.get().isLoaded("ftbteams")) {
			return ftbSameTeamUnsafe(a, b);
		}
		return false;
	}

	private static boolean ftbSameTeamUnsafe(UUID a, UUID b) {
		return false;//TODO FTBTeamsAPI.arePlayersInSameTeam(a, b);
	}

}
