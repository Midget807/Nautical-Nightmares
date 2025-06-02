package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.block.entity.ForgeBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<ForgeBlockEntity> FORGE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, NauticalNightmaresMain.id("forge_block_entity"),
                    BlockEntityType.Builder.create(ForgeBlockEntity::new, ModBlocks.FORGE).build());

    public static void registerModBlockEntities() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Block Entities");
    }
}
