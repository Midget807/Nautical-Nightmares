package net.midget807.nautical_nightmares.entity.projectile.client;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.entity.ModEntityModelLayers;
import net.midget807.nautical_nightmares.entity.projectile.AuraliteTridentEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class AuraliteTridentEntityRenderer extends EntityRenderer<AuraliteTridentEntity> {
    public static final Identifier TEXTURE = NauticalNightmaresMain.id("textures/entity/projectile/auralite_trident.png");
    private final AuraliteTridentEntityModel model;

    public AuraliteTridentEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new AuraliteTridentEntityModel(ctx.getPart(ModEntityModelLayers.AURALITE_TRIDENT));
    }

    public void render(AuraliteTridentEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(
                vertexConsumerProvider, this.model.getLayer(this.getTexture(tridentEntity)), false, tridentEntity.isEnchanted()
        );
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(AuraliteTridentEntity entity) {
        return TEXTURE;
    }
}
