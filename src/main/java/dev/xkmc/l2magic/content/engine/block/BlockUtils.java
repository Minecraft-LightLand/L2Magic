package dev.xkmc.l2magic.content.engine.block;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.init.data.LMTagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockUtils {

	public static final Codec<BlockState> BLOCK_OR_STATE = Codec.either(
			BuiltInRegistries.BLOCK.byNameCodec(), BlockState.CODEC
	).xmap(e -> e.map(Block::defaultBlockState, r -> r),
			e -> e == e.getBlock().defaultBlockState() ?
					Either.left(e.getBlock()) : Either.right(e));


	public static boolean test(BlockState state, ExtraCodecs.TagOrElementLocation block) {
		if (block.tag()) {
			TagKey<Block> key = TagKey.create(Registries.BLOCK, block.id());
			return state.is(key);
		}
		return state.is(BuiltInRegistries.BLOCK.get(block.id()));
	}

	public static void set(EngineContext ctx, BlockState state) {
		var level = ctx.user().level();
		if (level.isClientSide()) return;
		var pos = BlockPos.containing(ctx.loc().pos());
		if (!allowModification(level, pos)) return;
		var liquid = level.getFluidState(pos);
		if (liquid.is(FluidTags.WATER) && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			state = state.setValue(BlockStateProperties.WATERLOGGED, true);
		}
		level.setBlockAndUpdate(pos, state);
	}

	public static boolean allowModification(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return !state.is(LMTagGen.BLACKLIST);
	}

}
