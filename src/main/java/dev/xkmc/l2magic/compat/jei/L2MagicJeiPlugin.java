package dev.xkmc.l2magic.compat.jei;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.compat.jei.ingredients.ElemIngredientHelper;
import dev.xkmc.l2magic.compat.jei.ingredients.ElemIngredientRenderer;
import dev.xkmc.l2magic.compat.jei.ingredients.ElementIngredient;
import dev.xkmc.l2magic.compat.jei.recipes.DisEnchanterRecipeCategory;
import dev.xkmc.l2magic.compat.jei.recipes.MagicCraftRecipeCategory;
import dev.xkmc.l2magic.compat.jei.screen.ExtraInfoScreen;
import dev.xkmc.l2magic.content.magic.gui.craft.ArcaneInjectScreen;
import dev.xkmc.l2magic.content.magic.gui.craft.DisEnchanterScreen;
import dev.xkmc.l2magic.content.magic.gui.craft.SpellCraftScreen;
import dev.xkmc.l2magic.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import dev.xkmc.l2magic.init.registrate.LMItems;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import dev.xkmc.l2magic.init.special.LightLandRegistry;
import dev.xkmc.l2magic.init.special.MagicRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class L2MagicJeiPlugin implements IModPlugin {

	public static L2MagicJeiPlugin INSTANCE;

	public final ResourceLocation UID = new ResourceLocation(L2Magic.MODID, "main");

	public final DisEnchanterRecipeCategory DISENCHANT = new DisEnchanterRecipeCategory();
	public final MagicCraftRecipeCategory MAGIC_CRAFT = new MagicCraftRecipeCategory();

	public final ElemIngredientHelper ELEM_HELPER = new ElemIngredientHelper();
	public final ElemIngredientRenderer ELEM_RENDERER = new ElemIngredientRenderer();
	public final IIngredientType<ElementIngredient> ELEM_TYPE = () -> ElementIngredient.class;

	public final ExtraInfoScreen EXTRA_INFO = new ExtraInfoScreen();

	public IGuiHelper GUI_HELPER;

	public L2MagicJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ELEM_TYPE, LightLandRegistry.ELEMENT.get().getValues().stream()
						.map(ElementIngredient::new).collect(Collectors.toList()),
				ELEM_HELPER, ELEM_RENDERER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(DISENCHANT.init(helper));
		registration.addRecipeCategories(MAGIC_CRAFT.init(helper));
		GUI_HELPER = helper;
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(DISENCHANT.getRecipeType(), List.copyOf(IMagicRecipe.getMap(MagicRegistry.MPT_ENCH.get()).values()));
		registration.addRecipes(MAGIC_CRAFT.getRecipeType(), Proxy.getWorld().getRecipeManager().getAllRecipesFor(LMRecipes.RT_RITUAL.get()));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(LMItems.DISENC_BOOK.get().getDefaultInstance(), DISENCHANT.getRecipeType());
		registration.addRecipeCatalyst(LMBlocks.B_RITUAL_CORE.get().asItem().getDefaultInstance(), MAGIC_CRAFT.getRecipeType());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		ExtraInfoScreen.init();
		registration.addGuiContainerHandler(DisEnchanterScreen.class, EXTRA_INFO);
		registration.addGuiContainerHandler(SpellCraftScreen.class, EXTRA_INFO);
		registration.addGuiContainerHandler(ArcaneInjectScreen.class, EXTRA_INFO);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
