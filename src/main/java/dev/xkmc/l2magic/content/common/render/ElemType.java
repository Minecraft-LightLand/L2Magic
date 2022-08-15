package dev.xkmc.l2magic.content.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2magic.content.magic.gui.AbstractHexGui;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.init.special.MagicRegistry;

public enum ElemType {
	E(0, 30, MagicRegistry.ELEM_EARTH),
	W(-30, 0, MagicRegistry.ELEM_WATER),
	A(0, -30, MagicRegistry.ELEM_AIR),
	F(30, 0, MagicRegistry.ELEM_FIRE),
	Q(0, 0, MagicRegistry.ELEM_QUINT);

	public final int x, y;
	public final RegistryEntry<MagicElement> elem;

	ElemType(int x, int y, RegistryEntry<MagicElement> elem) {
		this.x = x;
		this.y = y;
		this.elem = elem;
	}

	public void renderElem(LLPlayerData handler, PoseStack matrix, int mx, int my) {
		int lv = handler.magicHolder.getElementalMastery(elem.get());
		int count = handler.magicHolder.getElement(elem.get());
		AbstractHexGui.drawElement(matrix, x, y, elem.get(), "" + lv);
		//if (within(mx, my) && handler.abilityPoints.canLevelElement())
		//	fill(matrix, x - 8, y - 8, x + 8, y + 8, 0x80FFFFFF);
	}

	public boolean within(double mx, double my) {
		return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
	}

}
