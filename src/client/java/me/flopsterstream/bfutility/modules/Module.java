package me.flopsterstream.bfutility.modules;

import me.flopsterstream.bfutility.modules.Category;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;  // assign category here
        this.description = description;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }


    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onTick();

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }
}
