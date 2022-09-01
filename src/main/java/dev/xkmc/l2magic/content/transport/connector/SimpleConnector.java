package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public BlockPos pos = null;

	public SimpleConnector(int max) {
		super(max);
	}

	@Override
	public List<BlockPos> getConnected() {
		return pos == null ? List.of() : List.of(pos);
	}

	@Override
	public void link(BlockPos pos) {
		if (this.pos != null && this.pos.equals(pos)) {
			this.pos = null;
		} else {
			this.pos = pos;
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		if (pos == null) return;
		if (o.test(pos)) pos = null;
	}

}
