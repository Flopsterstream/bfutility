package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.math.Vec3d;
import me.flopsterstream.bfutility.modules.FreecamCameraEntity;


public class FreeCamModule extends Module {
    private Entity freecamEntity = null;
    private Vec3d originalPos = null;

    public FreeCamModule() {
        super("Freecam (Broken)", Category.COMBAT, "Automatically attacks nearby mobs and players.");
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        ClientWorld world = mc.world;

        if (player == null || world == null) return;

        originalPos = player.getPos();

        // Create a dummy camera entity
        freecamEntity = new FreecamCameraEntity(world) {
            @Override
            protected void initDataTracker(DataTracker.Builder builder) {
                // No data needed
            }

            @Override
            protected void readCustomDataFromNbt(net.minecraft.nbt.NbtCompound nbt) {}

            @Override
            protected void writeCustomDataToNbt(net.minecraft.nbt.NbtCompound nbt) {}
        };

        freecamEntity.setPos(originalPos.x, originalPos.y, originalPos.z);
        freecamEntity.noClip = true;

        mc.setCameraEntity(freecamEntity);
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player != null) {
            mc.setCameraEntity(mc.player);

            if (originalPos != null) {
                mc.player.setPosition(originalPos.x, originalPos.y, originalPos.z);
            }
        }

        freecamEntity = null;
    }

    @Override
    public void onTick() {
        if (freecamEntity == null || !isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        double speed = 0.5;

        // Get directional inputs
        double forward = 0, strafe = 0, vertical = 0;

        if (mc.options.forwardKey.isPressed()) forward -= 1;
        if (mc.options.backKey.isPressed()) forward += 1;
        if (mc.options.leftKey.isPressed()) strafe -= 1;
        if (mc.options.rightKey.isPressed()) strafe += 1;
        if (mc.options.jumpKey.isPressed()) vertical += 1;
        if (mc.options.sneakKey.isPressed()) vertical -= 1;

        Vec3d camForward = mc.player.getRotationVec(1.0F);
        Vec3d camRight = camForward.crossProduct(new Vec3d(0, 1, 0));
        Vec3d camUp = new Vec3d(0, 1, 0);

        Vec3d motion = camForward.multiply(forward)
                .add(camRight.multiply(strafe))
                .add(camUp.multiply(vertical));

        if (motion.lengthSquared() > 0) {
            motion = motion.normalize().multiply(speed);
            freecamEntity.setPos(
                    freecamEntity.getX() + motion.x,
                    freecamEntity.getY() + motion.y,
                    freecamEntity.getZ() + motion.z
            );
        }
    }
}
