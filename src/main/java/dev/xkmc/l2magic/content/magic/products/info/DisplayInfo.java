package dev.xkmc.l2magic.content.magic.products.info;

import net.minecraft.advancements.FrameType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record DisplayInfo(int x, int y, FrameType type, Item icon) {

	public static final float SCALE = 1;

	public float getX() {
		return x * SCALE;
	}

	public float getY() {
		return y * SCALE;
	}

	public FrameType getFrame() {
		return type;
	}

	public ItemStack getIcon() {
		return icon.getDefaultInstance();
	}
}
