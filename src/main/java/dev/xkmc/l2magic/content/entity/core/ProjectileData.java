package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.UserContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.serialization.marker.OnInject;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SerialClass
public class ProjectileData {

	private static final int SALT_TICK = 0x342ab3c1, SALT_MOVE = 0xa6258bd1,
			SALT_HIT = 0xb286c235, SALT_RENDER = 0x1134ba51, SALT_SIZE = 0x82f34eab,
			SALT_LAND = 0x17f1ab5d, SALT_EXPIRE = 0x75ab5e3f;

	public static final Set<String> DEFAULT_PARAMS = Set.of("TickCount",
			"ProjectileX", "ProjectileY", "ProjectileZ");

	@SerialField
	public Holder<SpellAction> root;

	@SerialField
	public ProjectileParams params;

	@SerialField
	private ResourceLocation id;

	private boolean init;

	private ProjectileConfig config;

	@Deprecated
	public ProjectileData() {
	}

	public ProjectileData(Holder<SpellAction> root, ProjectileParams params, Holder<ProjectileConfig> config) {
		this.root = root;
		this.params = params;
		this.id = config.unwrapKey().orElseThrow().location();
		this.config = config.value();
	}

	@OnInject
	public void onInject() {
		init = false;
		config = null;
	}

	@Nullable
	public ProjectileConfig getConfig(Level level) {
		if (!init) {
			config = level.registryAccess().registryOrThrow(EngineRegistry.PROJECTILE).get(id);
		}
		return config;
	}

	private Map<String, Double> allParams(LMProjectile self) {
		var ans = new LinkedHashMap<>(params.params());
		ans.put("TickCount", (double) self.tickCount);
		ans.put("ProjectileX", self.getX());
		ans.put("ProjectileY", self.getY() + self.getBbHeight() / 2);
		ans.put("ProjectileZ", self.getZ());
		return ans;
	}

	@Nullable
	private EngineContext getContext(LMProjectile self, int salt, boolean addScheduler) {
		if (!(self.getOwner() instanceof LivingEntity user)) return null;
		var source = new SingleThreadedRandomSource(params.seed() ^ self.tickCount ^ salt);
		Scheduler sche = addScheduler ? new Scheduler() : null;
		return new EngineContext(new UserContext(user.level(), user, root, sche),
				self.location(), source, allParams(self));
	}

	public boolean shouldHurt(LMProjectile self, Entity target) {
		if (getConfig(self.level()) == null) return false;
		if (self.getOwner() instanceof LivingEntity le) {
			return config.filter().test(target, le);
		}
		return false;
	}

	public void hurtTarget(LMProjectile self, EntityHitResult result) {
		if (getConfig(self.level()) == null) return;
		if (!(result.getEntity() instanceof LivingEntity le)) return;
		hurtTargetImpl(self, le);
	}

	public void hurtTargetImpl(LMProjectile self, LivingEntity le) {
		var hit = config.hit();
		if (hit.isEmpty()) return;
		EngineContext ctx = getContext(self, SALT_HIT, true);
		if (ctx == null) return;
		if (!self.level().isClientSide()) {
			for (var e : hit) {
				if (!e.serverOnly()) {
					L2Magic.HANDLER.toTrackingPlayers(new ProjectileHitPacket(self.getId(), le.getId()), self);
					break;
				}
			}
		}
		for (var e : hit) {
			e.process(new SelectedEntities(le), ctx);
		}
		ctx.registerScheduler();
	}

	public void tick(LMProjectile self) {
		if (getConfig(self.level()) == null) return;
		ConfiguredEngine<?> tick = config.tick();
		if (tick == null) return;
		EngineContext ctx = getContext(self, SALT_TICK, true);
		if (ctx == null) return;
		tick.execute(ctx);
		ctx.registerScheduler();
	}

	public void land(LMProjectile self) {
		if (getConfig(self.level()) == null) return;
		ConfiguredEngine<?> tick = config.land();
		if (tick == null) return;
		EngineContext ctx = getContext(self, SALT_LAND, true);
		if (ctx == null) return;
		if (!self.level().isClientSide()) {
			L2Magic.HANDLER.toTrackingPlayers(new ProjectileLandPacket(self.getId()), self);
		}
		tick.execute(ctx);
		ctx.registerScheduler();
	}

	public void expire(LMProjectile self) {
		if (getConfig(self.level()) == null) return;
		ConfiguredEngine<?> tick = config.expire();
		if (tick == null) return;
		EngineContext ctx = getContext(self, SALT_EXPIRE, true);
		if (ctx == null) return;
		if (!self.level().isClientSide()) {
			L2Magic.HANDLER.toTrackingPlayers(new ProjectileExpirePacket(self.getId()), self);
		}
		tick.execute(ctx);
		ctx.registerScheduler();
	}

	public ProjectileMovement move(LMProjectile self, Vec3 vec, Vec3 pos) {
		if (getConfig(self.level()) != null) {
			Motion<?> motion = config.motion();
			EngineContext ctx = getContext(self, SALT_MOVE, false);
			if (motion != null && ctx != null) {
				return motion.move(ctx, vec, pos);
			}
		}
		return ProjectileMovement.of(vec);
	}

	@Nullable
	public ProjectileRenderer getRenderer(LMProjectile self) {
		if (getConfig(self.level()) == null) return null;
		var renderer = config.renderer();
		if (renderer == null) return null;
		EngineContext ctx = getContext(self, SALT_RENDER, false);
		if (ctx == null) return null;
		return renderer.resolve(ctx);
	}

	public double size(LMProjectile e) {
		if (getConfig(e.level()) == null) return 0;
		var size = config.size();
		if (size == null) return 0;
		EngineContext ctx = getContext(e, SALT_SIZE, false);
		if (ctx == null) return 0;
		return size.size().eval(ctx);
	}

	public boolean doFullBlockCollision(LMProjectile e) {
		if (getConfig(e.level()) == null) return false;
		var size = config.size();
		if (size == null) return false;
		return size.fullBlockCollision();
	}

}
