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
        // Nothing needed on enable
    }

    @Override
    public void onDisable() {
        // Nothing needed on disable
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.currentScreen != null) return;

        ItemStack offhand = client.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return; // Already has totem

        // Look for totem in inventory (slots 9–35 = inventory excluding hotbar)
        for (int i = 9; i < 36; i++) {
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

            // Slot IDs in the player inventory:
            // 0–8: hotbar, 9–35: inventory, 45: offhand
            // Convert inventory slot to screen slot
            int invSlot = slotIndex;
            int offhandSlot = 45;

            // Pick up totem
            interactionManager.clickSlot(windowId, invSlot, 0, SlotActionType.PICKUP, client.player);

            // Place it in offhand
            interactionManager.clickSlot(windowId, offhandSlot, 0, SlotActionType.PICKUP, client.player);

            // Put whatever was in offhand back into inventory
            interactionManager.clickSlot(windowId, invSlot, 0, SlotActionType.PICKUP, client.player);
        }
    }
}
