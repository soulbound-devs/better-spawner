package net.vakror.betterspawner.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.vakror.betterspawner.spawner.SpawnerDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModSpawnerItem extends BlockItem {
    public ModSpawnerItem(Block block, Properties pProperties) {
        super(block, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (pStack.getTag() != null) {
            SpawnerDefinition definition = SpawnerDefinition.CODEC.parse(NbtOps.INSTANCE, pStack.getTag().getCompound("BlockEntityTag").getCompound("definition")).resultOrPartial(error -> {
                throw new IllegalStateException(error);
            }).get();
            tooltip.add(Component.literal("Type: " + definition.getSpawnerName()));
        }

        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (pStack.getTag() != null) {
            SpawnerDefinition definition = SpawnerDefinition.CODEC.parse(NbtOps.INSTANCE, pStack.getTag().getCompound("BlockEntityTag").getCompound("definition")).resultOrPartial(error -> {throw new IllegalStateException(error);}).get();
            return Component.literal(definition.getSpawnerName());
        }
        return super.getName(pStack);
    }
}
