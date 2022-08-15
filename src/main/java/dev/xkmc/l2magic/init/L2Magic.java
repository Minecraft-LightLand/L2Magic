package dev.xkmc.l2magic.init;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.tabs.contents.AttributeEntry;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2magic.content.common.command.ArcaneCommand;
import dev.xkmc.l2magic.content.common.command.BaseCommand;
import dev.xkmc.l2magic.content.common.command.MagicCommand;
import dev.xkmc.l2magic.content.common.command.RegistryParser;
import dev.xkmc.l2magic.events.DamageEventHandler;
import dev.xkmc.l2magic.events.GenericEventHandler;
import dev.xkmc.l2magic.events.ItemUseEventHandler;
import dev.xkmc.l2magic.events.MiscEventHandler;
import dev.xkmc.l2magic.init.data.AllTags;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.data.configs.ConfigGenDispatcher;
import dev.xkmc.l2magic.init.data.recipe.RecipeGen;
import dev.xkmc.l2magic.init.registrate.*;
import dev.xkmc.l2magic.init.special.ArcaneRegistry;
import dev.xkmc.l2magic.init.special.LightLandRegistry;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.init.special.SpellRegistry;
import dev.xkmc.l2magic.network.NetworkManager;
import dev.xkmc.l2magic.util.EffectAddUtil;
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
		LLBlocks.register();
		LLEntities.register();
		LLItems.register();
		LLMenu.register();
		LLRecipes.register(bus);
		LLEffects.register();
		LLParticle.register();
		LightLandRegistry.register();
		MagicRegistry.register();
		ArcaneType.register();
		ArcaneRegistry.register();
		SpellRegistry.register();
		AllTags.register();
		NetworkManager.register();
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);

		LLPlayerData.register();
	}

	private static void registerForgeEvents() {
		MinecraftForge.EVENT_BUS.register(ItemUseEventHandler.class);
		MinecraftForge.EVENT_BUS.register(GenericEventHandler.class);
		MinecraftForge.EVENT_BUS.register(DamageEventHandler.class);
		MinecraftForge.EVENT_BUS.register(MiscEventHandler.class);

	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(L2Magic::setup);
		bus.addListener(L2MagicClient::clientSetup);
		bus.addListener(EventPriority.LOWEST, L2Magic::gatherData);
		bus.addListener(L2Magic::onParticleRegistryEvent);
		bus.addListener(L2Magic::registerCaps);
		bus.addListener(LLEntities::registerEntityAttributes);
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
			EffectAddUtil.init();
			LLEffects.registerBrewingRecipe();
			AttributeEntry.add(LightLandRegistry.MAX_MANA, true, 20000);
			AttributeEntry.add(LightLandRegistry.MAX_SPELL_LOAD, true, 21000);
			AttributeEntry.add(LightLandRegistry.MANA_RESTORE, true, 22000);
		});
	}

	private static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, LightLandRegistry.MAX_MANA.get());
		event.add(EntityType.PLAYER, LightLandRegistry.MAX_SPELL_LOAD.get());
		event.add(EntityType.PLAYER, LightLandRegistry.MANA_RESTORE.get());
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
		event.getGenerator().addProvider(true, new ConfigGenDispatcher(event.getGenerator()));
	}

	public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
		LLParticle.registerClient();
	}

	public static void registerCaps(RegisterCapabilitiesEvent event) {
	}

}
