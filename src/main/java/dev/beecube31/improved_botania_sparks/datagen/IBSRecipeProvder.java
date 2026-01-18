package dev.beecube31.improved_botania_sparks.datagen;


import dev.beecube31.improved_botania_sparks.core.IBSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

public class IBSRecipeProvder extends RecipeProvider {

    public IBSRecipeProvder(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, IBSItems.NILFHEIM_SPARK)
                .define('P', BotaniaItems.runeWrath)
                .define('S', BotaniaItems.spark)
                .define('T', BotaniaItems.terrasteel)
                .pattern(" S ")
                .pattern("PTP")
                .pattern(" S ")
                .unlockedBy("has_string", has(BotaniaItems.spark))
                .save(consumer);

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, IBSItems.MUSPELHEIM_SPARK)
                .define('P', BotaniaItems.runeEnvy)
                .define('S', IBSItems.NILFHEIM_SPARK)
                .define('T', BotaniaItems.lifeEssence)
                .pattern(" S ")
                .pattern("PTP")
                .pattern(" S ")
                .unlockedBy("has_string", has(IBSItems.NILFHEIM_SPARK))
                .save(consumer);

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, IBSItems.ALFHEIM_SPARK)
                .define('P', BotaniaItems.runePride)
                .define('S', IBSItems.MUSPELHEIM_SPARK)
                .define('T', BotaniaItems.gaiaIngot)
                .define('R', BotaniaItems.lifeEssence)
                .pattern("RSR")
                .pattern("PTP")
                .pattern("RSR")
                .unlockedBy("has_string", has(IBSItems.MUSPELHEIM_SPARK))
                .save(consumer);

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, IBSItems.ASGARD_SPARK)
                .define('P', BotaniaItems.runeLust)
                .define('S', IBSItems.ALFHEIM_SPARK)
                .define('T', BotaniaItems.gaiaIngot)
                .define('R', BotaniaBlocks.terrasteelBlock)
                .pattern("PSP")
                .pattern("RTR")
                .pattern("PSP")
                .unlockedBy("has_string", has(IBSItems.ALFHEIM_SPARK))
                .save(consumer);

    }
}
