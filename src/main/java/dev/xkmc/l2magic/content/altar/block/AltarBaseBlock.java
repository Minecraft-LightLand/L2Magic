package dev.xkmc.l2magic.content.altar.block;

import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2magic.content.altar.methods.*;
import dev.xkmc.l2magic.content.altar.tile.AltarBaseBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarCoreBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarHolderBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarTableBlockEntity;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class AltarBaseBlock {

	private static final Lazy<DelegateBlockProperties> STONE, PILLAR, CORE;

	static {
		STONE = Lazy.of(() -> DelegateBlockProperties.copy(Blocks.STONE).make(e -> e.lightLevel(bs -> AltarBaseState.isPowered(bs) ? 7 : 0)));
		PILLAR = Lazy.of(() -> DelegateBlockProperties.copy(Blocks.STONE).make(e -> e.noOcclusion()
				.lightLevel(bs -> bs.getValue(AltarPillarState.PILLAR).lighted(bs.getValue(AltarPillarState.GROUNDED)))));
		CORE = Lazy.of(() -> DelegateBlockProperties.copy(Blocks.STONE).make(e -> e.lightLevel(bs -> 15).noOcclusion()));
	}

	public static final BlockEntityBlockMethod<AltarBaseBlockEntity> TE_BASE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ALTAR_BASE, AltarBaseBlockEntity.class);
	public static final BlockEntityBlockMethod<AltarTableBlockEntity> TE_TABLE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ALTAR_TABLE, AltarTableBlockEntity.class);
	public static final BlockEntityBlockMethod<AltarHolderBlockEntity> TE_HOLDER = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ALTAR_HOLDER, AltarHolderBlockEntity.class);
	public static final BlockEntityBlockMethod<AltarCoreBlockEntity> TE_CORE = new BlockEntityBlockMethodImpl<>(LMBlocks.TE_ALTAR_CORE, AltarCoreBlockEntity.class);

	private static final DelayedTicker TICKER = new DelayedTicker();
	private static final EntityTicker ENTITY_TICKER = new EntityTicker();

	public static final Supplier<DelegateBlock> ALTAR_BASE, ALTAR_TABLE, ALTAR_HOLDER, ALTAR_PILLAR, ALTAR_CORE;

	static {
		ALTAR_BASE = () -> DelegateBlock.newBaseBlock(STONE.get(), TICKER, new AltarBaseState(), TE_BASE);
		ALTAR_TABLE = () -> DelegateBlock.newBaseBlock(PILLAR.get(), TICKER, new AltarPillarState(PillarStatus.CONNECTED), TE_TABLE);
		ALTAR_HOLDER = () -> DelegateBlock.newBaseBlock(PILLAR.get(), TICKER, ENTITY_TICKER, new AltarPillarState(PillarStatus.POWERED), TE_HOLDER);
		ALTAR_PILLAR = () -> DelegateBlock.newBaseBlock(PILLAR.get(), TICKER, new AltarPillarState(PillarStatus.DARK));
		ALTAR_CORE = () -> DelegateBlock.newBaseBlock(CORE.get(), TICKER, ENTITY_TICKER, new AltarCoreState(), TE_CORE);
	}

}
