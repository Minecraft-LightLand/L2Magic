package dev.xkmc.l2magic.content.common.effect;

import dev.xkmc.l2library.base.effects.api.FirstPlayerRenderEffect;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.init.registrate.LLParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public class EmeraldPopeEffect extends MobEffect implements FirstPlayerRenderEffect {

	public static final int RADIUS = 10;

	public EmeraldPopeEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	public void applyEffectTick(LivingEntity self, int level) {
		if (self.level.isClientSide())
			return;
		int radius = (level + 1) * RADIUS;
		int damage = (level + 1) * 10;
		DamageSource source = new IndirectEntityDamageSource("emerald", self, self);
		for (Entity e : self.level.getEntities(self, new AABB(self.blockPosition()).inflate(radius))) {
			if (e instanceof LivingEntity && !e.isAlliedTo(self) && ((LivingEntity) e).hurtTime == 0 &&
					e.position().distanceToSqr(self.position()) < radius * radius) {
				double dist = e.position().distanceTo(self.position());
				if (dist > 0.1) {
					((LivingEntity) e).knockback(0.4F, e.position().x - self.position().x, e.position().z - self.position().z);
				}
				e.hurt(source, damage);
			}
		}
	}

	public boolean isDurationEffectTick(int tick, int lv) {
		return tick % 10 == 0;
	}

	@Override
	public void render(LivingEntity entity, int lv, Consumer<ResourceLocation> consumer) {
		if (!Minecraft.getInstance().isPaused() && entity != Proxy.getClientPlayer()) {
			int r = RADIUS * (1 + lv);
			int count = (1 + lv) * (1 + lv) * 4;
			for (int i = 0; i < count; i++) {
				addParticle(entity.level, entity.position(), r);
			}
		}
	}

	@Override
	public void onClientLevelRender(AbstractClientPlayer player, MobEffectInstance ins) {
		int lv = ins.getAmplifier();
		int r = RADIUS * (1 + lv);
		int count = (1 + lv) * (1 + lv) * 4;
		for (int i = 0; i < count; i++) {
			addParticle(player.level, player.position(), r);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void addParticle(Level w, Vec3 vec, int r) {
		float tpi = (float) (Math.PI * 2);
		Vec3 v0 = new Vec3(0, r, 0);
		Vec3 v1 = v0.xRot(tpi / 3).yRot((float) (Math.random() * tpi));
		float a0 = (float) (Math.random() * tpi);
		float b0 = (float) Math.acos(2 * Math.random() - 1);
		v0 = v0.xRot(a0).yRot(b0);
		v1 = v1.xRot(a0).yRot(b0);
		w.addAlwaysVisibleParticle(LLParticle.EMERALD.get(),
				vec.x + v0.x,
				vec.y + v0.y,
				vec.z + v0.z,
				vec.x + v1.x,
				vec.y + v1.y,
				vec.z + v1.z);
	}

}
