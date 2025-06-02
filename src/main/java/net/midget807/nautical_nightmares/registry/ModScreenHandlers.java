package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.screen.ForgeScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ForgeScreenHandler> FORGE_SCREEN_HANDLER = registerType("forge", ForgeScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> registerType(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, NauticalNightmaresMain.id(name), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerModScreenHandlers() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Screen Handlers");
    }
}
