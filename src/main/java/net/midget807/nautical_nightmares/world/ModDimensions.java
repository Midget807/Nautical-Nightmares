package net.midget807.nautical_nightmares.world;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    public static final RegistryKey<World> MESOPELAGIC_WK = registerWorldKey("mesopelagic");
    public static final RegistryKey<World> BATHYPELAGIC_WK = registerWorldKey("bathypelagic");
    public static final RegistryKey<World> ABYSSOPELAGIC_WK = registerWorldKey("abyssopelagic");

    private static RegistryKey<World> registerWorldKey(String name) {
        return RegistryKey.of(RegistryKeys.WORLD, NauticalNightmaresMain.id(name));
    }
    private static RegistryKey<DimensionOptions> registerDimKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION, NauticalNightmaresMain.id(name));
    }
    private static RegistryKey<DimensionType> registerDimTypeKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, NauticalNightmaresMain.id(name));
    }
}
