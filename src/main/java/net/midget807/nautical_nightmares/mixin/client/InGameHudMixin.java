package net.midget807.nautical_nightmares.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.midget807.nautical_nightmares.entity.CanBePressurised;
import net.midget807.nautical_nightmares.util.ModTextureIds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @Shadow public abstract TextRenderer getTextRenderer();

    /*
        @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"))
        private void nauticalNightmare$render(DrawContext context, CallbackInfo ci, @Local PlayerEntity playerRef) {
            renderPressure(context, playerRef);
        }*/
    @Inject(method = "renderMainHud", at = @At("TAIL"))
    private void nauticalNightmare$renderMain(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        renderPressure(context, this.getCameraPlayer());
    }

    @Unique
    private void renderPressure(DrawContext context, PlayerEntity player) {
        this.client.getProfiler().push("pressureBar");
        int pressure = ((CanBePressurised)player).getPressure();
        int maxPressure = ((CanBePressurised)player).getMaxPressure();
        int i = context.getScaledWindowWidth() / 2 - 91;
        int j = context.getScaledWindowHeight() - 55;
        Integer progress = null;
        if (maxPressure > 0) {
            progress = MathHelper.clamp(Math.round((float) (pressure * 81) / maxPressure), 0, 81);
        }

        RenderSystem.enableBlend();
        if (progress != null && progress > 0) {
            context.drawGuiTexture(ModTextureIds.PRESSURE_BAR_SCALE, i, j, 1, progress, 5);
        }
        context.drawGuiTexture(ModTextureIds.PRESSURE_BAR_BG, i, j, 0, 81, 5);
        context.drawGuiTexture(ModTextureIds.PRESSURE_BAR_OVERLAY, i, j, 2, 81, 5);

        if (((CanBePressurised)player).getPressurisedTicks() > 0) {
            float lerpedAmount = MathHelper.abs((float) Math.tanh((2 * MathHelper.sin((float) ((CanBePressurised) player).getPressurisedTicks() / 20))));
            int lerpedColor = ColorHelper.Argb.lerp(lerpedAmount, ColorHelper.Argb.getArgb(0, 255, 0, 0), ColorHelper.Argb.getArgb(255, 255, 0, 0));
            context.drawText(this.getTextRenderer(), "Warning: Pressure Damage Imminent", context.getScaledWindowWidth() / 2 - (int) (this.getTextRenderer().getWidth("Warning: Pressure Damage Imminent") / 2) + 1, 15, lerpedColor, false);
        }
        RenderSystem.disableBlend();
        this.client.getProfiler().pop();
    }
}
