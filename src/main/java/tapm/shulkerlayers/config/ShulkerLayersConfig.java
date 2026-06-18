package tapm.shulkerlayers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import tapm.shulkerlayers.ShulkerLayers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public final class ShulkerLayersConfig {

	public static final class Data {
		public int maxDepth = 3;
	}

	private static Data INSTANCE = new Data();

	private ShulkerLayersConfig() {
	}

	public static Data get() {
		return INSTANCE;
	}

	public static void load() {
		Path configDir = FabricLoader.getInstance().getConfigDir();
		Path configFile = configDir.resolve(ShulkerLayers.MOD_ID + ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			if (Files.notExists(configFile)) {
				Files.createDirectories(configDir);
				Files.writeString(configFile, gson.toJson(INSTANCE));
				ShulkerLayers.LOGGER.info("Wrote default config to: {}", configFile);
				return;
			}

			String json = Files.readString(configFile);
			Data parsed = gson.fromJson(json, Data.class);
			if (parsed == null) {
				ShulkerLayers.LOGGER.warn("Config was empty, keeping defaults.");
				return;
			}

			// Validation: clamp negative or absurdly large depths.
			if (parsed.maxDepth < 0) {
				ShulkerLayers.LOGGER.warn("maxDepth < 0 is invalid, clamping to 0.");
				parsed.maxDepth = 0;
			}
			if (parsed.maxDepth > 64) {
				ShulkerLayers.LOGGER.warn("maxDepth > 64 is invalid, clamping to 64.");
				parsed.maxDepth = 64;
			}

			INSTANCE = parsed;
			ShulkerLayers.LOGGER.info("Config loaded: maxDepth={}", INSTANCE.maxDepth);
		} catch (JsonSyntaxException e) {
			ShulkerLayers.LOGGER.warn("Config file is malformed, using defaults: {}", e.getMessage());
		} catch (IOException e) {
			ShulkerLayers.LOGGER.warn("Config I/O error, using defaults: {}", e.getMessage());
		}
	}

	public static void save() {
		Path configDir = FabricLoader.getInstance().getConfigDir();
		Path configFile = configDir.resolve(ShulkerLayers.MOD_ID + ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			Files.createDirectories(configDir);
			Files.writeString(configFile, gson.toJson(INSTANCE));
			ShulkerLayers.LOGGER.info("Config saved: maxDepth={}", INSTANCE.maxDepth);
		} catch (IOException e) {
			ShulkerLayers.LOGGER.warn("Config I/O error while saving: {}", e.getMessage());
		}
	}
}
