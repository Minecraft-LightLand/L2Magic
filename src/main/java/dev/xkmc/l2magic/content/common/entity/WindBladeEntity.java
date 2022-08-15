package dev.xkmc.l2magic.content.common.entity;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.init.registrate.LLEntities;
import dev.xkmc.l2magic.util.EffectAddUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class WindBladeEntity extends ThrowableProjectile implements IEntityAdditionalSpawnData {

	@SerialClass.SerialField
	public float damage = 3;
	@SerialClass.SerialField
	public int last = 200;
	@SerialClass.SerialField
	public boolean isArcane = false;
	@SerialClass.SerialField
	public float zrot = 0f;

	private ItemStack issuer;

	public WindBladeEntity(EntityType<? extends WindBladeEntity> type, Level w) {
		super(type, w);
	}

	public WindBladeEntity(Level w) {
		this(LLEntities.ET_WIND_BLADE.get(), w);
	}

	@Override
	protected void defineSynchedData() {
	}

	public void setProperties(float damage, int last, float zrot, ItemStack issuer) {
		this.damage = damage;
		this.last = last;
		this.zrot = zrot;
		this.isArcane = !issuer.isEmpty();
		this.issuer = issuer;

		Vec3 vector3d = this.getDeltaMovement();
		float f = Mth.sqrt((float) MathHelper.horSq(vector3d));
		this.setXRot((float) (Mth.atan2(vector3d.y, f) * (double) (180F / (float) Math.PI)));
		this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
		this.xRotO = this.getXRot();
		this.yRotO = this.getYRot();
	}

	@Override
	public void tick() {
		Vec3 velocity = getDeltaMovement();
		super.tick();
		this.setDeltaMovement(velocity);
		last--;
		if (last <= 0) {
			discard();
		}

		double vx = velocity.x;
		double vy = velocity.y;
		double vz = velocity.z;
		for (int i = 0; i < 4; ++i) {
			this.level.addParticle(ParticleTypes.CRIT,
					this.getX() + vx * (double) i / 4.0D,
					this.getY() + vy * (double) i / 4.0D,
					this.getZ() + vz * (double) i / 4.0D,
					-vx, -vy + 0.2, -vz);
		}
	}

	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!level.isClientSide) {
			discard();
		}
	}

	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!level.isClientSide) {
			Entity entity = result.getEntity();
			Entity owner = this.getOwner();
			DamageSource source = new IndirectEntityDamageSource("wind_blade", entity, owner);
			entity.hurt(source, damage);
			if (isArcane && entity instanceof LivingEntity le && owner instanceof LivingEntity ow) {
				EffectAddUtil.addArcane(le, ow);
				if (issuer != null)
					ArcaneItemUseHelper.addArcaneMana(issuer, (int) damage);
			}
			if (owner instanceof LivingEntity) {
				doEnchantDamageEffects((LivingEntity) owner, entity);
			}
			discard();
		}
	}

	@Override
	protected float getGravity() {
		return 0;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public float getZRot() {
		return zrot;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeFloat(zrot);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		zrot = additionalData.readFloat();
	}
}
