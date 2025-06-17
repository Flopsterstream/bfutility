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
    private final Map<String, Object> optionValues = new HashMap<>();

    // Enum
    public enum OptionType {
        CHECKBOX,
        SLIDER,
        INPUT
    }


    private final Map<String, OptionType> options = new HashMap<>();

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


    public void addOption(String optionName, OptionType type) {
        options.put(optionName, type);
    }


    public List<String> getOptions() {
        return new ArrayList<>(options.keySet());
    }


    public OptionType getOptionType(String optionName) {
        return options.get(optionName);
    }


    public void onOptionSelected(String option) {

    }

    public void onOptionValueChanged(String optionName, Object value) {
        if (options.get(optionName) == OptionType.CHECKBOX && value instanceof Boolean bool) {
            checkboxValues.put(optionName, bool);
        }
    }


    public boolean isCheckboxEnabled(String optionName) {
        return checkboxValues.getOrDefault(optionName, false);
    }


    public void setOptionValue(String option, Object value) {
        optionValues.put(option, value);
    }

    public Object getOptionValue(String option, Object defaultValue) {
        return optionValues.getOrDefault(option, defaultValue);
    }

    public Map<String, Object> getAllOptionValues() {
        return optionValues;
    }


}