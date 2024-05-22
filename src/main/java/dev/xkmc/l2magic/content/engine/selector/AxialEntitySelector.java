package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record AxialEntitySelector(
		AxialEntitySelector.Type shape,
		DoubleVariable rx,
		DoubleVariable ry,
		DoubleVariable rz
) implements EntitySelector<AxialEntitySelector> {

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final Codec<AxialEntitySelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			TYPE_CODEC.fieldOf("shape").forGetter(e -> e.shape),
			DoubleVariable.codec("rx", AxialEntitySelector::rx),
			DoubleVariable.codec("ry", AxialEntitySelector::ry),
			DoubleVariable.codec("rz", AxialEntitySelector::rz)
	).apply(i, AxialEntitySelector::new));

	public enum Type {
		RECT, CYLINDER, BALL;

		public boolean check(Vec3 pos, double x, double y, double z, AABB box) {
			if (this == RECT) {
				return true;
			}
			if (this == CYLINDER) {
				return checkXZ(pos, x, y, z, box, box.minY);
			}
			return checkXZ(pos, x, y, z, box, box.minY) || checkXZ(pos, x, y, z, box, pos.y);
		}

		private boolean checkXZ(Vec3 pos, double x, double y, double z, AABB box, double by) {
			return checkXYZ(pos, x, y, z, box.minX, by, box.minZ) ||
					checkXYZ(pos, x, y, z, box.minX, by, box.maxZ) ||
					checkXYZ(pos, x, y, z, box.maxX, by, box.minZ) ||
					checkXYZ(pos, x, y, z, box.maxX, by, box.maxZ);
		}

		private boolean checkXYZ(Vec3 pos, double x, double y, double z, double bx, double by, double bz) {
			double dx = (bx - pos.x) / Math.max(1e-3, Math.abs(x));
			double dz = (bz - pos.z) / Math.max(1e-3, Math.abs(z));
			if (this == CYLINDER) {
				return dx * dx + dz * dz <= 1;
			}
			double dy = (by - pos.y) / Math.max(1e-3, Math.abs(y));
			return dx * dx + dy * dy + dz * dz <= 1;
		}

	}

	@Override
	public SelectorType<AxialEntitySelector> type() {
		return EngineRegistry.AXIAL.get();
	}

	public List<LivingEntity> find(ServerLevel sl, EngineContext ctx) {
		Vec3 pos = ctx.loc().pos();
		double x = rx().eval(ctx);
		double y = ry().eval(ctx);
		double z = rz().eval(ctx);
		var aabb = AABB.ofSize(pos, x * 2, y * 2, z * 2);
		List<LivingEntity> list = new ArrayList<>();
		for (var e : EntityStorageCache.get(sl).foreach(aabb, ctx.user()::canHitEntity)) {
			if (e instanceof LivingEntity le) {
				var box = le.getBoundingBox();
				if (shape.check(pos, x, y, z, box)) {
					list.add(le);
				}
			}
		}
		return list;
	}

}
