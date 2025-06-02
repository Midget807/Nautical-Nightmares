package net.midget807.nautical_nightmares.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.midget807.nautical_nightmares.block.ForgeBlock;
import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.midget807.nautical_nightmares.recipe.ForgingRecipeInput;
import net.midget807.nautical_nightmares.registry.ModBlockEntities;
import net.midget807.nautical_nightmares.registry.ModRecipes;
import net.midget807.nautical_nightmares.screen.ForgeScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.canUseAsFuel;

public class ForgeBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int FUEL_INPUT_SLOT = 2;
    public static final int FUEL_OUTPUT_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int[] TOP_SLOTS = new int[]{0, 1};
    public static final int[] SIDE_SLOTS = new int[]{2};
    public static final int[] BOTTOM_SLOTS = new int[]{3, 4};
    public static final int BURN_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_PROPERTY_INDEX = 2;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 3;
    public static final int PROPERTY_COUNT = 4;
    public static final int DEFAULT_COOK_TIME = 600;
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    int burnTime;
    int fuelTime;
    int cookTime;
    int cookTimeTotal;
    @Nullable
    public static volatile Map<Item, Integer> fuelTimes;
    public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return ForgeBlockEntity.this.burnTime;
                case 1:
                    return ForgeBlockEntity.this.fuelTime;
                case 2:
                    return ForgeBlockEntity.this.cookTime;
                case 3:
                    return ForgeBlockEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    ForgeBlockEntity.this.burnTime = value;
                    break;
                case 1:
                    ForgeBlockEntity.this.fuelTime = value;
                    break;
                case 2:
                    ForgeBlockEntity.this.cookTime = value;
                    break;
                case 3:
                    ForgeBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };
    public final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<>();
    public final RecipeManager.MatchGetter<ForgingRecipeInput, ForgingRecipe> matchGetter;


    public ForgeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FORGE_BLOCK_ENTITY, blockPos, blockState);
        this.matchGetter = RecipeManager.createCachedMatchGetter(ModRecipes.FORGING_TYPE);
    }
    public static void clearFuelTimes() {
        fuelTimes = null;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.nautical_nightmares.forge");
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ForgeScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }


    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        } else {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && (slot == FUEL_OUTPUT_SLOT || slot == OUTPUT_SLOT) ? stack.isOf(Items.BUCKET) : true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
        this.inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        if (slot == 0 && !bl) {
            this.cookTimeTotal = getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    private static int getCookTime(World world, ForgeBlockEntity forgeBlockEntity) {
        ForgingRecipeInput forgingRecipeInput = new ForgingRecipeInput(forgeBlockEntity.getStack(INPUT_SLOT_1), forgeBlockEntity.getStack(INPUT_SLOT_2), forgeBlockEntity.getStack(FUEL_INPUT_SLOT));
        return (Integer) forgeBlockEntity.matchGetter
                .getFirstMatch(forgingRecipeInput, world)
                .map(recipe -> recipe.value().getCookingTime())
                .orElse(600);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == OUTPUT_SLOT || slot == FUEL_OUTPUT_SLOT) {
            return false;
        } else if (slot != FUEL_INPUT_SLOT) {
            return true;
        } else {
            ItemStack itemStack = this.inventory.get(FUEL_INPUT_SLOT);
            return canUseAsFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }

    @Override
    public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.id();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    @Override
    public @Nullable RecipeEntry<?> getLastRecipe() {
        return null;
    }

    @Override
    public void unlockLastRecipe(PlayerEntity player, List<ItemStack> ingredients) {

    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<RecipeEntry<?>> list = this.getRecipesUsedAndDropExperience(player.getServerWorld(), player.getPos());
        player.unlockRecipes(list);

        for (RecipeEntry<?> recipeEntry : list) {
            if (recipeEntry != null) {
                player.onRecipeCrafted(recipeEntry, this.inventory);
            }
        }

        this.recipesUsed.clear();
    }
    public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        List<RecipeEntry<?>> list = Lists.<RecipeEntry<?>>newArrayList();

        for (Object2IntMap.Entry<Identifier> entry : this.recipesUsed.object2IntEntrySet()) {
            world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).getExperience());
            });
        }

        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            i++;
        }

        ExperienceOrbEntity.spawn(world, pos, i);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ForgeBlockEntity forgeBlockEntity) {
        boolean isBurning = forgeBlockEntity.isBurning();
        boolean shouldMarkDirty = false;
        if (forgeBlockEntity.isBurning()) {
            forgeBlockEntity.burnTime--;
        }
        ItemStack fuelStack = forgeBlockEntity.inventory.get(FUEL_INPUT_SLOT);
        ItemStack item1Stack = forgeBlockEntity.inventory.get(INPUT_SLOT_1);
        ItemStack item2Stack = forgeBlockEntity.inventory.get(INPUT_SLOT_2);
        boolean hasInputs = !item1Stack.isEmpty() && !item2Stack.isEmpty();
        boolean hasFuel = !fuelStack.isEmpty();
        if (forgeBlockEntity.isBurning() || hasFuel && hasInputs) {
            RecipeEntry<?> recipeEntry;
            if (hasInputs) {
                recipeEntry = forgeBlockEntity.matchGetter.getFirstMatch(new ForgingRecipeInput(item1Stack, item2Stack, fuelStack), world).orElse(null);
            } else {
                recipeEntry = null;
            }
            int maxCountPerStack = forgeBlockEntity.getMaxCountPerStack();
            if (!forgeBlockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, forgeBlockEntity.inventory, maxCountPerStack)) {
                forgeBlockEntity.burnTime = forgeBlockEntity.getFuelTime(fuelStack);
                forgeBlockEntity.fuelTime = forgeBlockEntity.burnTime;
                if (forgeBlockEntity.isBurning()) {
                    shouldMarkDirty = true;
                    if (hasFuel) {
                        Item fuelItem = fuelStack.getItem();
                        fuelStack.decrement(1);
                        if (fuelStack.isEmpty()) {
                            Item recipeRemainder = fuelItem.getRecipeRemainder();
                            forgeBlockEntity.inventory.set(FUEL_OUTPUT_SLOT, recipeRemainder == null ? ItemStack.EMPTY : new ItemStack(recipeRemainder));
                        }
                    }
                }
            }

            if (forgeBlockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, forgeBlockEntity.inventory, maxCountPerStack)) {
                forgeBlockEntity.cookTime++;
                if (forgeBlockEntity.cookTime == forgeBlockEntity.cookTimeTotal) {
                    forgeBlockEntity.cookTime = 0;
                    forgeBlockEntity.cookTimeTotal = getCookTime(world, forgeBlockEntity);
                    if (craftRecipe(world.getRegistryManager(), recipeEntry, forgeBlockEntity.inventory, maxCountPerStack)) {
                        forgeBlockEntity.setLastRecipe(recipeEntry);
                    }
                    shouldMarkDirty = true;
                }
            } else {
                forgeBlockEntity.cookTime = 0;
            }
        } else if (!forgeBlockEntity.isBurning() && forgeBlockEntity.cookTime > 0) {
            forgeBlockEntity.cookTime = MathHelper.clamp(forgeBlockEntity.cookTime - 2, 0, forgeBlockEntity.cookTimeTotal);
        }
        if (isBurning != forgeBlockEntity.isBurning()) {
            shouldMarkDirty = true;
            blockState = blockState.with(ForgeBlock.LIT, Boolean.valueOf(forgeBlockEntity.isBurning()));
            world.setBlockState(blockPos, blockState, ForgeBlock.NOTIFY_ALL);
        }
        if (shouldMarkDirty) {
            markDirty(world, blockPos, blockState);
        }
    }

    private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(registryManager, recipe, slots, count)) {
            ItemStack itemStack1 = slots.get(INPUT_SLOT_1);
            ItemStack itemStack2 = slots.get(INPUT_SLOT_2);
            ItemStack itemStack3 = recipe.value().getResult(registryManager);
            ItemStack itemStack4 = slots.get(OUTPUT_SLOT);
            if (itemStack4.isEmpty()) {
                slots.set(2, itemStack3.copy());
            } else if (ItemStack.areItemsAndComponentsEqual(itemStack4, itemStack3)) {
                itemStack4.increment(1);
            }

            itemStack1.decrement(1);
            itemStack2.decrement(1);
            return true;
        } else {
            return false;
        }
    }

    private int getFuelTime(ItemStack fuelStack) {
        return 0;
    }

    private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (!slots.get(INPUT_SLOT_1).isEmpty() && !slots.get(INPUT_SLOT_2).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.value().getResult(registryManager);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = slots.get(OUTPUT_SLOT);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsAndComponentsEqual(itemStack2, itemStack)) {
                    return false;
                } else {
                    return itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount() ? true : itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }
}
