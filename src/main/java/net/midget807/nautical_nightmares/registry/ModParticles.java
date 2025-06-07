package net.midget807.nautical_nightmares.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {

    public static final SimpleParticleType WATER_CHARGE = registerParticleType("water_charge", false);

    private static SimpleParticleType registerParticleType(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, NauticalNightmaresMain.id(name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void registerModParticles() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Particles");
    }
}
