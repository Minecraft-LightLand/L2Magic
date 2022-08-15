package dev.xkmc.l2magic.content.common.entity;

import dev.xkmc.l2magic.init.registrate.LLEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class FireArrowEntity extends Arrow {

	public FireArrowEntity(EntityType<FireArrowEntity> type, Level world) {
		super(type, world);
	}

	public FireArrowEntity(Level world, LivingEntity owner) {
		this(LLEntities.ET_FIRE_ARROW.get(), world);
		this.setOwner(owner);
	}

	@Override
	protected void tickDespawn() {
		++this.life;
		if (this.life > 200)
			discard();
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
