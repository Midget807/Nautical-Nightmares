package net.midget807.nautical_nightmares.screen;

import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.midget807.nautical_nightmares.recipe.ForgingRecipeInput;
import net.midget807.nautical_nightmares.registry.ModRecipes;
import net.midget807.nautical_nightmares.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    public final PropertyDelegate propertyDelegate;
    public final World world;
    private final List<Integer> inputSlotIndices;
    private final List<Integer> outputSlotIndices;
    @Nullable
    private RecipeEntry<ForgingRecipe> currentRecipe;
    private final List<RecipeEntry<ForgingRecipe>> recipes;

    public ForgeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.FORGE_SCREEN_HANDLER, syncId);
        this.world = playerInventory.player.getWorld();
        this.recipes = this.world.getRecipeManager().listAllOfType(ModRecipes.FORGING_TYPE);
        this.checkSize(inventory, 5);
        this.checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        ForgeSlotsManager forgeSlotsManager = this.getForgeSlotsManager();
        this.inputSlotIndices = forgeSlotsManager.getInputSlotIndices();
        this.outputSlotIndices = forgeSlotsManager.getOutputSlotIndices();
        this.addPlayerInventorySlots(playerInventory);
        this.addInputSlots(forgeSlotsManager);
        this.addOutputSlots(forgeSlotsManager);

    }

    public ForgeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), new ArrayPropertyDelegate(4));
    }

    private ForgeSlotsManager getForgeSlotsManager() {
        return ForgeSlotsManager.create()
                .input(0, 8, 48, stack -> this.recipes.stream().anyMatch(recipe -> ((ForgingRecipe)recipe.value()).item1.test(stack)))
                .input(1, 26, 48, stack -> this.recipes.stream().anyMatch(recipe -> ((ForgingRecipe)recipe.value()).item2.test(stack)))
                .input(2, 44, 48, stack -> this.recipes.stream().anyMatch(recipe -> ((ForgingRecipe)recipe.value()).fuel.test(stack)))
                .output(3, 98, 48)
                .output(4, 98, 70)
                .build();
    }

    private void addInputSlots(ForgeSlotsManager forgeSlotsManager) {
        for (final ForgeSlotsManager.ForgingSlot forgingSlot : forgeSlotsManager.getInputSlots()) {
            this.addSlot(new Slot(this.inventory, forgingSlot.slotId(), forgingSlot.x(), forgingSlot.y()) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return forgingSlot.mayPlace().test(stack);
                }
            });
        }
    }

    private void addOutputSlots(ForgeSlotsManager forgeSlotsManager) {
        for (final ForgeSlotsManager.ForgingSlot forgingSlot : forgeSlotsManager.getInputSlots()) {
            this.addSlot(new Slot(this.inventory, forgingSlot.slotId(), forgingSlot.x(), forgingSlot.y()) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }
                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    return ForgeScreenHandler.this.canTakeOutput(playerEntity, this.hasStack());
                }

                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    ForgeScreenHandler.this.onTakeOutput(player, stack);
                }
            });
        }
    }

    private void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player.getWorld(), player, stack.getCount());
        this.decrementStack(0);
        this.decrementStack(1);
        this.decrementStack(2);
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        if (!itemStack.isEmpty()) {
            itemStack.decrement(1);
            this.inventory.setStack(slot, itemStack);
        }
    }

    private boolean canTakeOutput(PlayerEntity playerEntity, boolean b) {
        return this.currentRecipe != null && this.currentRecipe.value().matches(this.createRecipeInput(), this.world);
    }

    private void addPlayerInventorySlots(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        this.updateResult();
    }

    private void updateResult() {
        ForgingRecipeInput forgingRecipeInput = this.createRecipeInput();
        List<RecipeEntry<ForgingRecipe>> list = this.world.getRecipeManager().getAllMatches(ModRecipes.FORGING_TYPE, forgingRecipeInput, this.world);
        if (list.isEmpty()) {
            this.inventory.setStack(4, ItemStack.EMPTY);
        } else {
            RecipeEntry<ForgingRecipe> recipeEntry = (RecipeEntry<ForgingRecipe>)list.get(0);
            ItemStack itemStack = recipeEntry.value().craft(forgingRecipeInput, this.world.getRegistryManager());
            if (itemStack.isItemEnabled(this.world.getEnabledFeatures())) {
                this.currentRecipe = recipeEntry;
                this.inventory.setStack(4, itemStack);
            }
        }
    }

    private ForgingRecipeInput createRecipeInput() {
        return new ForgingRecipeInput(this.inventory.getStack(0), this.inventory.getStack(1), this.inventory.getStack(2));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            int i = this.getPlayerInventoryStartIndex();
            int j = this.getPlayerHotbarEndIndex();
            if (slot == this.getResultSlotIndex(false)) {
                if (!this.insertItem(itemStack2, i, j, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (this.inputSlotIndices.contains(slot)) {
                if (!this.insertItem(itemStack2, i, j, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.isValidIngredient(itemStack2) && slot >= this.getPlayerInventoryStartIndex() && slot < this.getPlayerHotbarEndIndex()) {
                int k = this.getSlotFor(itemStack);
                if (!this.insertItem(itemStack2, k, this.getResultSlotIndex(true), false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.insertItem(itemStack2, k, this.getResultSlotIndex(false), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot >= this.getPlayerInventoryStartIndex() && slot < this.getPlayerInventoryEndIndex()) {
                if (!this.insertItem(itemStack2, this.getPlayerHotbarStartIndex(), this.getPlayerHotbarEndIndex(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot >= this.getPlayerHotbarStartIndex()
                    && slot < this.getPlayerHotbarEndIndex()
                    && !this.insertItem(itemStack2, this.getPlayerInventoryStartIndex(), this.getPlayerInventoryEndIndex(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    private int getSlotFor(ItemStack itemStack) {
        return this.inventory.isEmpty() ? 0 : this.inputSlotIndices.get(0);
    }

    private boolean isValidIngredient(ItemStack stack) {
        return true;
    }

    private int getResultSlotIndex(boolean fuel) {
        return fuel ? this.outputSlotIndices.get(0) : this.outputSlotIndices.get(1);
    }

    private int getPlayerHotbarStartIndex() {
        return this.getPlayerInventoryEndIndex();
    }

    private int getPlayerInventoryEndIndex() {
        return this.getPlayerInventoryStartIndex() + 27;
    }

    private int getPlayerHotbarEndIndex() {
        return getPlayerHotbarStartIndex() + 9;
    }

    private int getPlayerInventoryStartIndex() {
        return getResultSlotIndex(false) + 1;
    }

}
