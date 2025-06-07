package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.entity.projectile.AuraliteTridentEntity;
import net.midget807.nautical_nightmares.entity.projectile.WaterJetEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {

    public static final EntityType<WaterJetEntity> WATER_JET = register(
            "water_jet",
            EntityType.Builder.<WaterJetEntity>create(WaterJetEntity::new, SpawnGroup.MISC)
                    .dimensions(1.2f, 0.5f)
                    .eyeHeight(0.13f)
                    .maxTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
    );

    public static final EntityType<AuraliteTridentEntity> AURALITE_TRIDENT = register(
            "auralite_trident",
            EntityType.Builder.<AuraliteTridentEntity>create(AuraliteTridentEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .eyeHeight(0.13f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    private static <T extends Entity>EntityType<T> register(String name, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, NauticalNightmaresMain.id(name), type.build());
    }

    public static void registerModEntities() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Entities");
    }
}
