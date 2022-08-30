package dev.xkmc.l2magic.content.magic.gui.hex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.menu.WindowBox;
import dev.xkmc.l2library.idea.magic.*;
import dev.xkmc.l2library.util.math.Frac;
import dev.xkmc.l2magic.content.magic.gui.AbstractHexGui;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class HexGraphGui extends AbstractHexGui {

	private static final int PERIOD = 60;
	private static final double MARGIN = 0.9, RADIUS = 2 / Math.sqrt(3);
	private static final int COL_BG = 0xFF808080;
	private static final int COL_ENABLED = 0xFFFFFFFF;
	private static final int COL_DISABLED = 0xFF404040;
	private static final int COL_HOVER = 0xFFFFFF00;
	private static final int COL_ERROR = 0xFFFF0000;
	private static final int COL_FLOW = 0xFFFFFF7F;


	private static int getFlowColor(double val) {
		if (val <= 1) {
			val = Math.pow(val, 0.5);
			int col = (int) ((1 - val) * 255);
			return 0xFF0000FF | (col << 16) | (col << 8);
		} else {
			val = Math.pow(val, 1);
			int col = (int) (255 / val);
			return 0xFF000000 | ((255 - col) << 16) | col;
		}
	}

	private final MagicHexScreen screen;
	private final Map<MagicElement, Integer> ELEM_2_ID = new HashMap<>();

	HexHandler graph;
	FlowChart flow = null;
	boolean[] wrong_flow = new boolean[6];
	boolean[] ignore = new boolean[6];
	final WindowBox box = new WindowBox();

	protected HexCalcException error = null;
	protected HexDirection selected = null;

	private double magn = 14;
	private double scrollX, scrollY;
	private int tick;

	public HexGraphGui(MagicHexScreen screen) {
		this.screen = screen;
		graph = screen.product.getSolution();
		if (graph == null) {
			graph = new HexHandler(3);
		}
		MagicRegistry.ELEMENT.get().forEach((a) -> ELEM_2_ID.put(a, ELEM_2_ID.size()));
	}

	public void render(PoseStack matrix, double mx, double my, float partial) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		matrix.pushPose();
		matrix.translate(x0 + scrollX, y0 + scrollY, 0);
		LocateResult hover = graph.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
		renderBG(matrix, hover);

		double ratio, width, length;

		ratio = 1 / 4d;

		width = RADIUS * ratio * magn;
		length = HexHandler.WIDTH * (1 - ratio) * magn;

		renderPath(matrix, width, length, true);

		ratio *= 0.5;

		width = RADIUS * ratio * magn;
		length = HexHandler.WIDTH * (1 - ratio) * magn;
		renderPath(matrix, width, length, false);
		renderFlow(matrix, width, length, partial);
		renderError(matrix, width, length);
		renderIcons(matrix);

		matrix.popPose();
		RenderSystem.disableBlend();
	}

	public void renderHover(PoseStack matrix, double mx, double my) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		LocateResult hover = graph.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
		renderTooltip(matrix, mx, my, hover);
	}

	public void setRadius(int radius) {
		graph = new HexHandler(radius);
	}

	public int getRadius() {
		return graph.radius;
	}

	public void scroll(double dx, double dy) {
		scrollX += dx;
		scrollY += dy;
	}

	public boolean mouseClicked(double mx, double my, int button) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		if (button == 0) {
			LocateResult hover = graph.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
			if (click(hover)) {
				flow = null;
				error = null;
				screen.compile = HexStatus.Compile.EDITING;
				screen.updated();
				return true;
			}
		}
		return false;
	}

	private boolean click(@Nullable LocateResult hover) {
		if (hover == null)
			return false;
		if (hover.getType() == LocateResult.ResultType.ARROW) {
			ArrowResult res = (ArrowResult) hover;
			new HexCell(graph, res.row, res.cell).toggle(res.dir);
			return true;
		} else if (hover.getType() == LocateResult.ResultType.CELL) {
			CellResult res = (CellResult) hover;
			HexCell cell = new HexCell(graph, res.getRow(), res.getCell());
			if (flow == null) {
				for (HexDirection dire : HexDirection.values()) {
					if (cell.canWalk(dire) && cell.isConnected(dire))
						cell.toggle(dire);
				}
				return true;
			} else if (cell.isCorner()) {
				if (selected == cell.getCorner())
					selected = null;
				else
					selected = cell.getCorner();
				return false;
			}
		}
		return false;
	}

	public void tick() {
		tick++;
		tick %= PERIOD;
	}

	public boolean mouseScrolled(double mx, double my, double amount) {
		magn = Mth.clamp(magn + amount, 4, 20);
		return true;
	}

	public boolean charTyped(char ch) {
		if (ch == 'r') {
			compile();
			screen.updated();
			return true;
		} else if (ch == '=' && graph.radius < 7) {
			setRadius(graph.radius + 1);
			flow = null;
			error = null;
			screen.updated();
		} else if (ch == '-' && graph.radius > 2) {
			setRadius(graph.radius - 1);
			flow = null;
			error = null;
			screen.updated();
		}
		if (!Minecraft.getInstance().player.isCreative())
			return false;
		if (ch == 'f') {
			screen.forceSave(false);
			return true;
		} else
			return false;
	}

	void compile() {
		flow = null;
		error = null;
		try {
			flow = graph.getMatrix(true);
		} catch (HexCalcException e) {
			flow = null;
			error = e;
		} catch (Exception e) {
			flow = null;
			error = null;
			LogManager.getLogger().throwing(e);
		}
	}

	// --- render code ---

	private void renderBG(PoseStack matrix, LocateResult hover) {
		HexRenderUtil.hex_start(matrix);
		HexCell cell = new HexCell(graph, 0, 0);
		for (cell.row = 0; cell.row < graph.getRowCount(); cell.row++)
			for (cell.cell = 0; cell.cell < graph.getCellCount(cell.row); cell.cell++) {
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				double r = MARGIN * RADIUS * magn;
				HexRenderUtil.hex(x, y, r, COL_BG);
			}
		if (hover != null)
			HexRenderUtil.hex(hover.getX() * magn, hover.getY() * magn, RADIUS * magn / 2, COL_HOVER);
		HexRenderUtil.common_end();
	}

	private void renderPath(PoseStack matrix, double width, double length, boolean render_off) {
		HexCell cell = new HexCell(graph, 0, 0);
		HexRenderUtil.path_start(matrix, width, length, HexHandler.WIDTH * magn, 0);
		for (cell.row = 0; cell.row < graph.getRowCount(); cell.row++) {
			for (cell.cell = 0; cell.cell < graph.getCellCount(cell.row); cell.cell++) {
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int col;
				for (int i = 0; i < 3; i++) {
					HexDirection dire = HexDirection.values()[i];
					if (cell.canWalk(dire)) {
						col = cell.isConnected(dire) ? COL_ENABLED : COL_DISABLED;
						if (render_off || cell.isConnected(dire))
							HexRenderUtil.path(x, y, i, col);
					}
				}
			}
		}
		HexRenderUtil.common_end();
		HexRenderUtil.hex_start(matrix);
		for (cell.row = 0; cell.row < graph.getRowCount(); cell.row++) {
			for (cell.cell = 0; cell.cell < graph.getCellCount(cell.row); cell.cell++) {
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int col = cell.exists() ? COL_ENABLED : COL_DISABLED;
				if (render_off || cell.exists())
					HexRenderUtil.hex(x, y, width, col);
			}
		}
		HexRenderUtil.common_end();
	}

	private void renderFlow(PoseStack matrix, double width, double length, float partial) {
		if (flow == null) return;
		int[] masks = new int[6];
		int[] col_masks = new int[6];
		for (int i = 0; i < 6; i++) {
			MagicElement elem = screen.result.getElem(i);
			if (elem != null) {
				masks[i] = 1 << ELEM_2_ID.get(elem);
				col_masks[ELEM_2_ID.get(elem)] = elem.color;
			}
		}
		HexCell cell = new HexCell(graph, 0, 0);
		double[][] vals = new double[graph.getRowCount()][];
		int[][] node_masks = new int[graph.getRowCount()][];
		for (cell.row = 0; cell.row < graph.getRowCount(); cell.row++) {
			vals[cell.row] = new double[graph.getCellCount(cell.row)];
			node_masks[cell.row] = new int[graph.getCellCount(cell.row)];
		}
		HexRenderUtil.path_start(matrix, width, length, HexHandler.WIDTH * magn, tick + partial);
		renderFlowBase(masks, col_masks, vals, node_masks);
		HexRenderUtil.common_end();
		renderFlowSelected(matrix, width, length, partial, selected);
		renderFlowNode(matrix, vals, node_masks, col_masks, width);
	}

	private void renderFlowBase(int[] masks, int[] col_masks, double[][] vals, int[][] node_masks) {
		HexCell cell = new HexCell(graph, 0, 0);
		int[] cols = new int[6];
		for (FlowChart.Flow f : flow.flows) {
			cell.row = f.arrow.row;
			cell.cell = f.arrow.cell;
			if (selected == null) {
				int mask = 0;
				Frac[] forward = f.forward;
				for (int i = 0; i < forward.length; i++) {
					Frac fr = forward[i];
					if (fr != null) {
						mask |= masks[i];
					}
				}
				Frac[] backward = f.backward;
				for (int i = 0; i < backward.length; i++) {
					Frac fr = backward[i];
					if (fr != null) {
						mask |= masks[i];
					}
				}
				node_masks[cell.row][cell.cell] |= mask;
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int dire = f.arrow.dir.ind;
				int n = 0;
				for (int i = 0; i < 5; i++) {
					if ((mask & 1 << i) > 0) {
						cols[n] = col_masks[i];
						n++;
					}
				}
				HexRenderUtil.path(x, y, dire, cols, n);
				cell.walk(f.arrow.dir);
				node_masks[cell.row][cell.cell] |= mask;
			} else {
				double val = 0;
				if (!ignore[selected.ind] && f.forward[selected.ind] != null) {
					double fval = f.forward[selected.ind].getVal();
					updateNodeVal(vals, cell, f.arrow.dir, fval);
					val += fval;
				}
				if (!ignore[selected.ind] && f.backward[selected.ind] != null) {
					double bval = f.backward[selected.ind].getVal();
					updateNodeVal(vals, cell, f.arrow.dir, bval);
					val += bval;
				}
				int col = getFlowColor(val);
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int dire = f.arrow.dir.ind;
				HexRenderUtil.path(x, y, dire, col);
			}
		}
	}

	private void renderFlowSelected(PoseStack matrix, double width, double length, float partial, HexDirection sel) {
		if (sel == null)
			return;
		MagicElement elem = screen.result.getElem(sel.ind);
		if (elem == null || selected != null && selected != sel)
			return;
		int offset = ELEM_2_ID.get(elem);
		HexCell cell = new HexCell(graph, 0, 0);
		HexRenderUtil.flow_setup(matrix, elem.color, width, length, tick + partial, offset, HexHandler.WIDTH * magn, selected != null);
		for (FlowChart.Flow f : flow.flows) {
			int mask = 0;
			cell.row = f.arrow.row;
			cell.cell = f.arrow.cell;
			if (!ignore[sel.ind] && f.forward[sel.ind] != null) {
				double fval = f.forward[sel.ind].getVal();
				if (fval > 0) {
					mask |= 1;
				}
			}
			if (!ignore[sel.ind] && f.backward[sel.ind] != null) {
				double bval = f.backward[sel.ind].getVal();
				if (bval > 0) {
					mask |= 2;
				}
			}
			double x = cell.getX() * magn;
			double y = cell.getY() * magn;
			int dire = f.arrow.dir.ind;
			if (mask > 0) {
				HexRenderUtil.flow_path(x, y, dire, mask);
			}
		}
		HexRenderUtil.common_end();
	}

	private void renderFlowNode(PoseStack matrix, double[][] vals, int[][] masks, int[] col_masks, double width) {
		HexCell cell = new HexCell(graph, 0, 0);
		HexRenderUtil.hex_start(matrix);
		for (cell.row = 0; cell.row < graph.getRowCount(); cell.row++)
			for (cell.cell = 0; cell.cell < graph.getCellCount(cell.row); cell.cell++) {
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				double val = vals[cell.row][cell.cell];
				int col;
				if (!cell.exists()) {
					col = COL_DISABLED;
				} else {
					if (selected == null) {
						col = 0x00B0B0B0;
						for (int i = 0; i < 5; i++) {
							if (masks[cell.row][cell.cell] == 1 << i) {
								col = col_masks[i];
							}
						}
					} else {
						col = getFlowColor(val);
					}
				}
				HexRenderUtil.hex(x, y, width, col);
			}
		if (selected != null) {
			cell.toCorner(selected);
			double x = cell.getX() * magn;
			double y = cell.getY() * magn;
			HexRenderUtil.hex(x, y, width, COL_HOVER);
		}
		for (int i = 0; i < 6; i++) {
			if (wrong_flow[i]) {
				cell.toCorner(HexDirection.values()[i]);
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				HexRenderUtil.hex(x, y, width, COL_ERROR);
			}
		}
		HexRenderUtil.common_end();
	}

	private void renderError(PoseStack matrix, double width, double length) {
		if (error != null) {
			HexCell cell = new HexCell(graph, 0, 0);
			HexRenderUtil.path_start(matrix, width, length, HexHandler.WIDTH * magn, 0);
			for (HexCalcException.Side side : error.error) {
				cell.row = side.row;
				cell.cell = side.cell;
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int dire = side.dir.ind;
				HexRenderUtil.path(x, y, dire, COL_ERROR);
				cell.walk(side.dir);
			}
			HexRenderUtil.common_end();
			HexRenderUtil.hex_start(matrix);
			for (HexCalcException.Side side : error.error) {
				cell.row = side.row;
				cell.cell = side.cell;
				double x = cell.getX() * magn;
				double y = cell.getY() * magn;
				int col = COL_ERROR;
				HexRenderUtil.hex(x, y, width, col);
				cell.walk(side.dir);
				x = cell.getX() * magn;
				y = cell.getY() * magn;
				HexRenderUtil.hex(x, y, width, col);
			}
			HexRenderUtil.common_end();
		}
	}

	private void renderIcons(PoseStack matrix) {
		HexCell cell = new HexCell(graph, 0, 0);
		for (int i = 0; i < 6; i++) {
			MagicElement elem = screen.result.getElem(i);
			if (elem == null)
				continue;
			RenderSystem.setShaderTexture(0, elem.getIcon());
			cell.toCorner(HexDirection.values()[i]);
			double x = cell.getX() * magn;
			double y = cell.getY() * magn;
			drawIcon(matrix, x, y, magn / 10);
		}
	}

	private void renderTooltip(PoseStack matrix, double mx, double my, LocateResult hover) {
		if (hover == null || hover.getType() != LocateResult.ResultType.ARROW || selected == null || flow == null)
			return;
		flow.flows.stream().filter(e -> e.arrow.equals(hover)).findFirst().ifPresent(e -> {
			List<Component> list = new ArrayList<>();
			if (e.forward[selected.ind] != null)
				list.add(LangData.get(e.arrow.dir).append(e.forward[selected.ind].toString()));
			if (e.backward[selected.ind] != null)
				list.add(LangData.get(e.arrow.dir.next(3)).append(e.backward[selected.ind].toString()));
			if (list.size() > 0)
				AbstractHexGui.drawHover(matrix, list, mx, my, screen);
		});
	}

	private void updateNodeVal(double[][] vals, HexCell cell, HexDirection dir, double val) {
		vals[cell.row][cell.cell] += cell.isCorner() ? val : val / 2;
		cell.walk(dir);
		vals[cell.row][cell.cell] += cell.isCorner() ? val : val / 2;
		cell.walk(dir.next(3));
	}

}
