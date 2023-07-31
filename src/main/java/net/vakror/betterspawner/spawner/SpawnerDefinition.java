package net.vakror.betterspawner.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vakror.betterspawner.config.ConfigObject;
import net.vakror.betterspawner.entity.BetterSpawnerModifier;

import java.util.*;

public class SpawnerDefinition implements ConfigObject {
    public static final Codec<Map<String, Integer>> STRING_INT_MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);
    public static final Codec<Map<String, BetterSpawnerModifier>> STRING_MODIFIER_MAP_CODEC = Codec.unboundedMap(Codec.STRING, BetterSpawnerModifier.CODEC);
    public static final Codec<SpawnerDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("spawnerName").forGetter(SpawnerDefinition::getSpawnerName),
            Codec.INT.fieldOf("tickDelay").forGetter(SpawnerDefinition::getTickDelay),
            Codec.INT.fieldOf("playerDistanceFromSpawner").forGetter(SpawnerDefinition::getPlayerDistanceFromSpawner),
            Codec.STRING.listOf().fieldOf("mobTypes").forGetter(SpawnerDefinition::getMobTypes),
            STRING_INT_MAP_CODEC.optionalFieldOf("maxMobsOfType").forGetter(SpawnerDefinition::getMaxForEachMobTypeOptional),
            STRING_MODIFIER_MAP_CODEC.optionalFieldOf("entityModifiers").forGetter(SpawnerDefinition::getEntityModifiersOptional),
            Codec.INT.fieldOf("distance").forGetter(SpawnerDefinition::getTickDelay),
            Codec.INT.fieldOf("amount").forGetter(SpawnerDefinition::getAmount),
            Codec.INT.optionalFieldOf("maxMobs").forGetter(SpawnerDefinition::getMaxMobsOptional),
            Codec.BOOL.optionalFieldOf("destroyAfterHittingMaxMobs").forGetter(SpawnerDefinition::shouldDestroyAfterHittingMaxMobsOptional),
            Codec.BOOL.optionalFieldOf("infinite").forGetter(SpawnerDefinition::isInfiniteOptional),
            Codec.BOOL.optionalFieldOf("isSingleUse").forGetter(SpawnerDefinition::isSingleUseOptional)
    ).apply(instance, SpawnerDefinition::new));

    String spawnerName;
    int tickDelay;
    int playerDistanceFromSpawner;
    List<String> mobTypes;
    Map<String, Integer> maxForEachMobType = new HashMap<>();
    Map<String, BetterSpawnerModifier> entityModifiers = new HashMap<>();
    int distance;
    int amount;
    int maxMobs;
    boolean destroyAfterHittingMaxMobs;
    boolean infinite;
    boolean isSingleUse;

    public SpawnerDefinition(String spawnerName, int tickDelay, int playerDistanceFromSpawner,List<String> mobTypes, Optional<Map<String, Integer>> maxForEachMobType, Optional<Map<String, BetterSpawnerModifier>> entityModifiers, int distance, int amount, Optional<Integer> maxMobs, Optional<Boolean> destroyAfterHittingMaxMobs, Optional<Boolean> infinite, Optional<Boolean> isSingleUse) {
        this.spawnerName = spawnerName;
        this.tickDelay = tickDelay;
        this.playerDistanceFromSpawner = playerDistanceFromSpawner;
        this.mobTypes = mobTypes;
        maxForEachMobType.ifPresent(this::setMaxForEachMobType);
        entityModifiers.ifPresent(this::setEntityModifiers);
        this.distance = distance;
        this.amount = amount;
        maxMobs.ifPresent(this::setMaxMobs);
        destroyAfterHittingMaxMobs.ifPresent(this::setDestroyAfterHittingMaxMobs);
        infinite.ifPresent(this::setInfinite);
        isSingleUse.ifPresent(this::setSingleUse);
    }

    public boolean isSingleUse() {
        return isSingleUse;
    }


    public Optional<Boolean> isSingleUseOptional() {
        return Optional.of(isSingleUse);
    }

    public SpawnerDefinition setSingleUse(boolean singleUse) {
        isSingleUse = singleUse;
        return this;
    }

    public int getPlayerDistanceFromSpawner() {
        return playerDistanceFromSpawner;
    }

    public SpawnerDefinition setPlayerDistanceFromSpawner(int playerDistanceFromSpawner) {
        this.playerDistanceFromSpawner = playerDistanceFromSpawner;
        return this;
    }


    public boolean isInfinite() {
        return infinite;
    }

    public Optional<Boolean> isInfiniteOptional() {
        return Optional.of(infinite);
    }

    public SpawnerDefinition setInfinite(boolean infinite) {
        this.infinite = infinite;
        return this;
    }

    public SpawnerDefinition setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }

    public SpawnerDefinition setMobTypes(List<String> mobTypes) {
        this.mobTypes = new ArrayList<>(mobTypes);
        return this;
    }

    public SpawnerDefinition setMaxForEachMobType(Map<String, Integer> maxForEachMobType) {
        this.maxForEachMobType = new HashMap<>(maxForEachMobType);
        return this;
    }

    public SpawnerDefinition setEntityModifiers(Map<String, BetterSpawnerModifier> entityModifiers) {
        this.entityModifiers = new HashMap<>(entityModifiers);
        return this;
    }

    public SpawnerDefinition setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public SpawnerDefinition setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public SpawnerDefinition setMaxMobs(int maxMobs) {
        this.maxMobs = maxMobs;
        return this;
    }

    public SpawnerDefinition setDestroyAfterHittingMaxMobs(boolean destroyAfterHittingMaxMobs) {
        this.destroyAfterHittingMaxMobs = destroyAfterHittingMaxMobs;
        return this;
    }

    public boolean shouldDestroyAfterHittingMaxMobs() {
        return destroyAfterHittingMaxMobs;
    }

    public Optional<Boolean> shouldDestroyAfterHittingMaxMobsOptional() {
        return Optional.of(destroyAfterHittingMaxMobs);
    }

    public int getTickDelay() {
        return tickDelay;
    }

    public List<String> getMobTypes() {
        return mobTypes;
    }

    public int getDistance() {
        return distance;
    }

    public int getAmount() {
        return amount;
    }

    public Map<String, Integer> getMaxForEachMobType() {
        return maxForEachMobType;
    }

    public Map<String, BetterSpawnerModifier> getEntityModifiers() {
        return entityModifiers;
    }

    public Optional<Map<String, Integer>> getMaxForEachMobTypeOptional() {
        if (maxForEachMobType == null || maxForEachMobType.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(maxForEachMobType);
    }

    public Optional<Map<String, BetterSpawnerModifier>> getEntityModifiersOptional() {
        if (entityModifiers == null || entityModifiers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entityModifiers);
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public Optional<Integer> getMaxMobsOptional() {
        return Optional.of(maxMobs);
    }

    public String getSpawnerName() {
        return spawnerName;
    }

    public SpawnerDefinition setSpawnerName(String spawnerName) {
        this.spawnerName = spawnerName;
        return this;
    }

    @Override
    public String getFileName() {
        return spawnerName;
    }
}
