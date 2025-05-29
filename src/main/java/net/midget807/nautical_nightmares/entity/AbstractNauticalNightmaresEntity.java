package net.midget807.nautical_nightmares.entity;

import net.midget807.nautical_nightmares.util.DepthUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public abstract class AbstractNauticalNightmaresEntity extends LivingEntity implements CanBePressurised {
    private final DepthUtil.World depthWorld;
    public AbstractNauticalNightmaresEntity(EntityType<? extends LivingEntity> entityType, World world, DepthUtil.World depthWorld) {
        super(entityType, world);
        this.depthWorld = depthWorld;
    }
    public DepthUtil.World getDepthWorld() {
        return this.depthWorld;
    }

    @Override
    public boolean shouldPressurise() {
        return DepthUtil.entityIsOutOfDepthWorld(this);
    }

    @Override
    public boolean canPressurise() {
        return true;
    }
}
