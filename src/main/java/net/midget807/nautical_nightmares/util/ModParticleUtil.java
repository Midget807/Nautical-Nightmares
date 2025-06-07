package net.midget807.nautical_nightmares.util;

import net.midget807.nautical_nightmares.registry.ModParticles;
import net.minecraft.world.World;

public class ModParticleUtil {
    public static void spawnWaterChargeParticle(World world, double x, double y, double z, double velX, double velY, double velZ) {
        for (int i = 0; i < 30; i++) {
            world.addParticle(ModParticles.WATER_CHARGE, x + world.random.nextDouble() / 2, y + world.random.nextDouble() / 2, z + world.random.nextDouble() / 2, velX, velY, velZ); //todo fix particle to be like accumulate
        }
    }
}
