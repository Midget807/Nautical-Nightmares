package net.midget807.nautical_nightmares.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.List;

public class ForgingUtil {
    public static List<ItemStack> fuelItems = List.of(
            new ItemStack(Items.COAL),
            new ItemStack(Items.CHARCOAL),
            new ItemStack(Items.COAL_BLOCK),
            new ItemStack(Items.LAVA_BUCKET),
            new ItemStack(Items.BLAZE_ROD)
    );

    public static int getStrengthFromIngredient(Ingredient ingredient) {
        for (ItemStack stack : fuelItems) {
            if (ingredient.test(stack)) {
                if (stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL) {
                    return ForgeStrengths.COALS.ordinal();
                }
                if (stack.getItem() == Items.COAL_BLOCK) {
                    return ForgeStrengths.COAL_BLOCKS.ordinal();
                }
                if (stack.getItem() == Items.LAVA_BUCKET) {
                    return ForgeStrengths.LAVA.ordinal();
                }
                if (stack.getItem() == Items.BLAZE_ROD) {
                    return ForgeStrengths.BLAZE_ROD.ordinal();
                }
            }
        }
        return -1;
    }

    public static Item getItemFromOrdinal(int ordinal) {
        return switch (ordinal) {
            case 0 -> Items.COAL;
            case 1 -> Items.COAL_BLOCK;
            case 2 -> Items.LAVA_BUCKET;
            case 3 -> Items.BLAZE_ROD;
            default -> null;
        };
    }

    public enum ForgeStrengths {
        COALS,
        COAL_BLOCKS,
        LAVA,
        BLAZE_ROD
    }
}
