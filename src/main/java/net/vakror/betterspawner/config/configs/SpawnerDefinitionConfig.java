package net.vakror.betterspawner.config.configs;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.betterspawner.config.Config;
import net.vakror.betterspawner.entity.BetterSpawnerModifier;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

import java.util.*;

public class SpawnerDefinitionConfig extends Config<SpawnerDefinition> {
    public List<SpawnerDefinition> allSpawnerDefinitions = new ArrayList<>();
    @Override
    public String getSubPath() {
        return "definitions";
    }

    @Override
    public String getName() {
        return "definitions";
    }

    @Override
    public void add(SpawnerDefinition object) {
        allSpawnerDefinitions.add(object);
    }

    @Override
    public List<SpawnerDefinition> getObjects() {
        return allSpawnerDefinitions;
    }

    @Override
    protected void reset() {
    }
}
