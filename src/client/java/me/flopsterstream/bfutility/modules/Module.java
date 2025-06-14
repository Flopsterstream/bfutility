package me.flopsterstream.bfutility.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;

    // Enum to define option types
    public enum OptionType {
        CHECKBOX,
        SLIDER,
        INPUT
    }

    // Map to store options and their types
    private final Map<String, OptionType> options = new HashMap<>();
    // Map to store checkbox values
    protected final Map<String, Boolean> checkboxValues = new HashMap<>();

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
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

    // Method to add an option with its type
    public void addOption(String optionName, OptionType type) {
        options.put(optionName, type);
    }

    // Method to return a list of options for the module
    public List<String> getOptions() {
        return new ArrayList<>(options.keySet());
    }

    // Method to get the type of a specific option
    public OptionType getOptionType(String optionName) {
        return options.get(optionName);
    }

    // Method to handle the selection of an option
    public void onOptionSelected(String option) {
        // Override in subclasses to handle option selection
    }

    public void onOptionValueChanged(String optionName, Object value) {
        if (options.get(optionName) == OptionType.CHECKBOX && value instanceof Boolean bool) {
            checkboxValues.put(optionName, bool);
            System.out.println("Checkbox '" + optionName + "' set to: " + bool);
        }
    }


    public boolean isCheckboxEnabled(String optionName) {
        return checkboxValues.getOrDefault(optionName, false);
    }

}