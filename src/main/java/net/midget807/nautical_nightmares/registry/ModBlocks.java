package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.block.ForgeBlock;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static final Block AURALITE_BLOCK = registerBlock("auralite_block", new Block(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).mapColor(MapColor.PALE_YELLOW)));
    public static final Block AURALITE_ORE = registerBlock("auralite_ore", new Block(AbstractBlock.Settings.copy(Blocks.ANCIENT_DEBRIS).mapColor(MapColor.PALE_YELLOW)));
    public static final Block DEEPSLATE_AURALITE_ORE = registerBlock("deepslate_auralite_ore", new Block(AbstractBlock.Settings.copy(Blocks.ANCIENT_DEBRIS).mapColor(MapColor.PALE_YELLOW)));

    public static final Block FORGE = registerBlock("forge", new ForgeBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.STONE_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()
            .strength(3.5F)
            .luminance(Blocks.createLightLevelFromLitBlockState(13))));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, NauticalNightmaresMain.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, NauticalNightmaresMain.id(name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Blocks");
    }
}
