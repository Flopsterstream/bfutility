package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class ZoomModule extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private float targetFov = 30f;
    private float defaultFov = 110f;
    private float zoomSpeed = 0.1f;
    private float currentFov;

    public ZoomModule() {
        super("Zoom", Category.MOVEMENT, "Zoom");
        currentFov = defaultFov;
    }

    @Override
    public void onEnable() {

        defaultFov = mc.options.getFov().getValue();
        currentFov = defaultFov;
    }

    @Override
    public void onDisable() {

        mc.options.getFov().setValue((int) defaultFov);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        float desiredFov = isEnabled() ? targetFov : defaultFov;


        currentFov += (desiredFov - currentFov) * zoomSpeed;

        mc.options.getFov().setValue((int) currentFov);
    }
}
