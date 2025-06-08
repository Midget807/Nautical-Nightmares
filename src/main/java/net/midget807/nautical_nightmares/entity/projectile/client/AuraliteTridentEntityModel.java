package net.midget807.nautical_nightmares.entity.projectile.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class AuraliteTridentEntityModel extends Model {
	private final ModelPart root;

	public AuraliteTridentEntityModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.root = root;
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData2 = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -23.0F, 0.0F, 1.0F, 25.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelPartData2 = modelPartData.addChild("hilt", ModelPartBuilder.create().uv(12, 4).cuboid(-1.0F, 2.0F, 0.5F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(12, 3).cuboid(0.0F, 1.0F, 0.5F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(12, 2).cuboid(-2.0F, 1.0F, 0.5F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F)
		);
		modelPartData2 = modelPartData.addChild("base", ModelPartBuilder.create().uv(4, 0).cuboid(-2.0F, -25.0F, 0.0F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(12, 0).cuboid(2.0F, -25.0F, 0.5F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(8, 9).cuboid(-1.0F, -24.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(12, 1).cuboid(-4.0F, -25.0F, 0.5F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(8, 11).cuboid(-1.0F, -24.5F, 0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F)
		);
		modelPartData2 = modelPartData.addChild("right_spike", ModelPartBuilder.create().uv(4, 9).cuboid(-3.0F, -29.0F, 0.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelPartData2 = modelPartData.addChild("left_spike", ModelPartBuilder.create().uv(4, 3).cuboid(1.0F, -29.0F, 0.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelPartData2 = modelPartData.addChild("middle_spike", ModelPartBuilder.create().uv(8, 3).cuboid(-1.0F, -30.0F, 0.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.root.render(matrices, vertices, light, overlay, color);
	}
}