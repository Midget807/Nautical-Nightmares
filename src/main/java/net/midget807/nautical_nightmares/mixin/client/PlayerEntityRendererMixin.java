package net.midget807.nautical_nightmares.mixin.client;

import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void amarite$customPoses(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack main = player.getMainHandStack();
        if (main.isOf(ModItems.AURALITE_SWORD)) {
            boolean charging = player.isUsingItem();
            if (charging) {
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
            }
            /*if (hand == Hand.MAIN_HAND) {
                if (charging) {
                    cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
                } else {
                    cir.setReturnValue(BipedEntityModel.ArmPose.ITEM);
                }
            } else {
                if (charging) {
                    cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
                } else {
                    cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
                }
            }*/
        }
    }
}
