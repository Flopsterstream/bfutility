package me.flopsterstream.bfutility.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotemModule extends Module {
    public AutoTotemModule() {
        super("Autototem", Category.MOVEMENT, "Automatically moves a Totem to offhand");
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

        if (client.player == null || client.currentScreen != null) return;

        ItemStack offhand = client.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return; // Already has totem


        for (int i = 9; i < 45; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                swapToOffhand(i);
                break;
            }
        }
    }

    private void swapToOffhand(int slotIndex) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = client.interactionManager;

        if (client.player != null && client.player.playerScreenHandler != null && interactionManager != null) {
            int windowId = client.player.playerScreenHandler.syncId;


            int invSlot = slotIndex;
            int offhandSlot = 45;


            interactionManager.clickSlot(windowId, invSlot, 0, SlotActionType.PICKUP, client.player);


            interactionManager.clickSlot(windowId, offhandSlot, 0, SlotActionType.PICKUP, client.player);


            interactionManager.clickSlot(windowId, invSlot, 0, SlotActionType.PICKUP, client.player);
        }
    }
}
