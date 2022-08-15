package dev.xkmc.l2magic.content.magic.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2magic.network.config.SpellEntityConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SerialClass
public class SpellComponent {

	public static SpellComponent getFromConfig(String s) {
		return SpellEntityConfig.getConfig(new ResourceLocation(s));
	}

	@SerialClass.SerialField
	public ArrayList<Stroke> strokes = new ArrayList<>();

	@SerialClass.SerialField
	public ArrayList<Layer> layers = new ArrayList<>();

	@OnlyIn(Dist.CLIENT)
	public void render(RenderHandle handle) {
		handle.matrix.pushPose();
		for (Stroke stroke : strokes) {
			stroke.render(handle);
		}
		for (Layer layer : layers) {
			layer.render(handle);
		}
		handle.matrix.popPose();
	}

	@SerialClass
	public static class Value {

		@SerialClass.SerialField
		public float value, delta, amplitude, period = 300, dt;

		@OnlyIn(Dist.CLIENT)
		public float get(float tick) {
			return value + amplitude * (float) Math.sin((tick - dt) * 2 * Math.PI / period) + delta * tick;
		}

		public static Value constant(float val) {
			Value ans = new Value();
			ans.value = val;
			return ans;
		}

		public static Value linear(float val, float delta) {
			Value ans = new Value();
			ans.value = val;
			ans.delta = delta;
			return ans;
		}

		public static Value sin(float val, float amplitude, float period) {
			Value ans = new Value();
			ans.value = val;
			ans.amplitude = amplitude;
			ans.period = period;
			return ans;
		}

		public static Value sin(float val, float amplitude, float period, float dt) {
			Value ans = new Value();
			ans.value = val;
			ans.amplitude = amplitude;
			ans.period = period;
			ans.dt = dt;
			return ans;
		}

	}

	public record Stroke(int vertex, int cycle, String color, float width, float radius, float z, float angle) {

		@OnlyIn(Dist.CLIENT)
		public void render(RenderHandle handle) {
			float da = (float) Math.PI * 2 * cycle / vertex;
			float a = angle;
			float w = width / (float) Math.cos(da / 2);
			int col = getColor();
			for (int i = 0; i < vertex; i++) {
				rect(handle, a, da, radius, w, z, col);
				a += da;
			}

		}

		@OnlyIn(Dist.CLIENT)
		private int getColor() {
			String str = color;
			if (str.startsWith("0x")) {
				str = str.substring(2);
			}
			return Integer.parseUnsignedInt(str, 16);
		}

		@OnlyIn(Dist.CLIENT)
		private static void rect(RenderHandle handle, float a, float da, float r, float w, float z, int col) {
			vertex(handle, a, r - w / 2, z, col);
			vertex(handle, a, r + w / 2, z, col);
			vertex(handle, a + da, r + w / 2, z, col);
			vertex(handle, a + da, r - w / 2, z, col);
		}

		@OnlyIn(Dist.CLIENT)
		private static void vertex(RenderHandle handle, float a, float r, float z, int col) {
			int alp = (int) ((col >> 24 & 0xff) * handle.alpha);
			handle.builder.vertex(handle.matrix.last().pose(),
					r * (float) Math.cos(a),
					r * (float) Math.sin(a),
					z).color(
					col >> 16 & 0xff,
					col >> 8 & 0xff,
					col & 0xff,
					alp).endVertex();
		}

	}

	@SerialClass
	public static class Layer {

		@SerialClass.SerialField
		public ArrayList<String> children = new ArrayList<>();

		private List<SpellComponent> _children;

		@SerialClass.SerialField
		public Value z_offset, scale, radius, rotation, alpha;

		@OnlyIn(Dist.CLIENT)
		public void render(RenderHandle handle) {
			if (_children == null) {
				_children = children.stream().map(SpellComponent::getFromConfig).collect(Collectors.toList());
				return;
			}
			int n = _children.size();
			float z = get(z_offset, handle, 0);
			float s = get(scale, handle, 1);
			float a = get(rotation, handle, 0);
			double r = get(radius, handle, 0);
			float al = handle.alpha;
			if (alpha != null) {
				handle.alpha *= alpha.get(handle.tick);
			}
			handle.matrix.pushPose();
			handle.matrix.translate(0, 0, z);
			handle.matrix.scale(s, s, s);
			for (SpellComponent child : _children) {
				handle.matrix.pushPose();
				handle.matrix.mulPose(Vector3f.ZP.rotationDegrees(a));
				handle.matrix.translate(r, 0, 0);
				child.render(handle);
				handle.matrix.popPose();
				a += 360f / n;
			}
			handle.matrix.popPose();
			handle.alpha = al;
		}

		@OnlyIn(Dist.CLIENT)
		private float get(Value val, RenderHandle handle, float def) {
			return val == null ? def : val.get(handle.tick);
		}

		@Deprecated
		public Layer() {

		}

		@DataGenOnly
		public Layer(Value z_offset, Value scale, Value radius, Value rotation, Value alpha) {
			this.z_offset = z_offset;
			this.scale = scale;
			this.radius = radius;
			this.rotation = rotation;
			this.alpha = alpha;
		}

		@DataGenOnly
		public Layer add(int repeat, ResourceLocation... sub) {
			for (int i = 0; i < repeat; i++) {
				for (ResourceLocation s : sub) {
					children.add(s.toString());
				}
			}
			return this;
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static class RenderHandle {

		public final PoseStack matrix;
		public final VertexConsumer builder;
		public final float tick;
		public final int light;

		public float alpha = 1;

		public RenderHandle(PoseStack matrix, VertexConsumer builder, float tick, int light) {
			this.matrix = matrix;
			this.builder = builder;
			this.tick = tick;
			this.light = light;
		}
	}

	@DataGenOnly
	public SpellComponent addStroke(Stroke stroke) {
		strokes.add(stroke);
		return this;
	}

	@DataGenOnly
	public SpellComponent addLayer(Layer layer) {
		layers.add(layer);
		return this;
	}

}
