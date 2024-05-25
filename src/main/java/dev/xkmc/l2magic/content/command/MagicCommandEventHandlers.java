package dev.xkmc.l2magic.content.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Magic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicCommandEventHandlers {

	@SubscribeEvent
	public static void onCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(literal(L2Magic.MODID)
				.then(SpellCastCommand.build()));
	}

	protected static LiteralArgumentBuilder<CommandSourceStack> literal(String str) {
		return LiteralArgumentBuilder.literal(str);
	}

	protected static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

}
