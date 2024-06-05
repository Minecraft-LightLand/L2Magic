package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.fastprojectileapi.entity.BaseProjectile;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public class LMProjectile extends BaseProjectile {

	@SerialClass.SerialField(toClient = true)
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
		updateRotation(ProjectileMovement.of(initVec).rot());
	}

	@Override
	public void tick() {
		super.tick();
		data.tick(this);
	}

	@Override
	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		return data.move(this, vec, pos.add(0, getBbHeight() / 2, 0));
	}

	@Override
	public boolean checkBlockHit() {
		return !data.params.bypassWall();
	}

	@Override
	public int lifetime() {
		return data.params.life();
	}

	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(nbt.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		PacketCodec.to(buffer, this);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		PacketCodec.from(additionalData, getClass(), Wrappers.cast(this));
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!level().isClientSide) {
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

	public LocationContext location() {//TODO z-rot handling
		return LocationContext.of(new Vec3(getX(), getY(0.5), getZ()), getForward());
	}

	@OnlyIn(Dist.CLIENT)
	@Nullable
	public ProjectileRenderer getRenderer() {
		return null;//TODO
	}

}
