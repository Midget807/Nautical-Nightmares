package net.midget807.nautical_nightmares.mixin;

import net.midget807.nautical_nightmares.entity.CanBePressurised;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, CanBePressurised {
    private double pressure;
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public double getPressure() {
        return this.pressure;
    }

    @Override
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
