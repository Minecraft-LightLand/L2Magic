package dev.xkmc.l2magic.content.magic.gui.hex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

import java.util.function.IntConsumer;

public class HexRenderUtil {

	enum Op {
		FLOW, PATH, HEX
	}

	public static boolean vert_strip = true, time_shift = true;
	public static final float SHIFT_WIDTH = 0.2f, SHIFT_PERIOD = 60f;

	private static BufferBuilder builder;
	private static Matrix4f last;
	private static Op op = null;
	static float ca = 1, cr, cg, cb, cx, cy, lx, ly, wx, wy;

	static float peak, plen, time;
	static double width, length, r, x, y;
	static int offset;
	static boolean focus = false;

	static void common_end() {
		BufferUploader.drawWithShader(builder.end());
		ca = 1;
		op = null;
	}

	// --- flow ---

	static void flow_setup(PoseStack mat, int color, double w, double l, float t, int off, double radius, boolean foc) {
		if (op != null) throw new RuntimeException("op is " + op + ", expected null");
		op = Op.FLOW;
		last = mat.last().pose();
		builder = Tesselator.getInstance().getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		width = w;
		length = l;
		time = t;
		offset = off;
		r = radius;
		focus = foc;
		cr = (float) (color >> 16 & 255) / 255.0F;
		cg = (float) (color >> 8 & 255) / 255.0F;
		cb = (float) (color & 255) / 255.0F;
		peak = (float) (width / Math.sqrt(3) / length);
		plen = 0.1f;
	}

	static void flow_path(double x, double y, int dire, int mask) {
		if (op != Op.FLOW) throw new RuntimeException("op is " + op + ", expected FLOW");
		double a = dire * Math.PI / 3;
		cx = (float) (x + r * Math.cos(a) / 2);
		cy = (float) (y + r * Math.sin(a) / 2);
		lx = (float) (length / 2 * Math.cos(a));
		ly = (float) (length / 2 * Math.sin(a));
		wx = (float) (width / 2 * Math.cos(a - Math.PI / 2));
		wy = (float) (width / 2 * Math.sin(a - Math.PI / 2));
		int n = focus ? 4 : 2;
		for (int i = 0; i < n; i++) {
			flow_strip((time / 10 / n + offset * 0.1f + 1f / n * i) % 1, plen, peak, mask);
		}
	}

	private static void flow_strip(float p0, float plen, float peak, int mask) {
		if (p0 + plen < 1) {
			if ((mask & 1) > 0)
				flow_rect(p0, p0 + plen, peak);
			if ((mask & 2) > 0)
				flow_rect(1 - (p0 + plen), 1 - p0, -peak);
		} else {
			if ((mask & 1) > 0) {
				flow_rect(p0, 1, peak);
				flow_rect(0, p0 + plen - 1, peak);
			}
			if ((mask & 2) > 0) {
				flow_rect(0, 1 - p0, -peak);
				flow_rect(2 - (p0 + plen), 1, -peak);
			}
		}
	}

	private static void flow_rect(float p0, float p1, float peak) {
		if (focus || peak > 0) {
			flow_point(1, p1, 0);
			flow_point(1, p0, 0);
			flow_point(0, p0, peak);
			flow_point(0, p1, peak);
		}
		if (focus || peak < 0) {
			flow_point(0, p1, peak);
			flow_point(0, p0, peak);
			flow_point(-1, p0, 0);
			flow_point(-1, p1, 0);
		}
	}

	private static void flow_point(int sign, float p, float peak) {
		builder.vertex(last,
				cx + (p * 2 - 1 + peak) * lx + sign * wx,
				cy + (p * 2 - 1 + peak) * ly + sign * wy,
				0).color(cr, cg, cb, 1).endVertex();
	}

	//--- path ---

	static void path_start(PoseStack matrix, double w, double l, double radius, float t) {
		if (op != null) throw new RuntimeException("op is " + op + ", expected null");
		op = Op.PATH;
		last = matrix.last().pose();
		builder = Tesselator.getInstance().getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		width = w;
		length = l;
		r = radius;
		time = t;
	}

	static void path(double x, double y, int dire, int[] colors, int n) {
		if (op != Op.PATH) throw new RuntimeException("op is " + op + ", expected PATH");
		double a = dire * Math.PI / 3;
		cx = (float) (x + r * Math.cos(a) / 2);
		cy = (float) (y + r * Math.sin(a) / 2);
		lx = (float) (length / 2 * Math.cos(a));
		ly = (float) (length / 2 * Math.sin(a));
		wx = (float) (width / 2 * Math.cos(a - Math.PI / 2));
		wy = (float) (width / 2 * Math.sin(a - Math.PI / 2));
		for (int i = 0; i < n; i++) {
			int col = colors[i];
			path_color(col, 1f / n * i, 1f / n * (i + 1));
		}
	}

