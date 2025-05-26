package net.midget807.nautical_nightmares.item;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.midget807.nautical_nightmares.NauticalNightmaresMain;
import net.midget807.nautical_nightmares.registry.ModEntityAttributes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Supplier;

public class DiveSuitItem extends ArmorItem {
    public final double pressureResistance;
    private final Supplier<AttributeModifiersComponent> attributeModifiers;


    public DiveSuitItem(RegistryEntry<ArmorMaterial> material, ArmorItem.Type type, double pressureResistance, Settings settings) {
        super(material, type, settings);
        this.pressureResistance = pressureResistance;
        this.attributeModifiers = Suppliers.memoize(
                () -> {
                    int i = material.value().getProtection(type);
                    float f = material.value().toughness();
                    AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
                    AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());
                    Identifier identifier = Identifier.ofVanilla("armor." + type.getName());
                    builder.add(
                            EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(identifier, (double)i, EntityAttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
                    );
                    builder.add(
                            EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                            new EntityAttributeModifier(identifier, (double)f, EntityAttributeModifier.Operation.ADD_VALUE),
                            attributeModifierSlot
                    );
                    float g = material.value().knockbackResistance();
                    if (g > 0.0F) {
                        builder.add(
                                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                                new EntityAttributeModifier(identifier, (double)g, EntityAttributeModifier.Operation.ADD_VALUE),
                                attributeModifierSlot
                        );
                    }
                    builder.add(
                            ModEntityAttributes.GENERIC_PRESSURE_RESISTANCE,
                            new EntityAttributeModifier(NauticalNightmaresMain.id("dive_suit." + type.getName()), pressureResistance, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot())
                    );

                    return builder.build();
                }
        );
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return this.attributeModifiers.get();
    }

    public double getPressureResistance() {
        return this.pressureResistance;
    }

    public static enum Type implements StringIdentifiable {
        HELMET(EquipmentSlot.HEAD, 11, "helmet"),
        SUIT(EquipmentSlot.CHEST, 16, "suit"),
        PANTS(EquipmentSlot.LEGS, 15, "pants"),
        BOOTS(EquipmentSlot.FEET, 13, "boots");

        public static final Codec<DiveSuitItem.Type> CODEC = StringIdentifiable.createBasicCodec(DiveSuitItem.Type::values);
        private final EquipmentSlot equipmentSlot;
        private final String name;
        private final int baseMaxDamage;

        private Type(final EquipmentSlot equipmentSlot, final int baseMaxDamage, final String name) {
            this.equipmentSlot = equipmentSlot;
            this.name = name;
            this.baseMaxDamage = baseMaxDamage;
        }

        public int getBaseMaxDamage() {
            return this.baseMaxDamage;
        }

        public EquipmentSlot getEquipmentSlot() {
            return this.equipmentSlot;
        }

        public String getName() {
            return this.name;
        }
        public boolean isTrimmable() {
            return this == HELMET || this == SUIT || this == PANTS || this == BOOTS;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
