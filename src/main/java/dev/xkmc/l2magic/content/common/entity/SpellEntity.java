package dev.xkmc.l2magic.content.common.entity;

import dev.xkmc.l2library.base.BaseEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.magic.render.SpellComponent;
import dev.xkmc.l2magic.content.magic.spell.internal.ActivationConfig;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import dev.xkmc.l2magic.init.registrate.LMEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SerialClass
public class SpellEntity extends BaseEntity {

	@SerialClass.SerialField
	public int time, setup, close;

	@SerialClass.SerialField
	public String spell_id = "";

	private Consumer<SpellEntity> action;

	public SpellEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	public SpellEntity(Level w) {
		this(LMEntities.ET_SPELL.get(), w);
	}

	public void setData(double x, double y, double z, int time, int setup, int close, float xr, float yr, String id) {
		this.setPos(x, y - 1.5f, z);
		this.time = time;
		this.setup = setup;
		this.close = close;
		this.setXRot(xr);
		this.setYRot(yr);
		this.spell_id = id;
	}

	public void setData(Player player, SpellConfig.SpellDisplay spell, SpellPlane plane) {
		Vec3 pos = player.position();
		float xr = player.getXRot();
		float yr = player.getYRot();
		if (plane == SpellPlane.VERTICAL) {
			pos = pos.add(0, 1e-3, 0);
			xr = -90;
		} else if (plane == SpellPlane.HORIZONTAL) {
			pos = pos.add(0, player.getEyeHeight(), 0);
			xr = 0;
			pos = RayTraceUtil.getRayTerm(pos, xr, yr, -0.5);
		} else if (plane == SpellPlane.NORMAL) {
			pos = pos.add(0, player.getEyeHeight(), 0);
			pos = RayTraceUtil.getRayTerm(pos, xr, yr, 0.5);
		}
		setData(pos.x, pos.y, pos.z, spell.duration(), spell.setup(), spell.close(), xr, yr, spell.id());
	}

	public void setData(ActivationConfig act, SpellConfig.SpellDisplay spell) {
		Vec3 pos = act.target == null ? act.pos : act.target.position().add(0, act.target.getBbHeight() / 2, 0);
		pos = pos.add(0, 1e-3, 0);
		float xr = -90;
		setData(pos.x, pos.y, pos.z, spell.duration(), spell.setup(), spell.close(), xr, 0, spell.id());
	}

	public void setAction(Consumer<SpellEntity> cons) {
		action = cons;
	}

	@Override
	public void tick() {
		super.tick();
		if (action != null) {
			action.accept(this);
		}
		if (!level.isClientSide() && this.tickCount >= time) {
			discard();
		}
	}

	public float getSize(float partial) {
		float t = tickCount + partial;
		if (t < setup) {
			return t / setup;
		}
		if (time - t < close) {
			return (time - t) / close;
		}
		return 1;
	}

	@Nullable
	public SpellComponent getComponent() {
		return SpellComponent.getFromConfig(spell_id);
	}

	@Override
	protected void defineSynchedData() {

	}

	public enum SpellPlane {
		/**
		 * flat plane on the ground
		 */
		VERTICAL,
		/**
		 * arua after head
		 */
		HORIZONTAL,
		/**
		 * circle in front
		 */
		NORMAL
	}

}
