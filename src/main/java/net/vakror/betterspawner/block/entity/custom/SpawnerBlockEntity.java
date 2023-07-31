package net.vakror.betterspawner.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.betterspawner.BetterSpawnerMod;
import net.vakror.betterspawner.block.entity.ModBlockEntities;
import net.vakror.betterspawner.spawner.SpawnerDefinition;

import java.util.*;

public class SpawnerBlockEntity extends BlockEntity {
    private SpawnerDefinition definition;
    private int currentDelay;
    Map<String, Integer> spawnedMobs = new HashMap<>();
    private boolean locked;
    private int totalMobs;

    public SpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPAWNER.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (definition != null) {
            tag.put("definition", SpawnerDefinition.CODEC.encodeStart(NbtOps.INSTANCE, definition).resultOrPartial((error) -> {
                throw new IllegalStateException(error);
            }).get());
        }
        tag.putInt("delay", currentDelay);
        tag.putInt("totalMobs", totalMobs);
        tag.putBoolean("locked", locked);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag pTag) {
        if (pTag.contains("definition")) {
            definition = SpawnerDefinition.CODEC.parse(NbtOps.INSTANCE, pTag.getCompound("definition")).resultOrPartial((error -> {
                throw new IllegalStateException(error);
            })).get();
        }
        currentDelay = pTag.getInt("delay");
        totalMobs = pTag.getInt("totalMobs");
        locked = pTag.getBoolean("locked");
        super.load(pTag);
    }

    private boolean isNearPlayer(Level pLevel, BlockPos pPos) {
        return pLevel.hasNearbyAlivePlayer((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, (double) this.definition.getPlayerDistanceFromSpawner());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity blockEntity) {
        if (!level.isClientSide && blockEntity.definition != null && blockEntity.isNearPlayer(level, pos)) {
            blockEntity.currentDelay--;
            if (blockEntity.currentDelay <= 0) {
                blockEntity.currentDelay = blockEntity.definition.getTickDelay();
                if (!blockEntity.definition.isInfinite()) {
                    finiteEntityTick(level, pos, blockEntity);
                } else {
                    infiniteEntityTick(level, pos, blockEntity);
                }
            }
        }
    }

    private static void finiteEntityTick(Level level, BlockPos pos, SpawnerBlockEntity blockEntity) {
        for (int i = 0; i < blockEntity.definition.getAmount(); i++) {
            List<String> possibleMobTypes = new ArrayList<>();
            for (String mobType : blockEntity.definition.getMobTypes()) {
                if (!blockEntity.locked) {
                    if (blockEntity.totalMobs <= blockEntity.definition.getMaxMobs()) {
                        if (!blockEntity.spawnedMobs.containsKey(mobType)) {
                            possibleMobTypes.add(mobType);
                        } else if (blockEntity.definition.getMaxForEachMobType().get(mobType) != null && blockEntity.spawnedMobs.get(mobType) <= blockEntity.definition.getMaxForEachMobType().get(mobType)) {
                            possibleMobTypes.add(mobType);
                        }
                    } else {
                        blockEntity.locked = true;
                    }
                }
            }
            if (possibleMobTypes.size() > 0) {
                String mobToSpawn = possibleMobTypes.get(new Random().nextInt(possibleMobTypes.size()));
                if (ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)) == null) {
                    BetterSpawnerMod.LOGGER.error("Entity Type: " + mobToSpawn + " not found. Skipping Spawn!");
                } else {
                    blockEntity.spawnedMobs.put(mobToSpawn, blockEntity.spawnedMobs.get(mobToSpawn) == null ? 1 : blockEntity.spawnedMobs.get(mobToSpawn) + 1);
                    blockEntity.totalMobs++;

                    RandomSource randomSource = RandomSource.create();
                    double d0 = (double) pos.getX() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                    double d1 = pos.getY() + randomSource.nextInt(3) - 1;
                    double d2 = (double) pos.getZ() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                    int attempts = 0;
                    while (!level.noCollision(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)).getAABB(d0, d1, d2))) {
                        if (attempts >= 50) {
                            break;
                        }
                        d0 = (double) pos.getX() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                        d1 = pos.getY() + randomSource.nextInt(3) - 1;
                        d2 = (double) pos.getZ() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                        attempts++;
                    }
                    Entity entity = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)).spawn((ServerLevel) level, null, null, null, new BlockPos(d0, d1, d2), MobSpawnType.SPAWN_EGG, false, false);
                    if (entity instanceof LivingEntity living) {
                        blockEntity.definition.getEntityModifiers().forEach((mobType, mod) -> {
                            if (mobToSpawn.equals(mobType) && blockEntity.apply(mod.getChance())) {
                                mod.getModifiers().forEach((attribute, modifier) -> {
                                    if (living.getAttribute(Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute)))) != null) {
                                        Objects.requireNonNull(living.getAttribute(Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute))))).addTransientModifier(modifier);
                                    }
                                });
                            }
                        });
                    }
                }
            }
            if (blockEntity.definition.shouldDestroyAfterHittingMaxMobs() && blockEntity.totalMobs >= blockEntity.definition.getMaxMobs()) {
                level.removeBlock(pos, false);
                return;
            }
            if (blockEntity.definition.isSingleUse()) {
                if (blockEntity.definition.shouldDestroyAfterHittingMaxMobs()) {
                    level.removeBlock(pos, false);
                    return;
                } else {
                    blockEntity.locked = true;
                }
            }
        }
    }

    private static void infiniteEntityTick(Level level, BlockPos pos, SpawnerBlockEntity blockEntity) {
        for (int i = 0; i < blockEntity.definition.getAmount(); i++) {
            List<String> possibleMobTypes = new ArrayList<>();
            for (String mobType : blockEntity.definition.getMobTypes()) {
                if (!blockEntity.spawnedMobs.containsKey(mobType)) {
                    possibleMobTypes.add(mobType);
                } else if (blockEntity.definition.getMaxForEachMobType().get(mobType) != null && blockEntity.spawnedMobs.get(mobType) <= (blockEntity.definition.getMaxForEachMobType().get(mobType) <= 0 ? Integer.MAX_VALUE: blockEntity.definition.getMaxForEachMobType().get(mobType))) {
                    possibleMobTypes.add(mobType);
                } else if (blockEntity.definition.getMaxForEachMobType().get(mobType) == null) {
                    possibleMobTypes.add(mobType);
                }
            }
            if (possibleMobTypes.size() > 0) {
                String mobToSpawn = possibleMobTypes.get(new Random().nextInt(possibleMobTypes.size()));
                if (ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)) == null) {
                    BetterSpawnerMod.LOGGER.error("Entity Type: " + mobToSpawn + " not found. Skipping Spawn!");
                } else {
                    blockEntity.spawnedMobs.put(mobToSpawn, blockEntity.spawnedMobs.get(mobToSpawn) == null ? 1 : blockEntity.spawnedMobs.get(mobToSpawn) + 1);
                    blockEntity.totalMobs++;

                    RandomSource randomSource = RandomSource.create();
                    double d0 = (double) pos.getX() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                    double d1 = pos.getY() + randomSource.nextInt(3) - 1;
                    double d2 = (double) pos.getZ() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                    int attempts = 0;
                    while (!level.noCollision(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)).getAABB(d0, d1, d2))) {
                        if (attempts >= 50) {
                            break;
                        }
                        d0 = (double) pos.getX() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                        d1 = pos.getY() + randomSource.nextInt(3) - 1;
                        d2 = (double) pos.getZ() + (randomSource.nextDouble() - randomSource.nextDouble()) * (double) blockEntity.definition.getDistance() + 0.5D;
                        attempts++;
                    }
                    Entity entity = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobToSpawn)).spawn((ServerLevel) level, null, null, null, new BlockPos(d0, d1, d2), MobSpawnType.SPAWN_EGG, false, false);
                    if (entity instanceof LivingEntity living) {
                        blockEntity.definition.getEntityModifiers().forEach((mobType, mod) -> {
                            if (mobToSpawn.equals(mobType) && blockEntity.apply(mod.getChance())) {
                                mod.getModifiers().forEach((attribute, modifier) -> {
                                    if (living.getAttribute(Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute)))) != null) {
                                        Objects.requireNonNull(living.getAttribute(Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute))))).addTransientModifier(modifier);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean apply(float chance) {
        float rand = new Random().nextFloat(1.1f);
        return chance <= (float) Math.floor(rand * 10) / 10;
    }
}