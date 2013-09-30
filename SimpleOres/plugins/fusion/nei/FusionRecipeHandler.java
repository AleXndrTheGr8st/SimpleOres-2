package SimpleOres.plugins.fusion.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;
import static codechicken.nei.recipe.FurnaceRecipeHandler.afuels;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import SimpleOres.plugins.fusion.FusionRecipes;
import SimpleOres.plugins.fusion.FusionRecipes.Entry;
import SimpleOres.plugins.fusion.FusionRecipes.EntryInfo;
import SimpleOres.plugins.fusion.GuiFusionFurnace;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.google.common.collect.ImmutableList;

public class FusionRecipeHandler extends TemplateRecipeHandler {

	private static final int X1 = 0;
	private static final int Y1 = 15;
	
	private static final int X2 = 5;
	private static final int Y2 = 5;
	
	@Override public void drawBackground(int i) {
		GL11.glColor4f(1, 1, 1, 1);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(X1, Y1, X2, Y2, 166, 78);
	}
	
	protected static int x(int x) {
		return x + X1 - X2;
	}
	protected static int y(int y) {
		return y + Y1 - Y2;
	}
	
	@Override public void drawExtras(int recipe) {
		drawProgressBar(x(105), y(55), 176, 0, 190 - 176, 14, 48, 7); // Left Flames
		drawProgressBar(x(55), y(55), 176, 0, 190 - 176, 14, 48, 7); // Right Flames
		drawProgressBar(x(51), y(34), 176, 14, 200 - 176, 31 - 14, 48, 0); // Left Arrow
		drawProgressBar(x(100), y(34), 176, 31, 200 - 176, 31 - 14, 48, 2); // Right Arrow
		drawProgressBar(x(64), y(4), 176, 64, 11, 92 - 64, 48, 3); // Left Bubbles
		drawProgressBar(x(98), y(4), 188, 64, 11, 92 - 64, 48, 3); // Right Bubbles
	}
	
	
	public class CachedFusionRecipe extends CachedRecipe {
		private final ImmutableList<PositionedStack> ingredients;
		private final PositionedStack result;
		
		public CachedFusionRecipe(EntryInfo info) {
			ingredients = ImmutableList.of(
					new PositionedStack(info.input1.getItems(), x(33), y(35)),
					new PositionedStack(info.input2.getItems(), x(126), y(34)),
					new PositionedStack(info.catalyst.getItems(), x(79), y(7)));
			result = new PositionedStack(info.output.getItems(), x(79), y(34));
		}
		
		@Override public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, ingredients);
		}
		@Override public PositionedStack getResult() {
			randomRenderPermutation(result, cycleticks / 20);
			return result;
		}
		
		@Override public PositionedStack getOtherStack()  {
			return convertFuelStack(afuels.get((cycleticks / 48) % afuels.size()).stack);
		}
		
		public CachedFusionRecipe computeVisuals() {
			for(PositionedStack p : ingredients)
				p.generatePermutations();
			result.generatePermutations();
			return this;
		}
	}
	
	protected static PositionedStack convertFuelStack(PositionedStack stack) {
		stack = stack.copy();
		stack.relx = x(79);
		stack.rely = y(62);
		return stack;
	}
	
	
	
	
	@Override public Class<? extends GuiContainer> getGuiClass() {
		return GuiFusionFurnace.class;
	}
	@Override public String getRecipeName() {
		return "Fusion";
	}
	@Override public String getGuiTexture() {
		return "simpleoresfusion:textures/gui/fusionFurnace.png";
	}
	@Override public int recipiesPerPage() {
		return 1;
	}
	
	
	
	public static final String ID = "simpleores.fusion";
	
	@Override public String getOverlayIdentifier() {
		return ID;
	}
	
	@Override public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(ID))
			for (Entry e : FusionRecipes.getRecipeList())
				arecipes.add(new CachedFusionRecipe(e.getInfo()).computeVisuals());
		else
			super.loadCraftingRecipes(outputId, results);
	}
	@Override public void loadCraftingRecipes(ItemStack result) {
		result.stackSize = Integer.MAX_VALUE;
		for (Entry e : FusionRecipes.getRecipeList())
			if (e.getInfo().output.matches(result))
				arecipes.add(new CachedFusionRecipe(e.getInfo()).computeVisuals());
	}
	@Override public void loadUsageRecipes(ItemStack ingredient) {
		ingredient.stackSize = Integer.MAX_VALUE;
		for(Entry e : FusionRecipes.getRecipeList())
			if (e.isItemIput(ingredient) || e.isItemCatalyst(ingredient))
				arecipes.add(new CachedFusionRecipe(e.getInfo()).computeVisuals());
	}
	
}