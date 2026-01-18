package dev.beecube31.improved_botania_sparks.core;

import dev.beecube31.improved_botania_sparks.entities.EntityImprovedSpark;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import vazkii.botania.common.entity.ManaSparkEntity;

import java.util.function.BiConsumer;

public class IBSEntities {
    public enum Variant {
        NILFHEIM, MUSPELHEIM, ALFHEIM, ASGARD
    }

    public static final EntityType<ManaSparkEntity> NILFHEIM_SPARK = EntityType.Builder
            .<ManaSparkEntity>of((type, level) -> new EntityImprovedSpark(type, level, IBSConfig.COMMON.nilfheimSparkTransferRate, IBSConfig.COMMON.nilfheimSparkWorkRange), MobCategory.MISC)
            .sized(0.2F, 0.5F)
            .fireImmune()
            .clientTrackingRange(4)
            .updateInterval(8)
            .build("nilfheim_spark");

    public static final EntityType<ManaSparkEntity> MUSPELHEIM_SPARK = EntityType.Builder
            .<ManaSparkEntity>of((type, level) -> new EntityImprovedSpark(type, level, IBSConfig.COMMON.muspelheimSparkTransferRate, IBSConfig.COMMON.muspelheimSparkWorkRange), MobCategory.MISC)
            .sized(0.2F, 0.5F)
            .fireImmune()
            .clientTrackingRange(4)
            .updateInterval(6)
            .build("muspelheim_spark");

    public static final EntityType<ManaSparkEntity> ALFHEIM_SPARK = EntityType.Builder
            .<ManaSparkEntity>of((type, level) -> new EntityImprovedSpark(type, level, IBSConfig.COMMON.alfheimSparkTransferRate, IBSConfig.COMMON.alfheimSparkWorkRange), MobCategory.MISC)
            .sized(0.2F, 0.5F)
            .fireImmune()
            .clientTrackingRange(4)
            .updateInterval(4)
            .build("alfheim_spark");

    public static final EntityType<ManaSparkEntity> ASGARD_SPARK = EntityType.Builder
            .<ManaSparkEntity>of((type, level) -> new EntityImprovedSpark(type, level, IBSConfig.COMMON.asgardSparkTransferRate, IBSConfig.COMMON.asgardSparkWorkRange), MobCategory.MISC)
            .sized(0.2F, 0.5F)
            .fireImmune()
            .clientTrackingRange(4)
            .updateInterval(2)
            .build("asgard_spark");

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(IBS.MODID, path);
    }

    public static void registerEntities(BiConsumer<EntityType<?>, ResourceLocation> r) {
        r.accept(NILFHEIM_SPARK, prefix("nilfheim_spark"));
        r.accept(MUSPELHEIM_SPARK, prefix("muspelheim_spark"));
        r.accept(ALFHEIM_SPARK, prefix("alfheim_spark"));
        r.accept(ASGARD_SPARK, prefix("asgard_spark"));

    }
}
