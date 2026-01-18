package dev.beecube31.improved_botania_sparks.core;

import dev.beecube31.improved_botania_sparks.items.ItemAlfheimSpark;
import dev.beecube31.improved_botania_sparks.items.ItemAsgardSpark;
import dev.beecube31.improved_botania_sparks.items.ItemMuspelheimSpark;
import dev.beecube31.improved_botania_sparks.items.ItemNilfheimSpark;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class IBSItems {
    static final Map<ResourceLocation, Item> ALL = new LinkedHashMap<>();

    public static final Item NILFHEIM_SPARK = make(prefix("nilfheim_spark"), new ItemNilfheimSpark(defaultBuilder()));
    public static final Item MUSPELHEIM_SPARK = make(prefix("muspelheim_spark"), new ItemMuspelheimSpark(defaultBuilder()));
    public static final Item ALFHEIM_SPARK = make(prefix("alfheim_spark"), new ItemAlfheimSpark(defaultBuilder()));
    public static final Item ASGARD_SPARK = make(prefix("asgard_spark"), new ItemAsgardSpark(defaultBuilder()));

    public static Item.Properties defaultBuilder() {
        return XplatAbstractions.INSTANCE.defaultItemBuilder();
    }

    private static <T extends Item> T make(ResourceLocation id, T item) {
        var old = ALL.put(id, item);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return item;
    }

    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(IBS.MODID, path);
    }
}
