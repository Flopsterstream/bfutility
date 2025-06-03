package me.flopsterstream.bfutility.ui.clickgui;

import me.flopsterstream.bfutility.modules.Category;
import me.flopsterstream.bfutility.modules.Module;
import me.flopsterstream.bfutility.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class GuiCategory {

    private final Category category;
    private final String title;
    private int x, y;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    private boolean expanded = true;

    public GuiCategory(Category category, int x, int y) {
        this.category = category;
        this.title = category.name();
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        int titleWidth = mc.textRenderer.getWidth(title);
        int boxWidth = titleWidth + 20;
        int boxHeight = 15;

        int backgroundColor = 0xFF4F4F4F;
        int borderColor = 0xFF000000;
        int textColor = 0xFFFFFFFF;

        if (dragging) {
            this.x = mouseX - dragOffsetX;
            this.y = mouseY - dragOffsetY;
        }

        context.fill(x - 1, y - 1, x + boxWidth + 1, y + boxHeight + 1, borderColor);
        context.fill(x, y, x + boxWidth, y + boxHeight, backgroundColor);
        context.drawText(mc.textRenderer, title, x + 5, y + 3, textColor, false);

        if (expanded) {
            List<Module> modules = ModuleManager.getInstance().getModulesByCategory(category);

            int moduleY = y + boxHeight + 2;
            for (Module module : modules) {
                String moduleName = module.getName() + (module.isEnabled() ? " [ON]" : " [OFF]");
                int moduleWidth = mc.textRenderer.getWidth(moduleName) + 10;

                context.fill(x, moduleY, x + moduleWidth, moduleY + 12, 0xFF333333);
                context.drawText(mc.textRenderer, moduleName, x + 3, moduleY + 2, 0xFFFFFFFF, false);
                moduleY += 14;
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int titleWidth = mc.textRenderer.getWidth(title);
        int boxWidth = titleWidth + 20;
        int boxHeight = 15;

        boolean insideHeader = mouseX >= x && mouseX <= x + boxWidth && mouseY >= y && mouseY <= y + boxHeight;
        if (insideHeader) {
            if (button == 0) { // Left click to drag
                dragging = true;
                dragOffsetX = (int) (mouseX - x);
                dragOffsetY = (int) (mouseY - y);
                return true;
            } else if (button == 1) { // Right click to toggle expand/collapse
                expanded = !expanded;
                return true;
            }
        }

        if (expanded) {
            List<Module> modules = ModuleManager.getInstance().getModulesByCategory(category);

            int moduleY = y + boxHeight + 2;
            for (Module module : modules) {
                String moduleName = module.getName() + (module.isEnabled() ? " [ON]" : " [OFF]");
                int moduleWidth = mc.textRenderer.getWidth(moduleName) + 10;

                boolean insideModule = mouseX >= x && mouseX <= x + moduleWidth && mouseY >= moduleY && mouseY <= moduleY + 12;
                if (insideModule && button == 0) {
                    module.toggle();
                    return true;
                }
                moduleY += 14;
            }
        }

        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }
    }
}
