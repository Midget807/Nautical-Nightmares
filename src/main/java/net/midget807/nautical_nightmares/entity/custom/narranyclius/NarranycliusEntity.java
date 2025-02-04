package net.midget807.nautical_nightmares.entity.custom.narranyclius;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class NarranycliusEntity extends HostileEntity implements RangedAttackMob {
    boolean targeting;
    public final SwimNavigation waterNavigation;
    public NarranycliusEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new NarranycliusMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        this.waterNavigation = new SwimNavigation(this, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.TRIDENT));
    }
    public static DefaultAttributeContainer.Builder createNarranycliusAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 5.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    private void initCustomGoals() {
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(2, new HornBladeAttackGoal(this, 1.0, 30, 20.0f));
        this.goalSelector.add(3, new NarranycliusMeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new TargetAboveWaterGoal(this, 1.0, this.getWorld().getSeaLevel()));

        this.targetSelector.add(1, new RevengeGoal(this, NarranycliusEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::canAttackTarget));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AxolotlEntity.class, true, false));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world,difficulty, spawnReason, entityData);
        return entityData;
    }
    public static boolean canSpawn(EntityType<NarranycliusEntity> type, ServerWorldAccess worldAccess, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (!worldAccess.getFluidState(pos.down()).isIn(FluidTags.WATER) && !SpawnReason.isAnySpawner(spawnReason)) {
            return false;
        } else {
            RegistryEntry<Biome> registryEntry = worldAccess.getBiome(pos);
            boolean bl = worldAccess.getDifficulty() != Difficulty.PEACEFUL
                    && (SpawnReason.isTrialSpawner(spawnReason) || isSpawnDark(worldAccess, pos, random))
                    && (SpawnReason.isAnySpawner(spawnReason) || worldAccess.getFluidState(pos).isIn(FluidTags.WATER));
            if (bl && SpawnReason.isAnySpawner(spawnReason)) {
                return true;
            } else {
                return registryEntry.isIn(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)
                        ? random.nextInt(15) == 0 && bl
                        : random.nextInt(40) == 0 && isValidSpawnDepth(worldAccess, pos) && bl;
            }
        }
    }

    private static boolean isValidSpawnDepth(ServerWorldAccess worldAccess, BlockPos pos) {
        return pos.getY() < worldAccess.getSeaLevel() - 5;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMAN_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }
    public boolean canAttackTarget(@Nullable LivingEntity target) {
        return target != null;
    }

    @Override
    public boolean isPushedByFluids() {
        return !this.isSwimming();
    }
    public boolean isTargeting() {
        if (this.targeting) {
            return true;
        } else {
            LivingEntity livingEntity = this.getTarget();
            return livingEntity != null;
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater() && this.isTargeting()) {
            this.updateVelocity(0.01F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(1.3));
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public void updateSwimming() {
        this.setSwimming(true);
    }

    public boolean hasFinishedCurrentPath() {
        Path path = this.getNavigation().getCurrentPath();
        if (path != null) {
            BlockPos blockPos = path.getTarget();
            if (blockPos != null) {
                double d = this.squaredDistanceTo((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
                return d < 4.0;
            }
        }
        return false;
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        TridentEntity tridentEntity = new TridentEntity(this.getWorld(), this, new ItemStack(Items.TRIDENT)); //todo change projectile to horn blade
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - tridentEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        tridentEntity.setVelocity(d, e + g * 0.2F, f, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(tridentEntity);
    }
    public void setTargeting(boolean targeting) {
        this.targeting = targeting;
    }

    public static class NarranycliusMeleeAttackGoal extends MeleeAttackGoal {
        private final NarranycliusEntity narranycliusEntity;
        public NarranycliusMeleeAttackGoal(NarranycliusEntity narranycliusEntity, double speed, boolean pauseWhenMobIdle) {
            super(narranycliusEntity, speed, pauseWhenMobIdle);
            this.narranycliusEntity = narranycliusEntity;
        }

        @Override
        public boolean canStart() {
            return super.canStart() &&
                    this.narranycliusEntity.canAttackTarget(this.narranycliusEntity.getTarget()) &&
                    (this.narranycliusEntity.distanceTo(this.narranycliusEntity.getTarget()) < 10.0f);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() &&
                    this.narranycliusEntity.canAttackTarget(this.narranycliusEntity.getTarget()) &&
                    (this.narranycliusEntity.distanceTo(this.narranycliusEntity.getTarget()) < 10.0f);
        }
    }

    public static class NarranycliusMoveControl extends MoveControl {
        private final NarranycliusEntity narranycliusEntity;
        public NarranycliusMoveControl(NarranycliusEntity narranycliusEntity) {
            super(narranycliusEntity);
            this.narranycliusEntity = narranycliusEntity;
        }

        @Override
        public void tick() {
            LivingEntity target = this.narranycliusEntity.getTarget();
            if (this.narranycliusEntity.isTargeting() && this.narranycliusEntity.isTouchingWater()) {
                if (target != null && target.getY() > this.narranycliusEntity.getY() || this.narranycliusEntity.targeting) {
                    this.narranycliusEntity.setVelocity(this.narranycliusEntity.getVelocity().add(0.0, 0.1, 0.0));
                }

                if (this.state != MoveControl.State.MOVE_TO || this.narranycliusEntity.getNavigation().isIdle()) {
                    this.narranycliusEntity.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.narranycliusEntity.getX();
                double e = this.targetY - this.narranycliusEntity.getY();
                double f = this.targetZ - this.narranycliusEntity.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F;
                this.narranycliusEntity.setYaw(this.wrapDegrees(this.narranycliusEntity.getYaw(), h, 90.0F));
                this.narranycliusEntity.bodyYaw = this.narranycliusEntity.getYaw();
                float i = (float)(this.speed * this.narranycliusEntity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.narranycliusEntity.getMovementSpeed(), i);
                this.narranycliusEntity.setMovementSpeed(j);
                this.narranycliusEntity.setVelocity(this.narranycliusEntity.getVelocity().add((double)j * d * 0.01, (double)j * e * 0.2, (double)j * f * 0.01));
            } else {
                if (!this.narranycliusEntity.isOnGround()) {
                    this.narranycliusEntity.setVelocity(this.narranycliusEntity.getVelocity().add(0.0, -0.016, 0.0));
                }
                super.tick();
            }
        }
    }

    static class TargetAboveWaterGoal extends Goal {
        private final NarranycliusEntity narranycliusEntity;
        private final double speed;
        private final int minY;
        private boolean foundTarget;

        public TargetAboveWaterGoal(NarranycliusEntity narranycliusEntity, double speed, int minY) {
            this.narranycliusEntity = narranycliusEntity;
            this.speed = speed;
            this.minY = minY;
        }

        @Override
        public boolean canStart() {
            return this.narranycliusEntity.isTouchingWater() && this.narranycliusEntity.getY() < (double)(this.minY - 2);
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart() && !this.foundTarget;
        }

        @Override
        public void tick() {
            if (this.narranycliusEntity.getY() < (double)(this.minY - 1) && (this.narranycliusEntity.getNavigation().isIdle() || this.narranycliusEntity.hasFinishedCurrentPath())) {
                Vec3d vec3d = NoPenaltyTargeting.findTo(
                        this.narranycliusEntity, 8, 16, new Vec3d(this.narranycliusEntity.getX(), (double)(this.minY - 1), this.narranycliusEntity.getZ()), (float) (Math.PI / 2)
                );
                if (vec3d == null) {
                    this.foundTarget = true;
                    return;
                }

                this.narranycliusEntity.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }

        @Override
        public void start() {
            this.narranycliusEntity.setTargeting(true);
            this.foundTarget = false;
        }

        @Override
        public void stop() {
            this.narranycliusEntity.setTargeting(false);
        }
    }

    public static class HornBladeAttackGoal extends ProjectileAttackGoal {
        private final NarranycliusEntity narranycliusEntity;

        public HornBladeAttackGoal(RangedAttackMob rangedAttackMob, double mobSpeed, int intervalTicks, float maxShootRange) {
            super(rangedAttackMob, mobSpeed, intervalTicks, maxShootRange);
            this.narranycliusEntity = (NarranycliusEntity) rangedAttackMob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.narranycliusEntity.getMainHandStack().isOf(Items.TRIDENT) && (this.narranycliusEntity.distanceTo(this.narranycliusEntity.getTarget()) >= 10.0f); //todo change
        }

        @Override
        public void start() {
            super.start();
            this.narranycliusEntity.setAttacking(true);
            this.narranycliusEntity.setCurrentHand(Hand.MAIN_HAND);
        }

        @Override
        public void stop() {
            super.stop();
            this.narranycliusEntity.clearActiveItem();
            this.narranycliusEntity.setAttacking(false);
        }
    }

}
