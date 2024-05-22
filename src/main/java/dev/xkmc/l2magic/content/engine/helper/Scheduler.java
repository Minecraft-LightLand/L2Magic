package dev.xkmc.l2magic.content.engine.helper;

import dev.xkmc.l2magic.init.L2Magic;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

	private final Long2ObjectOpenHashMap<List<Runnable>> map = new Long2ObjectOpenHashMap<>();
	public long time = 0;

	public boolean isFinished() {
		return map.isEmpty();
	}

	public boolean tick() {
		time++;
		var list = map.remove(time);
		if (list != null) {
			try {
				for (var e : list) {
					e.run();
				}
			} catch (Exception e) {
				L2Magic.LOGGER.throwing(e);
				return true;
			}
		}
		return isFinished();
	}

	public void schedule(long tick, Runnable o) {
		if (tick <= 0) o.run();
		var list = map.computeIfAbsent(tick + time, k -> new ArrayList<>());
		list.add(o);
	}
}
