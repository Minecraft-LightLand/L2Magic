package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ActivatableBlockMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof ActivatableBlockEntity entity) {
			return entity.activate(player.getItemInHand(hand)) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

}
