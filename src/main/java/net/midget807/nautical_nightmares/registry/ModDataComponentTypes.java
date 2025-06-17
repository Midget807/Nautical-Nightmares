package net.midget807.nautical_nightmares.registry;

import com.mojang.serialization.Codec;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.component.SceptreInventoryComponent;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<SceptreInventoryComponent> SCEPTRE_INVENTORY = register("sceptre_inventory", builder -> builder.codec(SceptreInventoryComponent.CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderUnaryOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, NauticalNightmaresMain.id(name), builderUnaryOperator.apply(ComponentType.builder()).build());
    }

    public static void registerModDataComponentTypes() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Data Component Types");
    }
}
