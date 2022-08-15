package dev.xkmc.l2magic.content.magic.spell.internal;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ActivationConfig {

	public Level world;
	public Player player;
	public Vec3 pos;
	public Vec3 dire;
	public Entity target;

	public ActivationConfig(Level world, Player player, double reach) {
		this.world = world;
		this.player = player;

		float f = player.getXRot();
		float f1 = player.getYRot();
		dire = RayTraceUtil.getRayTerm(Vec3.ZERO, f, f1, 1);

		EntityHitResult ertr = RayTraceUtil.rayTraceEntity(player, reach, (e) -> e instanceof LivingEntity && e != player);
		if (ertr != null) {
			pos = ertr.getLocation();
			target = ertr.getEntity();
			return;
		}
		BlockHitResult brtr = RayTraceUtil.rayTraceBlock(world, player, reach);
		if (brtr.getType() == HitResult.Type.BLOCK) {
			pos = brtr.getLocation();
			return;
		}
		Vec3 vector3d = new Vec3(player.getX(), player.getEyeY(), player.getZ());
		pos = RayTraceUtil.getRayTerm(vector3d, f, f1, reach);
	}


}
