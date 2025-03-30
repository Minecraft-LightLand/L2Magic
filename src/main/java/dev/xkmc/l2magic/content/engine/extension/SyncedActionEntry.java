package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.EngineContextData;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2serial.util.Wrappers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SyncedActionEntry extends ExtensionEntry<SyncedActionEntry, SyncedActionEntry.Holder> {

	public static void run(EngineContext ctx, EngineContextData data, SyncedAction<?> action) {
		if (ctx.user().level().isClientSide()) {
			return;
		}
		data.remap(ctx).execute(action.child());
		int index = get(data.root().value()).getIndex(action);
		L2Magic.HANDLER.toTrackingPlayers(new SyncedActionPacket(action.type().key, data, index), ctx.user().user());
	}

	public record Holder(SpellAction e) implements ExtensionKey<SyncedActionEntry, Holder> {

		@Override
		public Verifiable getEntry() {
			return e.action();
		}

		@Override
		public SyncedActionEntry create() {
			return new SyncedActionEntry(e.action());
		}

	}

	public static final ExtensionTypeManager<SyncedActionEntry, Holder> MANAGER = new ExtensionTypeManager<>();

	public static SyncedActionEntry get(SpellAction root) {
		return MANAGER.get(new Holder(root));
	}

	private final Map<ExtensionHolder<?>, List<Object>> map = new LinkedHashMap<>();

	public SyncedActionEntry(Verifiable entry) {
		super(entry);
	}

	@Override
	protected boolean match(Holder e) {
		return entry == e.getEntry();
	}

	@Override
	protected void initAnalysis() {
		map.clear();
	}

	@Override
	protected void process(IExtended<?> p) {
		map.computeIfAbsent(p.type(), k -> new ArrayList<>()).add(p);
	}

	@Nullable
	public <T extends IExtended<T>> T get(ExtensionHolder<T> type, int index) {
		var list = map.get(type);
		if (list == null || list.size() <= index) return null;
		return Wrappers.cast(list.get(index));
	}

	public <T extends IExtended<T>> int getIndex(IExtended<T> e) {
		var list = map.get(e.type());
		if (list == null || list.isEmpty()) return -1;
		return list.indexOf(e);
	}

}
