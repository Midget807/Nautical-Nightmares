package net.midget807.nautical_nightmares.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class WaterChargeParticle extends SpriteBillboardParticle {
    private final double startX;
    private final double startY;
    private final double startZ;

    public WaterChargeParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.velocityX = g;
        this.velocityY = h;
        this.velocityZ = i;
        this.x = d;
        this.y = e;
        this.z = f;
        this.startX = this.x;
        this.startY = this.y;
        this.startZ = this.z;
        this.scale = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        float j = this.random.nextFloat() * 0.6F + 0.4F;
        this.red = j * 0.9F;
        this.green = j * 0.3F;
        this.blue = j;
        this.maxAge = (int)(Math.random() * 10.0) + 40;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float f = ((float)this.age + tickDelta) / (float)this.maxAge;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.scale * f;
    }

    @Override
    public int getBrightness(float tint) {
        int i = super.getBrightness(tint);
        float f = (float)this.age / (float)this.maxAge;
        f *= f;
        f *= f;
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        k += (int)(f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            float f = (float)this.age / (float)this.maxAge;
            float var3 = -f + f * f * 2.0F;
            float var4 = 1.0F - var3;
            this.x = this.startX + this.velocityX * (double)var4;
            this.y = this.startY + this.velocityY * (double)var4 + (double)(1.0F - f);
            this.z = this.startZ + this.velocityZ * (double)var4;
        }
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            WaterChargeParticle chargeParticle = new WaterChargeParticle(world, x, y, z, velocityX, velocityY, velocityZ);
            chargeParticle.setSprite(this.spriteProvider);
            return chargeParticle;
        }
    }
}
