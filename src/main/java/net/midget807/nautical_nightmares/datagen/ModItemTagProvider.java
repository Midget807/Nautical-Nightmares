package net.midget807.nautical_nightmares.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> AURALITE_ITEMS = register("auralite_items");

    private static TagKey<Item> register(String name) {
        return TagKey.of(RegistryKeys.ITEM, NauticalNightmaresMain.id(name));
    }

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(AURALITE_ITEMS)
                .add(ModItems.AURALITE_BOOTS)
                .add(ModItems.AURALITE_LEGGINGS)
                .add(ModItems.AURALITE_CHESTPLATE)
                .add(ModItems.AURALITE_HELMET)
                .add(ModItems.SEA_SCEPTRE)
                .add(ModItems.AURALITE_SWORD)
                .add(ModItems.AURALITE_TRIDENT);
    }
}
