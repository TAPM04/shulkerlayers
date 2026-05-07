package tapm.shulkerlayers.component;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import tapm.shulkerlayers.ShulkerLayers;

public final class ShulkerLayersComponents {

	private ShulkerLayersComponents() {
	}

	public static final DataComponentType<Integer> LAYER = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.intRange(0, 64))
			.networkSynchronized(ByteBufCodecs.VAR_INT)
			.build();

	public static void register() {
		Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				Identifier.fromNamespaceAndPath(ShulkerLayers.MOD_ID, "layer"),
				LAYER
		);
	}

	public static int getLayer(ItemStack stack) {
		Integer value = stack.get(LAYER);
		return value != null ? value : 0;
	}
}
