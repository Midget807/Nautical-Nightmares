package net.midget807.nautical_nightmares;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.midget807.nautical_nightmares.datagen.ModBlockLootTableProvider;
import net.midget807.nautical_nightmares.datagen.ModBlockTagProvider;
import net.midget807.nautical_nightmares.datagen.ModItemTagProvider;
import net.midget807.nautical_nightmares.datagen.ModModelProvider;

public class NauticalNightmaresDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);

	}
}
