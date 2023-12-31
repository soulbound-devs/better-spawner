package net.vakror.betterspawner.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.config.ModConfigs;
import net.vakror.betterspawner.packet.ModPackets;
import net.vakror.betterspawner.packet.SyncAllDataS2CPacket;

public class BetterSpawnerCommands {
    public static class ReloadSpawnerDefinitionsCommand {
        public ReloadSpawnerDefinitionsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("betterspawner")
                    .then(Commands.literal("reload-config")
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                ModConfigs.register(false);
                ModPackets.sendToClients(new SyncAllDataS2CPacket(ModConfigs.SPAWNERS.allSpawnerDefinitions, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    public static class DisableSpawnerCommand {
        public DisableSpawnerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("betterspawner")
                    .then(Commands.literal("disable")
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                BetterSpawnerMod.instance.enabled = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    public static class EnableSpawnerCommand {
        public EnableSpawnerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("betterspawner")
                    .then(Commands.literal("enable")
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                BetterSpawnerMod.instance.enabled = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}