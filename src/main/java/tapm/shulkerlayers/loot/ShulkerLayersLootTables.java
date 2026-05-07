package tapm.shulkerlayers.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import tapm.shulkerlayers.component.ShulkerLayersComponents;

public class ShulkerLayersLootTables {

    public static void registerLootTableModifications() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (!source.isBuiltin()) return;
            if (!"minecraft".equals(key.identifier().getNamespace())) return;
            if (!key.identifier().getPath().contains("shulker_box")) return;

            tableBuilder.modifyPools(poolBuilder -> {
                poolBuilder.apply(
                        CopyComponentsFunction.copyComponentsFromBlockEntity(
                                        net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY
                                )
                                .include(ShulkerLayersComponents.LAYER)
                                .build()
                );
            });
        });
    }
}
