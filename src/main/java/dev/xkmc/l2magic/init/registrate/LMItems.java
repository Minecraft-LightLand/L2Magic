package dev.xkmc.l2magic.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2itemselector.init.data.L2ISTagGen;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.item.CreativeWandItem;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleType;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;

public class LMItems {

	public static final RegistryEntry<CreativeModeTab> TAB = L2Magic.REGISTRATE.buildModCreativeTab(
			"magic", "L2Magic",
			e -> e.icon(LMItems.CREATIVE_WAND::asStack));

	public static final ItemEntry<CreativeWandItem> CREATIVE_WAND = L2Magic.REGISTRATE
			.item("creative_wand", p -> new CreativeWandItem(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx))
			.tag(L2ISTagGen.SELECTABLE)
			.register();

	public static final EntityEntry<LMProjectile> GENERIC_PROJECTILE = L2Magic.REGISTRATE
			.<LMProjectile>entity(LMProjectile::new, MobCategory.MISC)
			.properties(p -> p.setShouldReceiveVelocityUpdates(false).updateInterval(100)
					.sized(0.01f, 0.01f).clientTrackingRange(4))
			.renderer(() -> LMProjectileRenderer::new)
			.register();

	public static final RegistryEntry<LMGenericParticleType> GENERIC_PARTICLE = L2Magic.REGISTRATE
			.simple("generic_particle", Registries.PARTICLE_TYPE, LMGenericParticleType::new);

	public static void register() {

	}

}
