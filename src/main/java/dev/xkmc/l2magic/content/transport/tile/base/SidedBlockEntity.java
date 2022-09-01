package dev.xkmc.l2magic.content.transport.tile.base;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class SidedBlockEntity extends BlockEntity {

	public SidedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (level != null) {
			Direction dire = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos pos = getBlockPos().relative(dire);
			BlockEntity be = level.getBlockEntity(pos);
			if (be != null) {
				return be.getCapability(cap, dire.getOpposite());
			}
		}
		return super.getCapability(cap, side);
	}

}
