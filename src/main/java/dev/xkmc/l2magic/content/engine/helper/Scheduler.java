package dev.xkmc.l2magic.content.engine.helper;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

	private final Long2ObjectOpenHashMap<List<Runnable>> map = new Long2ObjectOpenHashMap<>();
	private long time = 0;

	public boolean isFinished() {
		return map.isEmpty();
	}

	public boolean tick() {
		time++;
		var list = map.remove(time);
		if (list != null) {
			for (var e : list) {
				e.run();
			}
		}
		return isFinished();
	}

	public void schedule(long tick, Runnable o) {
		if (tick <= time) o.run();
		var list = map.computeIfAbsent(tick, k -> new ArrayList<>());
		list.add(o);
	}
}
