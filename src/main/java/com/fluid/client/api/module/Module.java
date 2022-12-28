package com.fluid.client.api.module;

import com.fluid.client.api.event.EventManager;
import com.fluid.client.api.module.feature.Draggable;
import com.fluid.client.api.setting.Setting;
import com.fluid.client.api.setting.impl.BooleanSetting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name, description;
    private final Category category;
    private int key;
    private boolean enabled;
    private final List<Setting> settings = new ArrayList<>();

    public Module() {
        if (getClass().isAnnotationPresent(ModuleData.class)) {
            final ModuleData data = getClass().getAnnotation(ModuleData.class);
            this.name = data.name();
            this.description = data.description();
            this.category = data.category();
            this.key = data.key();
            this.setEnabled(data.enabled());
        } else {
            throw new IllegalStateException("@ModuleData not found!");
        }

        if (this instanceof Draggable) {
            this.addSettings(
                    new BooleanSetting("test", "test setting", true)
            );
        }
    }

    public void onEnable() { }
    public void onDisable() { }

    public final void toggle() {
        this.enabled = !this.enabled;

        if (this.enabled) {
            onEnable();
            EventManager.getPubSub().subscribe(this);
        } else {
            onDisable();
            EventManager.getPubSub().unsubscribe(this);
        }
    }

    public final Setting getSetting(String name) {
        return getSettings().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public final void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (this.enabled) {
            EventManager.getPubSub().subscribe(this);
        } else {
            EventManager.getPubSub().unsubscribe(this);
        }
    }

}
