package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;



import java.util.List;

public class ReachModule extends Module {
    private final double REACH_DISTANCE = 8;

    public ReachModule() {
        super("Reach", Category.COMBAT, "Lets you hit entities from farther away.");
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        ClientWorld world = mc.world;

        if (player == null || world == null) return;


        if (!mc.options.attackKey.isPressed()) return;


        Entity target = getEntityInReach(player, REACH_DISTANCE);

        if (target != null) {

            mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(target, player.isSneaking()));
            player.swingHand(Hand.MAIN_HAND);
        }


    }

    private Entity getEntityInReach(ClientPlayerEntity player, double reach) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(reach));

        Box box = player.getBoundingBox().stretch(direction.multiply(reach)).expand(0.5);
        List<Entity> entities = player.getWorld().getOtherEntities(player, box, e -> e.isAttackable() && !e.isSpectator());

        Entity closest = null;
        double closestSq = reach * reach;

        for (Entity entity : entities) {
            Box entityBox = entity.getBoundingBox().expand(0.5);
            Vec3d hit = entityBox.raycast(start, end).orElse(null);
            if (hit != null) {
                double distSq = start.squaredDistanceTo(hit);
                if (distSq < closestSq) {
                    closestSq = distSq;
                    closest = entity;
                }
            }
        }


        if (closest != null) {

        }



        return closest;
    }
}
