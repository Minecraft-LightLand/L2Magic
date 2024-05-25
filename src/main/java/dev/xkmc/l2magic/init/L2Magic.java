package dev.xkmc.l2magic.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2itemselector.select.item.IItemSelector;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2magic.content.engine.context.SpellUsePacket;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.item.CreativeSpellSelector;
import dev.xkmc.l2magic.init.data.LMDatapackRegistriesGen;
import dev.xkmc.l2magic.init.data.LMLangData;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Magic.MODID)
@Mod.EventBusSubscriber(modid = L2Magic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Magic {

	public static final String MODID = "l2magic";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(MODID, "main"), 1,
			e -> e.create(SpellUsePacket.class, NetworkDirection.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public L2Magic() {
		EngineRegistry.register();
		LMItems.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, LMLangData::genLang);
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		IItemSelector.register(new CreativeSpellSelector(loc("spell_select")));
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void onDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(EngineRegistry.SPELL, SpellAction.CODEC, SpellAction.CODEC);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		boolean run = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(run, new LMDatapackRegistriesGen(output, pvd));
	}

	public static ResourceLocation loc(String id) {
		return new ResourceLocation(MODID, id);
	}

}
