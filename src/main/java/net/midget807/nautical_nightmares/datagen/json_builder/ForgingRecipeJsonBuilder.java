package net.midget807.nautical_nightmares.datagen.json_builder;

import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.midget807.nautical_nightmares.registry.ModDataComponentTypes;
import net.midget807.nautical_nightmares.registry.ModRecipes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ForgingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final CookingRecipeCategory cookingCategory;
    private final Item output;
    private final Ingredient input1;
    private final Ingredient input2;
    private final Ingredient fuel;
    private final Ingredient catalyst;
    private final float experience;
    private final int cookTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public ForgingRecipeJsonBuilder(RecipeCategory category, CookingRecipeCategory cookingCategory, ItemConvertible output, Ingredient input1, Ingredient input2, Ingredient fuel, Ingredient catalyst, float experience, int cookTime) {
        this.category = category;
        this.cookingCategory = cookingCategory;
        this.output = output.asItem();
        this.input1 = input1;
        this.input2 = input2;
        this.fuel = fuel;
        this.catalyst = catalyst;
        this.experience = experience;
        this.cookTime = cookTime;
    }

    public static ForgingRecipeJsonBuilder create(Ingredient input1, Ingredient input2, Ingredient fuel, Ingredient catalyst, RecipeCategory category, ItemConvertible output, float experience, int cookTime) {
        return new ForgingRecipeJsonBuilder(category, CookingRecipeCategory.MISC, output, input1, input2, fuel, catalyst, experience, cookTime);
    }

    public ForgingRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
        this.criteria.put(string, advancementCriterion);
        return this;
    }

    public ForgingRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        this.validate(recipeId);
        Advancement.Builder builder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(builder::criterion);
        ForgingRecipe forgingRecipe = new ForgingRecipe((String) Objects.requireNonNullElse(this.group, ""), this.input1, this.input2, this.fuel, catalyst, new ItemStack(this.output), this.experience, this.cookTime);
        exporter.accept(recipeId, forgingRecipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));

    }

    private void validate(Identifier recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}
