package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.List;

public class KillauraModule extends Module {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private int attackCooldown = 0;

    public KillauraModule() {
        super("Killaura", Category.COMBAT, "Automatically attacks nearby mobs and players.");
    }

    @Override
    public void onEnable() {
        attackCooldown = 0;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        if (client.world == null || client.player == null) return;

        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        List<LivingEntity> entities = client.world.getEntitiesByClass(
                LivingEntity.class,
                client.player.getBoundingBox().expand(6), // attack range
                e -> e != client.player && e.isAlive() && !e.isInvisible() && client.player.canSee(e)
        );

        if (!entities.isEmpty()) {
            LivingEntity target = entities.get(0); // Pick the first target

            assert client.interactionManager != null;
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
            attackCooldown = 10; // 0.5s cooldown at 20 TPS
        }
    }
}
