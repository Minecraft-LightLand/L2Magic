package dev.xkmc.l2magic.content.magic.gui.tree;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;
import java.util.Map;

public class MagicTreeScreen extends Screen {

	private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
	private static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
	private static final Component VERY_SAD_LABEL = LangData.translate("advancements.sad_label");
	private static final Component NO_ADVANCEMENTS_LABEL = LangData.translate("advancements.empty");
	private static final Component TITLE = LangData.translate("gui.advancements");

	private static final MagicProductType<?, ?>[] TABS = {MagicRegistry.MPT_CRAFT.get(), MagicRegistry.MPT_EFF.get(), MagicRegistry.MPT_ENCH.get(), MagicRegistry.MPT_SPELL.get(), MagicRegistry.MPT_ARCANE.get()};

	public final AbstractClientPlayer player;
	public final MagicData handler;
	public final Map<MagicProductType<?, ?>, MagicTreeGui<?, ?>> tabs = Maps.newLinkedHashMap();

	private MagicTreeGui<?, ?> selected = null;
	private boolean isScrolling = false;

	public MagicTreeScreen() {
		super(TITLE);
		this.player = Proxy.getClientPlayer();
		handler = MagicData.get(player);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void init() {
		int index = 0;
		if (selected != null) {
			index = selected.index;
		}
		tabs.clear();
		for (MagicProductType<?, ?> type : TABS) {
			MagicTreeGui gui = new MagicTreeGui(this, type, tabs.size());
			if (tabs.size() == index) {
				selected = gui;
			}
			tabs.put(type, gui);
		}
		for (IMagicRecipe r : handler.magicHolder.listRecipe()) {
			((MagicTreeGui) tabs.get(r.product_type)).addWidget(handler.magicHolder.getProduct(r));
		}
	}

	public void render(PoseStack matrix, int mx, int my, float partialTick) {
		int x0 = (this.width - 252) / 2;
		int y0 = (this.height - 140) / 2;
		this.renderBackground(matrix);

		this.renderInside(matrix, x0, y0);
		this.renderWindow(matrix, x0, y0);
		this.renderTooltips(matrix, mx, my, x0, y0);
	}

	private void renderInside(PoseStack matrix, int x0, int y0) {
		if (selected == null) {
			fill(matrix, x0 + 9, y0 + 18, x0 + 9 + 234, y0 + 18 + 113, -16777216);
			int i = x0 + 9 + 117;
			drawCenteredString(matrix, this.font, NO_ADVANCEMENTS_LABEL, i, y0 + 18 + 56 - 4, -1);
			drawCenteredString(matrix, this.font, VERY_SAD_LABEL, i, y0 + 18 + 113 - 9, -1);
		} else {
			PoseStack mat = RenderSystem.getModelViewStack();
			mat.pushPose();
			mat.translate((float) (x0 + 9), (float) (y0 + 18), 0.0F);
			RenderSystem.applyModelViewMatrix();
			selected.drawContents(matrix);
			mat.popPose();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.depthFunc(515);
			RenderSystem.disableDepthTest();
		}

	}

	private void renderWindow(PoseStack matrix, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
		this.blit(matrix, x, y, 0, 0, 252, 140);
		if (this.tabs.size() > 1) {
			RenderSystem.setShaderTexture(0, TABS_LOCATION);
			Iterator<MagicTreeGui<?, ?>> iterator;
			iterator = this.tabs.values().iterator();
			while (iterator.hasNext()) {
				MagicTreeGui<?, ?> gui = iterator.next();
				gui.drawTab(matrix, x, y, gui == selected);
			}
			RenderSystem.defaultBlendFunc();
			iterator = this.tabs.values().iterator();
			while (iterator.hasNext()) {
				MagicTreeGui<?, ?> gui = iterator.next();
				gui.drawIcon(x, y, this.itemRenderer);
			}
			RenderSystem.disableBlend();
		}
		this.font.draw(matrix, TITLE, (float) (x + 8), (float) (y + 6), 4210752);
	}

	private void renderTooltips(PoseStack matrix, int mx, int my, int x0, int y0) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (selected != null) {
			PoseStack mat = RenderSystem.getModelViewStack();
			mat.pushPose();
			RenderSystem.enableDepthTest();
			mat.translate((float) (x0 + 9), (float) (y0 + 18), 400.0F);
			RenderSystem.applyModelViewMatrix();
			RenderSystem.enableDepthTest();
			selected.drawTooltips(matrix, mx - x0 - 9, my - y0 - 18, x0, y0);
			RenderSystem.disableDepthTest();
			mat.popPose();
			RenderSystem.applyModelViewMatrix();
		}

		if (this.tabs.size() > 1) {
			for (MagicTreeGui<?, ?> gui : this.tabs.values()) {
				if (gui.isMouseOver(x0, y0, mx, my)) {
					renderTooltip(matrix, gui.getTitle(), mx, my);
				}
			}
		}

	}

	public boolean mouseDragged(double x0, double y0, int button, double dx, double dy) {
		if (button != 0) {
			this.isScrolling = false;
			return false;
		} else {
			if (!this.isScrolling) {
				this.isScrolling = true;
			} else if (this.selected != null) {
				this.selected.scroll(dx, dy);
			}

			return true;
		}
	}

	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0) {
			int i = (this.width - 252) / 2;
			int j = (this.height - 140) / 2;
			for (MagicTreeGui<?, ?> gui : this.tabs.values()) {
				if (gui.isMouseOver(i, j, mx, my)) {
					this.selected = gui;
					break;
				}
			}
			if (selected.mouseClicked(i, j, (int) Math.round(mx - i - 9), (int) Math.round(my - j - 18))) {
				return true;
			}
		}
		return super.mouseClicked(mx, my, button);
	}


}
