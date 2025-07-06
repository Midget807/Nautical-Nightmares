package net.midget807.nautical_nightmares.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record ForgingRecipeInput(ItemStack item1, ItemStack item2, ItemStack fuel, ItemStack catalyst) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> this.item1;
            case 1 -> this.item2;
            case 2 -> this.fuel;
            case 3 -> this.catalyst;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        };
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return this.item1.isEmpty() && this.item2.isEmpty() && this.fuel.isEmpty() && this.catalyst.isEmpty();
    }
    public boolean needsCatalyst() {
        return this.catalyst != null;
    }
}
