package dev.xkmc.l2magic.content.magic.products.recipe;

import com.google.common.collect.Maps;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2magic.content.magic.products.IMagicProduct;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.info.DisplayInfo;
import dev.xkmc.l2magic.network.ConfigType;
import dev.xkmc.l2magic.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SerialClass
public class IMagicRecipe extends BaseConfig {

	@SerialClass.SerialField
	public ArrayList<ResourceLocation> predecessor = new ArrayList<>();
	@SerialClass.SerialField
	public ArrayList<ElementalMastery> elemental_mastery = new ArrayList<>();
	@SerialClass.SerialField
	public MagicProductType<?, ?> product_type;
	@SerialClass.SerialField
	public ResourceLocation product_id;
	@SerialClass.SerialField
	public DisplayInfo screen;

	private MagicElement[] elements;
	private boolean[][] maps;

	@Deprecated
	public IMagicRecipe() {
	}

	@DataGenOnly
	public IMagicRecipe(MagicProductType<?, ?> type, ResourceLocation id, DisplayInfo screen) {
		this.product_type = type;
		this.product_id = id;
		this.screen = screen;
	}

	@DataGenOnly
	public IMagicRecipe setPredecessors(ResourceLocation... pred) {
		this.predecessor = new ArrayList<>(List.of(pred));
		return this;
	}

	@DataGenOnly
	public IMagicRecipe addElemRequirement(ElementalMastery... elem) {
		this.elemental_mastery = new ArrayList<>(List.of(elem));
		return this;
	}

	public static List<IMagicRecipe> getAll() {
		return NetworkManager.getConfigs(ConfigType.MAGIC_DATA).map(e -> (IMagicRecipe) e.getValue()).toList();
	}

	public static <T> Map<T, IMagicRecipe> getMap(MagicProductType<T, ?> type) {
		Map<T, IMagicRecipe> ans = Maps.newLinkedHashMap();
		getAll().stream().filter(r -> r.product_type == type)
				.forEach(r -> Optional.of(type.getter.apply(r.product_id)).ifPresent((t) -> ans.put(t, r)));
		return ans;
	}

	public final IMagicProduct<?, ?> getProduct() {
		return IMagicProduct.getInstance(product_type, product_id);
	}

	protected final void register(MagicElement[] elements, boolean[][] maps) {
		this.elements = elements;
		this.maps = maps;
	}

	public final MagicElement[] getElements() {
		return elements;
	}

	public final boolean[][] getGraph() {
		return maps;
	}

	public record ElementalMastery(MagicElement element, int level) {

	}

}
