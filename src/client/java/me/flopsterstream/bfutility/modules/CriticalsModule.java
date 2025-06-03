package me.flopsterstream.bfutility.modules;
import  me.flopsterstream.bfutility.modules.Module;

import me.flopsterstream.bfutility.modules.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;



public class CriticalsModule extends Module {

    public CriticalsModule() {
        super("Criticals", Category.COMBAT, "Spoofs critical hits on entities.");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {

    }

    // Call this from your attack logic, e.g., in Killaura
    public void doCritical(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !(target instanceof LivingEntity)) return;
        if (!client.player.isOnGround() || client.player.isTouchingWater() || client.player.isInLava()) return;

        Vec3d pos = client.player.getPos();
        float yaw = client.player.getYaw();
        float pitch = client.player.getPitch();
        boolean hc = client.player.horizontalCollision;

        client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y + 0.0625, pos.z, yaw, pitch, false, hc));
        client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y, pos.z, yaw, pitch, false, hc));
        client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y + 1.0E-5, pos.z, yaw, pitch, false, hc));
        client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y, pos.z, yaw, pitch, false, hc));
    }
}
