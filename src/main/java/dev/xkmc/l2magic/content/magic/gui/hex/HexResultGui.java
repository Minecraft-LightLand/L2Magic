package dev.xkmc.l2magic.content.magic.gui.hex;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.menu.WindowBox;
import dev.xkmc.l2magic.content.magic.gui.AbstractHexGui;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProduct;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.data.LangData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class HexResultGui extends AbstractHexGui {

	private static final int FLOW_COUNT = 5;
	private static final float RADIUS = 30, PERIOD = 60, SCALE_NODE = 1f, SCALE_FLOW = 0.5f;

	private final Minecraft minecraft = Minecraft.getInstance();
	private final MagicHexScreen screen;
	private final MagicElement[] elements;
	private final boolean[][] graph;

	private int selected = -1;
	private double sele_x = 0;
	private double sele_y = 0;
	private int tick = 0;
	private boolean isDragging = false;

	int cost = 0;
	final MagicProduct.HexData data;
	final WindowBox box = new WindowBox();

	public HexResultGui(MagicHexScreen screen) {
		this.screen = screen;
		IMagicRecipe recipe = screen.product.recipe;
		this.elements = Arrays.copyOf(recipe.getElements(), 6);
		this.graph = recipe.getGraph();
		data = screen.product.getMiscData();
		if (data.order == null) {
			data.order = new int[6];
			for (int i = 0; i < 6; i++)
				data.order[i] = i;
		}
	}

	public void render(PoseStack matrix, double mx, double my, float partial) {
		float progress = (tick + partial) / PERIOD % 1;

		RenderSystem.disableTexture();
		float x0 = box.x + box.w / 2f;
		float y0 = box.y + box.w / 2f;
		int hover = within(mx, my);
		for (int i = 0; i < 6; i++) {
			double ri = i * Math.PI / 3;
			double xi = x0 + RADIUS * Math.cos(ri);
			double yi = y0 + RADIUS * Math.sin(ri);
			int color = i == hover ? 0xFF808080 : 0xFFFFFFFF;
			HexRenderUtil.renderHex(matrix, xi, yi, 10, color);
		}
		RenderSystem.enableTexture();

		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		for (int i = 0; i < 6; i++) {
			MagicElement elem = getElem(i);
			if (elem == null)
				continue;
			double xi = getX(i);
			double yi = getY(i);
			RenderSystem.setShaderTexture(0, elem.getIcon());
			for (int j = 0; j < 6; j++) {
				if (getElem(j) == null || !graph[data.order[i]][data.order[j]])
					continue;
				double xj = getX(j);
				double yj = getY(j);
				for (int k = 0; k < FLOW_COUNT; k++) {
					double p = (progress + k * 1.0 / FLOW_COUNT) % 1;
					double xp = xi + (xj - xi) * p;
					double yp = yi + (yj - yi) * p;
					//TODO icon
					drawIcon(matrix, xp, yp, SCALE_FLOW);
				}
			}
		}
		RenderSystem.disableBlend();

		for (int i = 0; i < 6; i++) {
			MagicElement elem = getElem(i);
			if (elem == null)
				continue;
			RenderSystem.setShaderTexture(0, elem.getIcon());
			double xi = getX(i);
			double yi = getY(i);
			drawIcon(matrix, xi, yi, SCALE_NODE);
		}

		for (int i = 0; i < data.list.size(); i++) {
			MagicElement elem = data.list.get(i);
			RenderSystem.setShaderTexture(0, elem.getIcon());
			double xi = box.x + box.w / 2d - 27 + i * 18;
			double yi = box.y + box.w + 18;
			drawIcon(matrix, xi, yi, SCALE_NODE);
		}
		float hi = box.y + box.w + 36;
		Font font = minecraft.font;
		font.draw(matrix, screen.save.getDesc(), box.x + 9, hi, screen.save.getColor());
		font.draw(matrix, screen.compile.getDesc(), box.x + 9, hi + 9, screen.compile.getColor());
		font.draw(matrix, LangData.IDS.HEX_COST.get(cost), box.x + 9, hi + 18, 0xFF000000);
	}

	@Nullable
	MagicElement getElem(int i) {
		return elements[data.order[i]];
	}

	private double getX(int i) {
		if (isDragging && selected == i)
			return sele_x;
		double ri = i * Math.PI / 3;
		float x0 = box.x + box.w / 2f;
		return x0 + RADIUS * Math.cos(ri);
	}

	private double getY(int i) {
		if (isDragging && selected == i)
			return sele_y;
		double ri = i * Math.PI / 3;
		float y0 = box.y + box.w / 2f;
		return y0 + RADIUS * Math.sin(ri);
	}

	public void tick() {
		tick++;
		tick %= PERIOD;
	}

	public boolean mouseDragged(double x0, double y0, int button, double dx, double dy) {
		if (!isDragging) {
			isDragging = true;
			int i = within(x0, y0);
			if (i >= 0) {
				if (getElem(i) != null) {
					selected = i;
				}
			}
		}
		if (selected != -1) {
			sele_x = x0;
			sele_y = y0;
		}
		return false;
	}

	public boolean mouseReleased(double x0, double y0, int button) {
		if (isDragging) {
			isDragging = false;
			if (selected != -1) {
				int i = within(x0, y0);
				if (i >= 0) {
					int elem = data.order[i];
					data.order[i] = data.order[selected];
					data.order[selected] = elem;
					screen.updated();
				}
			}
			selected = -1;
			return true;
		}
		return false;
	}

	private int within(double mx, double my) {
		//TODO hex coordinate
		float x0 = box.x + box.w / 2f;
		float y0 = box.y + box.w / 2f;
		for (int i = 0; i < 6; i++) {
			double ri = i * Math.PI / 3;
			double xi = x0 + RADIUS * Math.cos(ri);
			double yi = y0 + RADIUS * Math.sin(ri);
			if (mx > xi - 8 && mx < xi + 8 && my > yi - 8 && my < yi + 8) {
				return i;
			}
		}
		return -1;
	}

}
