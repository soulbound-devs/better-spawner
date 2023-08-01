package net.vakror.betterspawner.event;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.command.BetterSpawnerCommands;
import net.vakror.betterspawner.packet.ModPackets;
import net.vakror.betterspawner.packet.RequestAllDataC2SPacket;

public class Events {
    @Mod.EventBusSubscriber(modid = BetterSpawnerMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
            ModPackets.sendToServer(new RequestAllDataC2SPacket());
        }

        @SubscribeEvent
        public static void onCommandsRegister(RegisterCommandsEvent event) {
            new BetterSpawnerCommands.ReloadSpawnerDefinitionsCommand(event.getDispatcher());
            new BetterSpawnerCommands.SpawnerToggleCommand(event.getDispatcher());
        }
    }
}
