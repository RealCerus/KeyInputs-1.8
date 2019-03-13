package de.cerus.keyinputs.listeners;

import com.google.gson.JsonObject;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyInputListener {

    private LabyMod labyMod;
    private Minecraft minecraft;
    private MovementInput input;
    private boolean guiOpen;

    private List<String> pressedKeys;
    private boolean enabled;

    public KeyInputListener(boolean enabled) {
        this.enabled = enabled;
        labyMod = LabyMod.getInstance();
        minecraft = Minecraft.getMinecraft();
        pressedKeys = new ArrayList<>();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!enabled) return;
        if (!labyMod.isInGame()) return;
        if (minecraft.thePlayer == null) return;
        if (guiOpen) return;
        int key = Keyboard.getEventKey();
        if (!Keyboard.getEventKeyState()) {
            pressedKeys.remove(String.valueOf(key));
            return;
        }
        if (pressedKeys.contains(String.valueOf(key))) return;
        pressedKeys.add(String.valueOf(key));

        boolean moving = isMoving();
        String keyName = Keyboard.getKeyName(key);

        JsonObject finalJsonObject = new JsonObject();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("moving", moving);
        jsonObject.addProperty("name", keyName);
        finalJsonObject.add("KeyInputsMessage", jsonObject);

        labyMod.getLabyModAPI().sendJsonMessageToServer("KeyInputsAddon", finalJsonObject);
    }

    private boolean isMoving() {
        if (minecraft.thePlayer == null) return false;
        if (input == null)
            input = minecraft.thePlayer.movementInput;
        return minecraft.thePlayer.motionX != 0.0D || minecraft.thePlayer.motionZ != 0.0D || input.jump;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        guiOpen = (event.gui != null);
    }
}
