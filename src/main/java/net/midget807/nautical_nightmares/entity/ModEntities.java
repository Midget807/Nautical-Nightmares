package net.midget807.nautical_nightmares.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.entity.custom.narranyclius.NarranycliusEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<NarranycliusEntity> NARRANYCLIUS_ENTITY__TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            NauticalNightmaresMain.id("narranyclius"),
            EntityType.Builder.create(NarranycliusEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1f, 1f)
                    .build()
    );
    public static void registerModEntities() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Entities");
    }
}
