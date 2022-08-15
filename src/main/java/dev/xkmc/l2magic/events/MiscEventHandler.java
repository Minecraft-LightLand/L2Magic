package dev.xkmc.l2magic.events;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneEffect;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.content.arcane.internal.IArcaneItem;
import dev.xkmc.l2magic.content.common.command.BaseCommand;
import dev.xkmc.l2magic.content.common.render.MagicWandOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

public class MiscEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void keyEvent(InputEvent.Key event) {
		if (Minecraft.getInstance().screen == null && Proxy.getClientPlayer() != null && MagicWandOverlay.has_magic_wand) {
			MagicWandOverlay.input(event.getKey(), event.getAction());
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		DamageSource source = event.getSource();
		LivingEntity target = event.getEntity();
		if ((source.getMsgId().equals("player") || source.getMsgId().equals("mob")) && source.getDirectEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				ArcaneEffect.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		} else if (source.getDirectEntity() instanceof LightningBolt && source.getEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				ArcaneEffect.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		LiteralArgumentBuilder<CommandSourceStack> lightland = Commands.literal("l2magic");
		for (Consumer<LiteralArgumentBuilder<CommandSourceStack>> command : BaseCommand.LIST) {
			command.accept(lightland);
		}
		event.getDispatcher().register(lightland);
	}

}
