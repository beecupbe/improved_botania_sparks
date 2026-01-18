package dev.beecube31.improved_botania_sparks.render;

import dev.beecube31.improved_botania_sparks.core.IBS;
import dev.beecube31.improved_botania_sparks.core.IBSEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import vazkii.botania.client.render.entity.BaseSparkRenderer;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.Objects;
import java.util.function.Function;

public class ImprovedSparkRenderer extends BaseSparkRenderer<ManaSparkEntity> {
    private final TextureAtlasSprite dispersiveIcon;
    private final TextureAtlasSprite dominantIcon;
    private final TextureAtlasSprite recessiveIcon;
    private final TextureAtlasSprite isolatedIcon;

    private final TextureAtlasSprite asgardSprite;
    private final TextureAtlasSprite alfheimSprite;
    private final TextureAtlasSprite muspelheimSprite;
    private final TextureAtlasSprite nilfheimSprite;

    private final IBSEntities.Variant associatedEntity;

    public ImprovedSparkRenderer(EntityRendererProvider.Context ctx, IBSEntities.Variant associatedEntity) {
        super(ctx);
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        this.dispersiveIcon = Objects.requireNonNull(atlas.apply(ResourceLocationHelper.prefix("item/spark_upgrade_rune_dispersive")));
        this.dominantIcon = Objects.requireNonNull(atlas.apply(ResourceLocationHelper.prefix("item/spark_upgrade_rune_dominant")));
        this.recessiveIcon = Objects.requireNonNull(atlas.apply(ResourceLocationHelper.prefix("item/spark_upgrade_rune_recessive")));
        this.isolatedIcon = Objects.requireNonNull(atlas.apply(ResourceLocationHelper.prefix("item/spark_upgrade_rune_isolated")));

        this.asgardSprite = Objects.requireNonNull(atlas.apply(IBS.prefix("item/spark_asgard")));
        this.alfheimSprite = Objects.requireNonNull(atlas.apply(IBS.prefix("item/spark_alfheim")));
        this.muspelheimSprite = Objects.requireNonNull(atlas.apply(IBS.prefix("item/spark_muspelheim")));
        this.nilfheimSprite = Objects.requireNonNull(atlas.apply(IBS.prefix("item/spark_nilfheim")));

        this.associatedEntity = associatedEntity;
    }

    public TextureAtlasSprite getSpinningIcon(ManaSparkEntity entity) {
        TextureAtlasSprite var10000;
        switch (entity.getUpgrade()) {
            case NONE -> var10000 = null;
            case DISPERSIVE -> var10000 = this.dispersiveIcon;
            case DOMINANT -> var10000 = this.dominantIcon;
            case RECESSIVE -> var10000 = this.recessiveIcon;
            case ISOLATED -> var10000 = this.isolatedIcon;
            default -> throw new IncompatibleClassChangeError();
        }

        return var10000;
    }

    public TextureAtlasSprite getBaseIcon(ManaSparkEntity entity) {
        return switch (this.associatedEntity) {
            case ASGARD -> this.asgardSprite;
            case ALFHEIM -> this.alfheimSprite;
            case MUSPELHEIM -> this.muspelheimSprite;
            case NILFHEIM -> this.nilfheimSprite;
        };
    }
}
