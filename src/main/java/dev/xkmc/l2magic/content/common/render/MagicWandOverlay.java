package dev.xkmc.l2magic.content.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.magic.gui.AbstractHexGui;
import dev.xkmc.l2magic.content.magic.item.MagicWand;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.content.magic.ritual.AbstractLevelRitualRecipe;
import dev.xkmc.l2magic.content.magic.ritual.AbstractRitualRecipe;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import dev.xkmc.l2magic.network.packets.CapToServer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MagicWandOverlay implements IGuiOverlay {

	public static final MagicWandOverlay INSTANCE = new MagicWandOverlay();
	public static final List<MagicElement> ELEM = new ArrayList<>();
	public static boolean has_magic_wand = false;

	public static void input(int key, int action) {
		if (key == 259 && action == 1 && ELEM.size() > 0) {
			ELEM.remove(ELEM.size() - 1);
		}
		if (key == 257 && action == 1 && ELEM.size() > 0) {
			execute();
			ELEM.clear();
		}
		if (key == 'W' || key == 'A' || key == 'S' || key == 'D' || key == ' ') {
			if (action == 1 && ELEM.size() < 4) {
				MagicElement elem;
				if (key == 'W') elem = MagicRegistry.ELEM_AIR.get();
				else if (key == 'A') elem = MagicRegistry.ELEM_WATER.get();
				else if (key == 'S') elem = MagicRegistry.ELEM_EARTH.get();
				else if (key == 'D') elem = MagicRegistry.ELEM_FIRE.get();
				else elem = MagicRegistry.ELEM_QUINT.get();
				ELEM.add(elem);
			}
		}
	}

	@Nullable
	private static MagicProduct<?, ?> preview() {
		AbstractClientPlayer player = Proxy.getClientPlayer();
		if (player == null || !player.isAlive())
			return null;
		MagicData handler = MagicData.get(player);
		IMagicRecipe r = handler.magicHolder.getTree(ELEM);
		if (r == null) {
			return null;
		}
		MagicProduct<?, ?> p = handler.magicHolder.getProduct(r);
		if (p.usable())
			return p;
		return null;
	}

	private static void execute() {
		MagicProduct<?, ?> p = preview();
		if (p != null)
			CapToServer.activateWand(p.recipe);
	}

	@Override
	public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (!(has_magic_wand = renderMagicWandImpl(gui, mStack, partialTicks, width, height))) {
			ELEM.clear();
			GuiOverlayManager.findOverlay(VanillaGuiOverlay.CROSSHAIR.id()).overlay().render(gui, mStack, partialTicks, width, height);
		}
	}

	private boolean renderMagicWandImpl(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		AbstractClientPlayer player = Proxy.getClientPlayer();
		if (player == null || !player.isAlive())
			return false;
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!(stack.getItem() instanceof MagicWand wand))
			return false;
		if (wand.getData(player, stack) != null)
			return false;
		int x = width / 2 - 27;
		int y = height / 2;
		gui.setupOverlayRenderState(true, false);
		for (MagicElement elem : ELEM) {
			AbstractHexGui.drawElement(mStack, x, y + 60, elem, "");
			x += 18;
		}
		for (ElemType e : ElemType.values()) {
			AbstractHexGui.drawElement(mStack, width / 2f + e.x, height / 2f + e.y, e.elem.get(), "");
		}
		MagicProduct<?, ?> p = preview();
		if (p != null) {
			y = height / 2 - 60;
			Component text = LangData.translate(p.getDescriptionID());
			x = (width - gui.getFont().width(text)) / 2;
			gui.getFont().draw(mStack, text, x, y, 0xFFFFFFFF);
			if (p.type == MagicRegistry.MPT_ENCH.get() || p.type == MagicRegistry.MPT_EFF.get()) {
				AbstractClientPlayer pl = Proxy.getClientPlayer();
				if (pl != null) {
					x = (width / 2 + 60);
					int cost = p.getCost();
					Optional<AbstractRitualRecipe<?>> opr = Proxy.getWorld().getRecipeManager().getAllRecipesFor(LMRecipes.RT_RITUAL.get()).stream()
							.filter(e -> e instanceof AbstractLevelRitualRecipe<?>).filter(e -> p.recipe.getID().equals(e.getMagic()))
							.findFirst();
					if (opr.isPresent()) {
						AbstractRitualRecipe<?> r = opr.get();
						int lv = r.getLevel(cost);
						text = LangData.IDS.ENCH_LV.get(lv);
						gui.getFont().draw(mStack, text, x, y + 10, lv > 0 ? 0xFFFFFF : AbstractHexGui.RED);
						int next = r.getNextLevel(cost);
						if (next > 0) {
							text = LangData.IDS.ENCH_NEXT.get(next);
							gui.getFont().draw(mStack, text, x, y + 20, 0xFFFFFFFF);
						}
						text = LangData.IDS.ENCH_ELEM.get();
						gui.getFont().draw(mStack, text, x, y + 30, 0xFFFFFFFF);
						MagicElement[] elems = p.recipe.getElements();
						Map<MagicElement, Integer> map = new LinkedHashMap<>();
						for (MagicElement e : elems) {
							map.put(e, map.getOrDefault(e, 0) + lv);
						}
						for (MagicElement e : map.keySet()) {
							int has = MagicData.getClientAccess().magicHolder.getElement(e);
							int take = map.get(e);
							AbstractHexGui.drawElement(mStack, x + 9, y + 50, e, "" + take, has >= take ? 0xFFFFFF : AbstractHexGui.RED);
							x += 18;
						}
					}
				}
			}
			if (p.type == MagicRegistry.MPT_SPELL.get()) {
				Spell<?, ?> spell = (Spell<?, ?>) p.item;
				AbstractClientPlayer pl = Proxy.getClientPlayer();
				if (pl != null) {
					int mana = spell.getConfig(pl.level, pl).mana_cost;
					text = LangData.IDS.MANA_COST.get(mana);
					x = (width - gui.getFont().width(text)) / 2;
					gui.getFont().draw(mStack, text, x, y + 10, 0xFFFFFFFF);
				}
			}
		}
		return true;
	}


}