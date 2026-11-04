package com.crow.crowculling;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.literal("CrowCulling"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        CrowConfig cfg = CrowConfig.INSTANCE;
        int w = 220, x = width / 2 - w / 2, y = height / 4;

        addDrawableChild(ButtonWidget.builder(hideToggleText(), b -> {
            cfg.hidePlayers = !cfg.hidePlayers;
            b.setMessage(hideToggleText());
        }).dimensions(x, y, w, 20).build());

        addDrawableChild(new IntSlider(x, y + 28, w, 20, 0, 25, cfg.range,
                v -> v == 0 ? "Hide distance: 0 (only fought players)" : "Hide distance: " + v + " blocks",
                v -> cfg.range = v));

        addDrawableChild(ButtonWidget.builder(toggleText(), b -> {
            cfg.reduceRenderDistance = !cfg.reduceRenderDistance;
            b.setMessage(toggleText());
        }).dimensions(x, y + 56, w, 20).build());

        addDrawableChild(new IntSlider(x, y + 84, w, 20, 2, 16, cfg.combatRenderDistance,
                v -> "Combat render distance: " + v + " chunks",
                v -> cfg.combatRenderDistance = v));

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(x, y + 120, w, 20).build());
    }

    private Text toggleText() {
        return Text.literal("Lower render distance in combat: "
                + (CrowConfig.INSTANCE.reduceRenderDistance ? "ON" : "OFF"));
    }

    @Override
    public void close() {
        CrowConfig.save();
        client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
    }

    private static class IntSlider extends SliderWidget {
        private final int min, max;
        private final IntFunction<String> format;
        private final IntConsumer onChange;

        IntSlider(int x, int y, int w, int h, int min, int max, int value,
                  IntFunction<String> format, IntConsumer onChange) {
            super(x, y, w, h, Text.empty(), (value - min) / (double) (max - min));
            this.min = min;
            this.max = max;
            this.format = format;
            this.onChange = onChange;
            updateMessage();
        }

        private int current() {
            return min + (int) Math.round(value * (max - min));
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal(format.apply(current())));
        }

        @Override
        protected void applyValue() {
            onChange.accept(current());
        }
    }
}
