package tapm.shulkerlayers.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tapm.shulkerlayers.component.ShulkerLayersComponents;
import tapm.shulkerlayers.config.ShulkerLayersConfig;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin {

	@Shadow
	@Final
	private ItemStackTemplate result;

	@Inject(method = "matches", at = @At("RETURN"), cancellable = true)
	private void shulkerlayers$gateByMaxDepth(
			CraftingInput input,
			Level level,
			CallbackInfoReturnable<Boolean> cir) {

		if (!shulkerlayers$isLayerUpgradeRecipe()) {
			return;
		}
		if (!cir.getReturnValueZ()) {
			return;
		}

		int inputLayer = shulkerlayers$findInputShulkerBoxLayer(input);
		int maxDepth = ShulkerLayersConfig.get().maxDepth;
		if (inputLayer >= maxDepth) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
	private void shulkerlayers$buildDynamicOutput(
			CraftingInput input,
			CallbackInfoReturnable<ItemStack> cir) {

		if (!shulkerlayers$isLayerUpgradeRecipe()) {
			return;
		}

		ItemStack vanillaResult = cir.getReturnValue();
		if (vanillaResult.isEmpty()) {
			return;
		}

		ItemStack inputBox = shulkerlayers$findInputShulkerBox(input);
		if (inputBox.isEmpty()) {
			return;
		}

		int inputLayer = ShulkerLayersComponents.getLayer(inputBox);
		int maxDepth = ShulkerLayersConfig.get().maxDepth;
		if (inputLayer >= maxDepth) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		int newLayer = Math.min(inputLayer + 1, maxDepth);

		// copyWithCount preserves the item type (color!) and all components
		// (container contents, custom name, etc.). We only override the layer.
		ItemStack output = inputBox.copyWithCount(vanillaResult.getCount());
		output.set(ShulkerLayersComponents.LAYER, newLayer);
		cir.setReturnValue(output);
	}

	@Unique
	private boolean shulkerlayers$isLayerUpgradeRecipe() {
		return this.result.get(ShulkerLayersComponents.LAYER) != null;
	}

	@Unique
	private ItemStack shulkerlayers$findInputShulkerBox(CraftingInput input) {
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty() && Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Unique
	private int shulkerlayers$findInputShulkerBoxLayer(CraftingInput input) {
		ItemStack box = shulkerlayers$findInputShulkerBox(input);
		return box.isEmpty() ? 0 : ShulkerLayersComponents.getLayer(box);
	}
}
