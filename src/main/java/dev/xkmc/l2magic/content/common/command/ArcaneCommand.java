package dev.xkmc.l2magic.content.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2magic.content.arcane.internal.*;
import dev.xkmc.l2magic.content.arcane.item.ArcaneAxe;
import dev.xkmc.l2magic.content.arcane.item.ArcaneSword;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.network.packets.CapToClient;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ArcaneCommand extends BaseCommand {

	public ArcaneCommand(LiteralArgumentBuilder<CommandSourceStack> lightland) {
		super(lightland, "arcane");
	}

	public void register() {
		registerCommand("unlock", getPlayer()
				.then(Commands.argument("type", RegistryParser.ARCANE_TYPE)
						.executes(withPlayer((context, e) -> {
							ArcaneType type = context.getArgument("type", ArcaneType.class);
							MagicData magic = MagicData.get(e);
							magic.magicAbility.unlockArcaneType(type, true);
							new CapToClient(CapToClient.Action.ARCANE_TYPE, magic).toClientPlayer(e);
							send(context, LangData.IDS.ACTION_SUCCESS.get());
							return 1;
						}))));

		registerCommand("list", getPlayer()
				.executes(withPlayer((context, e) -> {
					MagicData magic = MagicData.get(e);
					MutableComponent comps = Component.literal("[");
					for (ArcaneType type : MagicRegistry.ARCANE_TYPE.get().getValues()) {
						boolean bool = magic.magicAbility.isArcaneTypeUnlocked(type);
						MutableComponent lock = (bool ? LangData.IDS.UNLOCKED : LangData.IDS.LOCKED).get();
						comps.append(type.getDesc().append(": ").append(lock).append(",\n"));
					}
					Component comp = comps.append("]");
					send(context, comp);
					return 1;
				})));


		registerCommand("give_mana", getPlayer()
				.then(Commands.argument("number", IntegerArgumentType.integer())
						.executes(withPlayer((context, e) -> {
							ItemStack stack = e.getMainHandItem();
							if (!ArcaneItemUseHelper.isArcaneItem(stack)) {
								send(context, LangData.IDS.WRONG_ITEM.get());
								return 0;
							}
							ArcaneItemUseHelper.addArcaneMana(stack, context.getArgument("number", Integer.class));
							send(context, LangData.IDS.ACTION_SUCCESS.get());
							return 1;
						}))));

		registerCommand("get_mana", getPlayer()
				.executes(withPlayer((context, e) -> {
					ItemStack stack = e.getMainHandItem();
					if (!ArcaneItemUseHelper.isArcaneItem(stack)) {
						send(context, LangData.IDS.WRONG_ITEM.get());
						return 0;
					}
					IArcaneItem item = (IArcaneItem) stack.getItem();
					int mana = ArcaneItemUseHelper.getArcaneMana(stack);
					int max = item.getMaxMana(stack);
					send(context, LangData.IDS.GET_ARCANE_MANA.get(mana, max));
					return 1;
				})));

		registerCommand("set_arcane", getPlayer()
				.then(Commands.argument("arcane", RegistryParser.ARCANE)
						.executes(withPlayer((context, e) -> {
							ItemStack stack = e.getMainHandItem();
							Arcane arcane = context.getArgument("arcane", Arcane.class);
							if (arcane == null || stack.isEmpty() ||
									arcane.type.get().weapon == ArcaneType.Weapon.AXE && !(stack.getItem() instanceof ArcaneAxe) ||
									arcane.type.get().weapon == ArcaneType.Weapon.SWORD && !(stack.getItem() instanceof ArcaneSword)) {
								send(context, LangData.IDS.WRONG_ITEM.get());
								return 0;
							}
							ArcaneItemCraftHelper.setArcaneOnItem(stack, arcane);
							send(context, LangData.IDS.ACTION_SUCCESS.get());
							return 1;
						}))));

		registerCommand("get_arcane", getPlayer()
				.executes(withPlayer((context, e) -> {
					ItemStack stack = e.getMainHandItem();
					if (!ArcaneItemUseHelper.isArcaneItem(stack)) {
						send(context, LangData.IDS.WRONG_ITEM.get());
						return 0;
					}
					MutableComponent list = Component.literal("[");
					for (Arcane a : ArcaneItemCraftHelper.getAllArcanesOnItem(stack)) {
						list.append(a.type.get().getDesc().append(": ")
								.append(a.getDesc()).append(",\n"));
					}
					send(context, list.append("]"));
					return 1;
				})));
	}

}
