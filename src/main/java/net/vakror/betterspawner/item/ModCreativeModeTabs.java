package net.vakror.betterspawner.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.block.ModBlocks;
import net.vakror.betterspawner.entity.BetterSpawnerModifier;

import java.util.List;

public class ModCreativeModeTabs {
    public static final CreativeModeTab SPAWNERS_TAB = new CreativeModeTab("betterspawner") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.SPAWNER);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            ModItems.addAllItemsToList();
            List<ItemStack> stacks = ModItems.ALL_SPAWNER_STACKS;
            items.addAll(ModItems.ALL_SPAWNER_STACKS);
        }
    };

    public static void register() {
        BetterSpawnerMod.LOGGER.info("Registering Creative Mode Tabs For " + BetterSpawnerMod.MOD_ID + "!");
    }
}
