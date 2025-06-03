package me.flopsterstream.bfutility.ui.clickgui;

import me.flopsterstream.bfutility.modules.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {

    private final List<GuiCategory> categories = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.of("Click GUI"));
        int startX = 20;
        int startY = 20;

        // Example: You can replace this with your actual categories and modules
        for (int i = 0; i < Category.values().length; i++) {
            categories.add(new GuiCategory(Category.values()[i], startX + i * 60, startY));
        }


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);


        for (GuiCategory category : categories) {
            category.render(context, mouseX, mouseY, delta);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiCategory category : categories) {
            if (category.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiCategory category : categories) {
            category.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Close GUI with Escape
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            MinecraftClient.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
