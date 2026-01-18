package dev.beecube31.improved_botania_sparks.datagen;

import dev.beecube31.improved_botania_sparks.core.IBS;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IBS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBSDatagen {

    public IBSDatagen() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(true, new IBSRecipeProvder(output));
        }
    }
}
