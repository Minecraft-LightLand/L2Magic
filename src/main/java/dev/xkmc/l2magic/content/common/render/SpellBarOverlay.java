package dev.xkmc.l2magic.content.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.overlay.OverlayManager;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.util.Lazy;

public class SpellBarOverlay implements IGuiOverlay {

	public static Lazy<OverlayManager> MANAGER = Lazy.of(() -> OverlayManager.get(L2Magic.MODID, "widgets"));

	@Override
	public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (gui.minecraft.options.hideGui) return;
		if (gui.minecraft.player == null) return;
		OverlayManager.ScreenRenderer renderer = MANAGER.get().getRenderer(gui, mStack);
		renderer.start();
		renderItem(renderer, partialTicks);
		renderMagicBars(renderer);
	}

	private void renderItem(OverlayManager.ScreenRenderer renderer, float partialTicks) {
		if (renderer.localPlayer == null) return;
		int x = 0;
		Player player = renderer.localPlayer;
		int sel = player.getInventory().selected;
		if (MagicData.isProper(player)) {
			x = MagicData.getClientAccess().magicAbility.spell_level;
		}
		// render hotbar
		if (x > 0) {
			int tlen = x == 9 ? 182 : 1 + x * 20;
			renderer.drawLeftRight("spell_list", tlen, 182);
		}
		if (x < 9) {
			int left = x == 0 ? 0 : 20 * x + 1;
			renderer.blit("item_list", left, 0, left, 0, -left, 0);
		}
		if (sel < x) {
			renderer.draw("spell_select", 20 * sel, 0);
		}
		if (sel >= x) {
			renderer.draw("item_select", 20 * sel, 0);
		}

		ItemStack itemstack = player.getOffhandItem();
		if (!itemstack.isEmpty()) {
			renderer.draw("assistant_list");
		}
		// render items
		int index = 1;
		int x0 = renderer.gui.screenWidth / 2;
		OverlayManager.Rect items = renderer.get("item_list");
		for (int slot = 0; slot < 9; ++slot) {
			int x1 = x0 + slot * 20 + items.sx + 3;
			int y1 = renderer.gui.screenHeight + items.sy + 3;
			renderer.gui.renderSlot(x1, y1, partialTicks, player, player.getInventory().items.get(slot), index++);
		}
		if (!itemstack.isEmpty()) {
			OverlayManager.Rect alt = renderer.get("assistant_list");
			int j2 = renderer.gui.screenHeight + alt.sy + 3;
			renderer.gui.renderSlot(x0 + alt.sx + 3, j2, partialTicks, player, itemstack, index);

		}
		renderer.start();
	}

	private void renderMagicBars(OverlayManager.ScreenRenderer renderer) {
		if (renderer.localPlayer == null || !MagicData.isProper(renderer.localPlayer)) return;
		MagicData data = MagicData.getClientAccess();
		int mana = data.magicAbility.getMana();
		int mana_max = data.magicAbility.getMaxMana();
		int load = data.magicAbility.getSpellLoad();
		int load_max = data.magicAbility.getMaxSpellEndurance();
		renderer.draw("mp_strip_empty");
		renderer.drawLeftRight("mp_strip", mana, mana_max);
		renderer.draw("mp");

		renderer.draw("spell_strip_empty");
		renderer.drawLeftRight("spell_strip", load, load_max);
		renderer.draw("spell");
	}

}
