package dev.xkmc.l2magic.content.magic.gui.hex;

import dev.xkmc.l2magic.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HexStatus {

	public enum Save {
		YES(ChatFormatting.GREEN), NO(ChatFormatting.RED);

		public final ChatFormatting col;

		Save(ChatFormatting col) {
			this.col = col;
		}

		public Component getDesc() {
			return LangData.get(this);
		}

		public int getColor() {
			return col.getColor();
		}

	}

	public enum Compile {
		COMPLETE(ChatFormatting.GREEN),
		FAILED(ChatFormatting.DARK_PURPLE),
		EDITING(ChatFormatting.BLUE),
		ERROR(ChatFormatting.RED);

		public final ChatFormatting col;

		Compile(ChatFormatting col) {
			this.col = col;
		}

		public Component getDesc() {
			return LangData.get(this);
		}

		public int getColor() {
			return col.getColor();
		}

	}

}
