package dev.xkmc.l2magic.content.altar.tile;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class AltarBaseBlockEntity extends BaseBlockEntity {

	@SerialClass.SerialField
	public int dx, dz;



	public AltarBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SerialClass.SerialField
	private boolean isDirty;

	@Nullable
	@SerialClass.SerialField
	private BlockPos dirtyPos;

}
