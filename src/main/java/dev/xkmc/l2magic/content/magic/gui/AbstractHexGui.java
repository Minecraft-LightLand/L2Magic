package dev.xkmc.l2magic.content.magic.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AbstractHexGui extends GuiComponent {

	public static final int RED = ChatFormatting.RED.getColor();

	public static final AbstractHexGui INSTANCE = new AbstractHexGui();

	private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");

	public static void drawIcon(PoseStack matrix, double x, double y, double scale) {
		PoseStack mat = RenderSystem.getModelViewStack();
		matrix.pushPose();
		matrix.translate(x, y, 0);
		matrix.scale((float) (1f / 16 * scale), (float) (1f / 16 * scale), 0);
		INSTANCE.blit(matrix, -128, -128, 0, 0, 256, 256);
		matrix.popPose();
	}

	public static void drawScaled(PoseStack matrix, double x, double y, int scale) {
		PoseStack mat = RenderSystem.getModelViewStack();
		matrix.pushPose();
		matrix.translate(x, y, 0);
		matrix.scale(1f / scale, 1f / scale, 0);
		blit(matrix, -8 * scale, -8 * scale, 0, 0, 16 * scale, 16 * scale, 16 * scale, 16 * scale);
		matrix.popPose();
	}

	public static void drawElement(PoseStack matrix, double x, double y, MagicElement elem, String s) {
		drawElement(matrix, x, y, elem, s, 0xFFFFFF);
	}

	public static void drawElement(PoseStack matrix, double x, double y, MagicElement elem, String s, int col) {
		RenderSystem.setShaderTexture(0, elem.getIcon());
		drawIcon(matrix, x, y, 1);
		Font font = Minecraft.getInstance().font;
		font.draw(matrix, s, (float) (x + 11 - 1 - font.width(s)), (float) (y + 2), 0x404040);
		font.draw(matrix, s, (float) (x + 11 - 2 - font.width(s)), (float) (y + 1), col);
	}

	public static void drawFrame(PoseStack matrix, FrameType type, boolean unlocked, int x, int y) {
		RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
		INSTANCE.blit(matrix, x - 8 - 5, y - 8 - 5, type.getTexture(), 128 + (unlocked ? 0 : 1) * 26, 26, 26);
	}

	public static void drawHover(PoseStack matrix, List<Component> list, double mx, double my, Screen screen) {
		screen.renderComponentTooltip(matrix, list, (int) mx, (int) my);
	}


}
