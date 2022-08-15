package dev.xkmc.l2magic.content.common.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.xkmc.l2magic.init.data.LangData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class BaseCommand {

	public static final List<Consumer<LiteralArgumentBuilder<CommandSourceStack>>> LIST = new ArrayList<>();

	public static RequiredArgumentBuilder<CommandSourceStack, ?> getPlayer() {
		return Commands.argument("player", GameProfileArgument.gameProfile());
	}

	public static Command<CommandSourceStack> withPlayer(BiFunction<CommandContext<CommandSourceStack>, ServerPlayer, Integer> then) {
		return (context) -> {

			GameProfileArgument.Result profile = context.getArgument("player", GameProfileArgument.Result.class);
			if (profile.getNames(context.getSource()).size() != 1) {
				send(context, LangData.IDS.PLAYER_NOT_FOUND.get());
				return 0;
			}
			GameProfile name = profile.getNames(context.getSource()).iterator().next();
			ServerPlayer e = (ServerPlayer) context.getSource().getLevel().getPlayerByUUID(name.getId());
			if (e == null) {
				send(context, LangData.IDS.PLAYER_NOT_FOUND.get());
				return 0;
			}
			return then.apply(context, e);
		};
	}

	public static void send(CommandContext<CommandSourceStack> context, Component comp) {
		context.getSource().sendSystemMessage(comp);
	}

	private final LiteralArgumentBuilder<CommandSourceStack> base;

	public BaseCommand(LiteralArgumentBuilder<CommandSourceStack> lightland, String id) {
		base = Commands.literal(id);
		register();
		lightland.then(base);
	}

	public abstract void register();

	public <T extends ArgumentBuilder<CommandSourceStack, T>> void registerCommand(String act, ArgumentBuilder<CommandSourceStack, T> builder) {
		base.then(Commands.literal(act)
				.requires(e -> e.hasPermission(2))
				.then(builder));
	}


}
