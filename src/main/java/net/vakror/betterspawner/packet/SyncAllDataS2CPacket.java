package net.vakror.betterspawner.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.betterspawner.config.ModConfigs;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncAllDataS2CPacket {
    private final List<SpawnerDefinition> spawnerDefinitions;
    private final boolean reloaded;

    public SyncAllDataS2CPacket(List<SpawnerDefinition> spawnerDefinitions, boolean reloaded) {
        this.spawnerDefinitions = spawnerDefinitions;
        this.reloaded = reloaded;
    }

    public SyncAllDataS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt().getCompound("data");
        CompoundTag definitionTag = nbt.getCompound("definitions");
        spawnerDefinitions = new ArrayList<>();
        for (String key : definitionTag.getAllKeys()) {
            spawnerDefinitions.add(SpawnerDefinition.CODEC.parse(NbtOps.INSTANCE, definitionTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        reloaded = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        CompoundTag definitionTag = new CompoundTag();

        for (SpawnerDefinition definition : spawnerDefinitions) {
            definitionTag.put(definition.getSpawnerName(), SpawnerDefinition.CODEC.encodeStart(NbtOps.INSTANCE, definition).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }

        nbt.put("definitions", definitionTag);
        tag.put("data", nbt);
        buf.writeNbt(tag);
        buf.writeBoolean(reloaded);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;

            ModConfigs.SPAWNERS.allSpawnerDefinitions.clear();
            for (SpawnerDefinition spawnerDefinition : spawnerDefinitions) {
                ModConfigs.SPAWNERS.add(spawnerDefinition);
            }
            if (reloaded) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Successfully Reloaded All Spawner Definitions!"));
            }
        });
        return true;
    }
}
