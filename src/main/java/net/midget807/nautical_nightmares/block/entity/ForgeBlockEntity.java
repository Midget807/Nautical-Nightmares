package net.midget807.nautical_nightmares.block.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.block.ForgeBlock;
import net.midget807.nautical_nightmares.datagen.ModItemTagProvider;
import net.midget807.nautical_nightmares.recipe.ForgingRecipe;
import net.midget807.nautical_nightmares.recipe.ForgingRecipeInput;
import net.midget807.nautical_nightmares.registry.ModBlockEntities;
import net.midget807.nautical_nightmares.registry.ModRecipes;
import net.midget807.nautical_nightmares.screen.ForgeScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ForgeBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
    protected static final int INPUT_SLOT_1 = 0;
    protected static final int INPUT_SLOT_2 = 1;
    protected static final int FUEL_SLOT = 2;
    protected static final int CATALYST_SLOT = 3;
    protected static final int OUTPUT_SLOT = 4;
    protected static final int[] TOP_SLOTS = new int[]{0, 1};
    protected static final int[] SIDE_SLOTS = new int[]{2, 3};
    protected static final int[] BOTTOM_SLOTS = new int[]{4};
    public static final int BURN_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_PROPERTY_INDEX = 2;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 3;
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    int fuelTime;
    int burnTime;
    int cookTime;
    int cookTimeTotal;
    @Nullable
    private static Map<Item, Integer> fuelTimes;
    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ForgeBlockEntity.this.burnTime;
                case 1 -> ForgeBlockEntity.this.fuelTime;
                case 2 -> ForgeBlockEntity.this.cookTime;
                case 3 -> ForgeBlockEntity.this.cookTimeTotal;
                default -> 0;
            };
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
    private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.MatchGetter<ForgingRecipeInput, ? extends ForgingRecipe> matchGetter;

    public ForgeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FORGE_BLOCK_ENTITY, blockPos, blockState);
        this.matchGetter = RecipeManager.createCachedMatchGetter(ModRecipes.FORGING_TYPE);
    }

    public static void clearFuelTimes() {
        fuelTimes = null;
    }

    public static Map<Item, Integer> getOrCreateFuelTimeMap() {
        Map<Item, Integer> map = fuelTimes;
        if (map != null) {
            return map;
        } else {
            Map<Item, Integer> map2 = Maps.<Item, Integer>newLinkedHashMap();
            addFuel(map2, Items.COAL, 400);
            addFuel(map2, Items.CHARCOAL, 400);
            addFuel(map2, Items.COAL_BLOCK, 400);
            addFuel(map2, Items.LAVA_BUCKET, 400);
            addFuel(map2, Items.BLAZE_ROD, 400);
            fuelTimes = map2;
            return map2;
        }
    }

    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw (IllegalStateException) Util.throwOrPause(
                        new IllegalStateException(
                                "A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"
                        )
                );
            }
        } else {
            fuelTimes.put(item2, fuelTime);
        }
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(tag)) {
            if (!isNonFlammableWood(registryEntry.value())) {
                fuelTimes.put(registryEntry.value(), fuelTime);
            }
        }
    }

    public static boolean canUseAsFuel(ItemStack itemStack) {
        return getOrCreateFuelTimeMap().containsKey(itemStack.getItem());
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTime = this.getFuelTime(this.inventory.get(FUEL_SLOT));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");

        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(Identifier.of(string), nbtCompound.getInt(string));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putShort("BurnTime", (short) this.burnTime);
        nbt.putShort("CookTime", (short) this.cookTime);
        nbt.putShort("CookTimeTotal", (short) this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);

        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, count) -> nbtCompound.putInt(identifier.toString(), count));
        nbt.put("RecipesUsed", nbtCompound);
    }

    public static void tick(World world, BlockPos pos, BlockState state, @NotNull ForgeBlockEntity blockEntity) {
        boolean isBurningInstance = blockEntity.isBurning();
        boolean shouldMarkDirty = false;
        if (blockEntity.isBurning()) {
            blockEntity.burnTime--;
        }

        ItemStack fuelStack = blockEntity.inventory.get(FUEL_SLOT);
        ItemStack inputStack1 = blockEntity.inventory.get(INPUT_SLOT_1);
        ItemStack inputStack2 = blockEntity.inventory.get(INPUT_SLOT_2);
        ItemStack catalystStack = blockEntity.inventory.get(CATALYST_SLOT);
        boolean hasInput1 = !inputStack1.isEmpty();
        boolean hasInput2 = !inputStack2.isEmpty();
        boolean hasFuel = !fuelStack.isEmpty();
        boolean hasCatalyst = !catalystStack.isEmpty();

        if (blockEntity.isBurning() || hasFuel && (hasInput1 && hasInput2)) {
            RecipeEntry<?> recipeEntry;
            if (hasInput1 && hasInput2) {
                recipeEntry = blockEntity.matchGetter.getFirstMatch(new ForgingRecipeInput(inputStack1, inputStack2, fuelStack, catalystStack), world).orElse(null);
            } else {
                recipeEntry = null;
            }

            int i = blockEntity.getMaxCountPerStack();

            if (!blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, blockEntity.inventory, i)) {
                blockEntity.burnTime = blockEntity.getFuelTime(fuelStack);
                blockEntity.fuelTime = blockEntity.burnTime;
                if (blockEntity.isBurning()) {
                    shouldMarkDirty = true;
                    if (hasCatalyst) {
                        catalystStack.decrement(1);
                    }
                    if (hasFuel) {
                        Item fuelItem = fuelStack.getItem();
                        fuelStack.decrement(1);
                        if (fuelStack.isEmpty()) {
                            Item recipeRemainder = fuelItem.getRecipeRemainder();
                            blockEntity.inventory.set(FUEL_SLOT, recipeRemainder == null ? ItemStack.EMPTY : new ItemStack(recipeRemainder));
                        }
                    }
                }
            }

            if (blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, blockEntity.inventory, i)) {
                blockEntity.cookTime++;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
                    if (craftRecipe(world.getRegistryManager(), recipeEntry, blockEntity.inventory, i)) {
                        blockEntity.setLastRecipe(recipeEntry);
                    }
                    shouldMarkDirty = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }

        } else if (!blockEntity.isBurning() && blockEntity.cookTime > 0) {
            blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        }

        if (isBurningInstance != blockEntity.isBurning()) {
            shouldMarkDirty = true;
            state = state.with(ForgeBlock.LIT, blockEntity.isBurning());
            world.setBlockState(pos, state, ForgeBlock.NOTIFY_ALL);
        }

        if (shouldMarkDirty) {
            markDirty(world, pos, state);
        }


    }


    public int getFuelTime(ItemStack fuelStack) {
        if (fuelStack.isEmpty()) {
            return 0;
        } else {
            Item item = fuelStack.getItem();
            return (Integer) getOrCreateFuelTimeMap().getOrDefault(item, 0);
        }
    }

    private static int getCookTime(World world, ForgeBlockEntity blockEntity) {
        ForgingRecipeInput forgingRecipeInput = new ForgingRecipeInput(blockEntity.getStack(INPUT_SLOT_1), blockEntity.getStack(INPUT_SLOT_2), blockEntity.getStack(FUEL_SLOT), blockEntity.getStack(CATALYST_SLOT));
        return blockEntity.matchGetter
                .getFirstMatch(forgingRecipeInput, world)
                .map(recipe -> {
                    if (recipe.value().needsCatalyst() && recipe.value().getCatalyst() != null) {
                        return blockEntityHasCatalyst(blockEntity) && recipe.value().getCatalyst().test(blockEntity.getStack(CATALYST_SLOT)) ? recipe.value().getCookingTime() : 0;
                    } else {
                        return blockEntityHasCatalyst(blockEntity) ? recipe.value().getCookingTime() / 2 : recipe.value().getCookingTime();
                    }
                })
                .orElse(20);
    }

    private static boolean blockEntityHasCatalyst(ForgeBlockEntity blockEntity) {
        return blockEntity.getStack(CATALYST_SLOT).isEmpty() && blockEntity.getStack(CATALYST_SLOT).isIn(ModItemTagProvider.FORGING_CATALYSTS);
    }

    private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, RecipeEntry<?> recipeEntry, DefaultedList<ItemStack> inventory, int count) {
        if (!inventory.get(INPUT_SLOT_1).isEmpty() && !inventory.get(INPUT_SLOT_2).isEmpty() && recipeEntry != null) {
            ItemStack resultStack = recipeEntry.value().getResult(registryManager);
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack inventoryOutputStack = inventory.get(OUTPUT_SLOT);
                if (inventoryOutputStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsAndComponentsEqual(inventoryOutputStack, resultStack)) {
                    return false;
                } else {
                    return inventoryOutputStack.getCount() < count && inventoryOutputStack.getCount() < inventoryOutputStack.getMaxCount() ? true : inventoryOutputStack.getCount() < resultStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipeEntry, DefaultedList<ItemStack> inventory, int count) {
        if (recipeEntry != null && canAcceptRecipeOutput(registryManager, recipeEntry, inventory, count)) {
            ItemStack inputStack1 = inventory.get(INPUT_SLOT_1);
            ItemStack inputStack2 = inventory.get(INPUT_SLOT_2);
            ItemStack resultStack = recipeEntry.value().getResult(registryManager);
            ItemStack currentOutputStack = inventory.get(OUTPUT_SLOT);
            if (currentOutputStack.isEmpty()) {
                inventory.set(OUTPUT_SLOT, resultStack.copy());
            } else if (ItemStack.areItemsAndComponentsEqual(currentOutputStack, resultStack)) {
                currentOutputStack.increment(1);
            }

            inputStack1.decrement(1);
            inputStack2.decrement(1);
            return true;
        } else {
            return false;
        }
    }



    @Override
    protected Text getContainerName() {
        return Text.translatable("container.nautical_nightmares.forge");
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
        this.inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        if (slot == 0 && !bl) {
            this.cookTimeTotal = getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == OUTPUT_SLOT) {
            return false;
        } else if (slot != FUEL_SLOT) {
            return true;
        } else {
            ItemStack itemStack = this.inventory.get(FUEL_SLOT);
            return canUseAsFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
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
        return dir == Direction.DOWN && (slot == FUEL_SLOT ? stack.isOf(Items.BUCKET) : slot == OUTPUT_SLOT);
    }

    @Override
    public int size() {
        return this.inventory.size();
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
        List<RecipeEntry<?>> list = this.getRecipesUsedAndDroppedExperience(player.getServerWorld(), player.getPos());
        player.unlockRecipes(list);

        for (RecipeEntry<?> recipeEntry : list) {
            if (recipeEntry != null) {
                player.onRecipeCrafted(recipeEntry, this.inventory);
            }
        }
        this.recipesUsed.clear();
    }

    public List<RecipeEntry<?>> getRecipesUsedAndDroppedExperience(ServerWorld world, Vec3d pos) {
        List<RecipeEntry<?>> list = Lists.<RecipeEntry<?>>newArrayList();

        for (Object2IntMap.Entry<Identifier> entry : this.recipesUsed.object2IntEntrySet()) {
            world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((ForgingRecipe)recipe.value()).getExperience());
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
}
