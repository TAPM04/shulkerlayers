package tapm.shulkerlayers.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tapm.shulkerlayers.component.ShulkerLayersComponents;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Inject(
			method = "addDetailsToTooltip",
			at = @At("TAIL")
	)
	private void shulkerlayers$appendLayerTooltip(
			Item.TooltipContext context,
			TooltipDisplay display,
			@Nullable Player player,
			TooltipFlag tooltipFlag,
			Consumer<Component> builder,
			CallbackInfo ci
	) {
		ItemStack self = (ItemStack) (Object) this;
		if (!(Block.byItem(self.getItem()) instanceof ShulkerBoxBlock)) {
			return;
		}

		int layer = ShulkerLayersComponents.getLayer(self);
		if (layer <= 0) {
			return;
		}

		builder.accept(
				Component.translatable("tooltip.shulkerlayers.layer", layer)
						.withStyle(ChatFormatting.AQUA)
		);
	}
}
