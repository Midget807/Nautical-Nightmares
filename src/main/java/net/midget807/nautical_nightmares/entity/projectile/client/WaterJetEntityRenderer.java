package net.midget807.nautical_nightmares.entity.projectile.client;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.entity.projectile.WaterJetEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class WaterJetEntityRenderer extends EntityRenderer<WaterJetEntity> {
    public static final Identifier TEXTURE = NauticalNightmaresMain.id("texture/entity/water_jet.png");

    public WaterJetEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(WaterJetEntity entity) {
        return TEXTURE;
    }
}
