package me.flopsterstream.bfutility.modules;


import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;


public class FlightModule extends Module {

    public FlightModule() {
        super("Flight", Category.MOVEMENT, "Allows oyu to fly ");

    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            PlayerAbilities abilities = client.player.getAbilities();
            abilities.allowFlying = true;
            client.player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            PlayerAbilities abilities = client.player.getAbilities();
            abilities.allowFlying = false;
            abilities.flying = false;
            client.player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && isEnabled()) {
            PlayerAbilities abilities = client.player.getAbilities();
            if (!abilities.allowFlying) {
                abilities.allowFlying = true;
                client.player.sendAbilitiesUpdate();
            }
        }
    }
}