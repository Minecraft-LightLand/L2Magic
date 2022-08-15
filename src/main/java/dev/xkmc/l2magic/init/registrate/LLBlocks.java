package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.block.RitualRenderer;
import dev.xkmc.l2magic.content.magic.block.RitualSide;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * handles blocks and block entities
 */
public class LLBlocks {

	static {
		LightLand.REGISTRATE.creativeModeTab(() -> LLItems.TAB_MAIN);
	}

	public static final BlockEntry<DelegateBlock> B_RITUAL_CORE, B_RITUAL_SIDE;
	public static final BlockEntry<Block> ENCHANT_GOLD_BLOCK, MAGICIUM_BLOCK;

	public static final BlockEntry<AnvilBlock> ETERNAL_ANVIL = LightLand.REGISTRATE
			.block("eternal_anvil", p -> new AnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)))
			.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().withExistingParent(ctx.getName(), "anvil")))
			.register();

	public static final BlockEntityEntry<RitualCore.TE> TE_RITUAL_CORE;
	public static final BlockEntityEntry<RitualSide.TE> TE_RITUAL_SIDE;

	static {
		{
			DelegateBlockProperties PEDESTAL = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
					.isRedstoneConductor((a, b, c) -> false));
			B_RITUAL_CORE = LightLand.REGISTRATE.block("ritual_core",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
									BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().getExistingFile(
							new ResourceLocation(LightLand.MODID, "block/ritual_core"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			B_RITUAL_SIDE = LightLand.REGISTRATE.block("ritual_side",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().getExistingFile(
							new ResourceLocation(LightLand.MODID, "block/ritual_side"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			TE_RITUAL_CORE = LightLand.REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
					.validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();
			TE_RITUAL_SIDE = LightLand.REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
					.validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();
		}
		{
			ENCHANT_GOLD_BLOCK = LightLand.REGISTRATE.block("enchant_gold_block", p ->
							new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			MAGICIUM_BLOCK = LightLand.REGISTRATE.block("magicium_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
		}
	}

	public static void register() {
	}

}
