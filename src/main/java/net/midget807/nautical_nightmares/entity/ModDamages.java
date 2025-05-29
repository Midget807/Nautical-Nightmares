package net.midget807.nautical_nightmares.entity;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModDamages {

    public static final RegistryKey<DamageType> PRESSURISED = registerDamageType("pressurised");

    private static RegistryKey<DamageType> registerDamageType(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, NauticalNightmaresMain.id(name));
    }
    public static DamageSource getDamageSource(Entity entity, RegistryKey<DamageType> damageType) {
        return new DamageSource(entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(damageType));
    }
}
