package dev.xkmc.l2magic.compat.jei.screen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2magic.compat.jei.ingredients.ElementIngredient;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.annotation.Nullable;
import java.util.*;

public class ExtraInfoScreen implements IGuiContainerHandler<AbstractContainerScreen<?>> {

	public static final List<Wrapper> LIST = new ArrayList<>();

	public static void init() {
		LIST.add((a, b) -> (a instanceof MagicElement) && (b instanceof Integer) ? new ElementIngredient((MagicElement) a, (Integer) b) : null);
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse(AbstractContainerScreen<?> screen, double mx, double my) {
		ExtraInfo<?> extra = (ExtraInfo<?>) screen;
		List<Object> potential = new ArrayList<>();
		extra.getInfoMouse(mx - screen.getGuiLeft(), my - screen.getGuiTop(), (x, y, w, h, e) -> unwrap(potential, e));
		Optional<Object> ans = potential.stream().filter(Objects::nonNull).findFirst();
		return ans.orElseGet(() -> IGuiContainerHandler.super.getIngredientUnderMouse(screen, mx, my));
	}

	public void unwrap(List<Object> list, Object o) {
		if (o instanceof Either<?, ?>) {
			o = ((Either<?, ?>) o).map(e -> e, e -> e);
		}
		Object left;
		Object right;
		if (o instanceof Map.Entry<?, ?>) {
			left = ((Map.Entry<?, ?>) o).getKey();
			right = ((Map.Entry<?, ?>) o).getValue();
		} else if (o instanceof Pair<?, ?>) {
			left = ((Pair<?, ?>) o).getFirst();
			right = ((Pair<?, ?>) o).getSecond();
		} else return;
		LIST.forEach(e -> list.add(e.predicate(left, right)));
	}

	public interface Wrapper {

		@Nullable
		Object predicate(Object left, Object right);

	}

}
