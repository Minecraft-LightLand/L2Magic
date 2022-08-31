package dev.xkmc.l2magic.content.transport.tile.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BeamRenderer {

	public static int HSBtoRGB(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
				case 0:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
	}

	private float r, b, g;
	private float u0, u1, v0, v1, y0, y1;

	private Matrix3f m3;
	private Matrix4f m4;
	private VertexConsumer vc;

	public BeamRenderer() {

	}

	public void drawCube(PoseStack mat, VertexConsumer vc, float y0, float y1, float h) {
		PoseStack.Pose entry = mat.last();
		m4 = entry.pose();
		m3 = entry.normal();
		this.vc = vc;
		this.y0 = y0;
		this.y1 = y1;
		drawRect(0, h, h, 0);
		drawRect(h, 0, 0, -h);
		drawRect(0, -h, -h, 0);
		drawRect(-h, 0, 0, h);
	}

	public BeamRenderer setColorHSB(float hue, float sat, float bright) {
		int col = HSBtoRGB(hue, sat, bright);
		return setColorRBG((col >> 16 & 0xFF) / 256f, (col >> 8 & 0xFF) / 256f, (col & 0xFF) / 256f);
	}

	public BeamRenderer setColorRBG(float r, float b, float g) {
		this.r = r;
		this.b = b;
		this.g = g;
		return this;
	}

	public BeamRenderer setUV(float u0, float u1, float v0, float v1) {
		this.u0 = u0;
		this.u1 = u1;
		this.v0 = v0;
		this.v1 = v1;
		return this;
	}

	private void drawRect(float x0, float z0, float x1, float z1) {
		drawVertex(y1, x0, z0, u1, v0, 1, 0);
		drawVertex(y0, x0, z0, u1, v1, 1, 1);
		drawVertex(y0, x1, z1, u0, v1, 1, 1);
		drawVertex(y1, x1, z1, u0, v0, 1, 0);
	}

	private void drawVertex(float y, float x, float z, float u, float v, int normal, float a) {
		vc.vertex(m4, z, x, y);
		vc.color(r, g, b, a);
		vc.uv(u, v);
		vc.overlayCoords(OverlayTexture.NO_OVERLAY);
		vc.uv2(15728880);
		vc.normal(m3, 0, normal, 0);
		vc.endVertex();
	}

}
