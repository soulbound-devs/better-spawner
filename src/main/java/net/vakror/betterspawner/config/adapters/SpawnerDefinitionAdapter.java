package net.vakror.betterspawner.config.adapters;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

import java.lang.reflect.Type;

public class SpawnerDefinitionAdapter
implements JsonSerializer<SpawnerDefinition>,
JsonDeserializer<SpawnerDefinition> {
    public static SpawnerDefinitionAdapter INSTANCE = new SpawnerDefinitionAdapter();

    private SpawnerDefinitionAdapter() {
    }

    public SpawnerDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return SpawnerDefinition.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(SpawnerDefinition src, Type typeOfSrc, JsonSerializationContext context) {
        return SpawnerDefinition.CODEC.encodeStart(JsonOps.INSTANCE, src).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

