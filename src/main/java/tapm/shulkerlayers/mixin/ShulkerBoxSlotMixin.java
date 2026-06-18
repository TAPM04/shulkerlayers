package tapm.shulkerlayers.mixin;

import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tapm.shulkerlayers.component.ShulkerLayersComponents;
import tapm.shulkerlayers.config.ShulkerLayersConfig;
import tapm.shulkerlayers.duck.LayerAware;

@Mixin(ShulkerBoxSlot.class)
public abstract class ShulkerBoxSlotMixin extends Slot {

	public ShulkerBoxSlotMixin(net.minecraft.world.Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}

	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	private void shulkerlayers$allowNestedLayer(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (!(Block.byItem(itemStack.getItem()) instanceof ShulkerBoxBlock)) {
			return;
		}

		// Treat every layer above maxDepth as if it were exactly maxDepth
		int maxDepth = ShulkerLayersConfig.get().maxDepth;
		int incomingLayer = Math.min(ShulkerLayersComponents.getLayer(itemStack), maxDepth);
		int hostLayer = Math.min(shulkerlayers$hostLayer(), maxDepth);

		if (incomingLayer < hostLayer) {
			cir.setReturnValue(true);
		}
	}

	@Unique
	private int shulkerlayers$hostLayer() {
		if (this.container instanceof LayerAware layerAware) {
			return layerAware.shulkerlayers$getLayer();
		}
		return 0;
	}
}
