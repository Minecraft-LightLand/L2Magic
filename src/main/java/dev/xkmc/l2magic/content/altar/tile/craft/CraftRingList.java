package dev.xkmc.l2magic.content.altar.tile.craft;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2magic.content.altar.recipe.AltarRecipeRing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class CraftRingList {

	private final Level level;
	private final TreeMap<Integer, Ring> map = new TreeMap<>();

	public CraftRingList(Level level, BlockPos core, Collection<BlockPos> list) {
		this.level = level;
		for (BlockPos pos : list) {
			int dis = (int) Math.round(pos.distSqr(core));
			map.computeIfAbsent(dis, i -> new Ring()).add(pos);
		}
	}

	public boolean ringMatch(Collection<AltarRecipeRing> ingredients, boolean simulate) {
		List<Ring> list = new ArrayList<>(map.values());
		List<Pair<Ring, AltarRecipeRing>> matched = new ArrayList<>();
		for (AltarRecipeRing recipe : ingredients) {
			Pair<Ring, AltarRecipeRing> pair = null;
			for (Ring ring : list) {
				if (ring.match(recipe, true)) {
					pair = Pair.of(ring, recipe);
					break;
				}
			}
			if (pair == null) return false;
			matched.add(pair);
			list.remove(pair.getFirst());
		}
		if (simulate) return true;
		for (var pair : matched) {
			pair.getFirst().match(pair.getSecond(), false);
		}
		return true;
	}

	private class Ring {

		private final ArrayList<AltarProvider> list = new ArrayList<>();

		public void add(BlockPos pos) {
			if (level.getBlockEntity(pos) instanceof AltarProvider pvd) {
				list.add(pvd);
			}
		}

		private boolean match(AltarRecipeRing ring, boolean simulate) {
			return false;//TODO
		}

	}

}
