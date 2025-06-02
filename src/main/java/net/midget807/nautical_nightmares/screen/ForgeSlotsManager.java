package net.midget807.nautical_nightmares.screen;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ForgeSlotsManager {
    private final List<ForgeSlotsManager.ForgingSlot> inputSlots;
    private final List<ForgeSlotsManager.ForgingSlot> outputSlots;

    ForgeSlotsManager(List<ForgeSlotsManager.ForgingSlot> inputSlots, List<ForgeSlotsManager.ForgingSlot> outputSlots) {
        if (!inputSlots.isEmpty() && !outputSlots.isEmpty()
        ) {
            this.inputSlots = inputSlots;
            this.outputSlots = outputSlots;
        } else {
            throw new IllegalArgumentException("Need to define both inputSlots and outputSlots");
        }
    }

    public static ForgeSlotsManager.Builder create() {
        return new ForgeSlotsManager.Builder();
    }

    public boolean hasInputSlotIndex(int index) {
        return this.inputSlots.size() >= index;
    }
    public boolean hasOutputSlotIndex(int index) {
        return this.outputSlots.size() >= index;
    }

    public ForgeSlotsManager.ForgingSlot getInputSlot(int index) {
        return this.inputSlots.get(index);
    }

    public ForgeSlotsManager.ForgingSlot getOutputSlot(int index) {
        return this.outputSlots.get(index);
    }

    public List<ForgeSlotsManager.ForgingSlot> getInputSlots() {
        return this.inputSlots;
    }
    public List<ForgeSlotsManager.ForgingSlot> getOutputSlots() {
        return this.inputSlots;
    }

    public int getInputSlotCount() {
        return this.inputSlots.size();
    }
    public int getOutputSlotCount() {
        return this.inputSlots.size();
    }


    public List<Integer> getInputSlotIndices() {
        return (List<Integer>)this.inputSlots.stream().map(ForgeSlotsManager.ForgingSlot::slotId).collect(Collectors.toList());
    }
    public List<Integer> getOutputSlotIndices() {
        return (List<Integer>)this.outputSlots.stream().map(ForgeSlotsManager.ForgingSlot::slotId).collect(Collectors.toList());
    }

    public static class Builder {
        private final List<ForgeSlotsManager.ForgingSlot> inputSlots = new ArrayList();
        private List<ForgeSlotsManager.ForgingSlot> outputSlots = new ArrayList();

        public ForgeSlotsManager.Builder input(int slotId, int x, int y, Predicate<ItemStack> mayPlace) {
            this.inputSlots.add(new ForgeSlotsManager.ForgingSlot(slotId, x, y, mayPlace));
            return this;
        }

        public ForgeSlotsManager.Builder output(int slotId, int x, int y) {
            this.outputSlots.add(new ForgeSlotsManager.ForgingSlot(slotId, x, y, stack -> false));
            return this;
        }

        public ForgeSlotsManager build() {
            return new ForgeSlotsManager(this.inputSlots, this.outputSlots);
        }
    }

    public static record ForgingSlot(int slotId, int x, int y, Predicate<ItemStack> mayPlace) {
        static final ForgeSlotsManager.ForgingSlot DEFAULT = new ForgeSlotsManager.ForgingSlot(0, 0, 0, stack -> true);
    }
}
