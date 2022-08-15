package dev.xkmc.l2magic.content.magic.gui.tree;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2magic.content.common.capability.MagicHolder;
import dev.xkmc.l2magic.content.magic.gui.GuiTabType;
import dev.xkmc.l2magic.content.magic.gui.hex.MagicHexScreen;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.info.TypeConfig;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Map;

public class MagicTreeGui<I, P extends MagicProduct<I, P>> extends GuiComponent {

	private final MagicTreeScreen screen;
	private final GuiTabType tab = GuiTabType.ABOVE;
	private final Map<P, MagicTreeEntry<I, P>> widgets = Maps.newLinkedHashMap();
	private final TypeConfig display;
	private final Component title;

	final int index;
	final MagicProductType<I, P> type;

	private boolean centered = false;
	private int maxX, maxY, minX, minY;
	private double scrollX, scrollY;
	private float fade;

	public MagicTreeGui(MagicTreeScreen screen, MagicProductType<I, P> type, int index) {
		this.screen = screen;
		this.index = index;
		this.type = type;
		this.display = type.getDisplay();
		this.title = type.getDesc();
	}

	public void drawTab(PoseStack matrix, int x, int y, boolean selected) {
		this.tab.draw(matrix, this, x, y, selected, this.index);
	}

	public boolean isMouseOver(int x, int y, double mx, double my) {
		return this.tab.isMouseOver(x, y, this.index, mx, my);
	}

	public void drawIcon(int x, int y, ItemRenderer renderer) {
		this.tab.drawIcon(x, y, this.index, renderer, display.getIcon());
	}

	public void drawContents(PoseStack matrix) {
		if (!this.centered) {
			this.scrollX = 117 - (this.maxX + this.minX) / 2.0;
			this.scrollY = 56 - (this.maxY + this.minY) / 2.0;
			this.centered = true;
		}

		matrix.pushPose();
		matrix.translate(0.0F, 0.0F, 950.0F);
		RenderSystem.enableDepthTest();
		RenderSystem.colorMask(false, false, false, false);
		fill(matrix, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		matrix.translate(0.0F, 0.0F, -950.0F);
		RenderSystem.depthFunc(518);
		fill(matrix, 234, 113, 0, 0, -16777216);
		RenderSystem.depthFunc(515);
		ResourceLocation rl = display.background();
		RenderSystem.setShaderTexture(0, rl != null ? rl : TextureManager.INTENTIONAL_MISSING_TEXTURE);
		int i = Mth.floor(this.scrollX);
		int j = Mth.floor(this.scrollY);
		int k = i % 16;
		int l = j % 16;
		for (int i1 = -1; i1 <= 15; ++i1) {
			for (int j1 = -1; j1 <= 8; ++j1) {
				blit(matrix, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
			}
		}
		for (MagicTreeEntry<I, P> entry : widgets.values()) {
			entry.drawConnectivity(matrix, i, j, true);
		}
		for (MagicTreeEntry<I, P> entry : widgets.values()) {
			entry.drawConnectivity(matrix, i, j, false);
		}
		for (MagicTreeEntry<I, P> entry : widgets.values()) {
			entry.draw(matrix, i, j);
		}
		RenderSystem.depthFunc(518);
		matrix.translate(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrix, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.depthFunc(515);
		matrix.popPose();
	}

	public void drawTooltips(PoseStack stack, int mx, int my, int x0, int y0) {
		stack.pushPose();
		stack.translate(0.0F, 0.0F, 200.0F);
		fill(stack, 0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
		boolean flag = false;
		int sx = Mth.floor(this.scrollX);
		int sy = Mth.floor(this.scrollY);
		if (mx > 0 && mx < 234 && my > 0 && my < 113) {

			for (MagicTreeEntry<I, P> entry : this.widgets.values()) {
				if (entry.isMouseOver(sx, sy, mx, my)) {
					flag = true;
					entry.drawHover(stack, sx, sy, this.fade, x0, y0);
					break;
				}
			}
		}

		stack.popPose();
		if (flag) {
			this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
		} else {
			this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
		}

	}

	public Component getTitle() {
		return title;
	}

	public void scroll(double dx, double dy) {
		if (this.maxX - this.minX > 234) {
			this.scrollX = Mth.clamp(this.scrollX + dx, 234 - maxX, -minX);
		}
		double pre = this.scrollY;
		if (this.maxY - this.minY > 113) {
			this.scrollY = Mth.clamp(this.scrollY + dy, 113 - maxY, -minY);
		}
	}

	@SuppressWarnings("unchecked")
	void addWidget(P product) {
		if (widgets.containsKey(product))
			return;
		MagicHolder holder = screen.handler.magicHolder;
		MagicTreeEntry<I, P> entry = new MagicTreeEntry<>(this, product, product.recipe.screen);
		for (ResourceLocation rl : product.recipe.predecessor) {
			IMagicRecipe recipe = holder.getRecipe(rl);
			if (recipe == null) {
				continue;
			}
			P parent = (P) holder.getProduct(recipe);
			if (parent.type == type) {
				addWidget(parent);
				entry.parents.add(widgets.get(parent));
			}
		}
		this.widgets.put(product, entry);
		int i = entry.getX();
		int j = i + 28;
		int k = entry.getY();
		int l = k + 27;
		this.minX = Math.min(this.minX, i);
		this.maxX = Math.max(this.maxX, j);
		this.minY = Math.min(this.minY, k);
		this.maxY = Math.max(this.maxY, l);
	}

	public MagicTreeScreen getScreen() {
		return screen;
	}

	public boolean mouseClicked(int x0, int y0, int mx, int my) {
		int sx = Mth.floor(this.scrollX);
		int sy = Mth.floor(this.scrollY);
		if (mx > 0 && mx < 234 && my > 0 && my < 113)
			for (Map.Entry<P, MagicTreeEntry<I, P>> entry : widgets.entrySet()) {
				if (entry.getValue().isMouseOver(sx, sy, mx, my) && entry.getKey().unlocked()) {
					Minecraft.getInstance().setScreen(new MagicHexScreen(screen.handler, entry.getKey()));
					return true;
				}
			}
		return false;
	}
}
