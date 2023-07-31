package net.vakror.betterspawner.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.loading.FMLPaths;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.config.adapters.CompoundTagAdapter;
import net.vakror.betterspawner.config.adapters.SpawnerDefinitionAdapter;
import net.vakror.betterspawner.spawner.SpawnerDefinition;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public abstract class Config<P extends ConfigObject> {
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(CompoundTag.class, CompoundTagAdapter.INSTANCE)
            .registerTypeAdapter(SpawnerDefinition.class, SpawnerDefinitionAdapter.INSTANCE)
            .setPrettyPrinting().create();

    public void generateConfig() {
        this.reset();
        try {
            this.writeConfig();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private File getConfigDir() {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve("betterspawner/" + getSubPath()).toFile();
        return configDir;
    }

    @NotNull
    private File getConfigFile(String fileName) {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve("betterspawner/" + getSubPath() + "/" + fileName + ".json").toFile();
        return configDir;
    }

    public abstract String getSubPath();

    public abstract String getName();

    public String toString() {
        return this.getName();
    }

    public void readConfig(boolean overrideCurrent, Class<P> type) {
        if (!overrideCurrent) {
            BetterSpawnerMod.LOGGER.info("Reading configs: " + this.getName());
            File[] configFiles = this.getConfigDir().listFiles(File::isFile);
            if (configFiles != null && configFiles.length != 0) {
                for (File file : configFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        P object = GSON.fromJson(reader, type);
                        this.add(object);
                    } catch (IOException e) {
                        System.out.println(e.getClass());
                        e.printStackTrace();
                        BetterSpawnerMod.LOGGER.warn("Error with config {}, generating new", this);
                        this.generateConfig();
                    }
                }
            } else {
                this.generateConfig();
                BetterSpawnerMod.LOGGER.warn("Config " + this.getName() + "not found, generating new");
            }
        } else {
            this.generateConfig();
            BetterSpawnerMod.LOGGER.info("Successfully Overwrote Config: " + this.getName());
        }
    }

    public abstract void add(P object);

    protected boolean isValid() {
        return true;
    }

    public static boolean checkAllFieldsAreNotNull(Object o) throws IllegalAccessException {
        for (Field v : o.getClass().getDeclaredFields()) {
            boolean b;
            if (!v.canAccess(o)) continue;
            Object field = v.get(o);
            if (field == null) {
                return false;
            }
            if (field.getClass().isPrimitive() || (b = Config.checkAllFieldsAreNotNull(field))) continue;
            return false;
        }
        return true;
    }

    public abstract List<P> getObjects();

    protected abstract void reset();

    public void writeConfig() throws IOException {
        File cfgDIr = this.getConfigDir();
        if (!cfgDIr.exists() && !cfgDIr.mkdirs()) {
            return;
        }
        for (P object : getObjects()) {
            FileWriter writer = new FileWriter(getConfigFile(object.getFileName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
            GSON.toJson(object, writer);
            writer.flush();
            writer.close();
        }
    }
}

