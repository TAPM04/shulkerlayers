package tapm.shulkerlayers.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import tapm.shulkerlayers.config.ShulkerLayersConfig;


public class ShulkerLayersConfigScreen extends Screen {

    private static final int MIN = 0;
    private static final int MAX = 64;
    private static final int DEFAULT = 3; // mirrors ShulkerLayersConfig.Data#maxDepth

    private final Screen parent;
    private EditBox depthField;

    public ShulkerLayersConfigScreen(Screen parent) {
        super(Component.translatable("screen.shulkerlayers.config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        StringWidget title = new StringWidget(this.title, this.font);
        title.setX((this.width - title.getWidth()) / 2);
        title.setY(28);
        this.addRenderableWidget(title);

        int descWidth = 300;
        MultiLineTextWidget description = new MultiLineTextWidget((this.width - descWidth) / 2, 52,
                Component.translatable("option.shulkerlayers.max_depth.desc"), this.font);
        description.setMaxWidth(descWidth);
        description.setCentered(true);
        this.addRenderableWidget(description);

        StringWidget label = new StringWidget(Component.translatable("option.shulkerlayers.max_depth"), this.font);
        label.setX((this.width - label.getWidth()) / 2);
        label.setY(this.height / 2 - 4);
        this.addRenderableWidget(label);

        this.depthField = new EditBox(this.font, (this.width - 100) / 2, this.height / 2 + 10, 100, 20,
                Component.translatable("option.shulkerlayers.max_depth"));
        this.depthField.setMaxLength(2);
        this.depthField.setValue(Integer.toString(ShulkerLayersConfig.get().maxDepth));
        this.addRenderableWidget(this.depthField);

        int btnW = 150;
        int gap = 8;
        int startX = (this.width - (btnW * 2 + gap)) / 2;
        int btnY = this.height - 32;
        this.addRenderableWidget(Button.builder(Component.translatable("screen.shulkerlayers.reset"),
                        _ -> this.depthField.setValue(Integer.toString(DEFAULT)))
                .bounds(startX, btnY, btnW, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, _ -> this.saveAndClose())
                .bounds(startX + btnW + gap, btnY, btnW, 20).build());
    }

    private void saveAndClose() {
        ShulkerLayersConfig.get().maxDepth = parseClamp(this.depthField.getValue(), ShulkerLayersConfig.get().maxDepth);
        ShulkerLayersConfig.save();
        this.minecraft.gui.setScreen(this.parent);
    }

    private static int parseClamp(String raw, int fallback) {
        try {
            return Math.max(MIN, Math.min(MAX, Integer.parseInt(raw.trim())));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @Override
    public void onClose() {
        // Escape = cancel (no save).
        this.minecraft.gui.setScreen(this.parent);
    }
}
