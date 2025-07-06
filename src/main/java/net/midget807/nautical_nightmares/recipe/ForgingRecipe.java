package net.midget807.nautical_nightmares.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.nautical_nightmares.registry.ModBlocks;
import net.midget807.nautical_nightmares.registry.ModRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ForgingRecipe implements Recipe<ForgingRecipeInput> {
    public static final String ITEM_1_KEY = "item_1";
    public static final String ITEM_2_KEY = "item_2";
    public static final String FUEL_KEY = "fuel";
    public static final String CATALYST_KEY = "catalyst";
    public static final String OUTPUT_KEY = "result";
    public final String group;
    public final Ingredient item1;
    public final Ingredient item2;
    public final Ingredient fuel;
    @Nullable
    public final Ingredient catalyst;
    public final ItemStack result;
    public final float experience;
    public final int cookingTime;

    public ForgingRecipe(String group, Ingredient item1, Ingredient item2, Ingredient fuel, @Nullable Ingredient catalyst, ItemStack result, float experience, int cookingTime) {
        this.group = group;
        this.item1 = item1;
        this.item2 = item2;
        this.fuel = fuel;
        this.catalyst = catalyst;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(ForgingRecipeInput input, World world) {
        return this.item1.test(input.item1()) && this.item2.test(input.item2()) && this.fuel.test(input.fuel()) && this.catalyst.test(input.catalyst());
    }

    @Override
    public ItemStack craft(ForgingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.item1);
        defaultedList.add(this.item2);
        return defaultedList;
    }

    public Ingredient getFuel() {
        return this.fuel;
    }

    public @Nullable Ingredient getCatalyst() {
        return this.catalyst;
    }

    public float getExperience() {
        return this.experience;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public boolean needsCatalyst() {
        return this.getCatalyst() != null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FORGING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FORGING_TYPE;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.FORGE);
    }

    public static class Serializer implements RecipeSerializer<ForgingRecipe> {
        public final MapCodec<ForgingRecipe> CODEC;
        public static final PacketCodec<RegistryByteBuf, ForgingRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                ForgingRecipe.Serializer::write, ForgingRecipe.Serializer::read
        );

        public Serializer(int cookingTime) {
            CODEC = RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf(ITEM_1_KEY).forGetter(recipe -> recipe.item1),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf(ITEM_2_KEY).forGetter(recipe -> recipe.item2),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf(FUEL_KEY).forGetter(recipe -> recipe.fuel),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf(CATALYST_KEY).forGetter(recipe -> recipe.catalyst),
                            ItemStack.VALIDATED_CODEC.fieldOf(OUTPUT_KEY).forGetter(recipe -> recipe.result),
                            Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(recipe -> recipe.experience),
                            Codec.INT.fieldOf("cookingTime").orElse(cookingTime).forGetter(recipe -> recipe.cookingTime)
                    ).apply(instance, ForgingRecipe::new)
            );
        }


        @Override
        public MapCodec<ForgingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ForgingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
        private static ForgingRecipe read(RegistryByteBuf buf) {
            String string = buf.readString();
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient2 = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient3 = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient4 = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
            float f = buf.readFloat();
            int i = buf.readVarInt();
            return new ForgingRecipe(string, ingredient, ingredient2, ingredient3, ingredient4, itemStack, f, i);
        }

        private static void write(RegistryByteBuf buf, ForgingRecipe recipe) {
            buf.writeString(recipe.group);
            Ingredient.PACKET_CODEC.encode(buf, recipe.item1);
            Ingredient.PACKET_CODEC.encode(buf, recipe.item2);
            Ingredient.PACKET_CODEC.encode(buf, recipe.fuel);
            Ingredient.PACKET_CODEC.encode(buf, recipe.catalyst);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeFloat(recipe.experience);
            buf.writeVarInt(recipe.cookingTime);
        }
    }
}
