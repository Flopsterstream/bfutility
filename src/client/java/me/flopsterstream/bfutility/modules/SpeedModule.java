package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;


public class SpeedModule extends Module {

    private double speedMultiplier = 1.2;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT, "Increases player movement speed.");
        addOption("Speed multiplier", OptionType.SLIDER);
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
        ClientPlayerEntity player = mc.player;



        if (player == null || !isEnabled()) return;


        double forward = 0;
        double strafe = 0;

        if (mc.options.forwardKey.isPressed()) strafe -= 1;
        if (mc.options.backKey.isPressed()) strafe += 1;
        if (mc.options.leftKey.isPressed()) forward += 1;
        if (mc.options.rightKey.isPressed()) forward -= 1;

        if (forward == 0 && strafe == 0) return;


        float yaw = player.getYaw();

        double rad = Math.toRadians(yaw);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);

        double motionX = (forward * cos + strafe * sin) * (speedMultiplier / 40);
        double motionZ = (forward * sin - strafe * cos) * (speedMultiplier / 40);


        player.setVelocity(motionX, player.getVelocity().y, motionZ);
        player.velocityModified = true;
    }


    public void onOptionValueChanged(String optionName, Object value) {
        if (optionName.equals("Speed multiplier")) {
            if (value instanceof Integer intValue) {
                this.speedMultiplier = intValue;



            }



        }
    }
}
