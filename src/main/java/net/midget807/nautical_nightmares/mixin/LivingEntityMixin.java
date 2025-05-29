package net.midget807.nautical_nightmares.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.midget807.nautical_nightmares.entity.AbstractNauticalNightmaresEntity;
import net.midget807.nautical_nightmares.entity.CanBePressurised;
import net.midget807.nautical_nightmares.entity.ModDamages;
import net.midget807.nautical_nightmares.util.DepthUtil;
import net.midget807.nautical_nightmares.world.ModDimensions;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, CanBePressurised {
    @Unique
    private double pressure;
    @Unique
    private static final TrackedData<Integer> PRESSURISED_TICKS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private boolean isPressurised;
    @Unique
    private boolean shouldPressurise;
    @Unique
    private boolean canPressurise;

    @Shadow public abstract boolean isAlive();


    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract void damageArmor(DamageSource source, float amount);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void nauticalNightmares$addData(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(PRESSURISED_TICKS, 0);
    }

    @Override
    public int getPressurisedTicks() {
        return this.dataTracker.get(PRESSURISED_TICKS);
    }

    @Override
    public void setPressurisedTicks(int ticks) {
        this.dataTracker.set(PRESSURISED_TICKS, ticks);
    }

    @Override
    public int getMinPressurisedDamageTicks() {
        return 5 * 20 * 2;
    }

    @Override
    public float getPressurisedScale() {
        int i = this.getMinPressurisedDamageTicks();
        return (float) Math.min(this.getPressurisedTicks(), i) / i;
    }

    @Override
    public double getPressure() {
        return this.pressure;
    }

    @Override
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Override
    public double getPressureAsStat() {
        return Math.floor(this.pressure / 120);
    }

    @Override
    public void setPressureAsStat(double pressureAsStat) {
        this.setPressure(this.getPressureAsStat() * 120);
    }

    @Override
    public boolean isPressurised() {
        return this.isPressurised;
    }

    @Override
    public boolean shouldPressurise() {
        return this.shouldPressurise;
    }

    @Override
    public void setShouldPressurise(boolean shouldPressurise) {
        this.shouldPressurise = shouldPressurise;
    }

    @Override
    public boolean canPressurise() {
        return this.canPressurise;
    }

    @Override
    public void setCanPressurise(boolean canPressurise) {
        this.canPressurise = canPressurise;
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;baseTick()V"))
    private void nauticalNightmares$tickPressure(CallbackInfo ci) {
        if (this.isAlive()) {
            if (this.getWorld().getRegistryKey() == ModDimensions.MESOPELAGIC_WK ||
                    this.getWorld().getRegistryKey() == ModDimensions.BATHYPELAGIC_WK ||
                    this.getWorld().getRegistryKey() == ModDimensions.ABYSSOPELAGIC_WK
            ) {
                this.isPressurised = this.getPressurisedTicks() >= this.getMinPressurisedDamageTicks();
                this.setPressure(Math.abs(this.getBlockY()));
            } else {
                this.setPressure(0);
            }
        }
    }
    @Inject(method = "tick", at = @At("TAIL"))
    private void nauticalNightmares$pressurise(CallbackInfo ci) {
        if (!this.getWorld().isClient && !((LivingEntity)((Object)this)).isDead()) {
            int i = this.getPressurisedTicks();
            if (this.shouldPressurise() && this.canPressurise()) {
                this.setPressurisedTicks(Math.min(this.getMinPressurisedDamageTicks(), i + 2));
            } else {
                this.setPressurisedTicks(Math.max(0, i - 1));
            }
            if (this.isPressurised() && this.canPressurise()) {
                this.damage(ModDamages.getDamageSource(((LivingEntity)((Object)this)), ModDamages.PRESSURISED), 8.0f);
            }
        }
    }
    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageArmor(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void nauticalNightmares$pressureBreaksDiamonds(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
        if (source.isOf(ModDamages.PRESSURISED)) {
            this.damageArmor(source, amount * 4);
        } else {
            original.call(instance, source, amount);
        }
    }
}
