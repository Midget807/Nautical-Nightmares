package net.midget807.nautical_nightmares.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> NEEDS_NETHERITE = TagKey.of(RegistryKeys.BLOCK, NauticalNightmaresMain.id("needs_netherite"));

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(NEEDS_NETHERITE)
                .add(ModBlocks.AURALITE_BLOCK)
                .add(ModBlocks.AURALITE_ORE)
                .add(ModBlocks.DEEPSLATE_AURALITE_ORE);
        this.getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(NEEDS_NETHERITE);
    }
}
