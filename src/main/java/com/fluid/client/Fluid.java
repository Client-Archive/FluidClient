package com.fluid.client;

import com.fluid.client.api.config.ConfigManager;
import com.fluid.client.api.module.ModuleManager;
import com.fluid.client.discord.DiscordIPC;
import lombok.Getter;
import org.lwjgl.opengl.Display;

@Getter
public class Fluid {

    @Getter
    private final static Fluid instance = new Fluid();

    private final double version = 1.0;

    private final ModuleManager moduleManager = new ModuleManager();
    private final ConfigManager configManager = new ConfigManager();

    public void start() {
        Display.setTitle("Fluid Client");
        DiscordIPC.INSTANCE.init();
        configManager.load();
    }

    public void stop() {
        configManager.save();
    }

}
