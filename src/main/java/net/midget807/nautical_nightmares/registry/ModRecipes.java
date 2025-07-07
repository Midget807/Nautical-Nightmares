package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeType<ForgingRecipe> FORGING_TYPE = registerType("forging");
    public static final RecipeSerializer<ForgingRecipe> FORGING_SERIALIZER = registerSerializer("forging", new ForgingRecipe.Serializer(20));

    private static <T extends Recipe<?>>RecipeType<T> registerType(String name) {
        return Registry.register(Registries.RECIPE_TYPE, NauticalNightmaresMain.id(name), new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, NauticalNightmaresMain.id(name), serializer);
    }

    public static void registerModRecipes() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Recipes");
    }
}
