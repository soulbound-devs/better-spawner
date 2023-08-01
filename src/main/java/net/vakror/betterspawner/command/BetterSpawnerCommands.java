package net.vakror.betterspawner.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
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

    public static class SpawnerToggleCommand {
        public SpawnerToggleCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("betterspawner")
                    .then(Commands.literal("enabled").then(Commands.argument("enabled", BoolArgumentType.bool()))
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                BetterSpawnerMod.instance.enabled = context.getArgument("enabled", Boolean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}