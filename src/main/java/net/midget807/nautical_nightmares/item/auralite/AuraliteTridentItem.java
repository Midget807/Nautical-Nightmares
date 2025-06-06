package net.midget807.nautical_nightmares.item.auralite;

import net.midget807.nautical_nightmares.entity.projectile.AuraliteTridentEntity;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class AuraliteTridentItem extends TridentItem {
    public AuraliteTridentItem(Item.Settings settings) {
        super(settings);
    }
    public static AttributeModifiersComponent createAttributes() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 10.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -3.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }
    public static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(), 1.0F, 2);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i >= 10) {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity);
                if (!(f > 0.0F) || playerEntity.isTouchingWaterOrRain()) {
                    if (!isAboutToBreak(stack)) {
                        RegistryEntry<SoundEvent> registryEntry = (RegistryEntry<SoundEvent>)EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND)
                                .orElse(SoundEvents.ITEM_TRIDENT_THROW);
                        if (!world.isClient) {
                            stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
                            if (f == 0.0F) {
                                AuraliteTridentEntity tridentEntity = new AuraliteTridentEntity(world, playerEntity, stack);
                                tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
                                if (playerEntity.isInCreativeMode()) {
                                    tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                }

                                world.spawnEntity(tridentEntity);
                                world.playSoundFromEntity(null, tridentEntity, registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                                if (!playerEntity.isInCreativeMode()) {
                                    playerEntity.getInventory().removeOne(stack);
                                }
                            }
                        }

                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                        if (f > 0.0F) {
                            float g = playerEntity.getYaw();
                            float h = playerEntity.getPitch();
                            float j = -MathHelper.sin(g * (float) (Math.PI / 180.0)) * MathHelper.cos(h * (float) (Math.PI / 180.0));
                            float k = -MathHelper.sin(h * (float) (Math.PI / 180.0));
                            float l = MathHelper.cos(g * (float) (Math.PI / 180.0)) * MathHelper.cos(h * (float) (Math.PI / 180.0));
                            float m = MathHelper.sqrt(j * j + k * k + l * l);
                            j *= f / m;
                            k *= f / m;
                            l *= f / m;
                            playerEntity.addVelocity((double)j, (double)k, (double)l);
                            playerEntity.useRiptide(20, 8.0F, stack);
                            if (playerEntity.isOnGround()) {
                                float n = 1.1999999F;
                                playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999F, 0.0));
                            }

                            world.playSoundFromEntity(null, playerEntity, registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }
    private static boolean isAboutToBreak(ItemStack stack) {
        return stack.getDamage() >= stack.getMaxDamage() - 1;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        AuraliteTridentEntity tridentEntity = new AuraliteTridentEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return tridentEntity;
    }
}
