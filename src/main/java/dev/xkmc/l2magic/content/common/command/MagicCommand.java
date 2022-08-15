package dev.xkmc.l2magic.content.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.magic.item.MagicScroll;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.network.packets.CapToClient;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class MagicCommand extends BaseCommand {

	public MagicCommand(LiteralArgumentBuilder<CommandSourceStack> lightland) {
		super(lightland, "magic");
	}

	public void register() {

		registerCommand("set_spell", getPlayer()
				.then(Commands.argument("spell", RegistryParser.SPELL)
						.executes(withPlayer((context, e) -> {
							ItemStack stack = e.getMainHandItem();
							Spell<?, ?> spell = context.getArgument("spell", Spell.class);
							ServerLevel world = context.getSource().getLevel();
							if (spell == null || stack.isEmpty() ||
									!(stack.getItem() instanceof MagicScroll) ||
									spell.getConfig(world, e).type != ((MagicScroll) stack.getItem()).type) {
								send(context, LangData.IDS.WRONG_ITEM.get());
								return 0;
							}
							MagicScroll.initItemStack(spell, stack);
							send(context, LangData.IDS.ACTION_SUCCESS.get());
							return 1;
						}))));

		registerCommand("add_spell_slot", getPlayer()
				.then(Commands.argument("slot", IntegerArgumentType.integer(0, 10))
						.executes(withPlayer((context, e) -> {
							MagicData handler = MagicData.get(e);
							int slot = context.getArgument("slot", Integer.class);
							handler.magicAbility.spell_level += slot;
							new CapToClient(CapToClient.Action.MAGIC_ABILITY, handler).toClientPlayer(e);
							send(context, LangData.IDS.SPELL_SLOT.get(handler.magicAbility.getMaxSpellSlot()));
							return 1;
						}))));

		registerCommand("master_element", getPlayer()
				.then(Commands.argument("elem", RegistryParser.ELEMENT)
						.executes(withPlayer((context, e) -> {
							MagicData handler = MagicData.get(e);
							MagicElement elem = context.getArgument("elem", MagicElement.class);
							handler.magicHolder.addElementalMastery(elem);
							new CapToClient(CapToClient.Action.ALL, handler).toClientPlayer(e);
							send(context, LangData.IDS.ACTION_SUCCESS.get());
							return 1;
						}))));

	}

}
