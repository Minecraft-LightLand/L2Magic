package dev.xkmc.l2magic.content.transport.core;

import dev.xkmc.l2magic.content.transport.api.IContentToken;
import dev.xkmc.l2magic.content.transport.api.INetworkNode;
import dev.xkmc.l2magic.content.transport.api.INodeSupplier;
import dev.xkmc.l2magic.content.transport.api.TransportContext;
import net.minecraft.core.BlockPos;

import java.util.function.BiFunction;

public record SimpleNodeSupplier<T>(BlockPos pos, boolean isValid,
									BiFunction<TransportContext<T>, IContentToken<T>, INetworkNode<T>> factory) implements INodeSupplier<T> {

	@Override
	public INetworkNode<T> constructNode(TransportContext<T> ctx, IContentToken<T> token) {
		return factory.apply(ctx, token);
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}
}
