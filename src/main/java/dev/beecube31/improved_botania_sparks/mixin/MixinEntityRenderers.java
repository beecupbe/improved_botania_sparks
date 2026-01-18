package dev.beecube31.improved_botania_sparks.mixin;

import dev.beecube31.improved_botania_sparks.core.IBSEntities;
import dev.beecube31.improved_botania_sparks.render.ImprovedSparkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.render.entity.EntityRenderers;

@Mixin(value = EntityRenderers.class, remap = false)
public class MixinEntityRenderers {
    @Inject(method = "registerEntityRenderers", at = @At("TAIL"), remap = false)
    private static void ibs$registerOwnRenders(EntityRenderers.EntityRendererConsumer consumer, CallbackInfo ci) {
        consumer.accept(IBSEntities.ALFHEIM_SPARK, (ctx) -> new ImprovedSparkRenderer(ctx, IBSEntities.Variant.ALFHEIM));
        consumer.accept(IBSEntities.NILFHEIM_SPARK, (ctx) -> new ImprovedSparkRenderer(ctx, IBSEntities.Variant.NILFHEIM));
        consumer.accept(IBSEntities.ASGARD_SPARK, (ctx) -> new ImprovedSparkRenderer(ctx, IBSEntities.Variant.ASGARD));
        consumer.accept(IBSEntities.MUSPELHEIM_SPARK, (ctx) -> new ImprovedSparkRenderer(ctx, IBSEntities.Variant.MUSPELHEIM));
    }
}
