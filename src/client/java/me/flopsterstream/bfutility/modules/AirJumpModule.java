package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class AirJumpModule extends Module {

    private boolean wasJumping = false;
    private int jumpCooldown = 0;

    public AirJumpModule() {
        super("AirJump", Category.MOVEMENT, "Allows you to jump while in the air with a cooldown.");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (jumpCooldown > 0)
            jumpCooldown--;

        boolean isJumpPressed = mc.options.jumpKey.isPressed();

        if (isJumpPressed && !wasJumping && jumpCooldown == 0) {
            player.jump();
            jumpCooldown = 3;
        }

        wasJumping = isJumpPressed;
    }
}
