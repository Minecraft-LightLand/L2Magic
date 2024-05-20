package dev.xkmc.l2magic.events;

import dev.xkmc.l2library.init.explosion.BaseExplosion;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@EventBusSubscriber(modid = L2Magic.MODID, bus = Bus.FORGE)
public class ClientEventHandler {
	private static List<BooleanSupplier> TASKS = new ArrayList<>();

	public ClientEventHandler() {
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END) {
			RayTraceUtil.serverTick();
			execute();
		}
	}

	@SubscribeEvent
	public static void onDetonate(ExplosionEvent.Detonate event) {
		Explosion var2 = event.getExplosion();
		if (var2 instanceof BaseExplosion exp) {
			event.getAffectedEntities().removeIf((e) -> {
				return !exp.hurtEntity(e);
			});
		}

	}

	public static synchronized void schedule(Runnable runnable) {
		TASKS.add(() -> {
			runnable.run();
			return true;
		});
	}

	public static synchronized void schedulePersistent(BooleanSupplier runnable) {
		TASKS.add(runnable);
	}

	private static synchronized void execute() {
		if (!TASKS.isEmpty()) {
			List<BooleanSupplier> temp = TASKS;
			TASKS = new ArrayList();
			temp.removeIf(BooleanSupplier::getAsBoolean);
			temp.addAll(TASKS);
			TASKS = temp;
		}
	}
}
