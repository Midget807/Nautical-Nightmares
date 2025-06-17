package net.midget807.nautical_nightmares.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.nautical_nightmares.component.SceptreInventoryComponent;
import net.midget807.nautical_nightmares.datagen.ModItemTagProvider;
import net.midget807.nautical_nightmares.registry.ModDataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SeaSceptreItem extends Item {
    public boolean charged;
    public List<ItemStack> requirements = List.of(
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.HEART_OF_THE_SEA),
            new ItemStack(Items.EMERALD),
            new ItemStack(Items.PRISMARINE_CRYSTALS),
            new ItemStack(Items.NAUTILUS_SHELL),
            new ItemStack(Items.PRISMARINE_SHARD)
    );
    private int ticking = 0;

    public SeaSceptreItem(Settings settings) {
        super(settings);
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack handStack = user.getStackInHand(hand);
        SceptreInventoryComponent sceptreInventoryComponent = handStack.get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        if ((sceptreInventoryComponent != null && !sceptreInventoryComponent.isEmpty()) || user.getAbilities().creativeMode) {
            this.pacify(world, user);
            return TypedActionResult.consume(handStack);
        } else if (this.getIngredientType(handStack, user)) {
            this.charged = false;
            user.setCurrentHand(hand);
            return TypedActionResult.consume(handStack);
        } else {
            return TypedActionResult.fail(handStack);
        }
    }

    private boolean getIngredientType(ItemStack handStack, PlayerEntity user) {
        if (!(handStack.getItem() instanceof SeaSceptreItem)) {
            return false;
        } else {
            boolean hasFoundValidIngredient = false;
            Predicate<ItemStack> predicate = getIngredients();
            ItemStack itemStack = getHeldIngredient(user, predicate);
            if (!itemStack.isEmpty()) {
                ItemStack itemStack2 = itemStack.copyWithCount(1);
                loadIngredient(itemStack2, handStack);
                hasFoundValidIngredient = true;
            }
            if (itemStack.isEmpty()) {
                predicate = ((SeaSceptreItem) handStack.getItem()).getIngredients();
                for (int i = 0; i < user.getInventory().size(); i++) {
                    ItemStack itemStack2 = user.getInventory().getStack(i);
                    if (predicate.test(itemStack2)) {
                        hasFoundValidIngredient = true;
                    }
                }
            }
            return hasFoundValidIngredient;
        }
    }

    private ItemStack getHeldIngredient(LivingEntity user, Predicate<ItemStack> predicate) {
        if (predicate.test(user.getStackInHand(Hand.OFF_HAND))) {
            return user.getStackInHand(Hand.OFF_HAND);
        } else {
            return predicate.test(user.getStackInHand(Hand.MAIN_HAND)) ? user.getStackInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    private Predicate<ItemStack> getIngredients() {
        return stack -> stack.equals(this.requirements.get(0)) ||
                stack.equals(this.requirements.get(1)) ||
                stack.equals(this.requirements.get(2)) ||
                stack.equals(this.requirements.get(3)) ||
                stack.equals(this.requirements.get(4)) ||
                stack.equals(this.requirements.get(5));
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, user) * 20 - remainingUseTicks;
        float f = getPullProgress(i, stack, user);
        if (f >= 1.0f && user instanceof PlayerEntity player && stack.getItem() instanceof SeaSceptreItem && !isFull(stack)) {
            loadIngredients(player); //todo add use action for coolness and shit
        }

    }

    public boolean isFull(ItemStack stack) {
        SceptreInventoryComponent sceptreInventoryComponent = stack.get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        if (sceptreInventoryComponent == null) {
            return false;
        }
        return sceptreInventoryComponent.getInventory().size() < this.requirements.size();
    }

    public static void loadIngredient(ItemStack stack, ItemStack sceptre) {
        SceptreInventoryComponent sceptreInventoryComponent = sceptre.get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        if (sceptreInventoryComponent != null && !sceptreInventoryComponent.contains(stack.getItem())) {
            sceptreInventoryComponent.add(stack);
        }
    }

    public static void loadIngredient(ItemStack stack, SceptreInventoryComponent sceptreInventoryComponent) {
        if (sceptreInventoryComponent != null && !sceptreInventoryComponent.contains(stack.getItem())) {
            sceptreInventoryComponent.add(stack);
        }
    }

    public void loadIngredients(PlayerEntity user) {
        SceptreInventoryComponent sceptreInventoryComponent = user.getActiveItem().get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        if (sceptreInventoryComponent == null) {
            return;
        }
        Predicate<ItemStack> predicate = this.getIngredients();
        for (int i = 0; i < user.getInventory().size(); i++) {
            ItemStack itemStack = user.getInventory().getStack(i);
            if (predicate.test(itemStack) && sceptreInventoryComponent.getInventory().contains(itemStack)) {
                loadIngredient(itemStack, sceptreInventoryComponent);
            }
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        SceptreInventoryComponent sceptreInventoryComponent = stack.get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        if (!world.isClient && sceptreInventoryComponent != null) {
            int i = this.getMaxUseTime(stack, user) * 20 - remainingUseTicks;
            float f = getPullProgress(i, stack, user);
            SeaSceptreItem.ChargingSound chargingSound = this.getLoadingSounds(stack);
            if (f < 0.2F) {
                this.charged = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                chargingSound.start()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F) {
                chargingSound.mid()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 1.0F) {
                chargingSound.end()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), user.getSoundCategory(), 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 5;
    }

    private SeaSceptreItem.ChargingSound getLoadingSounds(ItemStack stack) {
        return new ChargingSound(
                Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
                Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
                Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
        );
    }

    private float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float)useTicks / getMaxUseTime(stack, user) * 20;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private void pacify(World world, PlayerEntity user) {
        this.charged = false;
        user.sendMessage(Text.literal("pacify"), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        SceptreInventoryComponent sceptreInventoryComponent = stack.get(ModDataComponentTypes.SCEPTRE_INVENTORY);
        assert sceptreInventoryComponent != null;
        List<ItemStack> list = sceptreInventoryComponent.getInventory();
        tooltip.add(Text.literal("Contains: "));
        for (ItemStack itemStack : this.requirements) {
            tooltip.add(Text.literal("- ").append(itemStack.getItem().getName()).formatted(list.contains(itemStack) ? Formatting.GRAY : Formatting.DARK_GRAY));
        }

        super.appendTooltip(stack, context, tooltip, type);
    }

    public static record ChargingSound(Optional<RegistryEntry<SoundEvent>> start, Optional<RegistryEntry<SoundEvent>> mid, Optional<RegistryEntry<SoundEvent>> end) {
        public static final Codec<SeaSceptreItem.ChargingSound> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        SoundEvent.ENTRY_CODEC.optionalFieldOf("start").forGetter(SeaSceptreItem.ChargingSound::start),
                        SoundEvent.ENTRY_CODEC.optionalFieldOf("mid").forGetter(SeaSceptreItem.ChargingSound::mid),
                        SoundEvent.ENTRY_CODEC.optionalFieldOf("end").forGetter(SeaSceptreItem.ChargingSound::end)
                ).apply(instance, SeaSceptreItem.ChargingSound::new)
        );
    }
}
