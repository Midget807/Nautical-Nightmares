package net.midget807.nautical_nightmares.item.auralite;

import net.midget807.nautical_nightmares.datagen.ModItemTagProvider;
import net.midget807.nautical_nightmares.entity.projectile.WaterJetEntity;
import net.midget807.nautical_nightmares.registry.ModParticles;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class AuraliteSwordItem extends SwordItem {
    private int ticking = 0;

    public AuraliteSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings.component(DataComponentTypes.TOOL, createToolComponent()));
    }
    private static ToolComponent createToolComponent() {
        return new ToolComponent(
                List.of(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0F), ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2
        );
    }

    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)((float)baseAttackDamage + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && player.getActiveHand() == Hand.MAIN_HAND) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            float f = getPullProgress(i);
            if (!((double) f < 0.2)) {
                if (world instanceof ServerWorld serverWorld) {
                    this.spray(player, world, f);
                    player.setCurrentHand(player.getActiveHand());
                    player.swingHand(player.getActiveHand());
                    //player.getItemCooldownManager().set(stack.getItem(), 150);
                }
                world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_GENERIC_SPLASH,
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f
                );
                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
    }


    private void spray(LivingEntity shooter, World world, float pullProgress) {
        WaterJetEntity waterJetEntity = new WaterJetEntity(shooter, world);
        waterJetEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0f, 1.5f * pullProgress, 0.5f);
        waterJetEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        world.spawnEntity(waterJetEntity);
    }

    public float getPullProgress(int useTicks) {
        float f = (float) useTicks / 20.0f;
        f = (f * f + f * 2.0f) / 1.5f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user.getActiveHand() == Hand.MAIN_HAND) {
            float pullProgress = getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks);
            Vec3d start = user.getPos().add((double) 0.0F, (double) (user.getHeight() / 2.0F), (double) 0.0F);
            Vec3d rotation = user.getRotationVec(1.0F);
            Vec3d perp1 = rotation.crossProduct(new Vec3d((double) 0.0F, (double) 1.0F, (double) 0.0F)).normalize();
            Vec3d perp2 = rotation.crossProduct(perp1).normalize();
            if (pullProgress < 1.0f) {
                for (int i = 0; i < 10; ++i) {
                    double distance = (double) (4 * (i + 1)) - (double) pullProgress / (double) 120.0F % (double) 8.0F;
                    double angle = 0.015707962916848627 * (double) pullProgress + (Math.PI / 2D) * (double) i;
                    Vec3d particlePos = start.add(rotation.multiply(distance)).add(perp1.multiply(Math.sin(angle) * (double) 1.6F)).add(perp2.multiply(Math.cos(angle) * (double) 1.6F));
                    start.add(rotation.multiply(distance));
                    Vec3d velocity = particlePos.subtract(start).normalize().multiply((double) 2.0F);
                    user.getWorld().addParticle(ModParticles.WATER_CHARGE, particlePos.getX(), particlePos.getY(), particlePos.getZ(), -velocity.x, -velocity.y, -velocity.z);
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        if (user.getActiveHand() == Hand.MAIN_HAND) {
            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && player.isWet()) {
            ItemStack mainHandStack = ItemStack.EMPTY;
            ItemStack offHandStack = ItemStack.EMPTY;
            if (player.getStackInHand(Hand.MAIN_HAND).isIn(ModItemTagProvider.AURALITE_ITEMS)) {
                mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
            }
            if (player.getStackInHand(Hand.OFF_HAND).isIn(ModItemTagProvider.AURALITE_ITEMS)) {
                offHandStack = player.getStackInHand(Hand.OFF_HAND);
            }
            if (this.ticking % 10 == 0) {
                if (!mainHandStack.isEmpty()) {
                    mainHandStack.setDamage(mainHandStack.getDamage() - 1);
                }
                if (!offHandStack.isEmpty()) {
                    mainHandStack.setDamage(mainHandStack.getDamage() - 1);
                }
            }
            if (this.ticking >= 20) {
                this.ticking = 0;
            } else {
                this.ticking++;
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
