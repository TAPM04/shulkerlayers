package tapm.shulkerlayers;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tapm.shulkerlayers.component.ShulkerLayersComponents;
import tapm.shulkerlayers.config.ShulkerLayersConfig;
import tapm.shulkerlayers.loot.ShulkerLayersLootTables;

public class ShulkerLayers implements ModInitializer {
	public static final String MOD_ID = "shulkerlayers";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing...");

		ShulkerLayersConfig.load();
		ShulkerLayersComponents.register();
		ShulkerLayersLootTables.registerLootTableModifications();

		LOGGER.info("ShulkerLayers initialized. maxDepth = {}", ShulkerLayersConfig.get().maxDepth);
	}

}
