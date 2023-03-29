package dev.xkmc.l2magic.content.common.entity;

import dev.xkmc.l2complements.content.entity.ISizedItemEntity;
import dev.xkmc.l2magic.init.registrate.LMEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class MagicFireBallEntity extends LargeFireball implements IEntityAdditionalSpawnData, ISizedItemEntity {

	public MagicFireBallEntity(EntityType<MagicFireBallEntity> type, Level world) {
		super(type, world);
	}

	private float size;

	public MagicFireBallEntity(Level world, LivingEntity owner, Vec3 vec, float size) {
		this(LMEntities.ET_FIRE_BALL.get(), world);
		this.setOwner(owner);
		this.setPos(vec.x, vec.y, vec.z);
		this.size = size;
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level.isClientSide) {
			this.level.explode(this, getX(), getY(), getZ(), explosionPower, false, Explosion.BlockInteraction.NONE);
			this.discard();
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeFloat(size);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		size = additionalData.readFloat();
	}

	@Override
	public float getSize() {
		return 3 * (1 + size * 2);
	}

	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.scalable(1 + size * 2, 1 + size * 2);
	}

}
