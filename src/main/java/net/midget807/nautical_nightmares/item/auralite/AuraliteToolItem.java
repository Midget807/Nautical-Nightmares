package net.midget807.nautical_nightmares.item.auralite;

import net.midget807.nautical_nightmares.datagen.ModItemTagProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AuraliteToolItem extends ToolItem {
    private int ticking = 0;
    public AuraliteToolItem(ToolMaterial material, Settings settings) {
        super(material, settings);
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
