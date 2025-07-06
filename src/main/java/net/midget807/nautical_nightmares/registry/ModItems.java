package net.midget807.nautical_nightmares.registry;

import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.item.DiveSuitItem;
import net.midget807.nautical_nightmares.item.SeaSceptreItem;
import net.midget807.nautical_nightmares.item.auralite.AuraliteSwordItem;
import net.midget807.nautical_nightmares.item.auralite.AuraliteToolMaterial;
import net.midget807.nautical_nightmares.item.auralite.AuraliteTridentItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item BLANK = registerItem("blank", new Item(new Item.Settings()));

    public static final Item AURALITE_SCRAP = registerItem("auralite_scrap", new Item(new Item.Settings().fireproof()));
    public static final Item AURALITE_INGOT = registerItem("auralite_ingot", new Item(new Item.Settings().fireproof()));
    public static final Item AURALITE_HELMET = registerItem("auralite_helmet", new DiveSuitItem(ModArmorMaterials.AURALITE, ArmorItem.Type.HELMET, 8.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item AURALITE_CHESTPLATE = registerItem("auralite_chestplate", new DiveSuitItem(ModArmorMaterials.AURALITE, ArmorItem.Type.CHESTPLATE, 10.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item AURALITE_LEGGINGS = registerItem("auralite_leggings", new DiveSuitItem(ModArmorMaterials.AURALITE, ArmorItem.Type.LEGGINGS, 9.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item AURALITE_BOOTS = registerItem("auralite_boots", new DiveSuitItem(ModArmorMaterials.AURALITE, ArmorItem.Type.BOOTS, 7.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item AURALITE_TRIDENT = registerItem("auralite_trident", new AuraliteTridentItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(400).attributeModifiers(AuraliteTridentItem.createAttributeModifiers()).component(DataComponentTypes.TOOL, AuraliteTridentItem.createToolComponent()).fireproof().maxCount(1)));
    public static final Item AURALITE_SWORD = registerItem("auralite_sword", new AuraliteSwordItem(new AuraliteToolMaterial(), new Item.Settings().attributeModifiers(AuraliteSwordItem.createAttributeModifiers(new AuraliteToolMaterial(), 4, -2.4f)).fireproof().maxCount(1)));

    public static final Item SEA_SCEPTRE = registerItem("sceptre_of_the_sea", new SeaSceptreItem(new Item.Settings().fireproof().maxCount(1)));

    public static final Item FERRIC_COPPER_INGOT = registerItem("ferric_copper_ingot", new Item(new Item.Settings()));
    public static final Item STEEL_INGOT = registerItem("steel_ingot", new Item(new Item.Settings()));
    public static final Item AURIC_STEEL_INGOT = registerItem("auric_steel_ingot", new Item(new Item.Settings()));
    public static final Item AURIC_ALLOY_INGOT = registerItem("auric_alloy_ingot", new Item(new Item.Settings()));
    public static final Item NETHERITE_ALLOY_INGOT = registerItem("netherite_alloy_ingot", new Item(new Item.Settings()));

    public static final Item BARITE_SHARD = registerItem("barite_shard", new Item(new Item.Settings()));

    public static final Item C_DIVE_HELMET = registerItem("copper_dive_helmet", new DiveSuitItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, 2.0, new Item.Settings().maxCount(1)));
    public static final Item C_DIVE_SUIT = registerItem("copper_dive_suit", new DiveSuitItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, 4.0, new Item.Settings().maxCount(1)));
    public static final Item C_DIVE_PANTS = registerItem("copper_dive_pants", new DiveSuitItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, 3.0, new Item.Settings().maxCount(1)));
    public static final Item C_DIVE_BOOTS = registerItem("copper_dive_boots", new DiveSuitItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, 1.0, new Item.Settings().maxCount(1)));
    public static final Item FC_DIVE_HELMET = registerItem("ferric_copper_dive_helmet", new DiveSuitItem(ModArmorMaterials.FERRIC_COPPER, ArmorItem.Type.HELMET, 3.0, new Item.Settings().maxCount(1)));
    public static final Item FC_DIVE_SUIT = registerItem("ferric_copper_dive_suit", new DiveSuitItem(ModArmorMaterials.FERRIC_COPPER, ArmorItem.Type.CHESTPLATE, 5.0, new Item.Settings().maxCount(1)));
    public static final Item FC_DIVE_PANTS = registerItem("ferric_copper_dive_pants", new DiveSuitItem(ModArmorMaterials.FERRIC_COPPER, ArmorItem.Type.LEGGINGS, 4.0, new Item.Settings().maxCount(1)));
    public static final Item FC_DIVE_BOOTS = registerItem("ferric_copper_dive_boots", new DiveSuitItem(ModArmorMaterials.FERRIC_COPPER, ArmorItem.Type.BOOTS, 2.0, new Item.Settings().maxCount(1)));
    public static final Item S_DIVE_HELMET = registerItem("steel_dive_helmet", new DiveSuitItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET, 4.0, new Item.Settings().maxCount(1)));
    public static final Item S_DIVE_SUIT = registerItem("steel_dive_suit", new DiveSuitItem(ModArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE, 6.0, new Item.Settings().maxCount(1)));
    public static final Item S_DIVE_PANTS = registerItem("steel_dive_pants", new DiveSuitItem(ModArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS, 5.0, new Item.Settings().maxCount(1)));
    public static final Item S_DIVE_BOOTS = registerItem("steel_dive_boots", new DiveSuitItem(ModArmorMaterials.STEEL, ArmorItem.Type.BOOTS, 3.0, new Item.Settings().maxCount(1)));
    public static final Item AS_DIVE_HELMET = registerItem("auric_steel_dive_helmet", new DiveSuitItem(ModArmorMaterials.AURIC_STEEL, ArmorItem.Type.HELMET, 5.0, new Item.Settings().maxCount(1)));
    public static final Item AS_DIVE_SUIT = registerItem("auric_steel_dive_suit", new DiveSuitItem(ModArmorMaterials.AURIC_STEEL, ArmorItem.Type.CHESTPLATE, 7.0, new Item.Settings().maxCount(1)));
    public static final Item AS_DIVE_PANTS = registerItem("auric_steel_dive_pants", new DiveSuitItem(ModArmorMaterials.AURIC_STEEL, ArmorItem.Type.LEGGINGS, 6.0, new Item.Settings().maxCount(1)));
    public static final Item AS_DIVE_BOOTS = registerItem("auric_steel_dive_boots", new DiveSuitItem(ModArmorMaterials.AURIC_STEEL, ArmorItem.Type.BOOTS, 4.0, new Item.Settings().maxCount(1)));
    public static final Item AA_DIVE_HELMET = registerItem("auric_alloy_dive_helmet", new DiveSuitItem(ModArmorMaterials.AURIC_ALLOY, ArmorItem.Type.HELMET, 6.0, new Item.Settings().maxCount(1)));
    public static final Item AA_DIVE_SUIT = registerItem("auric_alloy_dive_suit", new DiveSuitItem(ModArmorMaterials.AURIC_ALLOY, ArmorItem.Type.CHESTPLATE, 8.0, new Item.Settings().maxCount(1)));
    public static final Item AA_DIVE_PANTS = registerItem("auric_alloy_dive_pants", new DiveSuitItem(ModArmorMaterials.AURIC_ALLOY, ArmorItem.Type.LEGGINGS, 7.0, new Item.Settings().maxCount(1)));
    public static final Item AA_DIVE_BOOTS = registerItem("auric_alloy_dive_boots", new DiveSuitItem(ModArmorMaterials.AURIC_ALLOY, ArmorItem.Type.BOOTS, 5.0, new Item.Settings().maxCount(1)));
    public static final Item NA_DIVE_HELMET = registerItem("netherite_alloy_dive_helmet", new DiveSuitItem(ModArmorMaterials.NETHERITE_ALLOY, ArmorItem.Type.HELMET, 7.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item NA_DIVE_SUIT = registerItem("netherite_alloy_dive_suit", new DiveSuitItem(ModArmorMaterials.NETHERITE_ALLOY, ArmorItem.Type.CHESTPLATE, 9.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item NA_DIVE_PANTS = registerItem("netherite_alloy_dive_pants", new DiveSuitItem(ModArmorMaterials.NETHERITE_ALLOY, ArmorItem.Type.LEGGINGS, 8.0, new Item.Settings().maxCount(1).fireproof()));
    public static final Item NA_DIVE_BOOTS = registerItem("netherite_alloy_dive_boots", new DiveSuitItem(ModArmorMaterials.NETHERITE_ALLOY, ArmorItem.Type.BOOTS, 6.0, new Item.Settings().maxCount(1).fireproof()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, NauticalNightmaresMain.id(name), item);
    }

    public static void registerModItems() {
        NauticalNightmaresMain.LOGGER.info("Registering Mod Items");
    }
}