	static void path(double x, double y, int dire, int color) {
		if (op != Op.PATH) throw new RuntimeException("op is " + op + ", expected PATH");
		double a = dire * Math.PI / 3;
		cx = (float) (x + r * Math.cos(a) / 2);
		cy = (float) (y + r * Math.sin(a) / 2);
		lx = (float) (length / 2 * Math.cos(a));
		ly = (float) (length / 2 * Math.sin(a));
		wx = (float) (width / 2 * Math.cos(a - Math.PI / 2));
		wy = (float) (width / 2 * Math.sin(a - Math.PI / 2));
		path_color(color, 0f, 1f);

	}

	private static void path_color(int color, float left, float right) {
		if (time_shift && !(left == 0f && right == 1f)) {
			float t0 = time / SHIFT_PERIOD % 1;
			if (t0 + SHIFT_WIDTH > 1) {
				if (left < t0 + SHIFT_WIDTH - 1) {
					t0 -= 1;
				} else if (right < t0) return;
			}
			left = Mth.clamp((left - t0) / SHIFT_WIDTH, 0f, 1f);
			right = Mth.clamp((right - t0) / SHIFT_WIDTH, 0f, 1f);
			if (left == right)
				return;
		}

		cr = (float) (color >> 16 & 255) / 255.0F;
		cg = (float) (color >> 8 & 255) / 255.0F;
		cb = (float) (color & 255) / 255.0F;

		float ll = 0, lr = 1, wl = 0, wr = 1;
		if (vert_strip) {
			wl = left;
			wr = right;
		} else {
			ll = left;
			lr = right;
		}
		float alx = lx * (lr * 2 - 1);
		float aly = ly * (lr * 2 - 1);
		float blx = lx * (ll * 2 - 1);
		float bly = ly * (ll * 2 - 1);
		float awx = wx * (wr * 2 - 1);
		float awy = wy * (wr * 2 - 1);
		float bwx = wx * (wl * 2 - 1);
		float bwy = wy * (wl * 2 - 1);
		path_point(cx + alx + awx, cy + aly + awy);
		path_point(cx + blx + awx, cy + bly + awy);
		path_point(cx + blx + bwx, cy + bly + bwy);
		path_point(cx + alx + bwx, cy + aly + bwy);
	}

	private static void path_point(float x, float y) {
		builder.vertex(last, x, y, 0).color(cr, cg, cb, 1).endVertex();
	}

	// --- hex ---

	static void hex_start(PoseStack matrix) {
		if (op != null) throw new RuntimeException("op is " + op + ", expected null");
		op = Op.HEX;
		last = matrix.last().pose();
		builder = Tesselator.getInstance().getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	}

	static void hex(double xpos, double ypos, double radius, int color) {
		if (op != Op.HEX) throw new RuntimeException("op is " + op + ", expected HEX");
		cr = (float) (color >> 16 & 255) / 255.0F;
		cg = (float) (color >> 8 & 255) / 255.0F;
		cb = (float) (color & 255) / 255.0F;
		x = xpos;
		y = ypos;
		r = radius;
		hex_point(0);
		hex_point(3);
		hex_point(2);
		hex_point(1);
		hex_point(0);
		hex_point(5);
		hex_point(4);
		hex_point(3);
	}

	private static void hex_point(int i) {
		double a = (i + 0.5) * Math.PI / 3;
		float px = (float) (x + r * Math.cos(a));
		float py = (float) (y + r * Math.sin(a));
		builder.vertex(last, px, py, 0).color(cr, cg, cb, ca).endVertex();
	}

	static void renderHex(PoseStack matrix, double x, double y, double r, int color) {
		Matrix4f last = matrix.last().pose();
		BufferBuilder builder = Tesselator.getInstance().getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		float ca = (float) (color >> 24 & 255) / 255.0F;
		float cr = (float) (color >> 16 & 255) / 255.0F;
		float cg = (float) (color >> 8 & 255) / 255.0F;
		float cb = (float) (color & 255) / 255.0F;
		IntConsumer c = i -> {
			double a = (i + 0.5) * Math.PI / 3;
			float px = (float) (x + r * Math.cos(a));
			float py = (float) (y + r * Math.sin(a));
			builder.vertex(last, px, py, 0).color(cr, cg, cb, ca).endVertex();
		};
		c.accept(0);
		c.accept(3);
		c.accept(2);
		c.accept(1);
		c.accept(0);
		c.accept(5);
		c.accept(4);
		c.accept(3);

		BufferUploader.drawWithShader(builder.end());
	}

}
