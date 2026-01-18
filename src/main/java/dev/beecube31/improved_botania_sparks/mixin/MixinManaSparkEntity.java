package dev.beecube31.improved_botania_sparks.mixin;

import dev.beecube31.improved_botania_sparks.interfaces.IMixinManaSparkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.entity.ManaSparkEntity;

import java.util.Collection;

@Mixin(ManaSparkEntity.class)
public abstract class MixinManaSparkEntity implements IMixinManaSparkEntity {
    @Shadow
    public abstract ManaReceiver getAttachedManaReceiver();

    @Shadow
    private boolean receiverWasFull;

    @Shadow
    private boolean shouldFilterTransfers;

    @Shadow
    protected abstract void particlesTowards(Entity e);

    @Shadow
    private boolean firstTick;

    @Override
    public void ibs$checkReceiverFull() {
        var receiver = getAttachedManaReceiver();
        if (receiver != null) {
            receiverWasFull = receiver.isFull();
        } else {
            receiverWasFull = true;
        }
    }

    @Override
    public void ibs$checkShouldFilterTransfers(Collection<ManaSpark> transfers, ForgeConfigSpec.IntValue transferRate, ManaReceiver receiver) {
        if (!transfers.isEmpty()) {
            int manaTotal = Math.min(transferRate.get() * transfers.size(), receiver.getCurrentMana());
            int count = transfers.size();
            int manaSpent = 0;

            if (manaTotal > 0) {

                for (ManaSpark spark : transfers) {
                    count--;
                    SparkAttachable attached = spark.getAttachedTile();
                    var attachedReceiver = spark.getAttachedManaReceiver();
                    if (attached == null || attachedReceiver == null || attachedReceiver.isFull() || spark.areIncomingTransfersDone()) {
                        shouldFilterTransfers = true;
                        continue;
                    }

                    int spend = Math.min(attached.getAvailableSpaceForMana(), (manaTotal - manaSpent) / (count + 1));
                    attachedReceiver.receiveMana(spend);
                    manaSpent += spend;
                    ((IMixinManaSparkEntity) spark).ibs$checkReceiverFull();

                    particlesTowards(spark.entity());
                }
                receiver.receiveMana(-manaSpent);
            }
        }

        firstTick = false;
    }
}
