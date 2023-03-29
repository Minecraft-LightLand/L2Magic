package dev.xkmc.l2magic.content.altar.tile.craft;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DoubleSidedCall;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2magic.content.altar.recipe.AltarRecipeRing;
import dev.xkmc.l2magic.content.altar.tile.structure.AltarCoreBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

@SerialClass
public class CraftManager {

	@SerialClass.SerialField
	private final TreeSet<BlockPos> tables = new TreeSet<>();

	private final AltarCoreBlockEntity parent;

	public CraftManager(AltarCoreBlockEntity parent) {
		this.parent = parent;
	}

	@ServerOnly
	public void terminate() {
		tables.clear();
	}

	@DoubleSidedCall
	public boolean activate() {
		return false;
	}

	@DoubleSidedCall
	public void tick() {
		if (parent.getLevel() != null && !parent.getLevel().isClientSide()) {

		}
		// client code
	}

	public void addTable(BlockPos pos, boolean add) {
		if (add) tables.add(pos);
		else tables.remove(pos);
	}

	public boolean ringMatch(Collection<AltarRecipeRing> ingredients, boolean simulate) {
		if (parent.getLevel() == null) return false;
		CraftRingList list = new CraftRingList(parent.getLevel(), parent.getBlockPos(), tables);
		return list.ringMatch(ingredients, simulate);
	}
}
