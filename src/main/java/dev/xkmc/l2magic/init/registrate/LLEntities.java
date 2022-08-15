package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2foundation.content.entity.SpecialSpriteRenderer;
import dev.xkmc.l2library.repack.registrate.util.entry.EntityEntry;
import dev.xkmc.l2magic.content.common.entity.*;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class LLEntities {

	public static final EntityEntry<WindBladeEntity> ET_WIND_BLADE;
	public static final EntityEntry<SpellEntity> ET_SPELL;
	public static final EntityEntry<FireArrowEntity> ET_FIRE_ARROW;
	public static final EntityEntry<MagicFireBallEntity> ET_FIRE_BALL;

	static {

		ET_WIND_BLADE = L2Magic.REGISTRATE
				.<WindBladeEntity>entity("wind_blade", WindBladeEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(0.5f, 0.5f)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20).fireImmune())
				.renderer(() -> WindBladeEntityRenderer::new)
				.defaultLang().register();

		ET_SPELL = L2Magic.REGISTRATE
				.<SpellEntity>entity("spell", SpellEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(3f, 3f)
						.setShouldReceiveVelocityUpdates(true)
						.fireImmune().updateInterval(20))
				.renderer(() -> SpellEntityRenderer::new)
				.defaultLang().register();

		ET_FIRE_ARROW = L2Magic.REGISTRATE
				.<FireArrowEntity>entity("fire_arrow", FireArrowEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(20))
				.renderer(() -> TippableArrowRenderer::new)
				.defaultLang().register();

		ET_FIRE_BALL = L2Magic.REGISTRATE
				.<MagicFireBallEntity>entity("fire_ball", MagicFireBallEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(10))
				.renderer(() -> ctx -> new SpecialSpriteRenderer<>(ctx, ctx.getItemRenderer(), true))
				.defaultLang().register();

	}

	public static void register() {
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
	}

}
