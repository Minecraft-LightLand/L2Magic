package dev.xkmc.l2magic.content.transport.tile.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2magic.content.transport.tile.base.SidedBlockEntity;
import dev.xkmc.l2magic.content.transport.tile.item.*;
import dev.xkmc.l2magic.init.registrate.LMBlocks;

public class ItemTransferBlock {

	public static final BlockEntityBlockMethod<SimpleItemNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_SIMPLE, SimpleItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedItemNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_ORDERED, OrderedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedItemNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_SYNCED, SyncedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeItemNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_DISTRIBUTE, DistributeItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<RetrieverItemNodeBlockEntity> RETRIEVE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_RETRIEVE, RetrieverItemNodeBlockEntity.class);

	public static final BlockEntityBlockMethod<SidedBlockEntity> SIDED = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_SIDED, SidedBlockEntity.class);

}
