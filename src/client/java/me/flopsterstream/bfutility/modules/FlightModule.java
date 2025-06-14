package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;

import java.util.List;

public class FlightModule extends Module {

    public FlightModule() {
        super("Flight", Category.MOVEMENT, "Allows you to fly");


        addOption("Anti kick", OptionType.CHECKBOX);

    }
    private int antiKickTickCounter = 0;

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

            if (isCheckboxEnabled("Anti kick")) {

                antiKickTickCounter++;
                if (antiKickTickCounter >= 20) {

                    client.player.setPos(
                            client.player.getX(),
                            client.player.getY() - 0.04,
                            client.player.getZ()
                    );
                    antiKickTickCounter = 0;
                }
            }




        }
    }


    public void onOptionValueChanged(String optionName, Object value) {
        if (getOptionType(optionName) == OptionType.CHECKBOX && value instanceof Boolean boolValue) {
            checkboxValues.put(optionName, boolValue);
        }

    }

}