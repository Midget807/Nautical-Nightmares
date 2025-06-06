package net.midget807.nautical_nightmares;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.midget807.nautical_nightmares.entity.ModEntityModelLayers;
import net.midget807.nautical_nightmares.entity.projectile.client.AuraliteTridentEntityModel;
import net.midget807.nautical_nightmares.entity.projectile.client.AuraliteTridentEntityRenderer;
import net.midget807.nautical_nightmares.registry.ModEntities;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class NauticalNightmaresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.AURALITE_TRIDENT, AuraliteTridentEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.AURALITE_TRIDENT, AuraliteTridentEntityModel::getTexturedModelData);
    }
}
