package net.vakror.betterspawner.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class SpawnBatchEvent extends Event {
    public int count;
    public List<Entity> entities;
    public Level level;

    public SpawnBatchEvent(int count, List<Entity> entities, Level level) {
        this.count = count;
        this.entities = entities;
        this.level = level;
    }
}
