package dev.beecube31.improved_botania_sparks.core;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.common.item.CustomCreativeTabContents;

import java.util.*;
import java.util.function.*;

@Mod(IBS.MODID)
@Mod.EventBusSubscriber(modid = IBS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBS {
    public static final String MODID = "improved_botania_sparks";

    private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

    public IBS() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::addCreative);

        IBSConfig.register(net.minecraftforge.fml.ModLoadingContext.get());

        bindForItems(IBSItems::registerItems);
        bind(Registries.ENTITY_TYPE, IBSEntities::registerEntities);

        bus.addListener((BuildCreativeModeTabContentsEvent e) -> {
            if (e.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
                for (Item item : this.itemsToAddToCreativeTab) {
                    if (item instanceof CustomCreativeTabContents cc) {
                        cc.addToCreativeTab(item, e);
                    } else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
                        cc.addToCreativeTab(item, e);
                    } else {
                        e.accept(item);
                    }
                }
            }
        });
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void bindForItems(Consumer<BiConsumer<Item, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                source.accept((t, rl) -> {
                    itemsToAddToCreativeTab.add(t);
                    event.register(Registries.ITEM, rl, () -> t);
                });
            }
        });
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
            event.accept(IBSItems.NILFHEIM_SPARK);
            event.accept(IBSItems.MUSPELHEIM_SPARK);
            event.accept(IBSItems.ALFHEIM_SPARK);
            event.accept(IBSItems.ASGARD_SPARK);
        }
    }
}