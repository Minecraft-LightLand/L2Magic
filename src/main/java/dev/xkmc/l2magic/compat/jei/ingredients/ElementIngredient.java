package dev.xkmc.l2magic.compat.jei.ingredients;

import dev.xkmc.l2magic.content.magic.products.MagicElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ElementIngredient {

	public static List<ElementIngredient> collect(Stream<ElementIngredient> stream) {
		List<ElementIngredient> list = stream.toList();
		LinkedHashMap<ElementIngredient, ElementIngredient> set = new LinkedHashMap<>();
		for (ElementIngredient t : list) {
			if (set.containsKey(t))
				set.get(t).count += t.count;
			else set.put(t, t);
		}
		return new ArrayList<>(set.values());
	}

	public final MagicElement elem;
	public int count;

	public ElementIngredient(MagicElement elem, int count) {
		this.elem = elem;
		this.count = count;
	}

	public ElementIngredient(MagicElement elem) {
		this(elem, 1);
	}

	public ElementIngredient(ElementIngredient elem) {
		this(elem.elem, elem.count);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ElementIngredient that = (ElementIngredient) o;
		return Objects.equals(elem, that.elem);
	}

	@Override
	public int hashCode() {
		return elem.hashCode();
	}
}
