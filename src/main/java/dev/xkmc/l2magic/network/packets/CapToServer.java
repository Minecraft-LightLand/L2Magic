package dev.xkmc.l2magic.network.packets;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.common.capability.MagicHolder;
import dev.xkmc.l2magic.content.magic.item.MagicWand;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.network.LLSerialPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@SerialClass
public class CapToServer extends LLSerialPacket {

	public enum Action {
		HEX((handler, tag) -> {
			MagicHolder holder = handler.magicHolder;
			String str = tag.getString("product");
			IMagicRecipe recipe = holder.getRecipe(new ResourceLocation(str));
			assert recipe != null;
			MagicProduct<?, ?> prod = holder.getProduct(recipe);
			CompoundTag ctag = prod.tag.tag;
			Set<String> set = new HashSet<>(ctag.getAllKeys());
			for (String key : set) {
				ctag.remove(key);
			}
			CompoundTag dtag = tag.getCompound("data");
			for (String key : dtag.getAllKeys()) {
				ctag.put(key, Objects.requireNonNull(dtag.get(key)));
			}
			holder.checkUnlocks();
		}),
		DEBUG((handler, tag) -> {
			LogManager.getLogger().info("server: " + tag.getCompound("server"));
			LogManager.getLogger().info("client: " + tag.getCompound("client"));
		}),
		WAND((handler, tag) -> {
			Player player = handler.player;
			IMagicRecipe recipe = handler.magicHolder.getRecipe(new ResourceLocation(tag.getString("recipe")));
			if (recipe == null)
				return;
			ItemStack stack = player.getMainHandItem();
			if (!(stack.getItem() instanceof MagicWand)) {
				stack = player.getOffhandItem();
			}
			if (!(stack.getItem() instanceof MagicWand wand))
				return;
			wand.setMagic(recipe, stack);
		});

		private final BiConsumer<MagicData, CompoundTag> cons;

		Action(BiConsumer<MagicData, CompoundTag> cons) {
			this.cons = cons;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendHexUpdate(MagicProduct<?, ?> prod) {
		CompoundTag tag = new CompoundTag();
		tag.putString("product", prod.recipe.getID().toString());
		tag.put("data", prod.tag.tag);
		new CapToServer(Action.HEX, tag).toServer();
		MagicData.getClientAccess().magicHolder.checkUnlocks();
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendDebugInfo(CompoundTag s, CompoundTag c) {
		CompoundTag tag = new CompoundTag();
		tag.put("server", s);
		tag.put("client", c);
		new CapToServer(Action.DEBUG, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void activateWand(IMagicRecipe recipe) {
		CompoundTag tag = new CompoundTag();
		tag.putString("recipe", recipe.getID().toString());
		Action.WAND.cons.accept(MagicData.getClientAccess(), tag);
		new CapToServer(Action.WAND, tag).toServer();
	}

	@SerialClass.SerialField
	public Action action;
	@SerialClass.SerialField
	public CompoundTag tag;

	@Deprecated
	public CapToServer() {

	}

	private CapToServer(Action act, CompoundTag tag) {
		this.action = act;
		this.tag = tag;
	}

	public void handle(NetworkEvent.Context ctx) {
		ServerPlayer player = ctx.getSender();
		if (player == null || !player.isAlive())
			return;
		MagicData handler = MagicData.get(player);
		action.cons.accept(handler, tag);
	}

}
