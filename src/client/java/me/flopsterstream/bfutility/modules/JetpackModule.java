package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;

public class JetpackModule extends Module {

    private int VerticalSpeed = 16;

    private final MinecraftClient client = MinecraftClient.getInstance();

    public JetpackModule() {
        super("Jetpack", Category.MOVEMENT, "Allows you to fly like you have a jetpack.");
        addOption("Vertical speed", OptionType.SLIDER);
    }


    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        if (client.player != null && !client.player.isOnGround() && client.options.jumpKey.isPressed()) {
            client.player.setVelocity(client.player.getVelocity().x, ((double) VerticalSpeed / 40), client.player.getVelocity().z);
        }
    }


    public void onOptionValueChanged(String optionName, Object value) {
        if (optionName.equals("Vertical speed")) {
            if (value instanceof Integer intValue) {
                this.VerticalSpeed = intValue;

            }



        }
    }


}