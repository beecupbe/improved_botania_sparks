package dev.beecube31.improved_botania_sparks.interfaces;


import net.minecraftforge.common.ForgeConfigSpec;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;

import java.util.Collection;

public interface IMixinManaSparkEntity {
    void ibs$checkReceiverFull();

    void ibs$checkShouldFilterTransfers(Collection<ManaSpark> transfers, ForgeConfigSpec.IntValue transferRate, ManaReceiver receiver);
}
