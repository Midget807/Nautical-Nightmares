package net.midget807.nautical_nightmares.screen;

import net.midget807.nautical_nightmares.block.entity.ForgeBlockEntity;
import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.midget807.nautical_nightmares.recipe.ForgingRecipeInput;
import net.midget807.nautical_nightmares.registry.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ForgeScreenHandler extends AbstractRecipeScreenHandler<ForgingRecipeInput, ForgingRecipe> {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int FUEL_SLOT = 2;
    public static final int CATALYST_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;
    public final Inventory inventory;
    public final PropertyDelegate propertyDelegate;
    public final World world;


    public ForgeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.FORGE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 5);
        checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getWorld();

        this.addSlot(new Slot(inventory, INPUT_SLOT_1, 44, 17));
        this.addSlot(new Slot(inventory, INPUT_SLOT_2, 68, 17));
        this.addSlot(new Slot(inventory, FUEL_SLOT, 44, 53));
        this.addSlot(new Slot(inventory, CATALYST_SLOT, 68, 53));
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, OUTPUT_SLOT, 116, 35));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperties(propertyDelegate);
    }

    public ForgeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), new ArrayPropertyDelegate(4));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.inventory).provideRecipeInputs(finder);
        }
    }

    @Override
    public void clearCraftingSlots() {
        this.getSlot(INPUT_SLOT_1).setStackNoCallbacks(ItemStack.EMPTY);
        this.getSlot(INPUT_SLOT_2).setStackNoCallbacks(ItemStack.EMPTY);
        this.getSlot(FUEL_SLOT).setStackNoCallbacks(ItemStack.EMPTY);
        this.getSlot(OUTPUT_SLOT).setStackNoCallbacks(ItemStack.EMPTY);
    }

    @Override
    public boolean matches(RecipeEntry<ForgingRecipe> recipe) {
        return recipe.value().matches(new ForgingRecipeInput(this.inventory.getStack(INPUT_SLOT_1), this.inventory.getStack(INPUT_SLOT_2), this.inventory.getStack(FUEL_SLOT), this.inventory.getStack(CATALYST_SLOT)), this.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return OUTPUT_SLOT;
    }

    @Override
    public int getCraftingWidth() {
        return 2;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 3;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.BLAST_FURNACE;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != OUTPUT_SLOT;
    }

    public boolean isBurning() {
        return this.propertyDelegate.get(ForgeBlockEntity.BURN_TIME_PROPERTY_INDEX) > 0;
    }

    public float getFuelProgress() {
        int i = propertyDelegate.get(ForgeBlockEntity.FUEL_TIME_PROPERTY_INDEX);
        return MathHelper.clamp((float) this.propertyDelegate.get(ForgeBlockEntity.BURN_TIME_PROPERTY_INDEX) / (float) i, 0.0f, 1.0f);
    }

    public float getCookProgress() {
        int i = this.propertyDelegate.get(ForgeBlockEntity.COOK_TIME_PROPERTY_INDEX);
        int j = this.propertyDelegate.get(ForgeBlockEntity.COOK_TIME_TOTAL_PROPERTY_INDEX);
        return j != 0 && i != 0 ? MathHelper.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }
}
