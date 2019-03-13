package de.cerus.keyinputs;

import de.cerus.keyinputs.listeners.KeyInputListener;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.util.List;

public class KeyInputs extends LabyModAddon {
    private boolean enabled;
    private Consumer<Boolean> consumer;

    @Override
    public void onEnable() {
        consumer = aBoolean -> getApi().registerForgeListener(new KeyInputListener(aBoolean));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void loadConfig() {
        enabled = getConfig().has("enabled") && getConfig().get("enabled").getAsBoolean();
        consumer.accept(enabled);
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER), enabled -> {
            if (getConfig().has("enabled"))
                getConfig().remove("enabled");
            getConfig().addProperty("enabled", enabled);
            saveConfig();
            this.enabled = enabled;
        }, this.enabled));
    }
}
