package dev.beecube31.improved_botania_sparks.entities;

import dev.beecube31.improved_botania_sparks.core.IBSEntities;
import dev.beecube31.improved_botania_sparks.core.IBSItems;
import dev.beecube31.improved_botania_sparks.interfaces.IMixinManaSparkEntity;
import dev.beecube31.improved_botania_sparks.mixin.AccessorManaSparkEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.SparkAugmentItem;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

public class EntityImprovedSpark extends ManaSparkEntity {
    private final ForgeConfigSpec.IntValue transferRate;
    private final ForgeConfigSpec.IntValue workRange;

    public EntityImprovedSpark(EntityType<ManaSparkEntity> type, Level world, ForgeConfigSpec.IntValue transferRate, ForgeConfigSpec.IntValue workRange) {
        super(type, world);

        this.transferRate = transferRate;
        this.workRange = workRange;
    }

    private void dropAndKill() {
        SparkUpgradeType upgrade = this.getUpgrade();
        this.spawnAtLocation(new ItemStack(this.getSparkItem()), 0.0F);
        if (upgrade != SparkUpgradeType.NONE) {
            this.spawnAtLocation(SparkAugmentItem.getByType(upgrade), 0.0F);
        }

        this.discard();
    }

    private void particlesTowards(Entity e) {
        XplatAbstractions.INSTANCE.sendToTracking(this, new BotaniaEffectPacket(EffectType.SPARK_MANA_FLOW, this.getX(), this.getY(), this.getZ(),
                this.getId(), e.getId(), ColorHelper.getColorValue(this.getNetwork())));
    }

    private void notifyOthers(DyeColor network) {
        for (ManaSpark spark : SparkHelper.getSparksAround(this.level(), this.getX(), this.getY() + (double)(this.getBbHeight() / 2.0F), this.getZ(), network)) {
            spark.updateTransfers();
        }
    }


    @Override
    public void tick() {
        if (level().isClientSide) {
            return;
        }

        if (((AccessorManaSparkEntity) this).firstTick()) {
            updateTransfers();
        }

        SparkAttachable tile = getAttachedTile();
        if (tile == null) {
            dropAndKill();
            return;
        }
        var receiver = getAttachedManaReceiver();

        SparkUpgradeType upgrade = getUpgrade();
        Collection<ManaSpark> transfers = getOutgoingTransfers();

        switch (upgrade) {
            case DISPERSIVE -> {
                AABB aabb = VecHelper.boxForRange(
                        this.position().with(Direction.Axis.Y, getY() + (getBbHeight() / 2.0)),
                        this.workRange.get());
                List<Player> players = level().getEntitiesOfClass(Player.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);

                Map<Player, Map<ManaItem, Integer>> receivingPlayers = new HashMap<>();

                ItemStack input = new ItemStack(getSparkItem());
                for (Player player : players) {
                    List<ItemStack> stacks = new ArrayList<>();
                    stacks.addAll(player.getInventory().items);
                    stacks.addAll(player.getInventory().armor);

                    Container inv = BotaniaAPI.instance().getAccessoriesInventory(player);
                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        stacks.add(inv.getItem(i));
                    }

                    for (ItemStack stack : stacks) {
                        var manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
                        if (stack.isEmpty() || manaItem == null) {
                            continue;
                        }

                        if (manaItem.canReceiveManaFromItem(input)) {
                            Map<ManaItem, Integer> receivingStacks;
                            boolean add = false;
                            if (!receivingPlayers.containsKey(player)) {
                                add = true;
                                receivingStacks = new HashMap<>();
                            } else {
                                receivingStacks = receivingPlayers.get(player);
                            }

                            int recv = Math.min(receiver.getCurrentMana(), Math.min(this.transferRate.get(), manaItem.getMaxMana() - manaItem.getMana()));
                            if (recv > 0) {
                                receivingStacks.put(manaItem, recv);
                                if (add) {
                                    receivingPlayers.put(player, receivingStacks);
                                }
                            }
                        }
                    }
                }

                if (!receivingPlayers.isEmpty()) {
                    List<Player> keys = new ArrayList<>(receivingPlayers.keySet());
                    Collections.shuffle(keys);
                    Player player = keys.iterator().next();

                    Map<ManaItem, Integer> items = receivingPlayers.get(player);
                    var e = items.entrySet().iterator().next();
                    ManaItem manaItem = e.getKey();
                    int cost = e.getValue();
                    int manaToPut = Math.min(receiver.getCurrentMana(), cost);
                    manaItem.addMana(manaToPut);
                    receiver.receiveMana(-manaToPut);
                    particlesTowards(player);
                }

            }
            case DOMINANT -> {
                if (((AccessorManaSparkEntity) this).receiverWasFull() && !receiver.isFull()) {
                    updateTransfers();
                }
                if (!((AccessorManaSparkEntity) this).transfersTowardsSelfToRegister().isEmpty()) {
                    ((AccessorManaSparkEntity) this).transfersTowardsSelfToRegister().remove(((AccessorManaSparkEntity) this).transfersTowardsSelfToRegister().size() - 1)
                            .registerTransfer(this);
                }
            }

            default -> {
                if (((AccessorManaSparkEntity) this).receiverWasFull() && !receiver.isFull()) {
                    notifyOthers(getNetwork());
                }
            }
        }

        ((IMixinManaSparkEntity) this).ibs$checkReceiverFull();

        ((IMixinManaSparkEntity) this).ibs$checkShouldFilterTransfers(transfers, this.transferRate, receiver);
    }

    @Override
    protected Item getSparkItem() {
        EntityType<?> type = this.getType();
        if (type == IBSEntities.NILFHEIM_SPARK) return IBSItems.NILFHEIM_SPARK;
        if (type == IBSEntities.MUSPELHEIM_SPARK) return IBSItems.MUSPELHEIM_SPARK;
        if (type == IBSEntities.ALFHEIM_SPARK) return IBSItems.ALFHEIM_SPARK;
        if (type == IBSEntities.ASGARD_SPARK) return IBSItems.ASGARD_SPARK;
        return BotaniaItems.spark;
    }
}
