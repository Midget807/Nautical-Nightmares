package net.midget807.nautical_nightmares;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.midget807.nautical_nightmares.entity.ModEntityModelLayers;
import net.midget807.nautical_nightmares.entity.projectile.client.AuraliteTridentEntityModel;
import net.midget807.nautical_nightmares.entity.projectile.client.AuraliteTridentEntityRenderer;
import net.midget807.nautical_nightmares.entity.projectile.client.WaterJetEntityRenderer;
import net.midget807.nautical_nightmares.registry.ModEntities;
import net.midget807.nautical_nightmares.registry.ModParticles;
import net.midget807.nautical_nightmares.registry.ModScreenHandlers;
import net.midget807.nautical_nightmares.screen.client.ForgeScreen;
import net.midget807.nautical_nightmares.util.ModModelPredicateProvider;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.particle.EndRodParticle;

public class NauticalNightmaresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicateProvider.registerModModelPredicates();
        EntityRendererRegistry.register(ModEntities.AURALITE_TRIDENT, AuraliteTridentEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.AURALITE_TRIDENT, AuraliteTridentEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.WATER_JET, WaterJetEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.WATER_CHARGE, EndRodParticle.Factory::new);
        HandledScreens.register(ModScreenHandlers.FORGE_SCREEN_HANDLER, ForgeScreen::new);
    }
}
