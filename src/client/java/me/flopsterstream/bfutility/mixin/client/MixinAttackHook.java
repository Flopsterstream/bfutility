package me.flopsterstream.bfutility.mixin.client;

import me.flopsterstream.bfutility.modules.CriticalsModule;
import me.flopsterstream.bfutility.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinAttackHook {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!(target instanceof LivingEntity)) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        CriticalsModule criticals = (CriticalsModule) ModuleManager.getInstance()
                .getModuleByName("Criticals");

        if (criticals != null && criticals.isEnabled()) {
            criticals.doCritical(target);
        }
    }
}
