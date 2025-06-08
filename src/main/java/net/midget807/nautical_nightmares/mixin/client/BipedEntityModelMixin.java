package net.midget807.nautical_nightmares.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {
    @WrapOperation(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isUsingItem()Z"))
    public boolean boolean$dualCharge(LivingEntity instance, Operation<Boolean> original) {
        return instance.getActiveItem().isOf(ModItems.AURALITE_SWORD) ? false : original.call(instance);
    }
}
