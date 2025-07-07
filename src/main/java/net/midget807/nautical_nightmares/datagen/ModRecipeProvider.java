package net.midget807.nautical_nightmares.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.midget807.nautical_nightmares.datagen.json_builder.ForgingRecipeJsonBuilder;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(Items.IRON_INGOT),
                        Ingredient.ofItems(Items.COPPER_INGOT),
                        Ingredient.fromTag(ItemTags.COALS),
                        false,
                        Ingredient.empty(),
                        RecipeCategory.MISC,
                        ModItems.FERRIC_COPPER_INGOT,
                        0.2f,
                        20
                )
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion("has_coals", conditionsFromTag(ItemTags.COALS))
                .offerTo(recipeExporter, getRecipeName(ModItems.FERRIC_COPPER_INGOT));

        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(Items.IRON_BLOCK),
                        Ingredient.fromTag(ItemTags.COALS),
                        Ingredient.ofItems(Items.COAL_BLOCK),
                        false,
                        Ingredient.empty(),
                        RecipeCategory.MISC,
                        ModItems.STEEL_INGOT,
                        0.5f,
                        20 /*todo change back to 400*/
                )
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.COAL_BLOCK), conditionsFromItem(Items.COAL_BLOCK))
                .criterion("has_coals", conditionsFromTag(ItemTags.COALS))
                .offerTo(recipeExporter, getRecipeName(ModItems.STEEL_INGOT));

        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(ModItems.STEEL_INGOT),
                        Ingredient.ofItems(Items.GOLD_INGOT),
                        Ingredient.ofItems(Items.LAVA_BUCKET),
                        false,
                        Ingredient.empty(),
                        RecipeCategory.MISC,
                        ModItems.AURIC_STEEL_INGOT,
                        0.8f,
                        20
                )
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(ModItems.STEEL_INGOT), conditionsFromItem(ModItems.STEEL_INGOT))
                .criterion(hasItem(Items.LAVA_BUCKET), conditionsFromItem(Items.LAVA_BUCKET))
                .offerTo(recipeExporter, getRecipeName(ModItems.AURIC_STEEL_INGOT));

        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(ModItems.AURIC_STEEL_INGOT),
                        Ingredient.ofItems(ModItems.FERRIC_COPPER_INGOT),
                        Ingredient.ofItems(Items.LAVA_BUCKET),
                        true,
                        Ingredient.ofItems(Items.QUARTZ),
                        RecipeCategory.MISC,
                        ModItems.AURIC_ALLOY_INGOT,
                        1.2f,
                        20
                )
                .criterion(hasItem(ModItems.AURIC_STEEL_INGOT), conditionsFromItem(ModItems.AURIC_STEEL_INGOT))
                .criterion(hasItem(ModItems.FERRIC_COPPER_INGOT), conditionsFromItem(ModItems.FERRIC_COPPER_INGOT))
                .criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
                .offerTo(recipeExporter, getRecipeName(ModItems.AURIC_ALLOY_INGOT));

        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(ModItems.AURIC_ALLOY_INGOT),
                        Ingredient.ofItems(Items.NETHERITE_INGOT),
                        Ingredient.ofItems(Items.BLAZE_ROD),
                        true,
                        Ingredient.ofItems(Items.QUARTZ),
                        RecipeCategory.MISC,
                        ModItems.NETHERITE_ALLOY_INGOT,
                        1.5f,
                        20
                )
                .criterion(hasItem(ModItems.AURIC_ALLOY_INGOT), conditionsFromItem(ModItems.AURIC_ALLOY_INGOT))
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.BLAZE_ROD), conditionsFromItem(Items.BLAZE_ROD))
                .offerTo(recipeExporter, getRecipeName(ModItems.NETHERITE_ALLOY_INGOT));

        ForgingRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(ModItems.NETHERITE_ALLOY_INGOT),
                        Ingredient.ofItems(ModItems.AURALITE_SCRAP),
                        Ingredient.ofItems(Items.BLAZE_ROD),
                        true,
                        Ingredient.ofItems(ModItems.BARITE_SHARD),
                        RecipeCategory.MISC,
                        ModItems.AURALITE_INGOT,
                        2.0f,
                        20
                )
                .criterion(hasItem(ModItems.NETHERITE_ALLOY_INGOT), conditionsFromItem(ModItems.NETHERITE_ALLOY_INGOT))
                .criterion(hasItem(ModItems.AURALITE_SCRAP), conditionsFromItem(ModItems.AURALITE_SCRAP))
                .criterion(hasItem(Items.BLAZE_ROD), conditionsFromItem(Items.BLAZE_ROD))
                .offerTo(recipeExporter, getRecipeName(ModItems.AURALITE_INGOT));

    }
}
