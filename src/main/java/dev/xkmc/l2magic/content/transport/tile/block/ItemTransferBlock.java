package dev.xkmc.l2magic.content.transport.tile.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2magic.content.transport.tile.item.DistributeItemNodeBlockEntity;
import dev.xkmc.l2magic.content.transport.tile.item.OrderedItemNodeBlockEntity;
import dev.xkmc.l2magic.content.transport.tile.item.SimpleItemNodeBlockEntity;
import dev.xkmc.l2magic.content.transport.tile.item.SyncedItemNodeBlockEntity;
import dev.xkmc.l2magic.init.registrate.LMBlocks;

public class ItemTransferBlock {

	public static final BlockEntityBlockMethod<SimpleItemNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_SIMPLE, SimpleItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedItemNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_ORDERED, OrderedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedItemNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_SYNCED, SyncedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeItemNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ITEM_DISTRIBUTE, DistributeItemNodeBlockEntity.class);

}
