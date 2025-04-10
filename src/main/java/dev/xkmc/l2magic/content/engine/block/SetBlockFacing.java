package dev.xkmc.l2magic.content.engine.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record SetBlockFacing(
		BlockState state
) implements IBlockProcessor<SetBlockFacing> {

	public static final MapCodec<SetBlockFacing> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BlockUtils.BLOCK_OR_STATE.fieldOf("state").forGetter(SetBlockFacing::state)
	).apply(i, SetBlockFacing::new));

	@Override
	public EngineType<SetBlockFacing> type() {
		return EngineRegistry.SET_BLOCK_FACING.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var state = state();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			state = state.setValue(BlockStateProperties.FACING, Direction.getNearest(ctx.loc().dir()));
		}
		if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			var dir = Direction.getNearest(ctx.loc().dir().multiply(1, 0, 1));
			if (dir.getAxis() != Direction.Axis.Y) state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
		}
		if (state.hasProperty(BlockStateProperties.AXIS)) {
			state = state.setValue(BlockStateProperties.AXIS, Direction.getNearest(ctx.loc().dir()).getAxis());
		}
		BlockUtils.set(ctx, state);
	}

}
