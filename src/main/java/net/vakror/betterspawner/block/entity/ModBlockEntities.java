package net.vakror.betterspawner.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.block.ModBlocks;
import net.vakror.betterspawner.block.entity.custom.SpawnerBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BetterSpawnerMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpawnerBlockEntity>> SPAWNER = BLOCK_ENTITIES.register("spawner",
            () -> BlockEntityType.Builder.of(SpawnerBlockEntity::new, ModBlocks.SPAWNER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
