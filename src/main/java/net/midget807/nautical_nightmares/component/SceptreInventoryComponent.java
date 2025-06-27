package net.midget807.nautical_nightmares.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class SceptreInventoryComponent {
    public static final SceptreInventoryComponent DEFAULT = new SceptreInventoryComponent(DefaultedList.of());
    public static final Codec<SceptreInventoryComponent> CODEC = ItemStack.CODEC
            .listOf()
            .xmap(SceptreInventoryComponent::new, component -> component.inventory);
    public static final PacketCodec<RegistryByteBuf, SceptreInventoryComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(SceptreInventoryComponent::new, component -> component.inventory);
    private List<ItemStack> inventory;

    private SceptreInventoryComponent(List<ItemStack> inventory) {
        this.inventory = inventory;
    }
    public DefaultedList<ItemStack> getInventory() {
        DefaultedList<ItemStack> list = DefaultedList.of();
        list.addAll(this.inventory);
        return list;
    }
    public void add(ItemStack stack) {
        this.getInventory().add(stack);
    }
    public void add(ItemStack stack, int index) {
        this.getInventory().add(index, stack);
    }
    public void remove(int index) {
        this.getInventory().remove(index);
    }
    public boolean contains(Item item) {
        for (ItemStack itemStack : this.getInventory()) {
            if (itemStack.isOf(item)) {
                return true;
            }
        }
        return false;
    }
    public boolean isEmpty() {
        return this.getInventory().isEmpty();
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o instanceof SceptreInventoryComponent component && ItemStack.stacksEqual(this.inventory, component.inventory);
        }
    }

    @Override
    public String toString() {
        return "SceptreInventory[items=" + this.getInventory() + "]";
    }
}
