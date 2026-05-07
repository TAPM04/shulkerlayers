package tapm.shulkerlayers.mixin;

import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tapm.shulkerlayers.duck.LayerAware;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin implements LayerAware {

	@Unique
	private int shulkerlayers$layer = 0;

	@Override
	public int shulkerlayers$getLayer() {
		return this.shulkerlayers$layer;
	}

	@Override
	public void shulkerlayers$setLayer(int layer) {
		this.shulkerlayers$layer = layer;
	}

	@Inject(method = "saveAdditional", at = @At("TAIL"))
	private void shulkerlayers$saveLayer(ValueOutput output, CallbackInfo ci) {
		if (this.shulkerlayers$layer > 0) {
			output.putInt("shulkerlayers_layer", this.shulkerlayers$layer);
		}
	}

	@Inject(method = "loadAdditional", at = @At("TAIL"))
	private void shulkerlayers$loadLayer(ValueInput input, CallbackInfo ci) {
		this.shulkerlayers$layer = input.getIntOr("shulkerlayers_layer", 0);
	}
}
