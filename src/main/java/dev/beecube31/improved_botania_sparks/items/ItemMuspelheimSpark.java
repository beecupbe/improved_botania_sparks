package dev.beecube31.improved_botania_sparks.items;

import dev.beecube31.improved_botania_sparks.core.IBSConfig;
import dev.beecube31.improved_botania_sparks.core.IBSEntities;
import dev.beecube31.improved_botania_sparks.entities.EntityImprovedSpark;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.item.ManaSparkItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class ItemMuspelheimSpark extends ManaSparkItem {
    public ItemMuspelheimSpark(Properties builder) {
        super(builder);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand())
                ? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide)
                : InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
        tooltip.add(Component.translatable("ibs.transfer_rate.formatted", IBSConfig.COMMON.muspelheimSparkTransferRate.get())
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));

        tooltip.add(Component.translatable("ibs.work_range.formatted", IBSConfig.COMMON.muspelheimSparkWorkRange.get())
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack) {
        var attach = XplatAbstractions.INSTANCE.findSparkAttachable(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), Direction.UP);
        if (attach != null) {
            if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
                if (!world.isClientSide) {
                    stack.shrink(1);
                    EntityImprovedSpark spark = new EntityImprovedSpark(IBSEntities.MUSPELHEIM_SPARK, world, IBSConfig.COMMON.muspelheimSparkTransferRate, IBSConfig.COMMON.muspelheimSparkWorkRange);
                    spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
                    world.addFreshEntity(spark);
                    attach.attachSpark(spark);
                }
                return true;
            }
        }
        return false;
    }
}
