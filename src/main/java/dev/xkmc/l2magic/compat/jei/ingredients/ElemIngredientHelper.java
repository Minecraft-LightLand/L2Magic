package dev.xkmc.l2magic.compat.jei.ingredients;

import dev.xkmc.l2magic.compat.jei.LightLandJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ElemIngredientHelper implements IIngredientHelper<ElementIngredient> {

	@Override
	public IIngredientType<ElementIngredient> getIngredientType() {
		return LightLandJeiPlugin.INSTANCE.ELEM_TYPE;
	}

	@Override
	public String getDisplayName(ElementIngredient magicElement) {
		return magicElement.elem.getDesc().getContents().toString();
	}

	@Override
	public String getUniqueId(ElementIngredient magicElement, UidContext context) {
		return magicElement.elem.getID();
	}

	@Override
	public ResourceLocation getResourceLocation(ElementIngredient magicElement) {
		return magicElement.elem.getRegistryName();
	}

	@Override
	public ElementIngredient copyIngredient(ElementIngredient magicElement) {
		return new ElementIngredient(magicElement);
	}

	@Override
	public String getErrorInfo(@Nullable ElementIngredient magicElement) {
		return "magic element error";
	}
}
