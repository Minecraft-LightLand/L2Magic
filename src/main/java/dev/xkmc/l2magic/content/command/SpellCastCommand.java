package dev.xkmc.l2magic.content.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.init.data.LMLangData;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class SpellCastCommand extends MagicCommandEventHandlers {

	private static final DynamicCommandExceptionType ERR_INVALID_NAME =
			new DynamicCommandExceptionType(LMLangData.CMD_INVALID_SPELL::get);


	private static <T> ResourceKey<T> getRegistryKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<?> ans = ctx.getArgument(name, ResourceKey.class);
		Optional<ResourceKey<T>> optional = ans.cast(reg);
		return optional.orElseThrow(() -> err.create(ans));
	}

	private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> ctx, ResourceKey<? extends Registry<T>> reg) {
		return ctx.getSource().getServer().registryAccess().registryOrThrow(reg);
	}

	private static <T> Holder.Reference<T> resolveKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<T> ans = getRegistryKey(ctx, name, reg, err);
		return getRegistry(ctx, reg).getHolder(ans).orElseThrow(() -> err.create(ans.location()));
	}

	public static LiteralArgumentBuilder<CommandSourceStack> build() {
		return literal("cast").requires(e -> e.hasPermission(1))
				.then(argument("user", EntityArgument.entities())
						.then(Commands.argument("spell", ResourceKeyArgument.key(EngineRegistry.SPELL))
								.then(literal("instant")
										.executes(ctx -> run(ctx, SpellCastType.INSTANT, false))
										.then(argument("power", DoubleArgumentType.doubleArg(0))
												.executes(ctx -> run(ctx, SpellCastType.INSTANT, true))
										)
								).then(literal("charge")
										.then(argument("chargeTime", IntegerArgumentType.integer(0))
												.executes(ctx -> run(ctx, SpellCastType.CHARGE, false))
												.then(argument("power", DoubleArgumentType.doubleArg(0))
														.executes(ctx -> run(ctx, SpellCastType.CHARGE, true))
												)
										)
								).then(literal("continuous")
										.then(argument("duration", IntegerArgumentType.integer(0))
												.executes(ctx -> run(ctx, SpellCastType.CONTINUOUS, false))
												.then(argument("power", DoubleArgumentType.doubleArg(0))
														.executes(ctx -> run(ctx, SpellCastType.CONTINUOUS, true))
												)
										)
								)
						)
				);
	}

	private static int run(CommandContext<CommandSourceStack> ctx, SpellCastType type, boolean usePower) throws CommandSyntaxException {
		var list = EntityArgument.getEntities(ctx, "user");
		var holder = resolveKey(ctx, "spell", EngineRegistry.SPELL, ERR_INVALID_NAME);
		var spell = holder.get();
		var id = holder.key().location();
		if (type != spell.castType()) {
			ctx.getSource().sendFailure(LMLangData.CMD_WRONG_TYPE.get(id, spell.castType().name()));
			return 1;
		}
		double power = !usePower ? 1 : DoubleArgumentType.getDouble(ctx, "power");
		int time = 0;
		if (type == SpellCastType.CONTINUOUS) {
			time = IntegerArgumentType.getInteger(ctx, "duration");
		}
		if (type == SpellCastType.CHARGE) {
			time = IntegerArgumentType.getInteger(ctx, "chargeTime");
		}
		int success = 0;
		for (var e : list) {
			if (e instanceof LivingEntity le) {
				if (CommandSpellExecutor.execute(le, spell, time, power, 64)) {
					success++;
				}
			}
		}

		if (success == 0) {
			ctx.getSource().sendFailure(LMLangData.CMD_FAIL.get(id));
			return 1;
		}
		Component comp;
		if (list.size() > 1) {
			comp = LMLangData.CMD_SUCCESS_COUNT.get(id, success);
		} else {
			comp = LMLangData.CMD_SUCCESS.get(id);
		}
		ctx.getSource().sendSuccess(() -> comp, false);
		return 0;
	}

}
