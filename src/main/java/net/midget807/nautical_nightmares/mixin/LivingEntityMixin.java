package net.midget807.nautical_nightmares.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.midget807.nautical_nightmares.entity.CanBePressurised;
import net.midget807.nautical_nightmares.entity.ModDamages;
import net.midget807.nautical_nightmares.entity.projectile.AuraliteTridentEntity;
import net.midget807.nautical_nightmares.registry.ModEntityAttributes;
import net.midget807.nautical_nightmares.util.DepthUtil;
import net.midget807.nautical_nightmares.world.ModDimensions;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, CanBePressurised {
    @Unique
    private int pressure;
    @Unique
    private static final TrackedData<Integer> PRESSURISED_TICKS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private boolean isPressurised;
    @Unique
    private boolean shouldPressurise;
    @Unique
    private boolean canPressurise = true;
    @Unique
    private int maxPressure;

    @Shadow public abstract boolean isAlive();


    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract void damageArmor(DamageSource source, float amount);

    @Shadow public abstract int getArmor();

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract AttributeContainer getAttributes();

    @Shadow protected abstract void updateAttributes();

    @Shadow public abstract @Nullable DamageSource getRecentDamageSource();

    @Shadow public abstract void takeKnockback(double strength, double x, double z);

    @Shadow public abstract @Nullable LivingEntity getAttacker();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void nauticalNightmares$addData(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(PRESSURISED_TICKS, 0);
    }
    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder nauticalNightmares$initAttributes(DefaultAttributeContainer.Builder original) {
        return original.add(ModEntityAttributes.GENERIC_PRESSURE_RESISTANCE);
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
    public int getPressure() {
        return this.pressure;
    }

    @Override
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    @Override
    public int getPressureAsStat() {
        return (int) Math.floor(this.pressure / 120);
    }

    @Override
    public void setPressureAsStat(int pressureAsStat) {
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

    @Override
    public int getMaxPressure() {
        return this.maxPressure;
    }

    @Override
    public int getMaxPressureAsStat() {
        return (int) this.maxPressure / 120;
    }

    @Override
    public void setMaxPressure(int maxPressure) {
        this.maxPressure = maxPressure;
    }

    @Override
    public void setMaxPressureAsStat(int maxPressureAsStat) {
        this.setMaxPressure(maxPressureAsStat * 120);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;baseTick()V"))
    private void nauticalNightmares$tickPressure(CallbackInfo ci) {
        if (this.isAlive()) {
            this.setShouldPressurise(DepthUtil.isInDangerZone(this.getWorld(), ((LivingEntity)((Object)this))) && Math.abs(this.getPressure()) > this.getMaxPressure());

            if (this.getWorld().getRegistryKey() == ModDimensions.MESOPELAGIC_WK ||
                    this.getWorld().getRegistryKey() == ModDimensions.BATHYPELAGIC_WK ||
                    this.getWorld().getRegistryKey() == ModDimensions.ABYSSOPELAGIC_WK
            ) {
                this.setMaxPressure(256 + (int) (((LivingEntity)((Object)this)).getAttributeValue(ModEntityAttributes.GENERIC_PRESSURE_RESISTANCE) * 120));
                this.isPressurised = this.getPressurisedTicks() >= this.getMinPressurisedDamageTicks();
                if (this.getBlockY() < 0) {
                    this.setPressure(Math.abs(this.getBlockY()));
                }
            } else {
                this.setPressure(0);
            }
        }
    }
    private int pressureDamageTick = 0;
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
                this.damage(ModDamages.getDamageSource(((LivingEntity)((Object)this)), ModDamages.PRESSURISED), 1.0f);
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
