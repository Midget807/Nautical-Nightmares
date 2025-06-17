package net.midget807.nautical_nightmares.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow public abstract ItemModels getModels();

    @Shadow @Final private ItemModels models;

    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), argsOnly = true)
    public BakedModel nauticalNightmares$handheldModels(BakedModel value, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ModelTransformationMode renderMode) {
        if (stack.isOf(ModItems.SEA_SCEPTRE) && (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
            return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(NauticalNightmaresMain.id("sea_sceptre")));
        }
        if (stack.isOf(ModItems.AURALITE_TRIDENT) && (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
            return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(NauticalNightmaresMain.id("auralite_trident")));
        }
        return value;
    }

    @ModifyVariable(method = "getModel", at = @At("STORE"), ordinal = 1)
    public BakedModel nauticalNightmares$getHeldItemModel(BakedModel value, @Local(argsOnly = true) ItemStack stack) {
        if (stack.isOf(ModItems.SEA_SCEPTRE)) {
            return this.models.getModelManager().getModel(ModelIdentifier.ofInventoryVariant(NauticalNightmaresMain.id("sea_sceptre_handheld")));
        }
        if (stack.isOf(ModItems.AURALITE_TRIDENT)) {
            return this.models.getModelManager().getModel(ModelIdentifier.ofInventoryVariant(NauticalNightmaresMain.id("auralite_trident_handheld")));
        }
        return value;
    }
}
