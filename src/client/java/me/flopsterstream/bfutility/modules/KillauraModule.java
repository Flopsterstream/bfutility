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
    private  int rate = 10; // Default attack rate (0.5 seconds at 20 TPS)

    public KillauraModule() {
        super("Killaura", Category.COMBAT, "Automatically attacks nearby mobs and players.");

        addOption("Attack rate", OptionType.SLIDER);

    }


    @Override
    public void onEnable() {
        attackCooldown = rate;
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
            LivingEntity target = entities.get(0);

            assert client.interactionManager != null;
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
            attackCooldown = rate;
        }
    }


    public void onOptionValueChanged(String optionName, Object value) {
        if (optionName.equals("Attack rate")) {
            if (value instanceof Integer intValue) {
                this.rate = intValue;

            }



        }
    }


}

