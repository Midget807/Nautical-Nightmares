package net.midget807.nautical_nightmares.world;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class ModDimensions {
    public static final RegistryKey<World> MESOPELAGIC_WK = registerWorldKey("mesopelagic");
    public static final RegistryKey<World> BATHYPELAGIC_WK = registerWorldKey("bathypelagic");
    public static final RegistryKey<World> ABYSSOPELAGIC_WK = registerWorldKey("abyssopelagic");

    public static final RegistryKey<DimensionOptions> MESOPELAGIC_OPTIONS = registerDimOptionKey("mesopelagic");
    public static final RegistryKey<DimensionOptions> BATHYPELAGIC_OPTIONS = registerDimOptionKey("bathypelagic");
    public static final RegistryKey<DimensionOptions> ABYSSOPELAGIC_OPTIONS = registerDimOptionKey("abyssopelagic");

    public static final RegistryKey<DimensionType> MESOPELAGIC_TYPE = registerDimTypeKey("mesopelagic");
    public static final RegistryKey<DimensionType> BATHYPELAGIC_TYPE = registerDimTypeKey("bathypelagic");
    public static final RegistryKey<DimensionType> ABYSSOPELAGIC_TYPE = registerDimTypeKey("abyssopelagic");

    private static RegistryKey<World> registerWorldKey(String name) {
        return RegistryKey.of(RegistryKeys.WORLD, NauticalNightmaresMain.id(name));
    }
    private static RegistryKey<DimensionOptions> registerDimOptionKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION, NauticalNightmaresMain.id(name));
    }
    private static RegistryKey<DimensionType> registerDimTypeKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, NauticalNightmaresMain.id(name));
    }

    public static void bootstrapType(Registerable<DimensionType> context) {
        context.register(
                MESOPELAGIC_TYPE,
                new DimensionType(
                        OptionalLong.of(12000L),
                        true,
                        true,
                        false,
                        true,
                        1.0,
                        true,
                        true,
                        -640,
                        768,
                        0,
                        BlockTags.INFINIBURN_OVERWORLD,
                        DimensionTypes.OVERWORLD_ID,
                        15.0f,
                        new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 0)
                )
        );
        context.register(
                BATHYPELAGIC_TYPE,
                new DimensionType(
                        OptionalLong.of(0L),
                        true,
                        true,
                        false,
                        true,
                        1.0,
                        true,
                        true,
                        -1024,
                        1152,
                        0,
                        BlockTags.INFINIBURN_OVERWORLD,
                        DimensionTypes.OVERWORLD_ID,
                        7.0f,
                        new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 7)
                )
        );
        context.register(
                ABYSSOPELAGIC_TYPE,
                new DimensionType(
                        OptionalLong.of(0L),
                        true,
                        true,
                        false,
                        true,
                        1.0,
                        true,
                        true,
                        -1984,
                        2112,
                        0,
                        BlockTags.INFINIBURN_OVERWORLD,
                        DimensionTypes.OVERWORLD_ID,
                        0.0f,
                        new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 15), 15)
                )
        );
    }
}
