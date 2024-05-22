package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public record CompoundEntitySelector(
		CompoundEntitySelector.Type function,
		List<EntitySelector<?>> selectors
) implements EntitySelector<CompoundEntitySelector> {

	public enum Type {
		UNION,
		INTERSECTION,
		FIRST_ONLY,
		;

		public void merge(LinkedHashSet<LivingEntity> ans, LinkedHashSet<LivingEntity> l) {
			if (this == UNION) {
				ans.addAll(l);
			}
			if (this == INTERSECTION) {
				ans.retainAll(l);
			}
			if (this == FIRST_ONLY) {
				ans.removeAll(l);
			}
		}
	}

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static Codec<CompoundEntitySelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			TYPE_CODEC.fieldOf("function").forGetter(e -> e.function),
			Codec.list(EntitySelector.CODEC).fieldOf("selectors").forGetter(e -> e.selectors)
	).apply(i, CompoundEntitySelector::new));

	@Override
	public SelectorType<CompoundEntitySelector> type() {
		return EngineRegistry.COMPOUND.get();
	}

	@Override
	public List<LivingEntity> find(ServerLevel sl, EngineContext ctx) {
		LinkedHashSet<LivingEntity> ans = null;
		for (var s : selectors) {
			var l = new LinkedHashSet<>(s.find(sl, ctx));
			if (ans == null) ans = l;
			else function.merge(ans, l);
		}
		return ans == null ? List.of() : new ArrayList<>(ans);
	}
}
