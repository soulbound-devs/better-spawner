package net.vakror.betterspawner.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.betterspawner.config.ModConfigs;

import java.util.function.Supplier;

public class RequestAllDataC2SPacket {


    public RequestAllDataC2SPacket() {
    }

    public RequestAllDataC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            ModPackets.sendToClient(new SyncAllDataS2CPacket(ModConfigs.SPAWNERS.allSpawnerDefinitions, false), context.getSender());
        });
        return true;
    }
}

