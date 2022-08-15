package dev.xkmc.l2magic.content.magic.products.recipe;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.info.DisplayInfo;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.util.Map;
import java.util.TreeMap;

@SerialClass
public class DefMagicRecipe extends IMagicRecipe {

	@SerialClass.SerialField
	public TreeMap<String, MagicElement> elements = new TreeMap<>();
	@SerialClass.SerialField
	public String[] flows;

	@Deprecated
	public DefMagicRecipe() {
		super();
	}

	@DataGenOnly
	public DefMagicRecipe(MagicProductType<?, ?> type, ResourceLocation id, DisplayInfo screen) {
		super(type, id, screen);
	}

	@DataGenOnly
	public DefMagicRecipe addElements(MagicElement... elems) {
		char a = 'A';
		for (MagicElement elem : elems) {
			this.elements.put("" + a, elem);
			a++;
		}
		return this;
	}

	@DataGenOnly
	public DefMagicRecipe setFlow(String... flows) {
		this.flows = flows;
		return this;
	}

	private static boolean flowRegex(char[] chars, String s0, String s1, boolean[][] bools, boolean bidirect) {
		int[] i0 = new int[s0.length()];
		int[] i1 = new int[s1.length()];
		for (int i = 0; i < s0.length(); i++) {
			i0[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == s0.charAt(i)) {
					i0[i] = c;
					break;
				}
			}
			if (i0[i] == -1)
				return false;
		}
		for (int i = 0; i < s1.length(); i++) {
			i1[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == s1.charAt(i)) {
					i1[i] = c;
					break;
				}
			}
			if (i1[i] == -1)
				return false;
		}
		for (int k : i0)
			for (int i : i1) {
				if (k == i)
					return false;
				if (bools[k][i])
					return false;
				bools[k][i] = true;
				if (bidirect) {
					if (bools[i][k])
						return false;
					bools[i][k] = true;
				}
			}
		return true;
	}

	private static boolean flowRound(char[] chars, String str, boolean[][] bools) {
		int[] i0 = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			i0[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == str.charAt(i)) {
					i0[i] = c;
					break;
				}
			}
			if (i0[i] == -1)
				return false;
		}
		for (int i : i0)
			for (int j : i0) {
				if (i == j)
					continue;
				bools[i][j] = true;
			}
		return true;
	}

	@SerialClass.OnInject
	public void onInject() {
		int n = elements.size();
		MagicElement[] elems = new MagicElement[n];
		char[] chars = new char[n];
		int i = 0;
		for (Map.Entry<String, MagicElement> ent : elements.entrySet()) {
			elems[i] = ent.getValue();
			if (ent.getKey().length() != 1)
				LogManager.getLogger().error("key length not 1 in " + getID());
			chars[i] = ent.getKey().charAt(0);
			i++;
		}
		boolean[][] bools = new boolean[6][6];
		for (String flow : flows) {
			if (flow.contains("<->")) {
				String[] strs = flow.split("<->");
				if (strs.length != 2 || !flowRegex(chars, strs[0], strs[1], bools, true))
					LogManager.getLogger().error("illegal side expression" + flow + " in " + getID());
			} else if (flow.contains("->")) {
				String[] strs = flow.split("->");
				if (strs.length != 2 || !flowRegex(chars, strs[0], strs[1], bools, false))
					LogManager.getLogger().error("illegal side expression " + flow + " in " + getID());
			} else if (flow.endsWith("|")) {
				if (!flowRound(chars, flow.substring(0, flow.length() - 1), bools))
					LogManager.getLogger().error("illegal round expression " + flow + " in " + getID());
			} else LogManager.getLogger().error("illegal connector " + flow + " in " + getID());
		}
		register(elems, bools);
	}

}
