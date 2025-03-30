package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.block.RemoveBlock;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockMatchCondition;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BlockSpells extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> WEBS = spell("cobwebs");
	public static final ResourceKey<SpellAction> FLOOR = spell("floor");
	public static final ResourceKey<SpellAction> MELT = spell("melt");
	public static final ResourceKey<SpellAction> SWEPT = spell("swept");

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(cobwebs(new DataGenContext(ctx)),
				Items.COBWEB, 612, SpellCastType.INSTANT, SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, WEBS);
		new SpellAction(floor(new DataGenContext(ctx)),
				Items.STONE, 622, SpellCastType.INSTANT, SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, FLOOR);
		new SpellAction(melt(new DataGenContext(ctx)),
				Items.MAGMA_BLOCK, 632, SpellCastType.INSTANT, SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, MELT);
		new SpellAction(swept(new DataGenContext(ctx)),
				Items.BRUSH, 642, SpellCastType.INSTANT, SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SWEPT);
	}

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(WEBS.location()), "Spider Nest");
		pvd.add(SpellAction.lang(FLOOR.location()), "Floor");
		pvd.add(SpellAction.lang(MELT.location()), "Melt");
		pvd.add(SpellAction.lang(SWEPT.location()), "Swpet");
	}


	private static ConfiguredEngine<?> floor(DataGenContext ctx) {
		return new SetBlock(Blocks.STONE.defaultBlockState()).circular("3", "0", true, null,
				BlockTestCondition.Type.REPLACEABLE.get()).move(OffsetModifier.BELOW);
	}

	private static ConfiguredEngine<?> cobwebs(DataGenContext ctx) {
		return new SetBlock(Blocks.COBWEB.defaultBlockState()).circular("6", "2", false, null,
				BlockTestCondition.Type.REPLACEABLE.get(), SurfaceBelowCondition.full());
	}

	private static ConfiguredEngine<?> melt(DataGenContext ctx) {
		return new SetBlock(Blocks.MAGMA_BLOCK.defaultBlockState()).circular("6", "2", false, null,
				BlockTestCondition.Type.BLOCKS_MOTION.get().move(OffsetModifier.ABOVE), BlockMatchCondition.of(BlockTags.SCULK_REPLACEABLE));
	}

	private static ConfiguredEngine<?> swept(DataGenContext ctx) {
		return RemoveBlock.Type.DROP.get().circular("6", "2", false, null,
				BlockTestCondition.Type.BLOCKS_MOTION.get().invert());
	}

}
