package net.midget807.nautical_nightmares.mixin.client;

import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowPosing.class)
public abstract class CrossbowPosingMixin {
    @Inject(method = "charge", at = @At("HEAD"), cancellable = true)
    private static void nauticalNightmares$dontAnimateSwordCharge(ModelPart holdingArm, ModelPart pullingArm, LivingEntity actor, boolean rightArmed, CallbackInfo ci) {
        ItemStack stack = actor.getMainHandStack();
        if (stack.isOf(ModItems.AURALITE_SWORD) && actor.getMainArm() == (!rightArmed ? Arm.RIGHT : Arm.LEFT)) {
            ModelPart modelPart = rightArmed ? holdingArm : pullingArm;
            ModelPart modelPart2 = rightArmed ? pullingArm : holdingArm;
            modelPart.yaw = rightArmed ? -0.8F : 0.8F;
            modelPart.pitch = -0.97079635F;
            modelPart2.pitch = modelPart.pitch;
            modelPart2.yaw = 0.4F * (float) (rightArmed ? 1 : -1);
            ci.cancel();
        }
    }
}
