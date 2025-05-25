package dev.xkmc.l2magic.content.engine.selector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class SelectedEntities {

	public record Entry(Entity e, Entity root, @Nullable LivingEntity le) {
	}

	private final LinkedHashMap<Entity, Entry> map = new LinkedHashMap<>();

	public SelectedEntities() {

	}

	public SelectedEntities(Entity e) {
		add(e);
	}

	public SelectedEntities(Collection<? extends Entity> list) {
		for (var e : list)
			add(e);
	}

	public void addAll(SelectedEntities other) {
		map.putAll(other.map);
	}

	public void retainAll(SelectedEntities other) {
		map.entrySet().retainAll(other.map.entrySet());
	}

	public void removeAll(SelectedEntities other) {
		map.entrySet().removeAll(other.map.entrySet());
	}

	public void add(Entity e) {
		if (e instanceof PartEntity<?> part) {
			if (part.getParent() instanceof LivingEntity le) {
				map.put(part, new Entry(part, le, le));
			}
		} else if (e instanceof LivingEntity le) {
			map.put(e, new Entry(e, e, le));
		} else if (e.isPickable()) {
			map.put(e, new Entry(e, e, null));
		}
	}

	public Collection<Entity> entity() {
		return map.keySet();
	}

	public Collection<Entry> entries() {
		return map.values();
	}

	public List<LivingEntity> living() {
		List<LivingEntity> list = new ArrayList<>();
		for (var e : map.values()) {
			if (e.le() != null)
				list.add(e.le());
		}
		return list;
	}

}
