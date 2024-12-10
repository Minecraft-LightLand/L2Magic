package dev.xkmc.l2magic.content.engine.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public record RemoveBlock(
		RemoveBlock.Type method
) implements IBlockProcessor<RemoveBlock> {

	public enum Type {
		DROP, DESTROY, REMOVE, SET_AIR;

		public RemoveBlock get() {
			return new RemoveBlock(this);
		}
	}

	public static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final MapCodec<RemoveBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			TYPE_CODEC.fieldOf("method").forGetter(RemoveBlock::method)
	).apply(i, RemoveBlock::new));

	@Override
	public EngineType<RemoveBlock> type() {
		return EngineRegistry.REMOVE_BLOCK.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var level = ctx.user().level();
		if (level.isClientSide()) return;
		var pos = BlockPos.containing(ctx.loc().pos());
		var state = level.getBlockState(pos);
		if (!BlockUtils.allowModification(level, pos)) return;
		if (state.isAir()) return;
		switch (method) {
			case DROP -> level.destroyBlock(pos, true, ctx.user().user(), 16);
			case DESTROY -> level.destroyBlock(pos, false, ctx.user().user(), 16);
			case REMOVE -> level.removeBlock(pos, false);
			case SET_AIR -> level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

}
