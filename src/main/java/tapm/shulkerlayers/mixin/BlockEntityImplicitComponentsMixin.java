package tapm.shulkerlayers.mixin;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tapm.shulkerlayers.component.ShulkerLayersComponents;
import tapm.shulkerlayers.duck.LayerAware;

@Mixin(BlockEntity.class)
public abstract class BlockEntityImplicitComponentsMixin {

	@Inject(method = "applyImplicitComponents", at = @At("TAIL"))
	private void shulkerlayers$readLayer(DataComponentGetter components, CallbackInfo ci) {
		if (!((BlockEntity) (Object) this instanceof ShulkerBoxBlockEntity)) {
			return;
		}
		LayerAware self = (LayerAware) this;
		Integer layer = components.get(ShulkerLayersComponents.LAYER);
		self.shulkerlayers$setLayer(layer != null ? layer : 0);
	}

	@Inject(method = "collectImplicitComponents", at = @At("TAIL"))
	private void shulkerlayers$writeLayer(DataComponentMap.Builder builder, CallbackInfo ci) {
		if (!((BlockEntity) (Object) this instanceof ShulkerBoxBlockEntity)) {
			return;
		}
		LayerAware self = (LayerAware) this;
		int layer = self.shulkerlayers$getLayer();
		if (layer > 0) {
			builder.set(ShulkerLayersComponents.LAYER, layer);
		}
	}
}
