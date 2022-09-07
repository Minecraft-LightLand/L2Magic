package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2magic.content.altar.block.AltarBaseBlock;
import dev.xkmc.l2magic.content.altar.methods.AltarBaseState;
import dev.xkmc.l2magic.content.altar.methods.AltarPillarState;
import dev.xkmc.l2magic.content.altar.methods.PillarStatus;
import dev.xkmc.l2magic.content.altar.tile.AltarBaseBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarCoreBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarHolderBlockEntity;
import dev.xkmc.l2magic.content.altar.tile.AltarTableBlockEntity;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.block.RitualRenderer;
import dev.xkmc.l2magic.content.magic.block.RitualSide;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

/**
 * handles blocks and block entities
 */
public class LMBlocks {

	static {
		L2Magic.REGISTRATE.creativeModeTab(() -> LMItems.TAB_MAIN);
	}

	public static final BlockEntry<DelegateBlock> B_RITUAL_CORE, B_RITUAL_SIDE;
	public static final BlockEntry<Block> ENCHANT_GOLD_BLOCK, MAGICIUM_BLOCK;

	public static final BlockEntityEntry<RitualCore.TE> TE_RITUAL_CORE;
	public static final BlockEntityEntry<RitualSide.TE> TE_RITUAL_SIDE;

	public static final BlockEntry<DelegateBlock> ALTAR_BASE, ALTAR_PILLAR, ALTAR_TABLE, ALTAR_HOLDER, ALTAR_CORE;
	public static final BlockEntityEntry<AltarBaseBlockEntity> TE_ALTAR_BASE;
	public static final BlockEntityEntry<AltarTableBlockEntity> TE_ALTAR_TABLE;
	public static final BlockEntityEntry<AltarHolderBlockEntity> TE_ALTAR_HOLDER;
	public static final BlockEntityEntry<AltarCoreBlockEntity> TE_ALTAR_CORE;

	static {
		{
			DelegateBlockProperties PEDESTAL = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
					.isRedstoneConductor((a, b, c) -> false));
			B_RITUAL_CORE = L2Magic.REGISTRATE.block("ritual_core",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
									BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().getExistingFile(
							new ResourceLocation(L2Magic.MODID, "block/ritual_core"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			B_RITUAL_SIDE = L2Magic.REGISTRATE.block("ritual_side",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().getExistingFile(
							new ResourceLocation(L2Magic.MODID, "block/ritual_side"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			TE_RITUAL_CORE = L2Magic.REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
					.validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();
			TE_RITUAL_SIDE = L2Magic.REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
					.validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();
		}
		{
			ENCHANT_GOLD_BLOCK = L2Magic.REGISTRATE.block("enchant_gold_block", p ->
							new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			MAGICIUM_BLOCK = L2Magic.REGISTRATE.block("magicium_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
		}
		{

			DelegateBlockProperties STONE = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.lightLevel(bs -> AltarBaseState.isPowered(bs) ? 7 : 0));

			DelegateBlockProperties PILLAR = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.lightLevel(bs -> bs.getValue(AltarPillarState.PILLAR).lighted(bs.getValue(AltarPillarState.GROUNDED)))
					.noOcclusion());

			DelegateBlockProperties CORE = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.lightLevel(bs -> 15).noOcclusion());

			ALTAR_BASE = L2Magic.REGISTRATE.block("altar_base", p -> AltarBaseBlock.ALTAR_BASE.get())
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state ->
							ConfiguredModel.builder().modelFile(pvd.models().getExistingFile(new ResourceLocation(
									AltarBaseState.isPowered(state) ? "blackstone" : "cobblestone"))).build()))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang()
					.item().model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation("item/coal"))).build().register();

			ALTAR_PILLAR = L2Magic.REGISTRATE.block("altar_pillar", p -> AltarBaseBlock.ALTAR_PILLAR.get())
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state ->
							ConfiguredModel.builder().modelFile(pvd.models().getExistingFile(new ResourceLocation(
									(state.getValue(AltarPillarState.PILLAR) == PillarStatus.DARK ?
											state.getValue(AltarPillarState.GROUNDED) ? "cobbled_deepslate" : "cobblestone" :
											state.getValue(AltarPillarState.PILLAR) == PillarStatus.CONNECTED ?
													state.getValue(AltarPillarState.GROUNDED) ? "deepslate_brick" : "stone_brick" :
													state.getValue(AltarPillarState.GROUNDED) ? "end_stone_brick" : "blackstone")
											+ "_wall_post"))).build()
					)).tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang()
					.item().model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation("item/stick"))).build().register();

			ALTAR_TABLE = L2Magic.REGISTRATE.block("altar_table", p -> AltarBaseBlock.ALTAR_TABLE.get())
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state ->
							ConfiguredModel.builder().modelFile(pvd.models().getExistingFile(new ResourceLocation(
									state.getValue(AltarPillarState.GROUNDED) ? "diamond_block" : "diamond_ore"
							))).build()))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang()
					.item().model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation("item/campfire"))).build().register();

			ALTAR_HOLDER = L2Magic.REGISTRATE.block("altar_holder", p -> AltarBaseBlock.ALTAR_HOLDER.get())
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state ->
							ConfiguredModel.builder().modelFile(pvd.models().getExistingFile(new ResourceLocation(
									state.getValue(AltarPillarState.GROUNDED) ?
											state.getValue(AltarPillarState.PILLAR).connectsHolder() ?
													"diamond_block" : "diamond_ore" :
											state.getValue(AltarPillarState.PILLAR).connectsHolder() ?
													"gold_block" : "gold_ore"
							))).build()))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang()
					.item().model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation("item/soul_campfire"))).build().register();

			ALTAR_CORE = L2Magic.REGISTRATE.block("altar_core", p -> AltarBaseBlock.ALTAR_CORE.get())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().getExistingFile(new ResourceLocation("bedrock"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang()
					.item().model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation("item/diamond"))).build().register();

			TE_ALTAR_BASE = L2Magic.REGISTRATE.blockEntity("altar_base", AltarBaseBlockEntity::new)
					.validBlock(ALTAR_BASE).register();
			TE_ALTAR_TABLE = L2Magic.REGISTRATE.blockEntity("altar_table", AltarTableBlockEntity::new)
					.validBlock(ALTAR_TABLE).register();
			TE_ALTAR_HOLDER = L2Magic.REGISTRATE.blockEntity("altar_holder", AltarHolderBlockEntity::new)
					.validBlock(ALTAR_HOLDER).register();
			TE_ALTAR_CORE = L2Magic.REGISTRATE.blockEntity("altar_core", AltarCoreBlockEntity::new)
					.validBlock(ALTAR_CORE).register();
		}
	}

	public static void register() {
	}

}
