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
    private Module selectedModule = null;
    private boolean showOptions = false;

    private boolean draggingOptions = false;
    private int optionsDragOffsetX, optionsDragOffsetY;
    private int optionsBoxX = 200, optionsBoxY = 200;
    private String activeInputOption = null;

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

        if (dragging) {
            this.x = mouseX - dragOffsetX;
            this.y = mouseY - dragOffsetY;
        }


        context.fill(x - 1, y - 1, x + boxWidth + 1, y + boxHeight + 1, 0xFF000000);
        context.fill(x, y, x + boxWidth, y + boxHeight, 0xFF4F4F4F);
        context.drawText(mc.textRenderer, title, x + 5, y + 3, 0xFFFFFFFF, false);

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

        if (showOptions && selectedModule != null) {
            if (draggingOptions) {
                optionsBoxX = mouseX - optionsDragOffsetX;
                optionsBoxY = mouseY - optionsDragOffsetY;
            }

            List<String> options = selectedModule.getOptions();
            int optionsBoxWidth = 150;
            int optionsBoxHeight = options.size() * 14 + 2;

            context.fill(optionsBoxX - 1, optionsBoxY - 1, optionsBoxX + optionsBoxWidth + 1, optionsBoxY + optionsBoxHeight + 1, 0xFF000000);
            context.fill(optionsBoxX, optionsBoxY, optionsBoxX + optionsBoxWidth, optionsBoxY + 12, 0xFF4F4F4F);
            context.drawText(mc.textRenderer, "Options", optionsBoxX + 5, optionsBoxY + 3, 0xFFFFFFFF, false);

            int optionY = optionsBoxY + boxHeight + 2;
            for (String option : options) {
                Module.OptionType type = selectedModule.getOptionType(option);
                switch (type) {
                    case CHECKBOX -> {
                        boolean isOn = (boolean) selectedModule.getOptionValue(option, false);
                        String optionText = option + (isOn ? " [ON]" : " [OFF]");
                        int optionWidth = mc.textRenderer.getWidth(optionText) + 10;
                        context.fill(optionsBoxX, optionY, optionsBoxX + optionWidth, optionY + 12, 0xFF333333);
                        context.drawText(mc.textRenderer, optionText, optionsBoxX + 3, optionY + 2, 0xFFFFFFFF, false);
                    }
                    case SLIDER -> {
                        int sliderValue = (int) selectedModule.getOptionValue(option, 50);
                        int sliderWidth = 100;
                        int sliderX = optionsBoxX + 10;
                        int sliderY = optionY + 2;
                        int handleX = sliderX + (sliderValue * sliderWidth / 100);

                        context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + 4, 0xFF666666);
                        context.fill(sliderX, sliderY, handleX, sliderY + 4, 0xFFAAAAAA);
                        context.fill(handleX - 2, sliderY - 2, handleX + 2, sliderY + 6, 0xFFFFFFFF);

                        String optionText = option + ": " + sliderValue;
                        context.drawText(mc.textRenderer, optionText, sliderX + sliderWidth + 5, sliderY - 2, 0xFFFFFFFF, false);
                    }
                    case INPUT -> {
                        String inputValue = (String) selectedModule.getOptionValue(option, "");
                        String optionText = option + ": " + inputValue;
                        int optionWidth = mc.textRenderer.getWidth(optionText) + 10;

                        int borderColor = option.equals(activeInputOption) ? 0xFFFFFF00 : 0xFF333333;
                        context.fill(optionsBoxX - 1, optionY - 1, optionsBoxX + optionWidth + 1, optionY + 13, borderColor);
                        context.fill(optionsBoxX, optionY, optionsBoxX + optionWidth, optionY + 12, 0xFF333333);
                        context.drawText(mc.textRenderer, optionText, optionsBoxX + 3, optionY + 2, 0xFFFFFFFF, false);
                    }
                }
                optionY += 14;
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
            if (button == 0) {
                dragging = true;
                dragOffsetX = (int) (mouseX - x);
                dragOffsetY = (int) (mouseY - y);
                return true;
            } else if (button == 1) {
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
                if (insideModule) {
                    if (button == 0) {
                        module.toggle();
                        return true;
                    } else if (button == 1) {
                        selectedModule = module;
                        showOptions = true;
                        return true;
                    }
                }
                moduleY += 14;
            }
        }

        if (showOptions) {
            boolean insideOptionsHeader = mouseX >= optionsBoxX && mouseX <= optionsBoxX + 150 && mouseY >= optionsBoxY && mouseY <= optionsBoxY + 12;
            if (insideOptionsHeader && button == 0) {
                draggingOptions = true;
                optionsDragOffsetX = (int) (mouseX - optionsBoxX);
                optionsDragOffsetY = (int) (mouseY - optionsBoxY);
                return true;
            }
        }

        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (activeInputOption != null) {
            if (keyCode == 257) {
                activeInputOption = null;
            } else if (keyCode == 259) {
                String currentValue = (String) selectedModule.getOptionValue(activeInputOption, "");
                if (!currentValue.isEmpty()) {
                    selectedModule.setOptionValue(activeInputOption, currentValue.substring(0, currentValue.length() - 1));
                }
            }
            return true;
        }

        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (activeInputOption != null) {
            String currentValue = (String) selectedModule.getOptionValue(activeInputOption, "");
            String newValue = currentValue + chr;
            selectedModule.setOptionValue(activeInputOption, newValue);
            selectedModule.onOptionValueChanged(activeInputOption, newValue);
            return true;
        }

        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
            draggingOptions = false;
        }

        if (showOptions && selectedModule != null) {
            int optionY = optionsBoxY + 15;
            for (String option : selectedModule.getOptions()) {
                Module.OptionType type = selectedModule.getOptionType(option);
                if (mouseX >= optionsBoxX && mouseX <= optionsBoxX + 150 && mouseY >= optionY && mouseY <= optionY + 12) {
                    switch (type) {
                        case CHECKBOX -> {
                            boolean currentState = (boolean) selectedModule.getOptionValue(option, false);
                            boolean newState = !currentState;
                            selectedModule.setOptionValue(option, newState);
                            selectedModule.onOptionValueChanged(option, newState);
                        }
                        case SLIDER -> {
                            int sliderWidth = 100;
                            int sliderX = optionsBoxX + 10;
                            int newValue = (int) (((mouseX - sliderX) / sliderWidth) * 100);
                            newValue = Math.max(0, Math.min(100, newValue));
                            selectedModule.setOptionValue(option, newValue);
                            selectedModule.onOptionValueChanged(option, newValue);
                        }
                        case INPUT -> {
                            activeInputOption = option;
                        }
                    }
                    return;
                }
                optionY += 14;
            }
        }
    }
}