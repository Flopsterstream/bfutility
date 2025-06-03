package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class ZoomModule extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private float targetFov = 30f;    // Zoomed-in FOV
    private float defaultFov = 110f;   // Default FOV (usually 70)
    private float zoomSpeed = 0.1f;   // How fast to interpolate FOV
    private float currentFov;

    public ZoomModule() {
        super("Zoom", Category.MOVEMENT, "Zoom");
        currentFov = defaultFov;
    }

    @Override
    public void onEnable() {
        // Save current default FOV
        defaultFov = mc.options.getFov().getValue();
        currentFov = defaultFov;
    }

    @Override
    public void onDisable() {
        // Reset FOV to default
        mc.options.getFov().setValue((int) defaultFov);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        float desiredFov = isEnabled() ? targetFov : defaultFov;

        // Smoothly interpolate FOV towards desiredFov
        currentFov += (desiredFov - currentFov) * zoomSpeed;

        mc.options.getFov().setValue((int) currentFov);
    }
}
