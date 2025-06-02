package net.midget807.nautical_nightmares;

import net.fabricmc.api.ModInitializer;

import net.midget807.nautical_nightmares.registry.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NauticalNightmaresMain implements ModInitializer {
	public static final String MOD_ID = "nautical_nightmares";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("rahhhhh");

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModDataComponentTypes.registerModDataComponentTypes();
		ModEntityAttributes.registerModEntityAttributes();
		ModFoodComponents.registerModFoodComponents();
		ModArmorMaterials.registerModArmorMaterials();
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
		ModEntities.registerModEntities();
		ModEnchantments.registerModEnchantments();
		ModSounds.registerModSounds();
		ModEffects.registerModEffects();
		ModRecipes.registerModRecipes();
		ModScreenHandlers.registerModScreenHandlers();
	}
}