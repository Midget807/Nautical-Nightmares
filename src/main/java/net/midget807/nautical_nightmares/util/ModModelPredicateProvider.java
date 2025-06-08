package net.midget807.nautical_nightmares.util;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;

public class ModModelPredicateProvider {
    public static void registerModModelPredicates() {
        registerAuraliteSword(ModItems.AURALITE_SWORD);
    }

    private static void registerAuraliteSword(Item sword) {
        ModelPredicateProviderRegistry.register(sword, NauticalNightmaresMain.id("charge"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            return entity.getActiveItem() != stack ? 0.0f : (float) (stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 20.0f;
        });
        ModelPredicateProviderRegistry.register(sword, NauticalNightmaresMain.id("charging"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }
}
