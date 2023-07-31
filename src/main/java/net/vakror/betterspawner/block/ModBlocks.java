package net.vakror.betterspawner.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.block.custom.SpawnerBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterSpawnerMod.MOD_ID);

    public static final RegistryObject<Block> SPAWNER = BLOCKS.register("spawner",
            () -> new SpawnerBlock(BlockBehaviour.Properties.of(Material.METAL).strength(5).noLootTable()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
