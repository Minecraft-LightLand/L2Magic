package dev.xkmc.l2magic.content.magic.products;

import dev.xkmc.l2library.idea.magic.HexHandler;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.common.capability.MagicHolder;
import dev.xkmc.l2magic.content.magic.products.info.ProductState;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MagicProduct<I, P extends MagicProduct<I, P>> extends IMagicProduct<I, P> {

	public static final int LOCKED = -2, UNLOCKED = -1;

	public final NBTObj tag;
	public final MagicHolder player;
	public final IMagicRecipe recipe;

	public MagicProduct(MagicProductType<I, P> type, MagicHolder player, @Nullable NBTObj tag, ResourceLocation rl, IMagicRecipe r) {
		super(type, rl);
		this.tag = tag;
		this.player = player;
		this.recipe = r;
		if (tag != null) {
			if (!tag.tag.contains("_base")) {
				tag.getSub("_base").tag.putInt("cost", LOCKED);
			}
		}
	}

	protected final NBTObj getBase() {
		return tag.getSub("_base");
	}

	public final boolean unlocked() {
		return getBase().tag.getInt("cost") > -2;
	}

	public final int getCost() {
		return getBase().tag.getInt("cost");
	}

	public final void setUnlock() {
		if (!unlocked())
			getBase().tag.putInt("cost", -1);
	}

	public void updateBestSolution(HexHandler hex, HexData data, int cost) {
		int prev = getBase().tag.getInt("cost");
		tag.tag.remove("hex");
		TagCodec.toTag(tag.getSub("misc").tag, data);
		hex.write(tag.getSub("hex"));
		getBase().tag.putInt("cost", cost);
	}

	public HexHandler getSolution() {
		if (!tag.tag.contains("hex"))
			return new HexHandler(3);
		return new HexHandler(tag.getSub("hex"));
	}

	public final boolean usable() {
		return getBase().tag.getInt("cost") > UNLOCKED;
	}

	public ProductState getState() {
		return switch (getBase().tag.getInt("cost")) {
			case LOCKED -> ProductState.LOCKED;
			case UNLOCKED -> ProductState.UNLOCKED;
			default -> ProductState.CRAFTED;
		};
	}

	public boolean visible() {
		return true;
	}

	public HexData getMiscData() {
		HexData data;
		if (tag.tag.contains("misc")) {
			data = TagCodec.fromTag(tag.getSub("misc").tag, HexData.class);
		} else {
			data = new HexData();
		}
		if (data.list.size() == 0)
			data.list.add(type.elem);
		else if (data.list.get(0) != type.elem)
			data.list.set(0, type.elem);
		return data;
	}

	public boolean matchList(List<MagicElement> elem) {
		return elem.equals(getMiscData().list);
	}

	@Nullable
	public CodeState logged(MagicData handler) {
		if (!usable())
			return null;
		List<MagicElement> list = getMiscData().list;
		if (list.size() < 4)
			return CodeState.SHORT;
		return handler.magicHolder.getTree(getMiscData().list) == recipe ? CodeState.FINE : CodeState.REPEAT;
	}

	@OnlyIn(Dist.CLIENT)
	public MutableComponent getDesc() {
		return LangData.translate(type.namer.apply(item));
	}

	@OnlyIn(Dist.CLIENT)
	public List<FormattedText> getFullDesc() {
		MagicData h = MagicData.getClientAccess();
		List<FormattedText> list = new ArrayList<>();
		list.add(LangData.get(getState()));
		if (!unlocked()) {
			for (IMagicRecipe.ElementalMastery em : recipe.elemental_mastery) {
				if (h.magicHolder.getElementalMastery(em.element()) < em.level())
					list.add(LangData.IDS.GUI_TREE_ELEM_PRE.get()
							.append(em.element().getDesc())
							.append(LangData.IDS.GUI_TREE_ELEM_POST.get(em.level()))
							.withStyle(ChatFormatting.RED));
			}
		}
		if (usable()) {
			list.add(LangData.IDS.GUI_TREE_COST.get(getCost()));
			CodeState state = logged(h);
			if (state == CodeState.SHORT)
				list.add(LangData.IDS.GUI_TREE_SHORT.get());
			if (state == CodeState.REPEAT)
				list.add(LangData.IDS.GUI_TREE_REPEAT.get());
		}
		return list;
	}

	@SerialClass
	public static class HexData {

		@SerialClass.SerialField
		public int[] order;

		@SerialClass.SerialField
		public ArrayList<MagicElement> list = new ArrayList<>();

	}

	public enum CodeState {
		SHORT, REPEAT, FINE
	}

}
