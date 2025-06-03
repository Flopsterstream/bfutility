package me.flopsterstream.bfutility.modules;

import me.flopsterstream.bfutility.modules.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NofallModule extends Module {

    public NofallModule() {
        super("Nofall", Category.MOVEMENT, "Prevents fall damage.");

    }

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && client.world != null) {
            if (client.player.fallDistance > 2.0f) {
                // Tell the server we're on the ground to cancel fall damage
                client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false
                ));
            }
        }
    }
}
