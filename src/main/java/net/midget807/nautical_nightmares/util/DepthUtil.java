package net.midget807.nautical_nightmares.util;

import net.midget807.nautical_nightmares.entity.AbstractNauticalNightmaresEntity;
import net.midget807.nautical_nightmares.world.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class DepthUtil {
    public static int MESOPELAGIC_WATER = 512;
    public static int MESOPELAGIC_LOWER_TRANSITION = 128;
    public static int BATHYPELAGIC_WATER = 1024;
    public static int BATHYPELAGIC_UPPER_TRANSITION = 128;
    public static int BATHYPELAGIC_LOWER_TRANSITION = 128;
    public static int ABYSSOPELAGIC_WATER = 2048;
    public static int ABYSSOPELAGIC_UPPER_TRANSITION = 128;

    public static boolean isBelowFreeDiveDepth(LivingEntity entity/*, RegistryKey<World> dimKey*/) {
        return entity.getBlockY() <= -256 /*&& todo add dimension*/;
    }

    public static boolean isInUpperTransition(LivingEntity entity, DepthUtil.World world) {
        if (world == DepthUtil.World.BATHYPELAGIC || world == DepthUtil.World.ABYSSOPELAGIC) {
            return entity.getBlockY() >= 16;
        }
        return false;
    }
    public static boolean isInLowerTransition(LivingEntity entity, DepthUtil.World world) {
        if (world == DepthUtil.World.MESOPELAGIC || world == DepthUtil.World.BATHYPELAGIC) {
            return entity.getBlockY() <= -(getWaterHeight(world) + 16);
        }
        return false;
    }

    public static int getWaterHeight(DepthUtil.World world) {
        return switch (world) {
            case MESOPELAGIC -> MESOPELAGIC_WATER;
            case BATHYPELAGIC -> BATHYPELAGIC_WATER;
            case ABYSSOPELAGIC -> ABYSSOPELAGIC_WATER;
        };
    }

    public static boolean entityIsOutOfDepthWorld(AbstractNauticalNightmaresEntity entity) {
        return entity.getEntityWorld().getRegistryKey() != getWorldKeyFromEnum(entity.getDepthWorld());
    }
    public static RegistryKey<net.minecraft.world.World> getWorldKeyFromEnum(DepthUtil.World world) {
        return switch (world) {
            case MESOPELAGIC -> ModDimensions.MESOPELAGIC_WK;
            case BATHYPELAGIC -> ModDimensions.BATHYPELAGIC_WK;
            case ABYSSOPELAGIC -> ModDimensions.ABYSSOPELAGIC_WK;
        };
    }
    public static boolean isInDangerZone(net.minecraft.world.World world, LivingEntity entity) {
        return world.getRegistryKey() == DepthUtil.getWorldKeyFromEnum(DepthUtil.World.MESOPELAGIC) && DepthUtil.isBelowFreeDiveDepth(entity) ||
                world.getRegistryKey() == DepthUtil.getWorldKeyFromEnum(DepthUtil.World.BATHYPELAGIC) ||
                world.getRegistryKey() == DepthUtil.getWorldKeyFromEnum(DepthUtil.World.ABYSSOPELAGIC);
    }

    public enum World {
        MESOPELAGIC,
        BATHYPELAGIC,
        ABYSSOPELAGIC
    }

}
