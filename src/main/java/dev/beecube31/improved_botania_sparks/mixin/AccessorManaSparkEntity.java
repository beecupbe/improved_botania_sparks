package dev.beecube31.improved_botania_sparks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.common.entity.ManaSparkEntity;

import java.util.ArrayList;

@Mixin(ManaSparkEntity.class)
public interface AccessorManaSparkEntity {
    @Accessor("firstTick") boolean firstTick();

    @Accessor("receiverWasFull") boolean receiverWasFull();

    @Accessor("transfersTowardsSelfToRegister") ArrayList<ManaSpark> transfersTowardsSelfToRegister();
}
