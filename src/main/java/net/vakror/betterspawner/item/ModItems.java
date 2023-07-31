package net.vakror.betterspawner.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.block.ModBlocks;
import net.vakror.betterspawner.block.entity.ModBlockEntities;
import net.vakror.betterspawner.config.ModConfigs;
import net.vakror.betterspawner.config.configs.SpawnerDefinitionConfig;
import net.vakror.betterspawner.item.custom.ModSpawnerItem;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterSpawnerMod.MOD_ID);

    public static final RegistryObject<Item> SPAWNER = ITEMS.register("spawner", () -> new ModSpawnerItem(ModBlocks.SPAWNER.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final List<ItemStack> ALL_SPAWNER_STACKS = new ArrayList<>();

    public static void addAllItemsToList() {
        ALL_SPAWNER_STACKS.clear();
        for (SpawnerDefinition spawnerDefinition : ModConfigs.SPAWNERS.allSpawnerDefinitions) {
            ItemStack stack = new ItemStack(SPAWNER.get(), 1);
            BlockItem.setBlockEntityData(stack, ModBlockEntities.SPAWNER.get(), save(spawnerDefinition));
            if (!ALL_SPAWNER_STACKS.contains(stack)) {
                ALL_SPAWNER_STACKS.add(stack);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static CompoundTag save(SpawnerDefinition definition) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("definition", SpawnerDefinition.CODEC.encodeStart(NbtOps.INSTANCE, definition).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        return nbt;
    }
}
