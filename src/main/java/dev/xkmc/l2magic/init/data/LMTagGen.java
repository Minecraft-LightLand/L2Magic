package dev.xkmc.l2magic.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LMTagGen {

	public static final TagKey<Block> BLACKLIST = BlockTags.create(L2Magic.loc("blacklist"));

	public static void genBlockTag(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(LMTagGen.BLACKLIST)
				.add(Blocks.BARRIER,
						Blocks.BEDROCK,
						Blocks.END_PORTAL,
						Blocks.END_PORTAL_FRAME,
						Blocks.END_GATEWAY,
						Blocks.COMMAND_BLOCK,
						Blocks.REPEATING_COMMAND_BLOCK,
						Blocks.CHAIN_COMMAND_BLOCK,
						Blocks.STRUCTURE_BLOCK,
						Blocks.JIGSAW,
						Blocks.MOVING_PISTON,
						Blocks.LIGHT
				).addOptional(ResourceLocation.fromNamespaceAndPath("powertool", "holographic_sign"));
	}

}
