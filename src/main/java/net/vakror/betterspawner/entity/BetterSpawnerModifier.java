package net.vakror.betterspawner.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

public class BetterSpawnerModifier {
    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            Codec.STRING.fieldOf("operation").forGetter((attributeModifier -> attributeModifier.getOperation().name().toLowerCase()))

    ).apply(instance, BetterSpawnerModifier::newModifier));
    public static final Codec<Map<String, AttributeModifier>> MODIFIER_CODEC = Codec.unboundedMap(Codec.STRING, ATTRIBUTE_MODIFIER_CODEC);
    public static final Codec<BetterSpawnerModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("chance").forGetter(BetterSpawnerModifier::getChance),
            MODIFIER_CODEC.fieldOf("modifiers").forGetter(BetterSpawnerModifier::getModifiers)
    ).apply(instance, BetterSpawnerModifier::new));
    float chance;
    Map<String, AttributeModifier> modifiers;

    public BetterSpawnerModifier(float chance, Map<String, AttributeModifier> modifiers) {
        this.chance = chance;
        this.modifiers = modifiers;
    }

    public static AttributeModifier newModifier(String name, double amount, String operation) {
        return new AttributeModifier(name, amount, AttributeModifier.Operation.valueOf(operation.toUpperCase()));
    }

    public float getChance() {
        return chance;
    }

    public BetterSpawnerModifier setChance(int chance) {
        this.chance = chance;
        return this;
    }

    public Map<String, AttributeModifier> getModifiers() {
        return modifiers;
    }

    public BetterSpawnerModifier setModifiers(Map<String, AttributeModifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }
}
