package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.fastprojectileapi.entity.BaseProjectile;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class LMProjectile extends BaseProjectile {

	@SerialField
	private ProjectileData data;

	public LMProjectile(EntityType<? extends LMProjectile> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public LMProjectile(EntityType<? extends LMProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	public LMProjectile(EntityType<? extends LMProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
		this(pEntityType, pShooter.getX(), pShooter.getEyeY() - (double) 0.1F, pShooter.getZ(), pLevel);
		this.setOwner(pShooter);
	}

	public void setup(ProjectileData data, Vec3 pos, Vec3 initVec) {
		this.data = data;
		setPos(pos);
		setDeltaMovement(initVec);
		var rot = ProjectileMovement.of(initVec).rot();
		setXRot((float) (rot.x * Mth.RAD_TO_DEG));
		setYRot((float) (rot.y * Mth.RAD_TO_DEG));
	}

	@Override
	public void tick() {
		super.tick();
		data.tick(this);
	}

	@Override
	protected void projectileMove() {
		// before move
		if (!level().isClientSide && !data.params.bypassWall() && data.doFullBlockCollision(this)) {
			var aabb = getBoundingBoxForEntityHit().expandTowards(getDeltaMovement());
			var list = this.level().getBlockCollisions(this, aabb);
			for (var e : list) {
				data.land(this);
				discard();
				break;
			}
		}

		super.projectileMove();
		// before discard
		if (!level().isClientSide && tickCount >= lifetime()) {
			data.expire(this);
		}
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		Vec3 vec3 = new Vec3(packet.getXa(), packet.getYa(), packet.getZa());
		this.setDeltaMovement(vec3);
	}

	@Override
	public AABB getBoundingBoxForEntityHit() {
		return getBoundingBox().inflate(data.size(this));
	}

	@Override
	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		return data.move(this, vec, pos.add(0, getBbHeight() / 2, 0));
	}

	@Override
	public boolean checkBlockHit() {
		return !data.doFullBlockCollision(this) && !data.params.bypassWall();
	}

	@Override
	public int lifetime() {
		return data.params.life();
	}

	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		var data = new TagCodec(level().registryAccess()).toTag(new CompoundTag(), this);
		if (data != null) nbt.put("auto-serial", data);
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("auto-serial")) {
			Wrappers.run(() -> new TagCodec(level().registryAccess()).fromTag(nbt.getCompound("auto-serial"), getClass(), this));
		}
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		PacketCodec.to(buffer, this);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		PacketCodec.from(additionalData, getClass(), Wrappers.cast(this));
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!level().isClientSide) {
			data.land(this);
			discard();
		}
	}

	@Override
	public boolean canHitEntity(Entity target) {
		return super.canHitEntity(target) && data.shouldHurt(this, target);
	}

	@Override
	public void onHitEntity(EntityHitResult result) {
		if (level().isClientSide) return;
		data.hurtTarget(this, result);
		if (!data.params.bypassEntity()) {
			discard();
		}
	}

	public void onClientHitEntity(Entity le) {
		data.hurtTargetImpl(this, le);
	}

	public void onClientExpire() {
		data.expire(this);
	}

	public void onClientLand() {
		data.land(this);
	}

	public LocationContext location() {//TODO z-rot handling
		return LocationContext.of(new Vec3(getX(), getY(0.5), getZ()), getForward());
	}

	@Nullable
	public ProjectileRenderer getRenderer() {
		return data.getRenderer(this);
	}

	public boolean shouldRenderAtSqrDistance(double distance) {
		return distance < 128 * 128;
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return getBoundingBox().inflate(0.5);
	}
}
