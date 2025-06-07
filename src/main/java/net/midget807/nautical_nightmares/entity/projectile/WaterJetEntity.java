package net.midget807.nautical_nightmares.entity.projectile;

import net.midget807.nautical_nightmares.registry.ModEntities;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class WaterJetEntity extends PersistentProjectileEntity {
    public int ticksUntilRemove = 5;

    public WaterJetEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public WaterJetEntity(LivingEntity owner, World world) {
        this(ModEntities.WATER_JET, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.BLANK);
    }

    @Override
    public void tick() {
        super.tick();
        for (float x = -0.6f; x <= 0.6f; x = (float)((double)x + 0.1)) {
            this.getWorld().addParticle(ParticleTypes.SPLASH, this.getX() + x * Math.cos(this.getYaw()), this.getY(), this.getZ() + x * Math.sin(this.getYaw()), this.getVelocity().getX(), this.getVelocity().getY(), this.getVelocity().getZ());
        }
        if (this.inGround || this.age> 20) {
            for (int i = 0; i < 50; ++i) {
                this.getWorld().addParticle(ParticleTypes.SPLASH, this.getX() + this.random.nextGaussian() * 0.5f * Math.cos(this.getYaw()), this.getY(), this.getZ() + this.random.nextGaussian() * 0.5f * Math.sin(this.getYaw()), (double) this.random.nextGaussian() / 10.0f, (double) this.random.nextFloat() / 2.0f, (double) this.random.nextGaussian() / 10.f);
            }
            --this.ticksUntilRemove;
        }
        if (this.ticksUntilRemove <= 0) {
            this.discard();
        }
        if (!this.getWorld().isClient) {
            for (LivingEntity livingEntity : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), entity -> this.getOwner() != entity)) {
                if (!livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0), this.getOwner());
                }
            }
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }
}
