package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class ModEntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_PRESSURE_RESISTANCE = register(
            "generic.pressure_resistance", new ClampedEntityAttribute("attribute.name.generic.pressure_resistance", 0.0, 0.0, 100.0).setTracked(true)
    );

    private static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, NauticalNightmaresMain.id(name), attribute);
    }

    public static void registerModEntityAttributes() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Entity Attributes");
    }
}
