package dev.xkmc.l2magic.content.altar.tile;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class AltarTableBlockEntity extends BaseBlockEntity {

	public AltarTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
}
