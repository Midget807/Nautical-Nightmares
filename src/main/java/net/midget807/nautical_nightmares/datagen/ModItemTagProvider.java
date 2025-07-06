package net.midget807.nautical_nightmares.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> AURALITE_ITEMS = register("auralite_items");
    public static final TagKey<Item> FORGING_CATALYSTS = register("forging_catalysts");
    public static final TagKey<Item> COAL_FORGING = register("coal_forging");
    public static final TagKey<Item> COAL_BLOCK_FORGING = register("coal_block_forging");
    public static final TagKey<Item> LAVA_FORGING = register("lava_forging");
    public static final TagKey<Item> BLAZE_POWDER_FORGING = register("blaze_powder_forging");
    public static final TagKey<Item> BLAZE_ROD_FORGING = register("blaze_rod_forging");
    public static final TagKey<Item> BARITE_FORGING = register("barite_forging");

    private static TagKey<Item> register(String name) {
        return TagKey.of(RegistryKeys.ITEM, NauticalNightmaresMain.id(name));
    }

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(AURALITE_ITEMS)
                .add(
                        ModItems.AURALITE_BOOTS,
                        ModItems.AURALITE_LEGGINGS,
                        ModItems.AURALITE_CHESTPLATE,
                        ModItems.AURALITE_HELMET,
                        ModItems.SEA_SCEPTRE,
                        ModItems.AURALITE_SWORD,
                        ModItems.AURALITE_TRIDENT
                );

        this.getOrCreateTagBuilder(FORGING_CATALYSTS)
                .add(
                        Items.QUARTZ,
                        ModItems.BARITE_SHARD
                );

        this.getOrCreateTagBuilder(COAL_FORGING)
                .add(
                        ModItems.FERRIC_COPPER_INGOT
                );

        this.getOrCreateTagBuilder(COAL_BLOCK_FORGING)
                .add(
                        ModItems.STEEL_INGOT
                );

        this.getOrCreateTagBuilder(LAVA_FORGING)
                .add(
                        ModItems.AURIC_STEEL_INGOT
                );

        this.getOrCreateTagBuilder(BLAZE_POWDER_FORGING)
                .add(
                        ModItems.AURIC_ALLOY_INGOT
                );

        this.getOrCreateTagBuilder(BLAZE_ROD_FORGING)
                .add(
                        ModItems.NETHERITE_ALLOY_INGOT
                );

        this.getOrCreateTagBuilder(BARITE_FORGING)
                .add(
                        ModItems.AURALITE_INGOT
                );


    }
}
