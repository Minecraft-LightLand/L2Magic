package dev.xkmc.l2magic.content.common.capability.player;

import dev.xkmc.l2library.util.Proxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CapProxy {

	@OnlyIn(Dist.CLIENT)
	public static LLPlayerData getHandler() {
		return LLPlayerData.get(Proxy.getClientPlayer());
	}

}
