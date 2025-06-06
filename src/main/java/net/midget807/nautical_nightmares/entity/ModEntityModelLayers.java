package net.midget807.nautical_nightmares.entity;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;

import java.util.stream.Collectors;

public class ModEntityModelLayers {
    public static final EntityModelLayer AURALITE_TRIDENT = registerMain("auralite_trident");

    private static EntityModelLayer registerMain(String name) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(NauticalNightmaresMain.id(name), "main");
        if (!EntityModelLayers.getLayers().collect(Collectors.toSet()).add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }
}
