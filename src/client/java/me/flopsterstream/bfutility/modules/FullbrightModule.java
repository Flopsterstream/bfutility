package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private float originalGamma;

    public FullbrightModule() {
        super("Fullbright", Category.RENDER, "Sets the gamma to a very high value to make the game brighter.");
    }

    @Override
    public void onEnable() {
        originalGamma = MinecraftClient.getInstance().options.getGamma().getValue().floatValue();
        MinecraftClient.getInstance().options.getGamma().setValue(1500.0);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue((double) originalGamma);
    }

    @Override
    public void onTick() {
        // Optional: continually reinforce gamma
        if (isEnabled()) {
            MinecraftClient.getInstance().options.getGamma().setValue(1500.0);
        }
    }
}
