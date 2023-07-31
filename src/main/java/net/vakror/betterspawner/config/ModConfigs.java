package net.vakror.betterspawner.config;


import net.vakror.betterspawner.config.configs.SpawnerDefinitionConfig;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

public class ModConfigs {

    public static SpawnerDefinitionConfig SPAWNERS;

    public static void register(boolean overrideCurrent) {
        SPAWNERS = new SpawnerDefinitionConfig();
        SPAWNERS.readConfig(overrideCurrent, SpawnerDefinition.class);
    }
}
