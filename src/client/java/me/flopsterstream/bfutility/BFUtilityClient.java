package me.flopsterstream.bfutility;
import me.flopsterstream.bfutility.modules.Module;
import me.flopsterstream.bfutility.modules.ModuleManager;
import me.flopsterstream.bfutility.modules.ZoomModule;
import me.flopsterstream.bfutility.ui.clickgui.ClickGuiScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BFUtilityClient implements ClientModInitializer {
	public static KeyBinding hotkey;
	private static ModuleManager moduleManager;
	private int waitUntil = 0;
	public static KeyBinding guiKey;


	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	@Override
	public void onInitializeClient() {
		guiKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.bfutility.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "BFUtility")
		);
		hotkey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.bfutility.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "BFUtility")
		);

		moduleManager = ModuleManager.getInstance();








		ClientTickEvents.END_CLIENT_TICK.register(client -> {


			if (guiKey.wasPressed()) {
				MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
			}
			waitUntil++;
			if (hotkey.wasPressed()) {
				ZoomModule.class.cast(moduleManager.getModuleByName("Zoom")).toggle();
			}



			moduleManager.tickAll();
		});
	}
}
