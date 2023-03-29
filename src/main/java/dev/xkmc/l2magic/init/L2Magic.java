package dev.xkmc.l2magic.init;

import dev.xkmc.l2complements.events.ItemUseEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.effects.EffectSyncEvents;
import dev.xkmc.l2library.base.tabs.contents.AttributeEntry;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.common.command.ArcaneCommand;
import dev.xkmc.l2magic.content.common.command.BaseCommand;
import dev.xkmc.l2magic.content.common.command.MagicCommand;
import dev.xkmc.l2magic.content.common.command.RegistryParser;
import dev.xkmc.l2magic.events.MiscEventHandler;
import dev.xkmc.l2magic.init.data.AllTags;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.data.configs.ConfigGenDispatcher;
import dev.xkmc.l2magic.init.data.recipe.RecipeGen;
import dev.xkmc.l2magic.init.registrate.*;
import dev.xkmc.l2magic.init.special.ArcaneRegistry;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.init.special.SpellRegistry;
import dev.xkmc.l2magic.network.NetworkManager;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Magic.MODID)
public class L2Magic {

	public static final String MODID = "l2magic";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	private static void registerRegistrates(IEventBus bus) {
		ForgeMod.enableMilkFluid();
		LMBlocks.register();
		LMEntities.register();
		LMItems.register();
		LMMenu.register();
		LMRecipes.register(bus);
		LMEffects.register();
		MagicRegistry.register();
		ArcaneType.register();
		ArcaneRegistry.register();
		SpellRegistry.register();
		AllTags.register();
		NetworkManager.register();
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);

		MagicData.register();
	}

	private static void registerForgeEvents() {
		ItemUseEventHandler.LIST.add(new ArcaneItemUseHelper());
		MinecraftForge.EVENT_BUS.register(MiscEventHandler.class);
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(L2Magic::setup);
		bus.addListener(L2MagicClient::clientSetup);
		bus.addListener(EventPriority.LOWEST, L2Magic::gatherData);
		bus.addListener(L2Magic::onParticleRegistryEvent);
		bus.addListener(L2Magic::registerCaps);
		bus.addListener(LMEntities::registerEntityAttributes);
		bus.addListener(L2Magic::modifyAttributes);
	}

	private static void registerCommands() {
		RegistryParser.register();
		BaseCommand.LIST.add(ArcaneCommand::new);
		BaseCommand.LIST.add(MagicCommand::new);
	}

	public L2Magic() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> L2MagicClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
		registerRegistrates(bus);
		registerForgeEvents();
		registerCommands();
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EffectSyncEvents.TRACKED.add(LMEffects.ARCANE.get());
			LMEffects.registerBrewingRecipe();
			AttributeEntry.add(MagicRegistry.MAX_MANA, true, 20000);
			AttributeEntry.add(MagicRegistry.MAX_SPELL_LOAD, true, 21000);
			AttributeEntry.add(MagicRegistry.MANA_RESTORE, true, 22000);
			AttributeEntry.add(MagicRegistry.LOAD_RESTORE, true, 23000);
		});
	}

	private static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, MagicRegistry.MAX_MANA.get());
		event.add(EntityType.PLAYER, MagicRegistry.MAX_SPELL_LOAD.get());
		event.add(EntityType.PLAYER, MagicRegistry.MANA_RESTORE.get());
		event.add(EntityType.PLAYER, MagicRegistry.LOAD_RESTORE.get());
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
		event.getGenerator().addProvider(true, new ConfigGenDispatcher(event.getGenerator()));
	}

	public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
	}

	public static void registerCaps(RegisterCapabilitiesEvent event) {
	}

}
