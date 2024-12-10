package dev.xkmc.l2magic.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2itemselector.select.item.IItemSelector;
import dev.xkmc.l2magic.content.engine.context.SpellUsePacket;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.core.ProjectileExpirePacket;
import dev.xkmc.l2magic.content.entity.core.ProjectileHitPacket;
import dev.xkmc.l2magic.content.entity.core.ProjectileLandPacket;
import dev.xkmc.l2magic.content.item.spell.CreativeSpellSelector;
import dev.xkmc.l2magic.init.data.LMDatapackRegistriesGen;
import dev.xkmc.l2magic.init.data.LMLangData;
import dev.xkmc.l2magic.init.data.LMTagGen;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import dev.xkmc.l2serial.network.PacketHandler;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Magic.MODID)
@EventBusSubscriber(modid = L2Magic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2Magic {

	public static final String MODID = "l2magic";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1,
			e -> e.create(SpellUsePacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(ProjectileHitPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(ProjectileLandPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(ProjectileExpirePacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
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
		event.dataPackRegistry(EngineRegistry.PROJECTILE, ProjectileConfig.CODEC, ProjectileConfig.CODEC);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, LMTagGen::genBlockTag);
		boolean run = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(run, new LMDatapackRegistriesGen(output, pvd));
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
