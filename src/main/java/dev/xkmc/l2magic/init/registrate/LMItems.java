package dev.xkmc.l2magic.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2itemselector.init.data.L2ISTagGen;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.item.spell.CreativeWandItem;
import dev.xkmc.l2magic.content.item.utility.AiConfigWand;
import dev.xkmc.l2magic.content.item.utility.EquipmentWand;
import dev.xkmc.l2magic.content.item.utility.TargetSelectWand;
import dev.xkmc.l2magic.content.menu.curios.EntityCuriosListMenu;
import dev.xkmc.l2magic.content.menu.curios.EntityCuriosListScreen;
import dev.xkmc.l2magic.content.menu.equipments.EquipmentsMenu;
import dev.xkmc.l2magic.content.menu.equipments.EquipmentsScreen;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleType;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;

public class LMItems {

	public static final SimpleEntry<CreativeModeTab> TAB = L2Magic.REGISTRATE.buildModCreativeTab(
			"magic", "L2Magic",
			e -> e.icon(LMItems.CREATIVE_WAND::asStack));

	public static final ItemEntry<CreativeWandItem> CREATIVE_WAND;
	public static final ItemEntry<TargetSelectWand> WAND_TARGET;
	public static final ItemEntry<AiConfigWand> WAND_AI;
	public static final ItemEntry<EquipmentWand> WAND_EQUIPMENT;

	private static final DCReg DC = DCReg.of(L2Magic.REG);
	public static final DCVal<String> SPELL = DC.str("spell");
	public static final DCVal<String> MODID = DC.str("modid");
	public static final DCVal<Integer> TARGET = DC.intVal("target");

	public static final EntityEntry<LMProjectile> GENERIC_PROJECTILE;
	public static final RegistryEntry<ParticleType<?>, LMGenericParticleType> GENERIC_PARTICLE;

	public static final MenuEntry<EquipmentsMenu> EQUIPMENTS;
	public static final MenuEntry<EntityCuriosListMenu> CURIOS;

	static {
		CREATIVE_WAND = L2Magic.REGISTRATE
				.item("creative_wand", p -> new CreativeWandItem(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.handheld(ctx))
				.tag(L2ISTagGen.SELECTABLE)
				.tab(TAB.key(), (x, m) -> x.get().fillTab(m))
				.register();

		WAND_TARGET = L2Magic.REGISTRATE.item(
						"target_select_wand", p -> new TargetSelectWand(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.handheld(ctx)).register();

		WAND_AI = L2Magic.REGISTRATE.item(
						"ai_config_wand", p -> new AiConfigWand(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.handheld(ctx)).register();

		WAND_EQUIPMENT = L2Magic.REGISTRATE.item(
						"equipment_wand", p -> new EquipmentWand(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.handheld(ctx)).register();

		GENERIC_PROJECTILE = L2Magic.REGISTRATE
				.<LMProjectile>entity("generic_projectile", LMProjectile::new, MobCategory.MISC)
				.properties(p -> p.setShouldReceiveVelocityUpdates(false).updateInterval(100)
						.sized(0.01f, 0.01f).clientTrackingRange(4))
				.renderer(() -> LMProjectileRenderer::new)
				.register();

		GENERIC_PARTICLE = L2Magic.REGISTRATE.simple("generic_particle", Registries.PARTICLE_TYPE, LMGenericParticleType::new);
		EQUIPMENTS = L2Magic.REGISTRATE.menu("equipments", EquipmentsMenu::fromNetwork, () -> EquipmentsScreen::new).register();
		CURIOS = L2Magic.REGISTRATE.menu("curios", EntityCuriosListMenu::fromNetwork, () -> EntityCuriosListScreen::new).register();
	}

	public static void register() {

	}

}
