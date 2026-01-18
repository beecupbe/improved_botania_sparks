package dev.beecube31.improved_botania_sparks.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = IBS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBSConfig {
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig {

        public final ForgeConfigSpec.IntValue nilfheimSparkTransferRate;
        public final ForgeConfigSpec.IntValue muspelheimSparkTransferRate;
        public final ForgeConfigSpec.IntValue alfheimSparkTransferRate;
        public final ForgeConfigSpec.IntValue asgardSparkTransferRate;

        public final ForgeConfigSpec.IntValue nilfheimSparkWorkRange;
        public final ForgeConfigSpec.IntValue muspelheimSparkWorkRange;
        public final ForgeConfigSpec.IntValue alfheimSparkWorkRange;
        public final ForgeConfigSpec.IntValue asgardSparkWorkRange;


        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Sparks configuration").push("sparks");

            nilfheimSparkTransferRate = builder
                    .defineInRange("nilfheimSparkTransferRate", 10000 * 20, 0, Integer.MAX_VALUE);

            muspelheimSparkTransferRate = builder
                    .defineInRange("muspelheimSparkTransferRate", 10000 * 200, 0, Integer.MAX_VALUE);

            alfheimSparkTransferRate = builder
                    .defineInRange("alfheimSparkTransferRate", 10000 * 2000, 0, Integer.MAX_VALUE);

            asgardSparkTransferRate = builder
                    .defineInRange("asgardSparkTransferRate", 10000 * 10000, 0, Integer.MAX_VALUE);



            nilfheimSparkWorkRange = builder
                    .defineInRange("nilfheimSparkWorkRange", 16, 0, Integer.MAX_VALUE);

            muspelheimSparkWorkRange = builder
                    .defineInRange("muspelheimSparkWorkRange", 20, 0, Integer.MAX_VALUE);

            alfheimSparkWorkRange = builder
                    .defineInRange("alfheimSparkWorkRange", 24, 0, Integer.MAX_VALUE);

            asgardSparkWorkRange = builder
                    .defineInRange("asgardSparkWorkRange", 28, 0, Integer.MAX_VALUE);


            builder.pop();
        }
    }

    public static void register(net.minecraftforge.fml.ModLoadingContext context) {
        context.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, COMMON_SPEC, "mythic_botania_sparks-common.toml");
    }
}
